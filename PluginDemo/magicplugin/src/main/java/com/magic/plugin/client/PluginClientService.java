package com.magic.plugin.client;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.magic.plugin.bean.IPluginService;
import com.magic.plugin.bean.PluginResource;

/**
 * Created by Administrator on 2017/9/19.
 */

public class PluginClientService extends Service implements IPluginService {
    protected Service proxyService;
    protected PluginResource pluginResource;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void attache(Service proxyService, PluginResource pluginResource) {
        this.proxyService = proxyService;
        this.pluginResource = pluginResource;
    }
}
