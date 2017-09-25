package com.magic.plugin.proxy;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

import com.magic.plugin.bean.IPluginService;
import com.magic.plugin.bean.PluginResource;

/**
 * Created by hong on 2017/9/18.
 */

public class PluginProxyService extends Service implements IPluginService, IPluginServiceAttacheable {
    IPluginService mIPluginService;
    PluginResource pluginResource;
    private PluginProxyHelper mPluginProxyHelper = new PluginProxyHelper();


    @Override
    public void attache(IPluginService remoteService, PluginResource pluginResource) {
        this.mIPluginService = remoteService;
        this.pluginResource = pluginResource;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart(Intent intent, int startId) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PluginProxyService", "mIPluginService  onStartCommand ");
        if (intent.getExtras() != null) {
            Log.d("PluginProxyService", "mIPluginService onStartCommand getExtras ");
            mPluginProxyHelper.initPluginService(this, intent.getExtras());

            if (mIPluginService != null) {
                mIPluginService.onStartCommand(intent, flags, startId);
            } else {
                Log.d("PluginProxyService", "mIPluginService mIPluginService is null ");
            }
        } else {
            Log.d("PluginProxyService", "mIPluginService onStartCommand  getExtras is null ");
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mIPluginService != null) {
            return mIPluginService.onBind(intent);
        }
        return null;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onRebind(Intent intent) {

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

    }
}
