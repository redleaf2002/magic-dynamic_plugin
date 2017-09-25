package com.magic.plugin.bean;

import android.content.pm.PackageInfo;
import android.content.res.Resources;

/**
 * Created by hong on 2017/9/14.
 */

public class PluginResource {
    public String pkgName = null;
    public String pkgR = null;
    public Resources mResources;
    public ClassLoader mClassLoader;
    public PackageInfo mPackageInfo;

    public String toString() {
        return "pkgName = " + pkgName + " pkgR = " + pkgR + " mResourcs = " + mResources.toString() + " mClassloader = " + mClassLoader.toString();
    }
}
