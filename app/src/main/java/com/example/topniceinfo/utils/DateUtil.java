package com.example.topniceinfo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String date2TimeStamp(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    public static String date2TimeStamp(String date_str, String format) {
        if (date_str == null||date_str.equals("")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.format(sdf.parse(date_str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
