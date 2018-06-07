package com.cmpe492.attendancesystem;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * A main Class which extends Application. Used to have static values.
 */
public class AppGlobals extends Application {

    private static Context sContext;
    public static final String KEY_NAME = "name";
    public static final String KEY_ROLL_NO = "roll_no";
    public static final String STUDENT_LOGIN = "is_login";
    public static final String CLASSES = "CLASSES";
    public static final String STUDENT = "STUDENT";
    public static final String MARKED_ATTENDANCE = "MARKED_ATTENDANCE";

    // Username and password for teacher login
    public static final String KEY_USERNAME = "test@test.com";
    public static final String KEY_PASSWORD = "test1122";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    // get default sharedPreferences.
    private static SharedPreferences getPreferenceManager() {
        return PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static void saveDataToSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getStringFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(key, "");
    }

    public static void saveLogin(boolean value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(STUDENT_LOGIN, value).apply();
    }

    public static boolean isLogin() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(STUDENT_LOGIN, false);
    }
}
