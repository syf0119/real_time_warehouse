package cn.syf.process

import cn.syf.bean.ClickBean
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.scala._
import redis.clients.jedis.Jedis
import cn.syf.utils.{DateUtil, RedisUtil}

object ProcessData {


  def process(data: DataStream[String]) = {
    data.map(line => {
      ClickBean.getClickBean(line)
    }).addSink(new ClickRedisSink)
  }

}

class ClickRedisSink extends RichSinkFunction[ClickBean] {
  var jedis: Jedis = null

  override def open(parameters: Configuration): Unit = {

    jedis = RedisUtil.getJedis()
  }

  //{
  //  "ip": "88.35.199.72",
  //  "guid": "388cfaba-1006-478b-bb80-411ece74dd11",
  //  "url": "http://order.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857"
  //}
  override def invoke(value: ClickBean): Unit = {
    /**
      * 业务需求：统计每日pv/uv/ip的数据量
      *
      * 开发步骤：
      * 1.数据转换->jsonString-》 bean
      * 2.业务数据加工/落地redis
      * 每日数据保存：hash
      * pv ：itcast_shop:pv
      * uv：itcast_shop:uv
      * ip：itcast_shop:ip
      */
    val today: String = DateUtil.getToday

    val pvKey = "syf_shop:pv" //hash
    val uvOuterKey = "syf_shop:uv" //hash
    val ipOuterKey = "syf_shop:ip" //hash

    val uvSetKey = "syf_set:uv"
    val ipSetKey = "syf_set:ip"
    jedis.hincrBy("pvKey", today, 1)

    if (!jedis.sismember(uvSetKey, value.guid)) {
      jedis.sadd(uvSetKey, value.guid)
      jedis.hincrBy(uvOuterKey, today, 1)

    }

    if (!jedis.sismember(ipSetKey, value.ip)) {
      jedis.sadd(ipSetKey, value.ip)
      jedis.hincrBy(ipOuterKey, today, 1)

    }





  }

  override def close(): Unit = {
    jedis.close()
  }
}
