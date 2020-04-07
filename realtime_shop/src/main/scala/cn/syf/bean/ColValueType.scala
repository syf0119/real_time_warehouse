package cn.syf.bean

import com.alibaba.fastjson.serializer.SerializerFeature
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}

import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer

/**
  **/
case class ColValueType(
                         @BeanProperty colName: String,
                         @BeanProperty colValue: String,
                         @BeanProperty colType: String
                       ){
  override def toString: String = {
JSON.toJSONString(this,SerializerFeature.DisableCircularReferenceDetect)

  }
}

object ColValueType {

  //字符串数据解析
  def apply(str: String): Map[String, ColValueType] = {


    //用map，存JSONArray数据
    var map = Map[String, ColValueType]()

    val jSONArray: JSONArray = JSON.parseArray(str)
    for (line <- 0 until jSONArray.size()) {

      //取数组内的每一个json数据
      val json: JSONObject = jSONArray.getJSONObject(line)
      val valueType = ColValueType(
        json.getString("col"),
        json.getString("val"),
        json.getString("type")
      )

      //封装map数据
      map += (json.getString("col") -> valueType)
    }
    map
  }
}




