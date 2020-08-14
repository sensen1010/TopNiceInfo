package com.example.topniceinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Util {

    public static File dir = new File(Environment.getExternalStorageDirectory() + "/.Imageloader/json/");

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    //md5加密
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    //对布局数据进行排序（根据每条数据中的Z）
    public static JSONArray sortProxyAndCdn(JSONArray bindArrayResult) {
        List<JSONObject> list = JSONArray.parseArray(bindArrayResult.toJSONString(), JSONObject.class);
        System.out.println("排序前："+bindArrayResult);
        Collections.sort(list, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                int a = Integer.parseInt(o1.getString("z"));
                int b = Integer.parseInt(o2.getString("z"));
                if (a < b) {
                    return -1;
                } else if(a == b) {
                    return 0;
                } else
                    return 1;
            }
        });
        JSONArray jsonArray = JSONArray.parseArray(list.toString());
        System.out.println("排序后："+jsonArray);
        return jsonArray;
    }
    //获取mac地址，仅支持8.0以下版本
    public static String getMac() {
        String macSerial = null;
        String str = "";
        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    //读取数据
    public static String readTextFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(dir + "/" + filePath);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;

            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    //保存数据
    public static void saveToSDCard(String filename, String content) {
        String en = Environment.getExternalStorageState();
        //获取SDCard状态,如果SDCard插入了手机且为非写保护状态
        if (en.equals(Environment.MEDIA_MOUNTED)) {
            try {
                dir.mkdirs(); //create folders where write files
                File file = new File(dir, filename);
                OutputStreamWriter out =  new OutputStreamWriter(new FileOutputStream(file), "utf-8");
                out.append(content);
                out.close();
                //Util.showToast(MyApplication.context, "保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                //Util.showToast(MyApplication.context, "保存失败");
            }
        } else {
            //提示用户SDCard不存在或者为写保护状态
            //Util.showToast(MyApplication.context, "SDCard不存在或者为写保护状态");
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
