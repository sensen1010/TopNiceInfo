package com.example.topniceinfo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSharedPreUtil {
    String filename="settingLogin";
    String userName;
    String pow;
    String enterId;
    String userId;
    String token;
    private SharedPreferences settingSpf;
    private static LoginSharedPreUtil sharedPreUtil;
    public  static LoginSharedPreUtil getSharePre(){
        if (sharedPreUtil!=null){
            return sharedPreUtil;
        }else {
            sharedPreUtil=new LoginSharedPreUtil();
            return sharedPreUtil;
        }
    }
    //判断是否已登录
    public  boolean judgeEmpty(){
        if(pow!=null&&!pow.equals("")){
            return true;
        }
        return false;
    }
    public LoginSharedPreUtil(){
        settingSpf=MyApplication.context.getSharedPreferences(filename, Context.MODE_PRIVATE);
         this.userName=settingSpf.getString("userName","");
         this.pow=settingSpf.getString("pow","");
         this.enterId=settingSpf.getString("enterId","");
         this.userId=settingSpf.getString("userId","");
         this.token=settingSpf.getString("token","");
    }

     public   void  SharedPreEdit(){
        SharedPreferences.Editor settingEdit=settingSpf.edit();
        settingEdit.putString("userName",userName);
        settingEdit.putString("pow",pow);
        settingEdit.putString("enterId",enterId);
         settingEdit.putString("userId",userId);
         settingEdit.putString("token",token);
        settingEdit.commit();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPow() {
        return pow;
    }

    public void setPow(String pow) {
        this.pow = pow;
    }

    public String getEnterId() {
        return enterId;
    }

    public void setEnterId(String enterId) {
        this.enterId = enterId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
