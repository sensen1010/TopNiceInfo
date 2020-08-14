package com.example.topniceinfo.broadcastReceiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.topniceinfo.MainActivity;
import com.example.topniceinfo.server.MyService;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.Util;



import static com.shuyu.gsyvideoplayer.GSYVideoBaseManager.TAG;

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
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {//接收升级广播
            Log.e("","安装了新应用");

        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {//接收安装广播
            Log.e("","安装新应用");
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) { //接收卸载广播
            Log.e("","卸载了新应用");
        }
    }
}
