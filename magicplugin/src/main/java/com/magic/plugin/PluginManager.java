package com.magic.plugin;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.magic.plugin.bean.PluginInfo;
import com.magic.plugin.bean.PluginResource;

/**
 * Created by hong on 2017/9/14.
 */

public class PluginManager {

    private PluginManager() {
    }

    public static void loadApk(Context mContext, PluginInfo mPluginInfo) {
        PluginHelper.getInstance(mContext).loadApk(mPluginInfo);
    }

    public static void loadDexFile(Context mContext, PluginInfo mPluginInfo) {
        PluginHelper.getInstance(mContext).loadDexFile(mPluginInfo);
    }

    public static PluginResource getResourceBean(Context mContext, String dexKey) {
        return PluginHelper.getInstance(mContext).getResourceBean(dexKey);
    }

    public static String getString(Context mContext, String dexKey, String stringName) {
        return PluginHelper.getInstance(mContext).getString(dexKey, stringName);
    }

    public static Drawable getDrawable(Context mContext, String dexKey, String drawableName) {
        return PluginHelper.getInstance(mContext).getDrawable(dexKey, drawableName);
    }

    public static Class<?> getClass(Context mContext, String dexKey, String className) {
        return PluginHelper.getInstance(mContext).getClass(dexKey, className);
    }


}
