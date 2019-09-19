package com.zxy.jisuloan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create by Fang ShiXian
 * on 2019/8/30
 */
public class DateUtils {

    // String format = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将时间戳转换成日期（yyyy-MM-dd）
     * @param time
     * @return
     */
    public static String timeStamp2Date(long time) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }
}
