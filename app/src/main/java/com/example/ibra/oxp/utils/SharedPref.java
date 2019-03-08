package com.example.ibra.oxp.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.ibra.oxp.R;

public class SharedPref {
    private SharedPreferences sharedPrefObj;
    private SharedPreferences.Editor editor;
    private int preMode = 0;
    private Context prefContext;
    private String MyPrefName;
    private boolean isFirst;

    public SharedPref(Context mContext) {
        this.prefContext = mContext;
        MyPrefName = mContext.getString(R.string.app_name);
        sharedPrefObj = prefContext.getSharedPreferences(MyPrefName, preMode);
        editor = sharedPrefObj.edit();
        editor.commit();
    }

    public void writeValue(String key, String value){
        editor = sharedPrefObj.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String readValue(String key, String defaultt){
        return sharedPrefObj.getString(key, defaultt);
    }

    public void writeValue(String key, Boolean value){
        editor = sharedPrefObj.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean readValue(String key, Boolean defaultt){
        return sharedPrefObj.getBoolean(key, defaultt);
    }

    public void writeValue(String key, int value){
        editor = sharedPrefObj.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int readValue(String key, int defaultt){
        return sharedPrefObj.getInt(key, defaultt);
    }
}
