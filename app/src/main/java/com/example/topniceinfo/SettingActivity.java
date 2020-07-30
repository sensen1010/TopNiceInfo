package com.example.topniceinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.topniceinfo.utils.LinkSharedPreUtil;
import com.example.topniceinfo.utils.LoginSharedPreUtil;
import com.example.topniceinfo.utils.Util;
import com.example.topniceinfo.websocket.WebSocketUtil;

public class SettingActivity extends AppCompatActivity {

    private ImageView setting_back;
    //设置信息控件
    private Button setting_btn;
    private EditText setting_edt_name,setting_edt_ip,setting_edt_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //初始化
        init();
        Util.showToast(SettingActivity.this,  WebSocketUtil.getwebSocket().isLink()+"");
        //读取保存的信息
        if (LinkSharedPreUtil.getSharePre().judgeEmpty()){
            setting_edt_name.setText(LinkSharedPreUtil.getSharePre().getName());
            setting_edt_ip.setText(LinkSharedPreUtil.getSharePre().getIp());
            setting_edt_port.setText(LinkSharedPreUtil.getSharePre().getPort());
        }

        //返回监听
        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //设置信息按钮
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip=setting_edt_ip.getText().toString();
                if (!Util.ipCheck(ip)){
                    Util.showToast(SettingActivity.this,getResources().getString(R.string.setting_judge_ip_no));
                    return;
                }
                String name=setting_edt_name.getText().toString();
                String port=setting_edt_port.getText().toString();
                if (name.isEmpty()||port.isEmpty()){
                    Util.showToast(SettingActivity.this,getResources().getString(R.string.setting_server_no));
                    return;
                }
                //保存信息
                LinkSharedPreUtil.getSharePre().setIp(ip);
                LinkSharedPreUtil.getSharePre().setPort(port);
                LinkSharedPreUtil.getSharePre().setName(name);
                LinkSharedPreUtil.getSharePre().SharedPreEdit();

                //判断是否已设置登录信息
                if (LoginSharedPreUtil.getSharePre().judgeEmpty()){
                    Util.showToast(SettingActivity.this,getResources().getString(R.string.setting_server_ok));
                }else {
                    //未设置
                    Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                    Util.showToast(SettingActivity.this,getResources().getString(R.string.setting_judge_login_no));
                    startActivity(intent);
                }

            }
        });

    }

    void init(){
        setting_back=findViewById(R.id.setting_back);
        setting_btn=findViewById(R.id.setting_btn);
        setting_edt_name=findViewById(R.id.setting_edt_name);
        setting_edt_ip=findViewById(R.id.setting_edt_ip);
        setting_edt_port=findViewById(R.id.setting_edt_port);
    }
}
