package com.magic.plugin.proxy;

import com.magic.plugin.bean.IPluginService;
import com.magic.plugin.bean.PluginResource;

/**
 * Created by jhong on 2017/9/19.
 */

public interface IPluginServiceAttacheable {
    void attache(IPluginService remoteService, PluginResource pluginResource);
}
