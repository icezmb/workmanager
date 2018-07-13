package com.yonyou.chaoke.base.esn.workmanager;

import java.util.HashMap;
import java.util.Map;

/**
 * work之间传递参数使用的对象
 * 使用HashMap实现
 * TODO 优化WorkData的实现，避免强制类型转换的出现
 */
public class WorkData {

    private Map<String, Object> mValues = new HashMap();

    public Map getAll() {
        return mValues;
    }

    public void putAll(Map<String, Object> values) {
        mValues.putAll(values);
    }

    public void putAll(WorkData workData) {
        if (workData == null) {
            return;
        }
        mValues.putAll(workData.getAll());
    }

    public boolean getBoolean(String name) {
       return (boolean)mValues.get(name);
    }

    public int getInt(String name) {
        return (int)mValues.get(name);
    }

    public String getString(String name) {
        return (String)mValues.get(name);
    }

    public float getFloat(String name) {
        return (float)mValues.get(name);
    }

    public double getDouble(String name) {
        return (double)mValues.get(name);
    }

    public long getLong(String name) {
        return (long)mValues.get(name);
    }

    public char getChar(String name) {
        return (char)mValues.get(name);
    }

    public Object get(String name) {
        return mValues.get(name);
    }

    public void putBoolean(String name, boolean value) {
        mValues.put(name, value);
    }

    public void putInt(String name, int value) {
        mValues.put(name, value);
    }

    public void putString(String name, String value) {
        mValues.put(name, value);
    }

    public void putFloat(String name, float value) {
        mValues.put(name, value);
    }

    public void putDouble(String name, double value) {
        mValues.put(name, value);
    }

    public void putLong(String name, long value) {
        mValues.put(name, value);
    }

    public void putChar(String name, char value) {
        mValues.put(name, value);
    }

    public void put(String name, Object value) {
        mValues.put(name, value);
    }

}
