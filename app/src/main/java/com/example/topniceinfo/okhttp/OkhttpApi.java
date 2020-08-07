package com.example.topniceinfo.okhttp;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.topniceinfo.LoginActivity;
import com.example.topniceinfo.ProgramHomeActivity;
import com.example.topniceinfo.utils.LinkSharedPreUtil;
import com.example.topniceinfo.utils.LoginSharedPreUtil;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.Util;
import com.example.topniceinfo.websocket.WebSocketUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpApi {


    public static OkhttpApi okhttpApi2;

    public static OkhttpApi getOkhttpApi(){
        if (okhttpApi2!=null){
            return okhttpApi2;
        }else {
            okhttpApi2=new OkhttpApi();
            return  okhttpApi2;
        }
    }

    public String login(String name,String pow){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        RequestBody requestBody=new FormBody.Builder()
                .add("userName",name)
                .add("password",pow)
                .build();
        Request request=new Request.Builder()
                .url(LinkSharedPreUtil.getSharePre().urlServer() +"/users/login")
                .post(requestBody)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkHttpOnFailure", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //访问正常
                if (response.isSuccessful()){
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        if ( jsonObject.getString("code").equals("1")){
                            Message message=new Message();
                            message.what=1;
                            handler.sendMessageDelayed(message,100);
                        }
                        else {
                            JSONObject data= new JSONObject(jsonObject.getString("data"));
                            LoginSharedPreUtil.getSharePre().setEnterId(data.getString("enterId"));
                            LoginSharedPreUtil.getSharePre().setUserId(data.getString("userId"));
                            LoginSharedPreUtil.getSharePre().setToken(jsonObject.getString("token"));
                            LoginSharedPreUtil.getSharePre().SharedPreEdit();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessageDelayed(message,100);
                }

            }
        });
        return "";
    }

    public String findByProgram(String enterId,String programId){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request=new Request.Builder()
                .url(LinkSharedPreUtil.getSharePre().urlServer() +"/program/pro/"+enterId+"/"+programId)
                .get()
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OkHttpOnFailure", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("查询成功");
                //访问正常
                if (response.isSuccessful()){
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONObject data= new JSONObject(jsonObject.getString("data"));
                        String content=data.getString("content");
                        //保存
                        Util.saveToSDCard("pramData",content);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessageDelayed(message,100);
                }

            }
        });
        return "";
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Util.showToast(MyApplication.context,"登录失败");
                    LoginSharedPreUtil.getSharePre().setEnterId("");
                    LoginSharedPreUtil.getSharePre().setUserId("");
                    LoginSharedPreUtil.getSharePre().setToken("");
                    LoginSharedPreUtil.getSharePre().SharedPreEdit();
                    break;
                case -1:
                    break;
            }
        }
    };


    //获取本机id



}
