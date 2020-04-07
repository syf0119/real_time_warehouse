package cn.syf.process

import java.lang
import java.util.Properties

import cn.syf.bean.{CanalEntity, ColValueType, OrderBean, OrderGoodsWideEntity}
import cn.syf.config.GlobalConfig
import cn.syf.utils.{HBaseUtil, RedisUtil}
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.RichAllWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.util.Collector
import redis.clients.jedis.Jedis

object ProcessOrderData {

  def hBaseProcess(dataStream: DataStream[CanalEntity]): Unit ={

    dataStream
      .timeWindowAll(Time.seconds(3))
      .apply(new WideFunc)



    .addSink(new SinkToHBase)


  }
  def druidProcess(dataStream: DataStream[CanalEntity]): Unit ={
    val propertiesPro = new Properties()
    propertiesPro.setProperty("bootstrap.servers",GlobalConfig.kafkaBroker)

    //构建kafka生产对象
    val kafkaProducer: FlinkKafkaProducer011[String] = new FlinkKafkaProducer011[String](GlobalConfig.kafkaTopicDruid,new SimpleStringSchema(),propertiesPro)



dataStream.map(ele=>{
  OrderBean(ele.cols).toString
}).addSink(kafkaProducer)



  }
}
class WideFunc extends RichAllWindowFunction[CanalEntity,OrderGoodsWideEntity,TimeWindow] {
  var jedis: Jedis=_
  override def open(parameters: Configuration): Unit = {
    jedis=RedisUtil.getJedis()

  }
  override def apply(window: TimeWindow, input: Iterable[CanalEntity], out: Collector[OrderGoodsWideEntity]): Unit = {
for(line<-input){
  val orderGoodsWideEntity: OrderGoodsWideEntity = getOrderGoodsWideEntity(line)
  out.collect(orderGoodsWideEntity)
}

  }

