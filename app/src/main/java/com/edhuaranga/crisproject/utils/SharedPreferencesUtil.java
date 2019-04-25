package com.edhuaranga.crisproject.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.edhuaranga.crisproject.Constants;
import com.edhuaranga.crisproject.R;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class SharedPreferencesUtil {

    private static SharedPreferencesUtil instance = null;

    private SharedPreferences preferences;
    private SharedPreferencesUtil(Context context){
        preferences = context.getSharedPreferences( context.getString(R.string.preference_file_key), Application.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesUtil getInstance(Context context){
        if(instance == null)
            instance = new SharedPreferencesUtil(context);
        return instance;
    }

    public String getValue(@NonNull Keys key){
        return preferences.getString(key.toString(),null);
    }

    public int getIntValue(@NonNull Keys key){
        return preferences.getInt(key.toString(), 0);
    }

    public SharedPreferencesUtil setValue(@NonNull Keys key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key.toString(),value);
        editor.apply();
        return this;
    }

    public SharedPreferencesUtil setValue(@NonNull Keys key, int value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key.toString(),value);
        editor.apply();
        return this;
    }

    public SharedPreferencesUtil setValue(String key, int value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ALARM_"+key,value);
        editor.apply();
        return this;
    }

    public enum Keys{
        USER_BACKEND_ID,
        USER_NAME,
        USER_MAIL,
        USER_CITY,
        USER_BIRTHDAY,
        LAST_LOGIN,
        ALARM_ONE_ID,
        ALARM_ONE_TITLE,
        ALARM_ONE_HOUR,
        ALARM_ONE_MINUTE,
        ALARM_ONE_STATUS,
        ALARM_TWO_ID,
        ALARM_TWO_TITLE,
        ALARM_TWO_HOUR,
        ALARM_TWO_MINUTE,
        ALARM_TWO_STATUS,
        ALARM_THREE_ID,
        ALARM_THREE_TITLE,
        ALARM_THREE_HOUR,
        ALARM_THREE_MINUTE,
        ALARM_THREE_STATUS,
    }


}
