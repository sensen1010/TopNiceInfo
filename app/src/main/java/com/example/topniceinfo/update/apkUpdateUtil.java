package com.example.topniceinfo.update;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.topniceinfo.update.pyUtils;
import com.example.topniceinfo.utils.MyApplication;
import com.example.topniceinfo.utils.UpdateShareProUtil;

public class apkUpdateUtil {
   public static void showDiaLog(){
       try {
           String modifyContent= UpdateShareProUtil.getSharePre().getModifyContent();
           String updateTime= UpdateShareProUtil.getSharePre().getUpdateTime();
           String apkSize= UpdateShareProUtil.getSharePre().getApkSize();
           AlertDialog dialog = new AlertDialog.Builder(MyApplication.activity).setTitle
                   ("有更新").setMessage(modifyContent+"\n\n\n"+updateTime+"\n"+apkSize)
                   .setNeutralButton("暂不更新", new DialogInterface
                           .OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                           //清除保存下的ApkMd5
                           UpdateShareProUtil.getSharePre().setApkMd5("");
                           UpdateShareProUtil.getSharePre().SharedPreEdit();
                       }
                   }).setNegativeButton("更新", new DialogInterface
                           .OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           pyUtils.downUrl(false);
                       }
                   }).show();
           dialog.setCanceledOnTouchOutside(false);//可选
           dialog.setCancelable(false);//可选
       }catch (Exception e){
           UpdateShareProUtil.getSharePre().setApkMd5("");
           UpdateShareProUtil.getSharePre().SharedPreEdit();
       }

    }

}
