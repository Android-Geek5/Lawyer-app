package com.eweblog.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
* ALarm broadcast receiver to start alarm service
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

       Intent service1 = new Intent(context, MyAlarmService.class);
        context.startService(service1);
    }
}