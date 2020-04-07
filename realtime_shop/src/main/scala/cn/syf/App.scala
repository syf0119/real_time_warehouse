package cn.syf


import java.util.Properties

import cn.syf.bean.CanalEntity
import cn.syf.config.GlobalConfig
import cn.syf.process.ProcessOrderData
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.time.Time
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

object App {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //2.设置事件时间
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //3.设置检查点机制
    env.enableCheckpointing(5000) //每5s钟制作一次检查点
    //设置检查点的保存目录
    env.setStateBackend(new FsStateBackend("hdfs://spring:8020/check"))
    //env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE) //默认模式
    //env.getCheckpointConfig.setCheckpointInterval(5000)//检查点的时间间隔
    env.getCheckpointConfig.setCheckpointTimeout(60000) //制作检查点的超时时间
    env.getCheckpointConfig.setFailOnCheckpointingErrors(false) //检查点制作失败，任务继续运行
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1) //最大线程数
    //取消任务的时候，保留检查点，注意：需要手动删除
    env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    //4.设置重启机器 :1.失败率重启；2.固定延迟重启策略，3.不重启策略，
    // 注意：如果不配置重启策略，但是配置了检查点机制，会无限重启
    env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.seconds(5)))
    //5.整合kafka
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", GlobalConfig.kafkaBroker)
    properties.setProperty("group.id", GlobalConfig.kafkaGroupId)
    properties.setProperty("enable.auto.commit", GlobalConfig.kafkaCommit)
    properties.setProperty("auto.commit.interval.ms", GlobalConfig.kafkaCommitMs)
    //---------------------pv uv  ip------------
    //    val flinkKafkaConsumer = new FlinkKafkaConsumer011[String](GlobalConfig.kafkaTopicClick, new SimpleStringSchema(), properties)
    //   flinkKafkaConsumer.setStartFromEarliest()
    //    val ds: DataStream[String] = env.addSource(flinkKafkaConsumer)

    // ProcessData.process(ds)
    //--------------------detail table sink to druid

    val kafkaConsumer: FlinkKafkaConsumer011[String] = new FlinkKafkaConsumer011[String](GlobalConfig.kafkaTopicCanal, new SimpleStringSchema(), properties)
    kafkaConsumer.setStartFromEarliest()

    val canalData: DataStream[CanalEntity] = env.addSource(kafkaConsumer)
      .map(CanalEntity(_))
      .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[CanalEntity] {
        var delayTime = 2000L
        var currentTime = 0L

        override def getCurrentWatermark: Watermark = {
          new Watermark(currentTime - delayTime)

        }

        override def extractTimestamp(element: CanalEntity, previousElementTimestamp: Long): Long = {
          val exeTime: Long = element.exeTime
          currentTime = math.max(exeTime, currentTime)
          exeTime
        }
      })

 // ProcessOrderData.hBaseProcess(canalData.filter(_.table == "shop.order_goods"))

   ProcessOrderData.druidProcess(canalData.filter(_.table == "shop.orders"))


    env.execute()
  }

}
