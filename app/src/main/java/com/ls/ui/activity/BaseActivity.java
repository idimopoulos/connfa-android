package com.ls.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.ls.utils.ActivityManager;

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
