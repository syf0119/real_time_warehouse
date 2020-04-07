package cn.syf.bean

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature

import scala.beans.BeanProperty

/**
  **/
case class OrderBean(
                      @BeanProperty orderId: Long,
                      @BeanProperty orderNo: String,
                      @BeanProperty shopId: Long,
                      @BeanProperty userId: Long,
                      @BeanProperty orderStatus: Int,
                      @BeanProperty goodsMoney: Double,
                      @BeanProperty deliverType: Int,
                      @BeanProperty deliverMoney: Double,
                      @BeanProperty totalMoney: Double,
                      @BeanProperty realTotalMoney: Double,
                      @BeanProperty payType: Int,
                      @BeanProperty isPay: Int,
                      @BeanProperty areaId: Int,
                      @BeanProperty areaIdPath: String,
                      @BeanProperty userName: String,
                      @BeanProperty userAddress: String,
                      @BeanProperty userPhone: String,
                      @BeanProperty orderScore: Int,
                      @BeanProperty isInvoice: Int,
                      @BeanProperty invoiceClient: String,
                      @BeanProperty orderRemarks: String,
                      @BeanProperty orderSrctiny: Int,
                      @BeanProperty needPay: Double,
                      @BeanProperty payRand: Int,
                      @BeanProperty orderType: Int,
                      @BeanProperty isRefund: Int,
                      @BeanProperty isAppraise: Int,
                      @BeanProperty cancelReason: Int,
                      @BeanProperty rejectReason: Int,
                      @BeanProperty rejectOtherReason: String,
                      @BeanProperty isClosed: Int,
                      @BeanProperty goodsSearchKeys: String,
                      @BeanProperty orderunique: String,
                      @BeanProperty receiveTime: String,
                      @BeanProperty deliveryTime: String,
                      @BeanProperty tradeNo: String,
                      @BeanProperty dataFlag: Int,
                      @BeanProperty createTime: String,
                      @BeanProperty settlementId: Int,
                      @BeanProperty commissionFee: Double,
                      @BeanProperty scoreMoney: Double,
                      @BeanProperty useScore: Int,
                      @BeanProperty orderCode: String,
                      @BeanProperty extraJson: String,
                      @BeanProperty orderCodeTargetId: Int,
                      @BeanProperty noticeDeliver: Int,
                      @BeanProperty invoiceJson: String,
                      @BeanProperty lockCashMoney: Double,
                      @BeanProperty payTime: String,
                      @BeanProperty isBatch: Int,
                      @BeanProperty totalPayFee: Int
                    ){
  override def toString: String = {

    JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect)
  }
}

object OrderBean {

  def apply(map: Map[String, ColValueType]): OrderBean = {
    OrderBean(
      map.get("orderId").get.getColValue.toLong,
      map.get("orderNo").get.getColValue,
      map.get("shopId").get.getColValue.toLong,
      map.get("userId").get.getColValue.toLong,
      map.get("orderStatus").get.getColValue.toInt,
      map.get("goodsMoney").get.getColValue.toDouble,
      map.get("deliverType").get.getColValue.toInt,
      map.get("deliverMoney").get.getColValue.toDouble,
      map.get("totalMoney").get.getColValue.toDouble,
      map.get("realTotalMoney").get.getColValue.toDouble,
      map.get("payType").get.getColValue.toInt,
      map.get("isPay").get.getColValue.toInt,
      map.get("areaId").get.getColValue.toInt,
      map.get("areaIdPath").get.getColValue,
      map.get("userName").get.getColValue,
      map.get("userAddress").get.getColValue,
      map.get("userPhone").get.getColValue,
      map.get("orderScore").get.getColValue.toInt,
      map.get("isInvoice").get.getColValue.toInt,
      map.get("invoiceClient").get.getColValue,
      map.get("orderRemarks").get.getColValue,
      map.get("orderSrc").get.getColValue.toInt,
      map.get("needPay").get.getColValue.toDouble,
      map.get("payRand").get.getColValue.toInt,
      map.get("orderType").get.getColValue.toInt,
      map.get("isRefund").get.getColValue.toInt,
      map.get("isAppraise").get.getColValue.toInt,
      map.get("cancelReason").get.getColValue.toInt,
      map.get("rejectReason").get.getColValue.toInt,
      map.get("rejectOtherReason").get.getColValue,
      map.get("isClosed").get.getColValue.toInt,
      map.get("goodsSearchKeys").get.getColValue,
      map.get("orderunique").get.getColValue,
      map.get("receiveTime").get.getColValue,
      map.get("deliveryTime").get.getColValue,
      map.get("tradeNo").get.getColValue,
      map.get("dataFlag").get.getColValue.toInt,
      map.get("createTime").get.getColValue,
      map.get("settlementId").get.getColValue.toInt,
      map.get("commissionFee").get.getColValue.toDouble,
      map.get("scoreMoney").get.getColValue.toDouble,
      map.get("useScore").get.getColValue.toInt,
      map.get("orderCode").get.getColValue,
      map.get("extraJson").get.getColValue,
      map.get("orderCodeTargetId").get.getColValue.toInt,
      map.get("noticeDeliver").get.getColValue.toInt,
      map.get("invoiceJson").get.getColValue,
      map.get("lockCashMoney").get.getColValue.toDouble,
      map.get("payTime").get.getColValue,
      map.get("isBatch").get.getColValue.toInt,
      map.get("totalPayFee").get.getColValue.toInt
    )

  }

}
