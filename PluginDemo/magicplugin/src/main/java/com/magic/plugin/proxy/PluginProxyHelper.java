package com.magic.plugin.proxy;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.magic.plugin.PluginManager;
import com.magic.plugin.bean.PluginResource;
import com.magic.plugin.client.PluginClientActivity;
import com.magic.plugin.client.PluginClientService;

/**
 * Created by hong on 2017/9/18.
 */

public class PluginProxyHelper {
    public static final String PLUGIN_CLASS_NAME = "plugin_class_name";
    public static final String PLUGIN_PACKAGE_NAME = "plugin_package_name";
    public static final String PLUGIN_KEY = "plugin_key";
    private static final String TAG = "PluginProxyHelper";
    private PluginResource pluginResource;
    private Resources.Theme mTheme;
    PluginProxyActivity mProxyActivity;
    private ActivityInfo mActivityInfo;

    public void initPlugin(PluginProxyActivity mProxyActivity, Bundle bundle) {
        if (bundle != null) {
            String pluginClass = bundle.getString(PLUGIN_CLASS_NAME);
            String pluginKey = bundle.getString(PLUGIN_KEY);
            String pluginPkg = bundle.getString(PLUGIN_PACKAGE_NAME);
            if (TextUtils.isEmpty(pluginKey)) {
                pluginKey = pluginPkg;
            }
            this.mProxyActivity = mProxyActivity;
            Log.d("PluginProxyHelper", "mIPluginActivity 11 pluginClass = " + pluginClass);
            pluginResource = PluginManager.getResourceBean(mProxyActivity.getApplicationContext(), pluginKey);
            if (pluginResource != null && pluginResource.mClassLoader != null) {

                initializeActivityInfo(pluginClass);
                handleActivityInfo();
                try {
                    Class<?> mClazz = pluginResource.mClassLoader.loadClass(pluginClass);
                    if (mClazz != null) {
                        Log.d("PluginProxyHelper", "mIPluginActivity 44");
                        PluginClientActivity mIPluginActivity = (PluginClientActivity) mClazz.newInstance();
                        if (mIPluginActivity != null) {
                            Log.d(TAG, "mIPluginActivity is not null ");
                            mProxyActivity.attache(mIPluginActivity);
                            mIPluginActivity.attache(mProxyActivity, pluginResource.mClassLoader);
                            mIPluginActivity.onCreate(bundle);
                        } else {
                            Log.d(TAG, "mIPluginActivity is null ");
                        }
                    } else {
                        Log.d("PluginProxyHelper", "mIPluginActivity 55");
                    }
                } catch (Exception e) {
                    Log.d(TAG, "initPlugin " + e.toString());
                }
                Log.d("PluginProxyHelper", "mIPluginActivity 33");
            }

        }
    }

    public void initPluginService(PluginProxyService mProxyService, Bundle bundle) {
        if (bundle == null) {
            return;
        }

        try {
            String pluginClass = bundle.getString(PLUGIN_CLASS_NAME);
            String pluginKey = bundle.getString(PLUGIN_KEY);
            String pluginPkg = bundle.getString(PLUGIN_PACKAGE_NAME);
            Log.d("PluginProxyService", "mIPluginService  getExtras initPluginService " + pluginClass + " pluginKey:" + pluginKey);
            if (TextUtils.isEmpty(pluginKey)) {
                pluginKey = pluginPkg;
            }
            pluginResource = PluginManager.getResourceBean(mProxyService.getApplicationContext(), pluginKey);
            Class<?> mClazz = pluginResource.mClassLoader.loadClass(pluginClass);
            Log.d(TAG, "mIPluginService  pluginClass " + pluginClass);
            if (mClazz != null) {
                PluginClientService mIPluginService = (PluginClientService) mClazz.newInstance();
                if (mIPluginService != null) {
                    Log.d(TAG, "mIPluginService is not null ");
                    mProxyService.attache(mIPluginService, pluginResource);
                    mIPluginService.attache(mProxyService, pluginResource);
                    mIPluginService.onCreate();
                } else {
                    Log.d(TAG, "mIPluginService is null ");
                }
            } else {
                Log.d("PluginProxyHelper", "mIPluginService 55");
            }
        } catch (Exception e) {
            Log.d(TAG, "mIPluginService initPluginService " + e.toString());
        }

    }

    private void initializeActivityInfo(String mClass) {
        PackageInfo packageInfo = pluginResource.mPackageInfo;
        if ((packageInfo.activities != null) && (packageInfo.activities.length > 0)) {
            if (mClass == null) {
                mClass = packageInfo.activities[0].name;
            }

            //Finals 修复主题BUG
            int defaultTheme = packageInfo.applicationInfo.theme;
            for (ActivityInfo a : packageInfo.activities) {
                Log.d("PluginProxyHelper", "mIPluginActivity 22 " + a.name);
                if (a.name.equals(mClass) || a.name.equals("PluginActivity")) {
                    mActivityInfo = a;
                    // Finals ADD 修复主题没有配置的时候插件异常
                    if (mActivityInfo.theme == 0) {
                        if (defaultTheme != 0) {
                            mActivityInfo.theme = defaultTheme;
                        } else {
                            if (Build.VERSION.SDK_INT >= 14) {
                                mActivityInfo.theme = android.R.style.Theme_DeviceDefault;
                            } else {
                                mActivityInfo.theme = android.R.style.Theme;
                            }
                        }
                    }
                }
            }

        }
    }

    private void handleActivityInfo() {
        Log.d(TAG, "handleActivityInfo, theme=" + mActivityInfo.theme);
        if (mActivityInfo.theme > 0) {
            mProxyActivity.setTheme(mActivityInfo.theme);
        }
        Resources.Theme superTheme = mProxyActivity.getTheme();
        mTheme = pluginResource.mResources.newTheme();
        mTheme.setTo(superTheme);
        // Finals适配三星以及部分加载XML出现异常BUG
        try {
            mTheme.applyStyle(mActivityInfo.theme, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: handle mActivityInfo.launchMode here in the future.
    }


    public ClassLoader getClassLoader() {
        if (pluginResource != null && pluginResource.mClassLoader != null) {
            return pluginResource.mClassLoader;
        }
        return null;
    }

    public Resources getResources() {
        if (pluginResource != null && pluginResource.mResources != null) {
            return pluginResource.mResources;
        }
        return null;
    }


}
