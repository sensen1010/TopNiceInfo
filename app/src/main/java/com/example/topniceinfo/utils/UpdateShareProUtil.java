package com.example.topniceinfo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UpdateShareProUtil {

    String filename="appUpdate";
    String apkMd5;
    String apkName;
    String modifyContent;
    String downLoadUrl;
    String apkSize;
    String updateTime;
    private SharedPreferences settingSpf;
    private static UpdateShareProUtil updateShareProUtil;
    public  static UpdateShareProUtil getSharePre(){
        if (updateShareProUtil!=null){
            return updateShareProUtil;
        }else {
            updateShareProUtil=new UpdateShareProUtil();
            return updateShareProUtil;
        }
    }

    public UpdateShareProUtil(){
        settingSpf=MyApplication.context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        this.apkMd5=settingSpf.getString("apkMd5","");
        this.apkName=settingSpf.getString("apkName","");
        this.modifyContent=settingSpf.getString("modifyContent","");
        this.downLoadUrl=settingSpf.getString("downLoadUrl","");
        this.apkSize=settingSpf.getString("apkSize","");
        this.updateTime=settingSpf.getString("updateTime","");
    }

    public  void  SharedPreEdit(){
        SharedPreferences.Editor settingEdit=settingSpf.edit();
        settingEdit.putString("apkMd5",apkMd5);
        settingEdit.putString("apkName",apkName);
        settingEdit.putString("modifyContent",modifyContent);
        settingEdit.putString("downLoadUrl",downLoadUrl);
        settingEdit.putString("aokSize",apkSize);
        settingEdit.putString("updateTime",updateTime);
        settingEdit.commit();
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getModifyContent() {
        return modifyContent;
    }

    public void setModifyContent(String modifyContent) {
        this.modifyContent = modifyContent;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }
}
