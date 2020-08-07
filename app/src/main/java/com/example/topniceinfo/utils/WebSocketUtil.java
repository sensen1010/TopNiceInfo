package com.example.topniceinfo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebSocketUtil {
    static final String pathName="/info";
    private static String WS = "ws://";
    private static String URL = "/imserver/";
    public static  boolean  isLink=false;
    public static  boolean  btnIsChink=false;
    public static String getuRL(){
        String name= LinkSharedPreUtil.getSharePre().getName();
        String linkId= LinkSharedPreUtil.getSharePre().getLinkId();
        String ip= LinkSharedPreUtil.getSharePre().getIp();
        String port= LinkSharedPreUtil.getSharePre().getPort();
        String enterId=LoginSharedPreUtil.getSharePre().getEnterId();
        try {
            //System.out.println(WS+ip+":"+port+URL+linkId+"/"+ URLDecoder.decode(name, "utf-8"));
           return WS+ip+":"+port+pathName+URL+enterId+"/"+linkId+"/"+ URLEncoder.encode(URLEncoder.encode(name,"UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       // return WS+ip+":"+port+URL+linkId+"/"+name;
       return null;
    }

}
