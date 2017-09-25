package com.magic.plugin.bean;

/**
 * Created by hong on 2017/9/15.
 */

public class PluginInfo {
    private String dexPath, pkgR, dexKey;

    public PluginInfo(String dexPath, String dexKey, String pkgR) {
        this.dexPath = dexPath;
        this.pkgR = pkgR;
        this.dexKey = dexKey;
    }

    public PluginInfo(String dexPath, String dexKey) {
        this(dexPath, dexKey, null);
    }

    public String getDexPath() {
        return dexPath;
    }

    public String getPkgR() {
        return pkgR;
    }

    public String getDexKey() {
        return dexKey;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("dexPath:");
        builder.append(dexPath);
        builder.append("pkgR:");
        builder.append(pkgR);
        builder.append("dexKey:");
        builder.append(dexKey);
        return builder.toString();
    }

}
