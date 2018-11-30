package com.bluebuddy.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class RegPrefHelper {

    static RegPrefHelper _instance;
    private static String SHARE_REGISTRATION_INFO = "registration_info";

    public static String SHARE_KEY_NAME = "name";
    public static String SHARE_KEY_EMAIL = "email";
    public static String SHARE_KEY_MOBILE = "mobile";
    public static String SHARE_KEY_PASS = "pass";
    public static String SHARE_KEY_AVATA = "avata";
    public static String SHARE_KEY_UID = "uid";

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    public static RegPrefHelper instance(Context context) {
        if (_instance == null) {
            _instance = new RegPrefHelper();
            _instance.configSessionUtils(context);
        }
        return _instance;
    }

    public static RegPrefHelper instance() {
        return _instance;
    }

    public void configSessionUtils(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(SHARE_REGISTRATION_INFO, Activity.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();
    }

    public void storeValueString(String key, String value) {
        sharedPrefEditor.putString(key, value);
        sharedPrefEditor.commit();
    }

    public String fetchValueString(String key) {
        return sharedPref.getString(key, null);
    }
}