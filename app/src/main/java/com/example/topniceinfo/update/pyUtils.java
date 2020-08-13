package com.example.topniceinfo.update;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class pyUtils {


    public static  String fileUrl="http://192.168.31.191:8080/file/asd.apk";

    public static void downUrl(final Activity activity) {
        System.out.println("进入下载");

        DownloadUtil.get().download(fileUrl,
                activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "asd.apk", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                //下载完成进行相关逻辑操作
                System.out.println("下载完成");
                //调用系统的安装方法
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data;
                String destFileDir=activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                File dir = new File(destFileDir);
                File files = new File(dir, "asd.apk");
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
                System.out.println(files);
                intent.setDataAndType(data, "application/vnd.android.package-archive");
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onDownloading(int progress) {
                //下载进行中展示进度
                System.out.println("下载中"+progress);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                //下载异常进行相关提示操作
                e.printStackTrace();
            }
        });
    }

}
