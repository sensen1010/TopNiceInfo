package com.example.topniceinfo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LinkSharedPreUtil {
    String filename="settingService";
    final  String pathName="/info";
    String ip;
    String name;
    String port;
    String linkId;
    private SharedPreferences settingSpf;
    private static LinkSharedPreUtil sharedPreUtil;
    public  static LinkSharedPreUtil getSharePre(){
        if (sharedPreUtil!=null){
            return sharedPreUtil;
        }else {
            sharedPreUtil=new LinkSharedPreUtil();
            return sharedPreUtil;
        }
    }
    //判断是否存在ip
    public  boolean judgeEmpty(){
        if(ip!=null&&!ip.equals("")){
            return true;
        }
        return false;
    }

    //返回地址
    public String urlServer(){
        return "http://"+ip+":"+port+pathName;
    }

    //返回文件地址
    public String imgUrlServer(){
        return "http://"+ip+":"+port+"/file/";
    }

    public LinkSharedPreUtil(){
        settingSpf=MyApplication.context.getSharedPreferences(filename, Context.MODE_PRIVATE);
         this.ip=settingSpf.getString("ip","");
         this.name=settingSpf.getString("name","");
         this.port=settingSpf.getString("port","");
         this.linkId=settingSpf.getString("linkId","");
    }
     public  void  SharedPreEdit(){
        SharedPreferences.Editor settingEdit=settingSpf.edit();
        settingEdit.putString("ip",ip);
        settingEdit.putString("name",name);
        settingEdit.putString("port",port);
        settingEdit.putString("linkId",linkId);
        settingEdit.commit();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }







}
