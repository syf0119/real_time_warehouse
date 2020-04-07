package cn.syf.utils

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat

object DateUtil {

def getToday:String={
  val dateFormat: FastDateFormat = FastDateFormat.getInstance("yyyyMMdd")
  dateFormat.format(new Date())
}


}
