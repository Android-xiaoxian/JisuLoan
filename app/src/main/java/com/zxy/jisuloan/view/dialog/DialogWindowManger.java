package com.zxy.jisuloan.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.zxy.jisuloan.utils.ViewUtil;

/**
 * Create by Fang ShiXian
 * on 2019/8/15
 * dialog窗口大小位置管理类
 */
public class DialogWindowManger {

    /**
     * 高度自适应，宽度比屏幕宽度少100dp
     *
     * @param context
     */
    public static void setDialogWindow(Context context, Dialog dialog) {
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() - ViewUtil.dp2px(100));   //宽度比屏幕宽度少100dp
        dialog.getWindow().setAttributes(p);     //设置生效
    }

    /**
     * 高度自适应，宽度跟屏幕一样宽
     *
     * @param context
     */
    public static void setDialogFullWindow(Context context, Dialog dialog) {
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth());    //宽度设置为屏幕宽度
        dialog.getWindow().setAttributes(p);     //设置生效
    }
}
