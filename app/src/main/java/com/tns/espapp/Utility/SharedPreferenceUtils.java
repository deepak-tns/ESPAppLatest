package com.tns.espapp.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import com.tns.espapp.AppConstraint;

/**
 * Created by root on 2/22/17.
 */

public class SharedPreferenceUtils {
    static SharedPreferenceUtils sharedPreferenceUtils;
    static SharedPreferences sharedPreferences;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private SharedPreferenceUtils() {

    }

    public static SharedPreferenceUtils getInstance() {
        if (sharedPreferenceUtils == null) {
            sharedPreferenceUtils = new SharedPreferenceUtils();
        }
        return sharedPreferenceUtils;
    }

    public SharedPreferences getSharedPreferences() {

        if (sharedPreferences == null) {
            if(context!=null)
            sharedPreferences = context.getSharedPreferences(AppConstraint.APPNAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }


    public String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();

    }

    public void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    public void putInteger(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    public int getInteger(String key) {
        return getSharedPreferences().getInt(key, 1);
    }

    public void clearALl() {
        getSharedPreferences().edit().clear().commit();
    }



}
