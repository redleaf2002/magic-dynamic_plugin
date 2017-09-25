package com.leaf.plugindemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class BaseActivity extends Activity {
    protected Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(this);
    }

    private class MyHandler extends Handler {
        private Reference<BaseActivity> mRef;

        public MyHandler(BaseActivity mActivity) {
            mRef = new WeakReference<>(mActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mRef != null && mRef.get() != null) {
                mRef.get().handleMessage(msg);
            }
        }
    }

    public void handleMessage(Message msg) {
    }


}
