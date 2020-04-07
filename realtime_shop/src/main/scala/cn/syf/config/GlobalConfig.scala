package cn.syf.config

import com.typesafe.config.{Config, ConfigFactory}

object GlobalConfig {
 val config: Config = ConfigFactory.load("application.properties")
  val kafkaBroker= config.getString("bootstrap.servers")
  val kafkaGroupId=config.getString("group.id")
  val kafkaCommit=config.getString("enable.auto.commit")
  val kafkaCommitMs=config.getString("auto.commit.interval.ms")
  //val kafkaTopicClick=config.getString("auto.offset.reset")
  val kafkaTopicCanal= config.getString("input.topic.canal")
  val kafkaTopicClick=config.getString("input.topic.click_log")

  val kafkaTopicDruid=config.getString("output.topic")
  val hbaseZk=config.getString("hbase.zookeeper.quorum")
  val hbaseTable=config.getString("hbase.table.order")
  val hbaseFamily=config.getString("hbase.table.family")

  def main(args: Array[String]): Unit = {
    println(kafkaBroker)
  }

}
