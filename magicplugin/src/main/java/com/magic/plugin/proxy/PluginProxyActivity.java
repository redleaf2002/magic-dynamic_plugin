package com.magic.plugin.proxy;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.magic.plugin.client.IPluginActivity;


/**
 * Created by hong on 2017/9/18.
 */

public class PluginProxyActivity extends Activity implements IPluginActivity, IPluginAttacheable {
    private PluginProxyHelper mPluginHelper = new PluginProxyHelper();
    private IPluginActivity mIPluginActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            mPluginHelper.initPlugin(this, bundle);
        }

    }


    @Override
    public void onStart() {
        if (mIPluginActivity != null) {
            mIPluginActivity.onStart();
        }
        super.onStart();
    }

    @Override
    public void onRestart() {
        if (mIPluginActivity != null) {
            mIPluginActivity.onRestart();
        }
        super.onRestart();
    }

    @Override
    public void onResume() {
        if (mIPluginActivity != null) {
            mIPluginActivity.onResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mIPluginActivity != null) {
            mIPluginActivity.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (mIPluginActivity != null) {
            mIPluginActivity.onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mIPluginActivity != null) {
            mIPluginActivity.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void attache(IPluginActivity mActivity) {
        if (mActivity != null && mActivity instanceof IPluginActivity) {
            this.mIPluginActivity = (IPluginActivity) mActivity;
        }
    }

    @Override
    public Resources getResources() {
        Resources resources = mPluginHelper.getResources();
        if (mIPluginActivity != null && resources != null) {
            return resources;
        } else {
            return super.getResources();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        ClassLoader classLoader = mPluginHelper.getClassLoader();
        if (mIPluginActivity != null && classLoader != null) {
            return classLoader;
        } else {
            return super.getClassLoader();
        }
    }


    @Override
    public void onBackPressed() {
        if (mIPluginActivity != null) {
            mIPluginActivity.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mIPluginActivity != null) {
            mIPluginActivity.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (mIPluginActivity != null) {
            mIPluginActivity.onNewIntent(intent);
        }
        super.onNewIntent(intent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

}
