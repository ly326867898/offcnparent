package com.offcn.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDateUtils {

    public static String getFormatTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(new Date());
        return str;
    }

    public static String getFormatTime(String pattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String str = simpleDateFormat.format(new Date());
        return str;
    }

    public static String getFormatTime(String pattern,Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String str = simpleDateFormat.format(date);
        return str;
    }
}
