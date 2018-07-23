package cn.xdf.wlyy.domain;

import cn.xdf.wlyy.utils.ExcelUtils;
import cn.xdf.wlyy.utils.JsonUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Application {

//    public static void main(String[] args) throws SQLException {
//        String depart = "英联邦";
//        String tableName = "dict_dep";
//        String select_sql = "select DISTINCT account, depart, cplan, eplan from " + tableName + " where depart='" + depart + "'";
//        Connection connection = MySQLUtil.getConnection();
//        Statement statement = connection.createStatement();
//        ResultSet select_rs = statement.executeQuery(select_sql);
//        while (select_rs.next()) {
//            String account = select_rs.getString(1);
//            String cplan = select_rs.getString(3);
//            String eplan = select_rs.getString(4);
//            if (eplan.indexOf("yinglianbang") < 0) {
//                String update_sql = "update " + tableName + " set eplan='" + eplan.replace("beijing", "beijing_yinglianbang") + "' where account='" + account + "' and depart='" + depart + "' and cplan='" + cplan + "'";
//                System.out.println(update_sql);
//                Statement update_statement = connection.createStatement();
//                update_statement.execute(update_sql);
//            }
//        }
//    }

    public static void main(String[] args) {
        String path = "D:\\WorkSpace\\data\\gaData.txt";
        try {
            JSONObject data = JsonUtils.readJSONObject(path);
            List<String> heads = ExcelUtils.getHeads(data);
            List<String> names = ExcelUtils.getNames(data);
            JSONArray array = ExcelUtils.transferGaData(data, heads);
            ExcelUtils.exportJSON2Excel(heads, names, array, "D:\\WorkSpace\\data\\transData" + UUID.randomUUID().toString() + ".xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
