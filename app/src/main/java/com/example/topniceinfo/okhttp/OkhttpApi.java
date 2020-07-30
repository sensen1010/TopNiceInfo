package com.example.topniceinfo.okhttp;


import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.topniceinfo.utils.LinkSharedPreUtil;

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
                .add("userName","ofTRqGYN")
                .add("password","123456")
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
                if (!response.isSuccessful()){

                }else {

                }
                Log.d("OkHttpOnResponse",response.isSuccessful()+"#####"+ response.body().string());
            }
        });
        return "";
    }

    //获取本机id



}
