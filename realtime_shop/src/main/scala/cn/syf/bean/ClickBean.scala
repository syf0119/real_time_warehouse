package cn.syf.bean

import com.alibaba.fastjson.{JSON, JSONObject}

//{
//  "referer": "http://star.itheima.com.cn/135/1351735_3.html",
//  "linkId": "1.1.16.0.5.KlBK557-10-6z7Ct",
//  "ip": "88.35.199.72",
//  "trackTime": "1578050472881",
//  "guid": "388cfaba-1006-478b-bb80-411ece74dd11",
//  "id": "1ae5a19e-90cb-4796-b363-f89f8c40f069",
//  "sessionId": "7d5b196f-8cc2-4636-9565-b2c999d27a06",
//  "attachedInfo": "login success",
//  "url": "http://order.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857"
//}
case class ClickBean(
                      refer:String,
                      linkId:String,
                      ip:String,
                      trackTime:String,
                      guid:String,
                      id:String,
                      sessionId:String,
                      attachedInfo:String,
                      url:String){

}

object ClickBean{
  def getClickBean(jsonString:String):ClickBean={
    val jSONObject: JSONObject = JSON.parseObject(jsonString)
    ClickBean(
      jSONObject.getString("refer"),
      jSONObject.getString("linkId"),
      jSONObject.getString("ip"),
      jSONObject.getString("trackTime"),
      jSONObject.getString("guid"),
      jSONObject.getString("id"),
      jSONObject.getString("sessionId"),
      jSONObject.getString("attachedInfo"),
      jSONObject.getString("url")
    )
  }


}


