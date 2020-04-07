package cn.syf;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;


import java.util.*;

public class ProductLog {
    public static void main(String[] args) throws InterruptedException {
        //获取logger
        Logger logger = Logger.getLogger("logFile");
        //生产日志数据，1生成一次
        while (true) {

            List<JSONObject> logs = getLogs();
            for (JSONObject log : logs) {
              KafkaUtil.sendData("click_log",log.toString());
                System.out.println(log);
              Thread.sleep(3000);
            }

        }

    }
    //生成日志
    private static List<JSONObject> getLogs() {

        List<JSONObject> list = new ArrayList<>();
        //数据字段请参考《用户行为日志相关接口文档信息》-用户行为日志相关接口文档信息
        String referer = "http://star.itheima.com.cn/135/1351735_3.html";
        String linkId = "1.1.16.0.5.KlBK557-10-6z7Ct";
        String ip = getIp();   //ip统计
        String trackTime = new Date().getTime() + ""; //事件时间
        String guid = UUID.randomUUID().toString(); //uv
        String id = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();
        String attachedInfo = "login success";// cep邮件告警
        List<String> urls = getUrl();
        for (String url : urls) {
            JSONObject json = new JSONObject();
            json.put("referer", referer);
            json.put("linkId", linkId);
            json.put("ip", ip);
            json.put("trackTime", trackTime);
            json.put("guid", guid);
            json.put("id", id);
            json.put("sessionId", sessionId);
            json.put("attachedInfo", attachedInfo);
            json.put("url", url);
            list.add(json);
        }

        return list;
    }

    private static List<String> getUrl() {

        ArrayList<String> list = new ArrayList<>();
        int i = new Random().nextInt(4);
        switch (i) {
            case 0:
                list.add("http://order.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                break;
            case 1:
                list.add("http://order.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                list.add("http://goods.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                break;
            case 2:
                list.add("http://order.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                list.add("http://goods.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                list.add("http://cart.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                break;
            case 3:
                list.add("http://order.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                list.add("http://goods.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                list.add("http://cart.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                list.add("http://pay.itcast.cn/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.itcast.cn/item/33678857");
                break;
        }

        return list;
    }

    private static String getIp() {

        //255.225.225.255
        int i1 = new Random().nextInt(255);
        int i2 = new Random().nextInt(255);
        int i3 = new Random().nextInt(255);
        int i4 = new Random().nextInt(255);
        return i1 + "." + i2 + "." + i3 + "." + i4;
    }
}
