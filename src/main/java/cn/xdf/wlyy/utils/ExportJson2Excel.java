package cn.xdf.wlyy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.FileOutputStream;
import java.util.List;

public class ExportJson2Excel {

    public static void export2Excel(List<String> heads, List<String> names, JSONArray array, String fileName) {
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

        try {
            FileOutputStream stream = new FileOutputStream(fileName);
            workbook.write(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
