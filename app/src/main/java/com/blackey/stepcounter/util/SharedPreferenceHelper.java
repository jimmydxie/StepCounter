package com.blackey.stepcounter.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {
    public String SHARED_FILE = "share_file";
    Context context;
    SharedPreferences sharedPreferences;

    public SharedPreferenceHelper(String SHARED_FILE, Context context) {
        this.SHARED_FILE = SHARED_FILE;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_FILE,
                Context.MODE_PRIVATE);
    }

    /**
     * 添加数据到sharedPreference中
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        if (value == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (long) value);
        } else {
            editor.putString(key, value.toString());
        }
        editor.commit();
    }

    /**
     * 获取sharedPreference中存储的数据
     */
    public Object get(String key, Object defValue) {
        if (defValue instanceof Integer) {
            return sharedPreferences.getInt(key, (int) defValue);
        } else if (defValue instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (boolean) defValue);
        } else if (defValue instanceof Float) {
            return sharedPreferences.getFloat(key, (float) defValue);
        } else if (defValue instanceof Long) {
            return sharedPreferences.getLong(key, (long) defValue);
        } else {
            return sharedPreferences.getString(key, defValue.toString());
        }
    }

    /**
     * 判断某个key是否存在
     */
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 删除某个键值对
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains(key)) {
            editor.remove(key);
        }
        editor.commit();
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


}
