package com.bluebuddy.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SHARED_PREF_NAME = "FCMSharedPref";
    public static final String TAG_TOKEN = "firebase_token";

    private static SharedPrefManager mInstance;
    private static Context mCtx;
    private SharedPreferences.Editor editor;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    public void setFirebaseLogout() {
        editor.clear();
        editor.commit();
        // Log.d("SessionManager", "Preference deleted");
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN, "");
    }

}