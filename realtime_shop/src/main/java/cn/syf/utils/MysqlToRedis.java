package cn.syf.utils;

import cn.syf.bean.DimAreaDBEntity;
import cn.syf.bean.DimGoodsCatDBEntity;
import cn.syf.bean.DimGoodsDBEntity;
import cn.syf.bean.DimShopsDBEntity;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

class MysqlToRedis {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://summer:3306/shop", "root", "0119");
        Statement statement = connection.createStatement();
        Jedis redis = JedisUtil.getRedis();


        process(DimGoodsDBEntity.class, statement, "goods", redis);
        process(DimAreaDBEntity.class, statement, "areas", redis);
        process(DimShopsDBEntity.class, statement, "shops", redis);
        process(DimGoodsCatDBEntity.class, statement, "goods_cats", redis);


        statement.close();
        connection.close();
        redis.close();


    }

    private static void process(Class<?> clazz, Statement statement, String tableName, Jedis redis) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Field[] declaredFields = clazz.getDeclaredFields();

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        //"select goodsId,goodsName,goodsCatId,shopId from "+tableName;
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            sb.append(name + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" from " + tableName);


        ResultSet resultSet = statement.executeQuery(sb.toString());
        while (resultSet.next()) {
            Object instance = clazz.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            String id = resultSet.getString(1);
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String methodName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                Method method = clazz.getDeclaredMethod(methodName, String.class);
                method.invoke(instance, resultSet.getString(columnName));

            }

            String jsonString = JSON.toJSONString(instance);

            redis.hset(tableName, id, jsonString);

            System.out.println(jsonString);


        }

    }


}
/**
 * Method[] methods = clazz.getDeclaredMethods();
 * for (Method method : methods) {
 * String methodName = method.getName();
 * if(methodName.startsWith("set")){
 * System.out.println(methodName);
 * }
 * }
 */
