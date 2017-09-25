package com.magic.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.magic.plugin.bean.PluginInfo;
import com.magic.plugin.bean.PluginResource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by hong on 2017/9/14.
 */

public class PluginHelper {

    private static volatile PluginHelper mPluginHelper = null;
    private static Object object = new Object();
    private static ConcurrentHashMap<String, PluginResource> mResourcesMap = new ConcurrentHashMap<>();
    private Context mContext;
    private static final String TAG = "PluginProxyHelper";


    public static PluginHelper getInstance(Context mContext) {
        if (mPluginHelper == null) {
            synchronized (object) {
                if (mPluginHelper == null) {
                    mPluginHelper = new PluginHelper(mContext);
                }
            }
        }
        return mPluginHelper;
    }

    private PluginHelper(Context mContext) {
        if (mContext != null) {
            this.mContext = mContext.getApplicationContext();
        }
    }

    public void loadApk(PluginInfo mPluginInfo) {
        loadApk(mPluginInfo, true);
    }

    public void loadDexFile(PluginInfo mPluginInfo) {
        loadApk(mPluginInfo, false);
    }

    public PluginResource getResourceBean(String dexKey) {
        PluginResource bean = mResourcesMap.get(dexKey);
        if (bean == null) {
            Log.d(TAG, "resources is empty , to load the apk firstly");
        }
        return bean;
    }

    private void loadApk(PluginInfo mPluginInfo, boolean isApkFile) {
        if (mPluginInfo == null) {
            return;
        }
        if (TextUtils.isEmpty(mPluginInfo.getDexKey())) {
            Log.d(TAG, "dexKey is unique identity for a dexFilePlugin , it should not be empty");
        }
        String dexPath = mPluginInfo.getDexPath();
        if (TextUtils.isEmpty(dexPath)) {
            Log.d(TAG, "apkPath should not be empty");
            return;
        }
        if (isApkFile) {
            PackageInfo packageInfo = getPackageInfo(dexPath);
            if (packageInfo == null) {
                Log.d(TAG, "packageInfo is null by this apkpath , please check that");
                return;
            } else {
                Log.d(TAG, "packageInfo packageName: " + packageInfo.packageName);
            }
            createResource(mPluginInfo, packageInfo);
        } else {
            createResource(mPluginInfo, null);
        }
    }


    private Resources createResource(PluginInfo mPluginInfo, PackageInfo packageInfo) {
        try {
            String pkgName = null;
            if (packageInfo != null) {
                pkgName = packageInfo.packageName;
            }
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetMethod.invoke(assetManager, mPluginInfo.getDexPath());
            Resources resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(), mContext.getResources().getConfiguration());
            PluginResource bean = new PluginResource();
            bean.mResources = resources;
            bean.mPackageInfo = packageInfo;
            if (!TextUtils.isEmpty(mPluginInfo.getPkgR())) {
                bean.pkgR = mPluginInfo.getPkgR();
            } else {
                bean.pkgR = pkgName;
            }
            bean.pkgName = pkgName;
            bean.mClassLoader = getClassCloder(mPluginInfo.getDexPath(), mContext.getDir("dex", Context.MODE_PRIVATE).getAbsolutePath());
            mResourcesMap.put(mPluginInfo.getDexKey(), bean);
        } catch (Exception e) {
            Log.d(TAG, "createResource Exception: " + e.toString());
        }
        return null;
    }

    private PackageInfo getPackageInfo(String apkPath) {
        PackageManager pm = mContext.getPackageManager();
        return pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
    }

    private int getResorceId(PluginResource resourceBean, String type, String resName) {
        String mClassName = resourceBean.pkgR + ".R$" + type;
        try {
            Class<?> clazz = resourceBean.mClassLoader.loadClass(mClassName);
            Field field = clazz.getField(resName);
            if (field != null) {
                return (int) field.get(null);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return -1;
    }

    private ClassLoader getClassCloder(String resourcePath, String mDexDir) {
        return new DexClassLoader(resourcePath, mDexDir, null,
                mContext.getClassLoader());
    }

    public String getString(String dexKey, String stringName) {
        PluginResource resourceBean = getResourceBean(dexKey);
        if (resourceBean != null) {
            int resId = getResorceId(resourceBean, "string", stringName);
            if (resId != -1 && resourceBean.mResources != null) {
                return resourceBean.mResources.getString(resId);
            }
        }
        return null;
    }

    public Drawable getDrawable(String dexKey, String drawableName) {
        PluginResource resourceBean = getResourceBean(dexKey);
        if (resourceBean != null) {
            int resId = getResorceId(resourceBean, "drawable", drawableName);
            if (resId != -1 && resourceBean.mResources != null) {
                return resourceBean.mResources.getDrawable(resId);
            }
        }
        return null;
    }

    public Class<?> getClass(String dexKey, String className) {
        PluginResource resourceBean = getResourceBean(dexKey);
        Class<?> clazz = null;
        try {
            if (resourceBean != null && resourceBean.mClassLoader != null) {
                clazz = resourceBean.mClassLoader.loadClass(className);
            }
        } catch (Exception e) {
            Log.d(TAG, " Exception " + e.toString());
        }
        return clazz;
    }


}
