package com.example.topniceinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.topniceinfo.utils.LinkSharedPreUtil;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.Util;
import com.example.topniceinfo.websocket.WebSocketUtil;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button main_login_btn,main_setting_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置bar为无、全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //设置语言（根据系统语言设置）
        settingLang();
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }

        DisplayMetrics dm = MainActivity.this.getResources().getDisplayMetrics();
        int height= dm.heightPixels;
        int width= dm.widthPixels;
        int sw=MainActivity.this.getResources().getConfiguration().smallestScreenWidthDp;
       // Util.showToast(MainActivity.this,"屏幕分辨率:" + width + "*" + height+",dpi:"+dm.densityDpi+",sw:"+sw);
//        try {
//            Date date = new Date();
//            Util.showToast(MainActivity.this,"当前时间："+ DateUtil.date2TimeStamp(date,"yyyy-MM-dd HH:mm:ss")+"当前缓存大小："+ DataCleanUtil.getTotalCacheSize(MyApplication.context));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //初始化组件
        init();
        //判断是否设置了
        judgeSetting();
        /* 登录界面跳转 */
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (!LinkSharedPreUtil.getSharePre().getIp().equals("")){
                    WebSocketUtil.getwebSocket().OneClickStart();//开启连接
                }

            }
        }).start();
        main_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (LinkSharedPreUtil.getSharePre().judgeEmpty()){
                    intent=new Intent(MainActivity.this,LoginActivity.class);
                }else {
                    Util.showToast(MainActivity.this,getResources().getString(R.string.main_judgeEmpty_toast));
                    intent=new Intent(MainActivity.this,SettingActivity.class);
                }
                startActivity(intent);
            }
        });
        //设置界面跳转
        main_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
    }
    //初始化
    void init(){
        main_login_btn=findViewById(R.id.main_login_btn);
        main_setting_btn=findViewById(R.id.main_setting_btn);
    }
    void judgeSetting(){
        if(LinkSharedPreUtil.getSharePre().getLinkId().equals("")){
            LinkSharedPreUtil.getSharePre().setLinkId(UUID.randomUUID().toString().replace("-",""));
            LinkSharedPreUtil.getSharePre().SharedPreEdit();
        }
        if (!LinkSharedPreUtil.getSharePre().judgeEmpty()){
            Util.showToast(MainActivity.this,getResources().getString(R.string.main_judgeEmpty_toast));
            Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
        }
    }
    void settingLang(){
        String locale = Locale.getDefault().toString();
        switch (locale){
            case "zh_CN":
                changeAppLanguage(Locale.SIMPLIFIED_CHINESE);
                break;
            case "zh_TW":
                changeAppLanguage(Locale.TRADITIONAL_CHINESE);
                break;
//            case "en_US":
//                changeAppLanguage(Locale.US);
//                break;
            default:
                changeAppLanguage(Locale.US);
        }
    }
    //修改语言
    public void changeAppLanguage(Locale locale) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Configuration configuration = getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        getResources().updateConfiguration(configuration, metrics);
//        //重新启动Activity
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

}
