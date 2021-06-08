package com.example.alarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private Vibrator vibrator;

    @Override public void onReceive(Context context, Intent intent) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        Toast.makeText(context, "Alarm fired!!!", Toast.LENGTH_SHORT).show();
        Log.d("TAG","Fired: "+ System.currentTimeMillis());
    }
}
