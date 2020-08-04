package com.example.topniceinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.topniceinfo.utils.LoginSharedPreUtil;
import com.example.topniceinfo.utils.Util;
import com.example.topniceinfo.websocket.WebSocketUtil;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ImageView login_back;

    private EditText login_userName,login_password;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //初始化
        init();

        //显示已保存的数据
        if (LoginSharedPreUtil.getSharePre().judgeEmpty()){
           login_userName.setText(LoginSharedPreUtil.getSharePre().getUserName());
           login_password.setText(LoginSharedPreUtil.getSharePre().getPow());
        }


        //返回按钮监听
        login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //登录按钮监听
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=login_userName.getText().toString();
                String password=login_password.getText().toString();
                if (userName.isEmpty()||password.isEmpty()){
                    Util.showToast(LoginActivity.this,getResources().getString(R.string.login_info_no));
                    return;
                }
                LoginSharedPreUtil.getSharePre().setUserName(userName);
                LoginSharedPreUtil.getSharePre().setPow(password);
                LoginSharedPreUtil.getSharePre().SharedPreEdit();

                WebSocketUtil.getwebSocket().OneClickStart();//开启连接

                Message message=new Message();
                message.what=1;
                handler.sendMessageDelayed(message,1000);

            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Util.showToast(LoginActivity.this,  WebSocketUtil.getwebSocket().isLink()+"");
//                    Intent intent=new Intent(LoginActivity.this,ProgramHomeActivity.class);
//                    startActivity(intent);
                    break;
                case -1:
                    break;
            }
        }
    };

    void init(){
        login_back=findViewById(R.id.login_back);
        login_userName=findViewById(R.id.login_userName);
        login_password=findViewById(R.id.login_password);
        login_btn=findViewById(R.id.login_btn);
    }
}
