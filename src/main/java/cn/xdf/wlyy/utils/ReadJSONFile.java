package cn.xdf.wlyy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;


public class ReadJSONFile {

    public static JSONObject readJSONObject(String filePath) throws IOException {
        InputStream is = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        StringBuilder s = new StringBuilder();
        String tmpStr;
        JSONObject respJSON = new JSONObject();
        try {
            is = new FileInputStream(filePath);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            while ((tmpStr = br.readLine()) != null) {
                s.append(tmpStr);
            }
            respJSON = JSON.parseObject(s.toString());
        } catch (Exception e) {
            e.printStackTrace();
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
