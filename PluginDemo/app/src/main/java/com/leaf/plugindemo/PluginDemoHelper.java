package com.leaf.plugindemo;

import android.os.Environment;

import java.io.File;

/**
 * Created by hong on 2017/9/14.
 */

public class PluginDemoHelper {

    public static final String DynamicJar = "dynamicdex.jar";
    public static final String DynamicJarKey = "jarDdynamic";

    public static final String DynamicApk = "dynamic.apk";
    public static final String DynamicAPKKey = "apkDynamic";
    public static final String DynamicPkgR = "com.leaf.apkpluginclient";


    public static String getApkPath(String apkName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsoluteFile().getAbsolutePath();
        File file = new File(dirPath, apkName);
        return file.getAbsolutePath();
    }
}
