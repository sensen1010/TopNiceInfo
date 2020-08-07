package com.example.topniceinfo.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class ProgramSharedPreUtil {
    String filename="settingProgram";
    String programId;
    private SharedPreferences settingSpf;
    private static ProgramSharedPreUtil sharedPreUtil;
    public  static ProgramSharedPreUtil getSharePre(){
        if (sharedPreUtil!=null){
            return sharedPreUtil;
        }else {
            sharedPreUtil=new ProgramSharedPreUtil();
            return sharedPreUtil;
        }
    }

    public ProgramSharedPreUtil(){
        settingSpf=MyApplication.context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        this.programId=settingSpf.getString("programId","");
    }

    public  void  SharedPreEdit(){
        SharedPreferences.Editor settingEdit=settingSpf.edit();
        settingEdit.putString("programId",programId);
        settingEdit.commit();
    }


    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }
}
