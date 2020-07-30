package com.example.topniceinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class Util {

    public static File dir = new File(Environment.getExternalStorageDirectory() + "/.Imageloader/json/");

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    //读取数据
    public static String readTextFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(dir + "/" + filePath);
            InputStream in = null;
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                sb.append((char) tempbyte);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    //保存数据
    public static void saveToSDCard(Activity mActivity, String filename, String content) {
        String en = Environment.getExternalStorageState();
        //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
        if (en.equals(Environment.MEDIA_MOUNTED)) {
            try {
                dir.mkdirs(); //create folders where write files
                File file = new File(dir, filename);
                OutputStream out = new FileOutputStream(file);
                out.write(content.getBytes());
                out.close();
                Util.showToast(mActivity, "保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                Util.showToast(mActivity, "保存失败");
            }
        } else {
            //提示用户SDCard不存在或者为写保护状态
            Util.showToast(mActivity, "SDCard不存在或者为写保护状态");
        }
    }
    //随机生成颜色
    public static String randomColor() {
        String r,g,b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length()==1 ? "0" + r : r ;
        g = g.length()==1 ? "0" + g : g ;
        b = b.length()==1 ? "0" + b : b ;
        return "#"+r+g+b;
    }

    //判断ip是否合法
    public static Boolean ipCheck(String ip) {
        if (ip != null && !ip.isEmpty()) {
            // 定义正则表达式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
//            //二级域名
//            String regStr = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）――+|{}【】‘；：”“'。，、？]){6,20}$";

            // 判断ip地址是否与正则表达式匹配
            if (ip.matches(regex)) {
                return true;
            }else {
                return false;
            }
        }
        // 返回判断信息
        return false;
    }

}
