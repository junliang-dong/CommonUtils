package cn.xdf.wlyy.utils;

import java.util.ResourceBundle;

/**
 * Created by dongjunliang on 2018-6-22.
 */
public class MyConfig {

    public static String getString(String s) {
        return getResourceBundle().getString(s);
    }

    static ResourceBundle bundle;

    private static ResourceBundle getResourceBundle() {
        try {
            if (bundle == null) {
                bundle = ResourceBundle.getBundle("dbConfig");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundle;
    }

    public static String MySQL_Driver = getString("mysql-driver");
    public static String MySQL_Url = getString("mysql-url");
    public static String MySQL_Username = getString("mysql-username");
    public static String MySQL_Password = getString("mysql-password");
}
