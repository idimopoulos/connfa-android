package com.ls.ui.activity;

import com.ls.utils.ActivityManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity ;

public class BaseActivity extends AppCompatActivity  {

    private ActivityManager mActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityManager = new ActivityManager(this);
        mActivityManager.registerExitReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityManager.unregisterReceiver();
    }

}
