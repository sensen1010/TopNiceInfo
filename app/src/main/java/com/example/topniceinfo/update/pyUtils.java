package com.example.topniceinfo.update;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.example.topniceinfo.MainActivity;
import com.example.topniceinfo.okhttp.OkhttpApi;
import com.example.topniceinfo.utils.LinkSharedPreUtil;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.UpdateShareProUtil;
import com.example.topniceinfo.utils.Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class pyUtils {


   // public static  String fileUrl="http://192.168.31.191:8080/file/asd.apk";

    public static void downUrl(boolean isRoot) {
        System.out.println("进入下载");
        String downUrl= UpdateShareProUtil.getSharePre().getDownLoadUrl();
        String url= LinkSharedPreUtil.getSharePre().imgUrlServer()+downUrl;
        String apkMd5=UpdateShareProUtil.getSharePre().getApkMd5();
        DownloadUtil.get().download(url,
                MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), apkMd5+".apk", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                //下载完成进行相关逻辑操作
                System.out.println("下载完成");
                if (isRoot){
                    String destFileDir=MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    File dir = new File(destFileDir);
                    File files = new File(dir, apkMd5+".apk");
                    installSlient(MyApplication.context,files);
                }else {
                    //                //调用系统的安装方法
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data;
                    String destFileDir=MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    File dir = new File(destFileDir);
                    File files = new File(dir, apkMd5+".apk");
                    // 判断版本大于等于7.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
                        data = Uri.fromFile(files);
                        // 给目标应用一个临时授权
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        //LogUtils.e("AutoUpdate","7.0data="+data);
                    } else {
                        data = Uri.fromFile(files);
                        //  LogUtils.e("AutoUpdate","111data="+data);
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(data, "application/vnd.android.package-archive");
                    MyApplication.context.startActivity(intent);
                }


            }

            @Override
            public void onDownloading(int progress) {
                //下载进行中展示进度
                System.out.println("下载中"+progress);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                //下载异常进行相关提示操作

                UpdateShareProUtil.getSharePre().setApkMd5("");
                UpdateShareProUtil.getSharePre().SharedPreEdit();
            }
        });
    }

    public static void installSlient(Context context, File apk) {

        //适用于安卓6.0以下
        String cmd = "pm install -r " + apk.getPath();
        String start="am start -n com.example.topniceinfo/com.example.topniceinfo.MainActivity";

        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            //静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.write(start.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            process.waitFor();

//            Intent intent = context.getPackageManager()
//                    .getLaunchIntentForPackage(MyApplication.context.getPackageName());
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(intent);


            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();

            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }

            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Log.e(TAG, "=================== successMsg: " + successMsg.toString() + ", errorMsg: " + errorMsg.toString());
       // installApk(MyApplication.context);
        //安装成功
        if ("Success".equals(successMsg.toString())) {
//            try {
//                Runtime.getRuntime().exec("su -c \"/system/bin/reboot\"");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            //   Log.e(TAG, "======= apk install success");
        }

    }
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Util.showToast(MyApplication.context,"俺咋说的");
                    // showLog();
                    break;
            }
        }
    };
    static  void anapk(){

        Intent intent = new Intent(MyApplication.context.getApplicationContext(), MainActivity.class);
        @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(
                MyApplication.context.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

        AlarmManager mgr = (AlarmManager) MyApplication.context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10000, restartIntent);
        //杀死老线程
        //android.os.Process.killProcess(android.os.Process.myPid());

    }


   static void installApk(Context context){
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0及以上
            mgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, restartIntent);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            mgr.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, restartIntent);
        }

    }

}
