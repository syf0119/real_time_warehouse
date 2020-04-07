package cn.syf.utils

import com.alibaba.fastjson.{JSON, JSONObject}
import redis.clients.jedis.{Jedis, JedisCluster, JedisPool, JedisPoolConfig}

object RedisUtil {
  def getJedis(): Jedis = {

    val config = new JedisPoolConfig
    config.setMaxTotal(10) //最大连接总数
    config.setMaxIdle(5) //最大空闲连接数
    config.setMinIdle(5) //最小空闲连接数
    //set封装ip和port
    val jedisPool = new JedisPool(config, "spring", 6379)


    jedisPool.getResource
  }


  def main(args: Array[String]): Unit = {
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
    val jedis: Jedis = getJedis()
    val goodsStr: String = jedis.hget("goods", "100155")
    val goodsJson: JSONObject = JSON.parseObject(goodsStr)
    println(goodsJson)
    val goodsId = goodsJson.getString("goodsId")
    val goodsName = goodsJson.getString("goodsName")
    val goodsThirdCatId = goodsJson.getIntValue("goodsCatId")
    val goodsShopId = goodsJson.getLong("shopId")


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
    val shopId = shopJson.getString("shopId")
    val shopName = shopJson.getString("shopName")
    val shopAreaId = shopJson.getIntValue("areaId")
    val shopCompany = shopJson.getString("shopCompany")


    println(shopId)
    println(shopName)
    println(shopAreaId)
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

    val areaString: String = jedis.hget("areas",shopAreaId.toString)
    val areaJson: JSONObject = JSON.parseObject(areaString)
    val cityId: Int = areaJson.getIntValue("cityId")
    val cityName: String = areaJson.getString("cityName")


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

  }

}
