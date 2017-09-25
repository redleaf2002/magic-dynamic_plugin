package com.leaf.apkpluginclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.magic.plugin.client.PluginClientActivity;


public class SkipActivity extends PluginClientActivity {
    private static final String TAG = "MainActivity";
    private ImageView mImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mProxyActivity != null) {
            mProxyActivity.setContentView(R.layout.activity_skip);
            mImage = (ImageView) mProxyActivity.findViewById(R.id.image);
            mImage.setImageResource(R.drawable.image_girl);
            Button serviceBnt = (Button) mProxyActivity.findViewById(R.id.go_service);
            serviceBnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startPluginService(MainActivity.DynamicAPKKey, "com.leaf.apkpluginclient.MyService");
                }
            });
            Log.d(TAG, "plugin SkipActivity setContentView");
        } else {
            setContentView(R.layout.activity_main);
        }
    }
}
