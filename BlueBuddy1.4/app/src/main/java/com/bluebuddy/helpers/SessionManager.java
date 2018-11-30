package com.bluebuddy.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

import static com.bluebuddy.app.AppConfig.*;


public class SessionManager {
    // LogCat tag
    public static String TAG = SessionManager.class.getSimpleName();

    // Shared Preference
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context scontext;

    public SessionManager(Context context) {
        this.scontext = context;
        pref = scontext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setPref(String key, String value) {
        editor.putString(key, value);

        // commit changes
        editor.commit();

        Log.d(TAG, "Preference updated");
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public void setLogin(Map<String, String> d) {
        /*
        editor.putBoolean(KEY_IS_LOGGED_IN, Boolean.valueOf(d.get(KEY_IS_LOGGED_IN)));
        editor.putString(ACCESS_TOKEN, d.get(ACCESS_TOKEN));
        editor.putString(USERID, d.get(USERID));
        editor.putString(FIRSTNAME, d.get(FIRSTNAME));
        editor.putString(LASTNAME, d.get(LASTNAME));
        editor.putString(EMAIL, d.get(EMAIL));
        editor.putString(LOCATION, d.get(LOCATION));
        editor.putString(PROFILEIMAGE, d.get(PROFILEIMAGE));
        editor.putString(LOGINWITH, d.get(LOGINWITH));

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
        */
    }

    public void setLogout() {
        editor.clear();
        editor.commit();
       // Log.d("SessionManager", "Preference deleted");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}