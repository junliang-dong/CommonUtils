package cn.xdf.wlyy.utils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by dongjunliang on 2018-7-17.
 */
public class ReadExcelFile {

    private static Logger logger = Logger.getLogger(ReadExcelFile.class);

    public static void readExcel(String path, String tableName, String idate, String account) throws IOException {
        logger.info("Start reading excel file -> " + path);
        ZipSecureFile.setMinInflateRatio(0);
        String fileType = path.substring(path.lastIndexOf(".") + 1);
        InputStream is = null;
        Workbook workbook = null;
        Connection connection = null;
        try {
            is = new FileInputStream(path);
            if ("xls".equals(fileType)) {
                workbook = new HSSFWorkbook(is);
            } else if ("xlsx".equals(fileType)) {
                workbook = new XSSFWorkbook(is);
            } else {
                return;
            }

            Sheet sheet = workbook.getSheetAt(0);
            connection = MySQLUtil.getConnection();
            checkDataWithMySql(connection, sheet, tableName, idate, account);

        } catch (Exception e) {
            logger.error("An error occurred when trying to read excel file -> " + path + ", error message is like " + e.getMessage());
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (is != null) {
                is.close();
            }
            MySQLUtil.closeConnection(connection);
        }
    }

    private static void checkDataWithMySql(Connection connection, Sheet sheet, String tableName, String idate, String account) {
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
