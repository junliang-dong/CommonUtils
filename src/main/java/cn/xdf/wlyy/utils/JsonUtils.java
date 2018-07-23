package cn.xdf.wlyy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import java.io.*;


public class JsonUtils {

    private static Logger logger = Logger.getLogger(JsonUtils.class);

    public static JSONObject readJSONObject(String filePath) throws IOException {
        logger.info("Start reading file -> " + filePath);
        InputStream is = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        StringBuilder s = new StringBuilder();
        String tmpStr;
        JSONObject respJSON = new JSONObject();
        try {
            is = new FileInputStream(filePath);
            isr = new InputStreamReader(is, "GB2312");
            br = new BufferedReader(isr);
            while ((tmpStr = br.readLine()) != null) {
                s.append(tmpStr);
            }
            respJSON = JSON.parseObject(s.toString());
            logger.info("Finish reading file -> " + filePath);
        } catch (Exception e) {
            logger.error("An error occurred when trying to read file -> " + filePath + ", error message is like: " + e.getMessage());
        } finally {
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
            if (isr != null) {
                isr.close();
            }
        }
        return respJSON;
    }
}
