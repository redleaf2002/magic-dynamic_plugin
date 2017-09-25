package com.leaf.apkpluginclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.magic.plugin.client.PluginClientActivity;


public class MainActivity extends PluginClientActivity {
    private static final String TAG = "MainActivity";
    public static final String DynamicAPKKey = "apkDynamic";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mProxyActivity != null) {
            mProxyActivity.setContentView(R.layout.activity_main);
            Button skipBnt = (Button) mProxyActivity.findViewById(R.id.skip);
            skipBnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "plugin MainActivity skip onClick");
                    startPluginActivity(DynamicAPKKey, "com.leaf.apkpluginclient.SkipActivity");
                }
            });
        }
    }
}
