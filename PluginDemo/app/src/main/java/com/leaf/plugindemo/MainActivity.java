package com.leaf.plugindemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leaf.dynamicclient.dynamicjar.IDynamic;
import com.magic.plugin.PluginManager;
import com.magic.plugin.bean.PluginInfo;
import com.magic.plugin.bean.PluginResource;
import com.magic.plugin.proxy.PluginProxyActivity;
import com.magic.plugin.proxy.PluginProxyHelper;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int MSG_UPDATE_JAR_TEXT = 0;
    private static final int MSG_UPDATE_PLUGIN_STRING = 1;
    private static final int MSG_UPDATE_PLUGIN_IMAGE = 2;
    private TextView mNameView, mPluginView;
    private ImageView mPluginImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button goDexBnt = (Button) findViewById(R.id.go_jar);
        Button goApkBnt = (Button) findViewById(R.id.go_apk);
        Button goResourcesBnt = (Button) findViewById(R.id.go_source);
        mNameView = (TextView) findViewById(R.id.name);
        mPluginView = (TextView) findViewById(R.id.plugin_name);
        mPluginImage = (ImageView) findViewById(R.id.plugin_image);
        goDexBnt.setOnClickListener(this);
        goApkBnt.setOnClickListener(this);
        goResourcesBnt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_apk:
                loadApk();
                break;
            case R.id.go_jar:
                loadDexJar();
                break;
            case R.id.go_source:
                loadApkResource();
                break;
        }
    }


    private void loadApk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Context mContext = MainActivity.this;
                String dexPath = PluginDemoHelper.getApkPath(PluginDemoHelper.DynamicApk);
                PluginInfo mPluginInfo = new PluginInfo(dexPath, PluginDemoHelper.DynamicAPKKey);
                PluginManager.loadApk(mContext, mPluginInfo);
                Intent intentPlugin = new Intent();
                intentPlugin.setClass(MainActivity.this, PluginProxyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(PluginProxyHelper.PLUGIN_CLASS_NAME, "com.leaf.apkpluginclient.MainActivity");
                bundle.putString(PluginProxyHelper.PLUGIN_KEY, PluginDemoHelper.DynamicAPKKey);
                intentPlugin.putExtras(bundle);
                startActivity(intentPlugin);

            }
        }).start();
    }


    private void loadApkResource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Context mContext = MainActivity.this;
                String dexPath = PluginDemoHelper.getApkPath(PluginDemoHelper.DynamicApk);
                PluginInfo mPluginInfo = new PluginInfo(dexPath, PluginDemoHelper.DynamicAPKKey);
                PluginManager.loadApk(mContext, mPluginInfo);
                Drawable pluginDrawable = PluginManager.getDrawable(MainActivity.this, PluginDemoHelper.DynamicAPKKey, "image_girl");
                mHandler.obtainMessage(MSG_UPDATE_PLUGIN_IMAGE, pluginDrawable).sendToTarget();

                String plugStr = PluginManager.getString(MainActivity.this, PluginDemoHelper.DynamicAPKKey, "app_name");
                mHandler.obtainMessage(MSG_UPDATE_PLUGIN_STRING, plugStr).sendToTarget();
            }
        }).start();
    }

    private void loadDexJar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Context mContext = MainActivity.this;
                String dexPath = PluginDemoHelper.getApkPath(PluginDemoHelper.DynamicJar);
                PluginInfo mPluginInfo = new PluginInfo(dexPath, PluginDemoHelper.DynamicJarKey);
                PluginManager.loadDexFile(mContext, mPluginInfo);
                PluginResource pluginResource = PluginManager.getResourceBean(mContext, PluginDemoHelper.DynamicJarKey);
                if (pluginResource != null) {
                    try {
                        Class<?> clazz = pluginResource.mClassLoader.loadClass("com.leaf.dynamicclient.dynamicjar.DynamicImpl");
                        IDynamic myDynamic = (IDynamic) clazz.newInstance();
                        if (myDynamic != null) {
                            String name = myDynamic.getName();
                            Log.d(TAG, "name = " + name);
                            mHandler.obtainMessage(0, name).sendToTarget();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE_JAR_TEXT:
                String name = (String) msg.obj;
                mNameView.setText(name);
                break;
            case MSG_UPDATE_PLUGIN_STRING:
                String pluginStr = (String) msg.obj;
                mPluginView.setText(pluginStr);
                break;
            case MSG_UPDATE_PLUGIN_IMAGE:
                Drawable pluginImage = (Drawable) msg.obj;
                mPluginImage.setImageDrawable(pluginImage);
                break;
        }
    }
}
