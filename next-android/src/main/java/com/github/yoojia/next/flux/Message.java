package com.github.yoojia.next.flux;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 陈小锅 (yoojiachen@gmail.com)
 * @since 1.0
 */
public class Message {

    public final Bundle data = new Bundle();
    
    protected Message(){}

    protected Message(Bundle data) {
        this.data.putAll(data);
    }

    public int getInt(String key, int defaultValue) {
        return data.getInt(key, defaultValue);
    }

    public int getInt(String key) {
        return data.getInt(key);
    }

    public long getLong(String key, long defaultValue) {
        return data.getLong(key, defaultValue);
    }

    public long getLong(String key) {
        return data.getLong(key);
    }

    public float getFloat(String key, float defaultValue) {
        return data.getFloat(key, defaultValue);
    }

    public float getFloat(String key) {
        return data.getFloat(key);
    }

    public double getDouble(String key, double defaultValue) {
        return data.getDouble(key, defaultValue);
    }

    public double getDouble(String key) {
        return data.getDouble(key);
    }

    public String getString(String key, String defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return data.getString(key, defaultValue);
        }else{
            final String value = data.getString(key);
            return value == null ? defaultValue : value;
        }
    }

    public String getString(String key) {
        return data.getString(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return data.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key){
        return data.getBoolean(key);
    }

    public Bundle getBundle(String key, Bundle defaultValue) {
        final Bundle value = data.getBundle(key);
        return value == null ? defaultValue : value;
    }

    public Bundle getBundle(String key) {
        return getBundle(key, new Bundle(0));
    }

    public ArrayList<? extends Parcelable> getParcelableArrayList(String key, ArrayList<? extends Parcelable> defaultValue) {
        ArrayList<? extends Parcelable> value =  data.getParcelableArrayList(key);
        return value == null ? defaultValue : value;
    }

    public ArrayList<? extends Parcelable> getParcelableArrayList(String key) {
        return getParcelableArrayList(key, new ArrayList<Parcelable>(0));
    }

    @SuppressWarnings("unchecked")
    public <T extends Parcelable> ArrayList<T> getTypedParcelableArrayList(String key) {
        return (ArrayList<T>) getParcelableArrayList(key, new ArrayList<Parcelable>(0));
    }

    @SuppressWarnings("unchecked")
    public <T extends Parcelable> ArrayList<T>  getTypedParcelableArrayList(String key, ArrayList<T> defaultValue) {
        return (ArrayList<T>) getParcelableArrayList(key, defaultValue);
    }

    public Serializable getSerializable(String key, Serializable defaultValue) {
        final Serializable value = data.getSerializable(key);
        return value == null ? defaultValue : value;
    }

    public Serializable getSerializable(String key) {
        return data.getSerializable(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getTypedSerializable(String key, T defaultValue) {
        return (T) getSerializable(key, defaultValue);
    }

    public Parcelable getParcelable(String key, Parcelable defaultValue) {
        final Parcelable value = data.getParcelable(key);
        return value == null ? defaultValue : value;
    }

    public Parcelable getParcelable(String key) {
        return data.getParcelable(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends Parcelable> T getTypedParcelable(String key, T defaultValue) {
        return (T) getParcelable(key, defaultValue);
    }

    public <T extends Parcelable> T getTypedParcelable(String key) {
        return data.getParcelable(key);
    }

    public ArrayList<Integer> getIntArrayList(String key) {
        return data.getIntegerArrayList(key);
    }

    public ArrayList<String> getStringArrayList(String key) {
        return data.getStringArrayList(key);
    }

    public ArrayList<CharSequence> getCharSequenceArrayList(String key) {
        return data.getCharSequenceArrayList(key);
    }

    public Message putAll(Bundle src) {
        data.putAll(src);
        return this;
    }

    public Message putByte(String key, byte value) {
        data.putByte(key, value);
        return this;
    }

    public Message putChar(String key, char value) {
        data.putChar(key, value);
        return this;
    }

    public Message putShort(String key, short value) {
        data.putShort(key, value);
        return this;
    }

    public Message putCharSequence(String key, CharSequence value) {
        data.putCharSequence(key, value);
        return this;
    }

    public Message putInt(String key, int value) {
        data.putInt(key, value);
        return this;
    }

    public Message putLong(String key, long value) {
        data.putLong(key, value);
        return this;
    }

    public Message putFloat(String key, float value) {
        data.putFloat(key, value);
        return this;
    }

    public Message putDouble(String key, double value) {
        data.putDouble(key, value);
        return this;
    }

    public Message putBoolean(String key, boolean value) {
        data.putBoolean(key, value);
        return this;
    }

    public Message putString(String key, String value) {
        data.putString(key, value);
        return this;
    }

    public Message putBundle(String key, Bundle value) {
        data.putBundle(key, value);
        return this;
    }

    public Message putSerializable(String key, Serializable value) {
        data.putSerializable(key, value);
        return this;
    }

    public Message putParcelable(String key, Parcelable value) {
        data.putParcelable(key, value);
        return this;
    }

    public Message putParcelableArray(String key, Parcelable[] values) {
        data.putParcelableArray(key, values);
        return this;
    }

    public Message putParcelableArrayList(String key, ArrayList<? extends Parcelable> value) {
        data.putParcelableArrayList(key, value);
        return this;
    }

    public Message putIntArrayList(String key, ArrayList<Integer> value) {
        data.putIntegerArrayList(key, value);
        return this;
    }

    public Message putStringArrayList(String key, ArrayList<String> value) {
        data.putStringArrayList(key, value);
        return this;
    }

    public Message putCharSequenceArrayList(String key, ArrayList<CharSequence> value) {
        data.putCharSequenceArrayList(key, value);
        return this;
    }
    
    @Override
    public String toString() {
        return data.toString();
    }

}