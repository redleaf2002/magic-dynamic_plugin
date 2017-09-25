package com.leaf.apkpluginclient;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.magic.plugin.client.PluginClientService;

/**
 * Created by hong on 2017/9/19.
 */

public class MyService extends PluginClientService {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("hong", "MainActivity on startCommand");
        Toast.makeText(proxyService.getApplicationContext(), "execute my service", Toast.LENGTH_LONG).show();
        return START_REDELIVER_INTENT;
    }
}
