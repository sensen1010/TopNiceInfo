package com.example.topniceinfo.websocket;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.websocket.im.JWebSocketClient;
import com.example.topniceinfo.websocket.im.JWebSocketClientService;

import static android.content.Context.BIND_AUTO_CREATE;

public class WebSocketUtil {

    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private  ServiceConnection serviceConnection;

    private static class SingletonInstance {
        private static final WebSocketUtil webSocketUtil = new WebSocketUtil();
    }
    public static WebSocketUtil getwebSocket() {
        return SingletonInstance.webSocketUtil;
    }


    public void mesg(String msg){
        jWebSClientService.sendMsg(msg);
    }

    //一键启动连接
    public void OneClickStart(){
        closeHear();//关闭心跳检测
        startJWebSClientService();//启动服务
        startConnect();//启动连接
        bindService();//绑定服务
    }


    //判断是否连接
    public boolean isLink(){
        if (client!=null &&client.isOpen()){
            return true;
        }
        return false;
    }

    //关闭心跳检测
    private void closeHear(){
        if (jWebSClientService!=null){
            jWebSClientService.closeHeartBeat();
        }
    }

//    public ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            Log.e("MainActivity", "服务与活动成功绑定");
//            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
//            jWebSClientService = binder.getService();
//            client = jWebSClientService.client;
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            Log.e("MainActivity", "服务与活动成功断开");
//        }
//    };



    private void startConnect(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.e("MainActivity", "服务与活动成功绑定");
                binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
                jWebSClientService = binder.getService();
                client = jWebSClientService.client;
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e("MainActivity", "服务与活动成功断开");
            }
        };
    }


    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(MyApplication.context, JWebSocketClientService.class);
        MyApplication.context.startService(intent);
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(MyApplication.context, JWebSocketClientService.class);
        MyApplication.context.bindService(bindIntent, serviceConnection,BIND_AUTO_CREATE);
    }

}
