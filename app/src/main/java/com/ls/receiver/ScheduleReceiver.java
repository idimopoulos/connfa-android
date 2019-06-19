package com.ls.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.legacy.content.WakefulBroadcastReceiver;

public class ScheduleReceiver extends WakefulBroadcastReceiver {
    public ScheduleReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        if (data != null && data.isHierarchical()) {
            String uri = intent.getDataString();
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
