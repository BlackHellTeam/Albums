package com.liang.albums.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by liang on 15/1/3.
 * Desc : SharedPreferences Util
 */
public class PreferenceUtil {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public PreferenceUtil(Context ctx){
        settings = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        editor = settings.edit();
    }

    public String getPrefString( String key,
                                       final String defaultValue) {
        return settings.getString(key, defaultValue);
    }

    public void setPrefString( final String key,
                                     final String value) {
        settings.edit().putString(key, value).commit();
    }

    public boolean getPrefBoolean( final String key,
                                         final boolean defaultValue) {
        return settings.getBoolean(key, defaultValue);
    }

    public boolean hasKey( final String key) {
        return settings.contains(key);
    }

    public void setPrefBoolean( final String key,
                                      final boolean value) {
        settings.edit().putBoolean(key, value).commit();
    }

    public void setPrefInt( final String key,
                                  final int value) {
        settings.edit().putInt(key, value).commit();
    }

    public int getPrefInt( final String key,
                                 final int defaultValue) {
        return settings.getInt(key, defaultValue);
    }

    public void setPrefFloat( final String key,
                                    final float value) {
        settings.edit().putFloat(key, value).commit();
    }

    public float getPrefFloat( final String key,
                                     final float defaultValue) {
        return settings.getFloat(key, defaultValue);
    }

    public void setSettingLong( final String key,
                                      final long value) {
        settings.edit().putLong(key, value).commit();
    }

    public long getPrefLong( final String key,
                                   final long defaultValue) {
        return settings.getLong(key, defaultValue);
    }

    public void clearPreference() {
        editor.clear();
        editor.commit();
    }
}
