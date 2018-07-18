package cn.xdf.wlyy.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by dongjunliang on 2018-7-17.
 */
public class MySQLUtil {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(MyConfig.MySQL_Driver);
            connection = DriverManager.getConnection(MyConfig.MySQL_Url, MyConfig.MySQL_Username, MyConfig.MySQL_Password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
