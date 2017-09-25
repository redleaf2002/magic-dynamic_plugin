package com.magic.plugin.bean;


import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

public interface IPluginService {

     void onCreate();

     void onStart(Intent intent, int startId);

     int onStartCommand(Intent intent, int flags, int startId);

     void onDestroy();

     void onConfigurationChanged(Configuration newConfig);

     void onLowMemory();

     void onTrimMemory(int level);

     IBinder onBind(Intent intent);

     boolean onUnbind(Intent intent);

     void onRebind(Intent intent);

     void onTaskRemoved(Intent rootIntent);

}