  def getOrderGoodsWideEntity(canalEntity:CanalEntity):OrderGoodsWideEntity={

    val map: Map[String, ColValueType] = canalEntity.cols
    val ogId: Long = map.getOrElse("ogId", null).getColValue.toLong
    val orderId: Long = map.getOrElse("orderId", null).getColValue.toLong
    val goodsId: Long = map.getOrElse("goodsId", null).getColValue.toLong
    val goodsNum: Long = map.getOrElse("goodsNum", null).getColValue.toLong
    val goodsPrice: Double = map.getOrElse("goodsPrice", null).getColValue.toDouble
    /**
      * ogId:Long,
      * orderId:Long,
      * goodsId:Long,
      * goodsNum:Long,
      * goodsPrice:Double,
      * goodsName:String,
      * shopId:Long,
      * goodsThirdCatId:Int,
      * goodsThirdCatName:String,
      * goodsSecondCatId:Int,
      * goodsSecondCatName:String,
      * goodsFirstCatId:Int,
      * goodsFirstCatName:String,
      * areaId:Int,
      * shopName:String,
      * shopCompany:String,
      * cityId:Int,
      * cityName:String)
      */
   // OrderGoodsWideEntity(169066,138608,100110,28,9492.89,花果山 烟台红富士苹果 12个 净重2.1kg以上 单果160-190g 一二级混装 自营水果,100056,73,苹果,61,进口水果,47,时蔬水果、网上菜场,76,OPPO自营店,广州欧泊通信科技有限公司,0,null)

    val goodsStr: String = jedis.hget("goods", goodsId.toString)
    val goodsJson: JSONObject = JSON.parseObject(goodsStr)
    println(goodsJson)

    val goodsName: String = goodsJson.getString("goodsName")
    val goodsThirdCatId: Int = goodsJson.getIntValue("goodsCatId")
    val goodsShopId: lang.Long = goodsJson.getLong("shopId")


    println(goodsId)
    println(goodsName)
    println(goodsThirdCatId)
    println(goodsShopId)


    /**
      * areaId:Int,
      * shopName:String,
      * shopCompany:String,
      */
    val shopStr: String = jedis.hget("shops", goodsShopId.toString)

    val shopJson: JSONObject = JSON.parseObject(shopStr)
    println(shopJson)
    val shopId: Long = shopJson.getLong("shopId")
    val shopName: String = shopJson.getString("shopName")
    val areaId: Int = shopJson.getIntValue("areaId")
    val shopCompany: String = shopJson.getString("shopCompany")


    println(shopId)
    println(shopName)
    println(areaId)
    println(shopCompany)
    /**
      * goodsThirdCatId:Int,
      * goodsThirdCatName:String,
      * goodsSecondCatId:Int,
      * goodsSecondCatName:String,
      * goodsFirstCatId:Int,
      * goodsFirstCatName:String,
      */
    //{"catId":"293","catName":"白酒","cat_level":"3","parentId":"78"}

    val areaString: String = jedis.hget("areas",areaId.toString)
    val areaJson: JSONObject = JSON.parseObject(areaString)
//    val areaId: Int = areaJson.getIntValue("areaId")
//    val cityName: String = areaJson.getString("areaName")

   var  cityName:String=areaJson.getString("areaName")
    var  cityId:Int=areaId
    val areaType: Int = areaJson.getIntValue("areaType")
    if(areaType==2){
      val parentId: String = areaJson.getString("parentId")

      val parentAreaString: String = jedis.hget("areas",parentId)
      val parentAreaJson: JSONObject = JSON.parseObject(parentAreaString)
      cityId=parentId.toInt
      cityName=parentAreaJson.getString("areaName")
    }

    val thirdCatStr: String = jedis.hget("goods_cats", goodsThirdCatId.toString)

    println(thirdCatStr)
    val thirdCatJson: JSONObject = JSON.parseObject(thirdCatStr)
    val goodsThirdCatName: String = thirdCatJson.getString("catName")


    val goodsSecondCatId: Int = thirdCatJson.getIntValue("parentId")
    val secondCatString: String = jedis.hget("goods_cats", goodsSecondCatId.toString)
    println(secondCatString)
    val secondCatJson: JSONObject = JSON.parseObject(secondCatString)
    val goodsSecondCatName: String = secondCatJson.getString("catName")


    val goodsFirstCatId: Int = secondCatJson.getIntValue("parentId")
    val firstCatString: String = jedis.hget("goods_cats", goodsFirstCatId.toString)
    println(firstCatString)
    val firstCatJson: JSONObject = JSON.parseObject(firstCatString)
    val goodsFirstCatName: String = firstCatJson.getString("catName")
    /**
      * ogId:Long,
      * orderId:Long,
      * goodsId:Long,
      * goodsNum:Long,
      * goodsPrice:Double,
      * goodsName:String,
      * shopId:Long,
      * goodsThirdCatId:Int,
      * goodsThirdCatName:String,
      * goodsSecondCatId:Int,
      * goodsSecondCatName:String,
      * goodsFirstCatId:Int,
      * goodsFirstCatName:String,
      * areaId:Int,
      * shopName:String,
      * shopCompany:String,
      * cityId:Int,
      * cityName:String)
      */
    OrderGoodsWideEntity(ogId,
      orderId,
      goodsId,
      goodsNum,
      goodsPrice,
      goodsName,
      shopId,
      goodsThirdCatId,
      goodsThirdCatName,
      goodsSecondCatId,
      goodsSecondCatName,
      goodsFirstCatId,
      goodsFirstCatName,
      areaId,
      shopName,
      shopCompany,
      cityId,
      cityName)


  }

  override def close(): Unit = {

    if(null!=jedis){
      jedis.close()
    }
  }
}



class SinkToHBase extends RichSinkFunction[OrderGoodsWideEntity]{



  override def invoke(value: OrderGoodsWideEntity): Unit = {

val map: Map[String, String] = OrderGoodsWideEntity.toMap(value)
    HBaseUtil.putMap(GlobalConfig.hbaseTable,GlobalConfig.hbaseFamily,value.ogId.toString,map)
  }


}




