package cn.xdf.wlyy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.FileOutputStream;
import java.util.List;

public class ExportJson2Excel {

    private static Logger logger = Logger.getLogger(ExportJson2Excel.class);

    public static void export2Excel(List<String> heads, List<String> names, JSONArray array, String fileName) {
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
}
