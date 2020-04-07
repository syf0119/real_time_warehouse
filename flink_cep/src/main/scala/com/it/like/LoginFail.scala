package com.it.like

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

object LoginFail {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val loginEventStream= env.fromCollection(List(
      LoginEvent(1, "192.168.0.1", "fail", 1558430842),
      LoginEvent(1, "192.168.0.2", "fail", 1558430843),
      LoginEvent(1, "192.168.0.3", "fail", 1558430844),
      LoginEvent(2, "192.168.10.10", "success", 1558430845)
    ))
     .assignAscendingTimestamps(_.eventTime*1000)
      .keyBy(_.ip)

  }

}
case class LoginEvent(userId: Long, ip: String, eventType: String, eventTime: Long)

class MatchFunction extends KeyedProcessFunction[Long, LoginEvent, LoginEvent] {
  // 定义状态变量KeyedProcessFunction
  lazy val loginState: ListState[LoginEvent] = getRuntimeContext.getListState(
    new ListStateDescriptor[LoginEvent]("saved login", classOf[LoginEvent]))
  override def processElement(login: LoginEvent,
                              context: KeyedProcessFunction[Long, LoginEvent,
                                LoginEvent]#Context, out: Collector[LoginEvent]): Unit = {

    if (login.eventType == "fail") {
      loginState.add(login)
    }
    // 注册定时器，触发事件设定为2秒后
    context.timerService.registerEventTimeTimer(login.eventTime + 2 * 1000)
  }
  override def onTimer(timestamp: Long,
                       ctx: KeyedProcessFunction[Long, LoginEvent,
                         LoginEvent]#OnTimerContext, out: Collector[LoginEvent]): Unit = {
    val allLogins: ListBuffer[LoginEvent] = ListBuffer()
    import scala.collection.JavaConversions._
    for (login <- loginState.get) {
      allLogins += login
    }
    loginState.clear()
    if (allLogins.length > 1) {
      out.collect(allLogins.head)
    }
  }

}
