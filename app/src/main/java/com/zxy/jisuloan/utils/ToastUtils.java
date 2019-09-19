package com.zxy.jisuloan.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.jisuloan.R;


/**
 * Created by Fang ShiXian on 2019/8/15 0021.
 */

public class ToastUtils {
    private static Context context = BaseApplication.getAppContext();
    private static Toast toast = null;
    private static TextView tv;

    //context取用Application全局的context，
    //showToast不用每次调用都传入context
    //也可以这样传入两个参数，每次调用都传context
//        public static void showToast(Context context,String message) {
    public static void showToast(String message) {
        if (toast == null) {
            toast = new Toast(context);
            //dp2px这个方法是将dp转成px
            toast.setGravity(Gravity.CENTER, 0, ViewUtil.dp2px(100));
            toast.setDuration(Toast.LENGTH_SHORT);
            //自定义toast样式
            View v = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
            tv = v.findViewById(R.id.tv_toast);
            toast.setView(v);
        }
        tv.setText(message);
        toast.show();
    }

    public static void showToast(String message, int duration) {
        toast.setDuration(duration);
        showToast(message);
    }

    public static void showToast(int stringId) {
        showToast(context.getString(stringId));
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
