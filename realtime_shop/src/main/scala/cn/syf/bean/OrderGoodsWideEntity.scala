package cn.syf.bean



import com.alibaba.fastjson.serializer.SerializerFeature
import com.alibaba.fastjson.{JSON, JSONObject}

import scala.beans.BeanProperty
import scala.collection.mutable
case class OrderGoodsWideEntity(@BeanProperty ogId:Long,
                                @BeanProperty orderId:Long,
                                @BeanProperty goodsId:Long,
                                @BeanProperty goodsNum:Long,
                                @BeanProperty goodsPrice:Double,
                                @BeanProperty goodsName:String,
                                @BeanProperty shopId:Long,
                                @BeanProperty goodsThirdCatId:Int,
                                @BeanProperty goodsThirdCatName:String,
                                @BeanProperty goodsSecondCatId:Int,
                                @BeanProperty goodsSecondCatName:String,
                                @BeanProperty goodsFirstCatId:Int,
                                @BeanProperty goodsFirstCatName:String,
                                @BeanProperty areaId:Int,
                                @BeanProperty shopName:String,
                                @BeanProperty shopCompany:String,
                                @BeanProperty cityId:Int,
                                @BeanProperty cityName:String){
  override def toString: String = {
    JSON.toJSONString(this,SerializerFeature.DisableCircularReferenceDetect)

  }
}
object OrderGoodsWideEntity {
  def toMap(orderGoodsWideEntity: OrderGoodsWideEntity):Map[String, String] ={

    val jSONObject: JSONObject = JSON.parseObject(orderGoodsWideEntity.toString)
    import collection.JavaConverters._
  val map: Map[String, String] = jSONObject.getInnerMap.asScala.map(tup=>tup._1->tup._2.toString).toMap
    map



  }

}
