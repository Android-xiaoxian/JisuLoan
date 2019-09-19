package com.zxy.jisuloan.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.jisuloan.R;


/**
 * Created by Administrator on 2018/6/21 0021.
 */

public class ToastUtils2 {
    private static Context context;
    private static Toast toast = null;
    private TextView tv;
    private static ToastUtils2 instance;

    private ToastUtils2(Context context) {
        this.context = context;
        createToast();
    }

    //基于application类获取全局的context（与下面一个方法效果差不多）
    public static ToastUtils2 getInstance() {
        if (instance == null) {
            synchronized (ToastUtils2.class) {
                if (instance == null) {
                    instance = new ToastUtils2(BaseApplication.getAppContext());
                }
            }
        }
        return instance;
    }

    //使用时传入的context（与上面这个方法效果差不多）
    public static ToastUtils2 getInstance(Context context) {
        if (instance == null) {
            synchronized (ToastUtils2.class) {
                if (instance == null) {
                    instance = new ToastUtils2(context);
                }
            }
        }
        return instance;
    }

    private void createToast() {
        toast = new Toast(context);
        //dp2px这个方法是将dp转成px
        toast.setGravity(Gravity.CENTER, 0, ViewUtil.dp2px(100));
        toast.setDuration(Toast.LENGTH_LONG);
        //自定义toast样式
        View v = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
        tv = v.findViewById(R.id.tv_toast);
        toast.setView(v);
    }


    public void showtoast(String message, int duration) {
        toast.setDuration(duration);
        showToast(message);
    }

    public void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
            tv.setText(message);
        }
        toast.show();
    }


    public void showToast(int stringId) {
        showToast(context.getString(stringId));
    }

    public void cancel() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}
