package cn.xdf.wlyy.domain;

import cn.xdf.wlyy.utils.ExcelUtils;
import cn.xdf.wlyy.utils.ExportJson2Excel;
import cn.xdf.wlyy.utils.ReadJSONFile;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Application {

        public static void main(String[] args) {
        String path = "D:\\WorkSpace\\data\\gaData.txt";
        try {
            JSONObject data = ReadJSONFile.readJSONObject(path);
            List<String> heads = ExcelUtils.getHeads(data);
            List<String> names = ExcelUtils.getNames(data);
            JSONArray array = ExcelUtils.transferGaData(data, heads);
            System.out.println("data: " + array);
            ExportJson2Excel.export2Excel(heads, names, array, "D:\\WorkSpace\\data\\transData" + UUID.randomUUID().toString() + ".xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        String path = "D:\\WorkSpace\\data\\gaData.txt";
//        try {
//            JSONObject data = ReadJSONFile.readJSONObject(path);
//            List<String> heads = new ArrayList<String>();
//            List<String> names = new ArrayList<String>();
//            JSONArray array = new JSONArray();
//            for (int i = 0; i < data.getJSONObject("columnHeader").getJSONArray("dimensions").size(); i++) {
//                heads.add(data.getJSONObject("columnHeader").getJSONArray("dimensions").getString(i));
//                names.add(data.getJSONObject("columnHeader").getJSONArray("dimensions").getString(i));
//            }
//            for (int i = 0; i < data.getJSONObject("columnHeader").getJSONObject("metricHeader").getJSONArray("metricHeaderEntries").size(); i++) {
//                heads.add(data.getJSONObject("columnHeader").getJSONObject("metricHeader").getJSONArray("metricHeaderEntries").getJSONObject(i).getString("name"));
//                names.add(data.getJSONObject("columnHeader").getJSONObject("metricHeader").getJSONArray("metricHeaderEntries").getJSONObject(i).getString("name"));
//            }
//            JSONArray rows = data.getJSONObject("data").getJSONArray("rows");
//            for (int i = 0; i < rows.size(); i++) {
//                int j = 0;
//                JSONObject object = new JSONObject();
//                for (int k = 0; k < rows.getJSONObject(i).getJSONArray("dimensions").size(); k++) {
//                    object.put(heads.get(j++), rows.getJSONObject(i).getJSONArray("dimensions").getString(k));
//                }
//                for (int k = 0; k < rows.getJSONObject(i).getJSONArray("metrics").getJSONObject(0).getJSONArray("values").size(); k++) {
//                    object.put(heads.get(j++), rows.getJSONObject(i).getJSONArray("metrics").getJSONObject(0).getJSONArray("values").getString(k));
//                }
//                array.add(object);
//            }
//            System.out.println("heads: " + heads.toString());
//            System.out.println("names: " + names.toString());
//            System.out.println(array.toJSONString());
//            ExportJson2Excel.export2Excel(heads, names, array, "D:\\WorkSpace\\data\\transData" + UUID.randomUUID().toString() + ".xls");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
