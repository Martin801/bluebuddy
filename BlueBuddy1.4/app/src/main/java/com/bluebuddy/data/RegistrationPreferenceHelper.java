package com.bluebuddy.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.bluebuddy.models.User;

public class RegistrationPreferenceHelper {

    private static RegistrationPreferenceHelper instance = null;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private static String SHARE_REGISTRATION_INFO = "registration_info";

    private static String SHARE_KEY_NAME = "name";
    private static String SHARE_KEY_EMAIL = "email";
    private static String SHARE_KEY_AVATA = "avata";
    private static String SHARE_KEY_UID = "uid";

    private RegistrationPreferenceHelper() {
    }

    public static RegistrationPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RegistrationPreferenceHelper();
            preferences = context.getSharedPreferences(SHARE_REGISTRATION_INFO, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return instance;
    }

    public void saveRegistrationInfo(User user) {
        editor.putString(SHARE_KEY_NAME, user.name);
        editor.putString(SHARE_KEY_EMAIL, user.email);
        editor.putString(SHARE_KEY_AVATA, user.avata);
        editor.putString(SHARE_KEY_UID, StaticConfig.UID);
        editor.apply();
    }

    public User getRegistrationInfo() {
        String userName = preferences.getString(SHARE_KEY_NAME, "");
        String email = preferences.getString(SHARE_KEY_EMAIL, "");
        String avatar = preferences.getString(SHARE_KEY_AVATA, "default");

        User user = new User();
        user.name = userName;
        user.email = email;
        user.avata = avatar;

        return user;
    }

    public String getUID() {
        return preferences.getString(SHARE_KEY_UID, "");
    }
}
