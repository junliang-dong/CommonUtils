package cn.xdf.wlyy.utils;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.sql.*;

/**
 * Created by dongjunliang on 2018-7-17.
 */
public class MySQLUtils {

    private static Logger logger = Logger.getLogger(MySQLUtils.class);

    public static Connection getConnection() {
        logger.info("Start to connect to mysql...");
        Connection connection = null;
        try {
            Class.forName(MyConfig.MySQL_Driver);
            connection = DriverManager.getConnection(MyConfig.MySQL_Url, MyConfig.MySQL_Username, MyConfig.MySQL_Password);
            logger.info("Connect successfully...");
        } catch (Exception e) {
            logger.error("An error occurred when trying to connect to mysql, errro message is like: " + e.getMessage());
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

    public static void checkDataWithMySql(Connection connection, Sheet sheet, String tableName, String idate, String account) {
        logger.info("Start checking data in mysql...");
        Statement statement = null;
        int count = 0;
        try {
            statement = connection.createStatement();
            String sql = "select * from "+ tableName + " where idate='" + idate + "' and account='" + account + "'";
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                int index = 0;
                boolean flag = false;
                for (Row row : sheet) {
                    boolean existInMysql = set.getString(4).equals(row.getCell(1).toString()) &&
                            set.getString(5).equals(row.getCell(2).toString()) &&
                            set.getString(6).equals(row.getCell(3).toString().toLowerCase());
                    if (index > 1 && index < sheet.getLastRowNum() && existInMysql) {
                        flag = true;
                        break;
                    }
                    index++;
                }
                if (!flag) {
                    count ++;
                    System.out.println(set.getString(6));
                }
            }
            logger.info("Finish checking data...");
        } catch (SQLException e) {
            logger.error("An error occurred when trying to check data in mysql, error message is like " + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(count);
        }
    }
}
