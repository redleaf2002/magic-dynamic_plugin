package com.magic.plugin.client;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.magic.plugin.proxy.PluginProxyActivity;
import com.magic.plugin.proxy.PluginProxyHelper;
import com.magic.plugin.proxy.PluginProxyService;

/**
 * Created by hong on 2017/9/18.
 */

public class PluginClientActivity extends Activity implements IPluginActivity {
    public Activity mProxyActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (mProxyActivity == null) {
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        if (mProxyActivity == null) {
            super.onStart();
        }
    }

    @Override
    public void onRestart() {
        if (mProxyActivity == null) {
            super.onRestart();
        }
    }

    @Override
    public void onResume() {
        if (mProxyActivity == null) {
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mProxyActivity == null) {
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mProxyActivity == null) {
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mProxyActivity == null) {
            super.onDestroy();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mProxyActivity == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (mProxyActivity == null) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (mProxyActivity == null) {
            super.onBackPressed();
        }
    }

    //called
    public void attache(Activity mActivity, ClassLoader mClassLoader) {
        mProxyActivity = mActivity;
    }

    @Override
    public Resources getResources() {
        if (mProxyActivity != null) {
            return mProxyActivity.getResources();
        }
        return super.getResources();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (mProxyActivity != null) {
            mProxyActivity.setContentView(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (mProxyActivity != null) {
            mProxyActivity.setContentView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (mProxyActivity != null) {
            mProxyActivity.setContentView(view, params);
        } else {
            super.setContentView(view, params);
        }
    }

    @Override
    public View findViewById(@IdRes int id) {
        if (mProxyActivity != null) {
            return mProxyActivity.findViewById(id);
        }
        return super.findViewById(id);
    }

    @Override
    public void finish() {
        if (mProxyActivity != null) {
            mProxyActivity.finish();
        } else {
            super.finish();
        }

    }

    public void startPluginActivity(String dexKey, String className) {
        if (mProxyActivity != null) {
            Intent intentPlugin = new Intent();
            intentPlugin.setClass(mProxyActivity, PluginProxyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(PluginProxyHelper.PLUGIN_CLASS_NAME, className);
            bundle.putString(PluginProxyHelper.PLUGIN_KEY, dexKey);
            intentPlugin.putExtras(bundle);
            mProxyActivity.startActivity(intentPlugin);
        }

    }

    public void startPluginService(String dexKey, String className) {
        if (mProxyActivity != null) {
            Intent intentPlugin = new Intent();
            intentPlugin.setClass(mProxyActivity, PluginProxyService.class);
            Bundle bundle = new Bundle();
            bundle.putString(PluginProxyHelper.PLUGIN_CLASS_NAME, className);
            bundle.putString(PluginProxyHelper.PLUGIN_KEY, dexKey);
            intentPlugin.putExtras(bundle);
            mProxyActivity.startService(intentPlugin);
        }
    }


}
