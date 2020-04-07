package cn.syf;

import java.sql.*;

public class PhoenixTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        Connection connection = DriverManager.getConnection("jdbc:phoenix:summer:2181");

        String sql="select * from \"dwd_order_detail\"";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <=columnCount; i++) {
                System.out.print(resultSet.getString(i)+"  ");
            }
            System.out.println();
        }
        resultSet.close();
        statement.close();
        connection.close();


    }
}
