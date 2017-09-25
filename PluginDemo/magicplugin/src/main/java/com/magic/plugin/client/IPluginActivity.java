package com.magic.plugin.client;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2017/9/18.
 */

public interface IPluginActivity {
    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onBackPressed();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);
}
