package com.it.like
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}

object CepTest {
  def main(args: Array[String]): Unit = {
val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)
    case class LoginEvent(userId: String, ip: String, eventType: String, weight: Int)

    val ds: DataStream[LoginEvent] = environment.fromCollection(List(
      LoginEvent("1", "192.168.0.1", "fail", 8),
      LoginEvent("1", "192.168.0.2", "fail", 9),

      LoginEvent("1", "192.168.0.3", "fail", 10),
      LoginEvent("1", "192.168.0.4", "fail", 10),
      LoginEvent("2", "192.168.10.10", "success", -1),
      LoginEvent("3", "192.168.10.10", "fail", 5),
      LoginEvent("3", "192.168.10.11", "fail", 6),
      LoginEvent("4", "192.168.10.10", "fail", 6),
      LoginEvent("4", "192.168.10.11", "fail", 7),
      LoginEvent("4", "192.168.10.12", "fail", 8),
      LoginEvent("5", "192.168.10.13", "success", 8),
      LoginEvent("5", "192.168.10.14", "success", 9),
      LoginEvent("5", "192.168.10.15", "success", 10),
      LoginEvent("6", "192.168.10.16", "fail", 6),
      LoginEvent("6", "192.168.10.17", "fail", 8),
      LoginEvent("6", "192.168.10.18", "fail", 8),
      LoginEvent("7", "192.168.10.18", "fail", 5),
      LoginEvent("6", "192.168.10.19", "fail", 10),
      LoginEvent("6", "192.168.10.19", "fail", 9)
    )
    )


    val pattern: Pattern[LoginEvent, LoginEvent] = Pattern.begin[LoginEvent]("hello")
      .where(_.eventType == "fail")
      .times(2)
      .optional



    val loginFailDataPattern : PatternStream[LoginEvent] = CEP.pattern( ds.keyBy(_.userId),pattern)

    val failResult: DataStream[Iterable[LoginEvent]] = loginFailDataPattern.select(ele => {
      println(1)

      val maybeEvents: Option[Iterable[LoginEvent]] = ele.get("hello")
      maybeEvents match {
        case Some(value) => value
      }
    })
    failResult.print()



    environment.execute()
  }

}
