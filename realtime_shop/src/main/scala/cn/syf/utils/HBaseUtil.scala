package cn.syf.utils

import cn.syf.config.GlobalConfig
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}

object HBaseUtil {
  private val configuration: Configuration = HBaseConfiguration.create()
  configuration.set("hbase.zookeeper.quorum", GlobalConfig.hbaseZk)
  private val connection: Connection = ConnectionFactory.createConnection(configuration)

  def put(tableName: String, family: String, rowkey: String, qualifier: String, value: String) {
var table: Table = null

    try {
      table = connection.getTable(TableName.valueOf(tableName))
      val put = new Put(rowkey.getBytes)
      put.add(family.getBytes, qualifier.getBytes, value.getBytes)
      table.put(put)
    } catch {
      case  e:Exception=>e.printStackTrace()
    } finally {

      if(null!=table){
        table.close()
      }
    }

  }
  def putMap(tableName: String, family: String, rowkey: String,map:Map[String,String]) {
    var table: Table = null
    try {
      table = connection.getTable(TableName.valueOf(tableName))

      for((qualifier,value)<-map){
        val put = new Put(rowkey.getBytes)
        put.add(family.getBytes, qualifier.getBytes, value.getBytes)
        table.put(put)

      }


    } catch {
      case e: Exception => e.printStackTrace()
    } finally {

      if (null != table) {
        table.close()
      }
    }
  }
}
