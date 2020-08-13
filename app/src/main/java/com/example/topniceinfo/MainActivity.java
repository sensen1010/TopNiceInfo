package com.example.topniceinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.topniceinfo.broadcastReceiver.ScreenOffAdminReceiver;
import com.example.topniceinfo.utils.LinkSharedPreUtil;
import com.example.topniceinfo.utils.LoginSharedPreUtil;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.Util;
import com.example.topniceinfo.websocket.WebSocketUtil;
import java.util.Locale;
import java.util.UUID;



public class MainActivity extends AppCompatActivity {

    private Button main_login_btn,main_setting_btn;
    private ComponentName adminReceiver;
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
        adminReceiver = new ComponentName(MainActivity.this, ScreenOffAdminReceiver.class);
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

        Message message=new Message();
        message.what=1;
        handler.sendMessageDelayed(message,3000);

        Util.showToast(this,"当前："+Util.getMac());

        //初始化组件
        init();
        //判断是否设置了
        judgeSetting();
        /* 进入界面，判断是否已登录 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!LinkSharedPreUtil.getSharePre().getIp().equals("")){
                    if (!LoginSharedPreUtil.getSharePre().getEnterId().equals("")){
                         WebSocketUtil.getwebSocket().OneClickStart();//开启连接
                    }
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
        checkAndTurnOnDeviceManager(null);
    }

    /**
     * @param view 检测并去激活设备管理器权限
     */
    public void checkAndTurnOnDeviceManager(View view) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...");//显示位置见图二
        startActivityForResult(intent, 0);
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


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    };

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
