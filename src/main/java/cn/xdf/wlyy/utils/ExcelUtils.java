package cn.xdf.wlyy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongjunliang on 2018-7-19.
 */
public class ExcelUtils {

    private static Logger logger = Logger.getLogger(ExcelUtils.class);

    public static JSONArray transferGaData(JSONObject data, List<String> heads) {

        JSONArray array = new JSONArray();
        try {
            logger.info("Start parsing GA data...");
            JSONArray rows = data.getJSONObject("data").getJSONArray("rows");
            //将数据与维度、指标一一对应
            for (int i = 0; i < rows.size(); i++) {
                int j = 0;
                JSONObject object = new JSONObject();
                for (int k = 0; k < rows.getJSONObject(i).getJSONArray("dimensions").size(); k++) {
                    object.put(heads.get(j++), rows.getJSONObject(i).getJSONArray("dimensions").getString(k));
                }
                for (int k = 0; k < rows.getJSONObject(i).getJSONArray("metrics").getJSONObject(0).getJSONArray("values").size(); k++) {
                    object.put(heads.get(j++), rows.getJSONObject(i).getJSONArray("metrics").getJSONObject(0).getJSONArray("values").getString(k));
                }
                array.add(object);
            }
        } catch (Exception e) {
            logger.error("An error occurred when trying to parse GA data, error message is like: " + e.getMessage());
        }
        logger.info("Finish parsing GA data...");
        return array;
    }

    public static List<String> getHeads(JSONObject data) {
        List<String> heads = new ArrayList<>();
        //解析维度列
        for (int i = 0; i < data.getJSONObject("columnHeader").getJSONArray("dimensions").size(); i++) {
            heads.add(data.getJSONObject("columnHeader").getJSONArray("dimensions").getString(i));
        }
        //解析指标列
        for (int i = 0; i < data.getJSONObject("columnHeader").getJSONObject("metricHeader").getJSONArray("metricHeaderEntries").size(); i++) {
            heads.add(data.getJSONObject("columnHeader").getJSONObject("metricHeader").getJSONArray("metricHeaderEntries").getJSONObject(i).getString("name"));
        }
        return heads;
    }

    public static List<String> getNames(JSONObject data) {
        List<String> names = new ArrayList<>();
        //解析维度列
        for (int i = 0; i < data.getJSONObject("columnHeader").getJSONArray("dimensions").size(); i++) {
            names.add(data.getJSONObject("columnHeader").getJSONArray("dimensions").getString(i));
        }
        //解析指标列
        for (int i = 0; i < data.getJSONObject("columnHeader").getJSONObject("metricHeader").getJSONArray("metricHeaderEntries").size(); i++) {
            names.add(data.getJSONObject("columnHeader").getJSONObject("metricHeader").getJSONArray("metricHeaderEntries").getJSONObject(i).getString("name"));
        }
        return names;
    }

    public static void exportJSON2Excel(List<String> heads, List<String> names, JSONArray array, String fileName) {
        logger.info("Start writing into file -> " + fileName);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.BLUE_GREY.getIndex());
        HSSFRow titleRow = sheet.createRow(0);

        for (int i = 0; i < heads.size(); i++) {
            titleRow.createCell(i).setCellValue(heads.get(i));
        }
        titleRow.setRowStyle(style);

        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                JSONObject json = array.getJSONObject(i); // 遍历 jsonArray
                for (int j = 0; j < names.size(); j++) {
                    row.createCell(j).setCellValue(json.get(names.get(j)).toString());
                }
            }
        }

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(fileName);
            workbook.write(stream);
            logger.info("Finish writing file -> " + fileName);
        } catch (Exception e) {
            logger.error("An error occurred when trying to write file, error message is like: " + e.getMessage());
        } finally {
            try {
                workbook.close();
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e2) {
                logger.error("An error occurred when trying to close stream, error message is like: " + e2.getMessage());
            }
        }
    }

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
            MySQLUtil.checkDataWithMySql(connection, sheet, tableName, idate, account);

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
}
