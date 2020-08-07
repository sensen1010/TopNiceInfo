package com.example.topniceinfo.broadcastReceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.topniceinfo.MainActivity;
import com.example.topniceinfo.server.MyService;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.Util;

public class MyReceiver extends BroadcastReceiver {
    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        if(intent.getAction().equals(ACTION_BOOT)){
            Intent server = new Intent(MyApplication.context, MyService.class);
            server.putExtra("startType","BOOT_COMPLETED");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Util.showToast(context,"安卓8.0开机自启动");
               context.startForegroundService(server);
            } else {
                Util.showToast(context,"小于开机自启动");
                context.startService(server);
            }
        }
    }
}
