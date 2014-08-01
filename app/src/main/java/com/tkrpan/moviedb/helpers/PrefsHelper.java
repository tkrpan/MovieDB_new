package com.tkrpan.moviedb.helpers;

import android.preference.PreferenceManager;

import com.tkrpan.moviedb.MyApplication;

/**
 * Created by tomislav on 7/29/14. Helper for PreferenceManager
 */

public class PrefsHelper {

    public static void setBooleanValue(String key, Boolean value){

        PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext())
                .edit().putBoolean(key, value).commit();
    }

    public static Boolean getBooleanValue(String key){

        Boolean value = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext())
                .getBoolean(key, false);

        return value;
    }
}
