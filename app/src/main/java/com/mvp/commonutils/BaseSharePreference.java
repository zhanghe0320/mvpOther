package com.mvp.commonutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.mvp.base.MvpApplication;


/**
 * sp基础操作类
 */
public class BaseSharePreference {
    
    private SharedPreferences mSharePreferences;
    
    public void init(Context context, String sharedFileName) {
    
        if (!(context instanceof MvpApplication)) {
            throw new RuntimeException(
                    "you can't invoke this in other context but AppStoreApplication, in case memory leak");
        }
        if (TextUtils.isEmpty(sharedFileName)) {
            throw new RuntimeException("sharedFileName can't be null");
        }
        mSharePreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE);
    }
    
    public SharedPreferences getSharedPreferences() {
        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        return mSharePreferences;
    }
    
    public void putString(String key, String value) {

        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        Editor editor = mSharePreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    public void putFloat(String key, float value) {

        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        Editor editor = mSharePreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }
    
    public void putInt(String key, int value) {

        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        Editor editor = mSharePreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    
    public void putLong(String key, long value) {

        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        Editor editor = mSharePreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }
    
    public void putBoolean(String key, boolean value) {

        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        Editor editor = mSharePreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    
    public String getString(String key, String defValue) {
    
        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        return mSharePreferences.getString(key, defValue);
    }
    
    public boolean getBoolean(String key, boolean defValue) {
    
        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        return mSharePreferences.getBoolean(key, defValue);
    }
    
    public int getInt(String key, int defValue) {
    
        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        return mSharePreferences.getInt(key, defValue);
    }
    
    public float getFloat(String key, float defValue) {
    
        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        return mSharePreferences.getFloat(key, defValue);
    }
    
    public long getLong(String key, long defValue) {
    
        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        return mSharePreferences.getLong(key, defValue);
    }

    public void deleteKey(String keyName) {
        if (mSharePreferences == null) {
            throw new RuntimeException("SharedPreferences is not init", new Throwable());
        }
        mSharePreferences.edit().remove(keyName).apply();
    }
}
