package cn.xdf.wlyy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

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
}
