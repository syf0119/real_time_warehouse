package cn.syf.bean


import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.commons.lang3.StringUtils

/**
  **/

case class CanalEntity(
                        binlog: String,
                        cols: Map[String, ColValueType],
                        eventType: String,
                        exeTime: Long,
                        table: String
                      )

object CanalEntity {
  //解析表数据（每一行）
  def apply(str: String): CanalEntity = {

    val json: JSONObject = JSON.parseObject(str)
    val binlog: String = json.getString("binlog")
    val strJSONArray: String = json.getString("cols")
    val eventType: String = json.getString("eventType")
    val exeTimeStr: String = json.getString("exeTime")
    var exeTime: Long = 0L
    if(StringUtils.isNotEmpty(exeTimeStr)){
      exeTime = json.getLong("exeTime")
    }
    val table: String = json.getString("table")

    //cols jsonArray数据解析
    val map: Map[String, ColValueType] = ColValueType(strJSONArray)
    val entity = CanalEntity(binlog, map, eventType, exeTime, table)
    entity
  }

  def main(args: Array[String]): Unit = {

  }

}

