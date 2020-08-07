package com.example.topniceinfo.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.topniceinfo.ProgramHomeActivity;
import com.example.topniceinfo.utils.MyApplication;

public class MyService extends Service {

    public final int NOTIFICATION_ID=1001;
    public final String CHANNEL_ID="myService01";
    public final String CHANNEL_NAME="myService";

    public MyService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground();
    }

    /**
     * 设置服务在前台可见
     */
    private void startForeground(){
        /**
         * 创建Notification
         */
        NotificationChannel notificationChannel;
        Notification notification;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationChannel= new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("")
                    .build();
        }else{
            notification = new Notification.Builder(this)
                    .setContentTitle("")
                    .setContentText("")
                    .build();
        }

        /**
         * 设置notification在前台展示
         */
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String startType=intent.getStringExtra("startType");
        startForeground();
        showDialog(startType);
        return super.onStartCommand(intent, flags, startId);
    }

    private void showDialog(String startType) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ProgramHomeActivity.programHomeActivity!=null){
                            ProgramHomeActivity.programHomeActivity.finish();
                        }
                        Intent intent=new Intent(MyApplication.context, ProgramHomeActivity.class);
                        intent.putExtra("startType",startType);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).start();
            }
        },0);
    }

}
