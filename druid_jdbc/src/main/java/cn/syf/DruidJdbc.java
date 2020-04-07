package cn.syf;

import java.sql.*;

public class DruidJdbc {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.calcite.avatica.remote.Driver");
        Connection conn = DriverManager.getConnection("jdbc:avatica:remote:url=http://autumn:8888/druid/v2/sql/avatica/");
        Statement statement = conn.createStatement();
        String sql="SELECT * FROM \"metrics-kafka\"";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            String s1 = resultSet.getString(1);
            String s2 = resultSet.getString("user");
            System.out.println(s1+"==="+s2);
        }
    }
}
