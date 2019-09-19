package com.zxy.jisuloan.utils;

import android.util.Log;

import com.zxy.jisuloan.BuildConfig;

/**
 *  debug模式下打印log，打包后不打印
 *  create by Fang Shixian
 *  on 2018/9/25 0025
 */
public class MyLog {

    //读取BuildConfig.LOG_DEBUG 签名时为FALSE 不打印 debug时为true 打印
    public static void i(String tag, String message) {
        if (BuildConfig.LOG_DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (BuildConfig.LOG_DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(tag, message);
        }
    }


}
