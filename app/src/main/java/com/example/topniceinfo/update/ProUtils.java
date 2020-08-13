package com.example.topniceinfo.update;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ProUtils {

    public static  ProUtils proUtils=new ProUtils();

    public static ProUtils proUtils(){
        return proUtils;
    }

     Activity activity;
    public void canUpdata(Activity activity){
        this.activity=activity;
    }

    public  void check() {
        //当所用app前版本号
        int codeversin = getVersion();
       // getLineVersion(checkurl, codeversin);

    }

    public int getVersion() {
        PackageInfo pkg;
        int versionCode = 0;
        String versionName = "";
        try {
            pkg = activity.getPackageManager().getPackageInfo(activity.getApplication().getPackageName(), 0);
            versionCode = pkg.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionCode;
    }
}
