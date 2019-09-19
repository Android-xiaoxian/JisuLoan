package com.zxy.jisuloan.utils;

import android.content.Context;
import android.graphics.Color;

import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * Created by fengwei on 2018/5/16 0016.
 */

public class LoadDialog {
    private static ZLoadingDialog dialog;

    private static void initDialog(Context context) {
        dialog = new ZLoadingDialog(context);
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.WHITE)//颜色
                .setHintText("正在加载")
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.WHITE)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.parseColor("#CC111111")); // 设置背景色，默认白色
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public static void show(Context context) {
        initDialog(context);
        dialog.show();
    }

    public static void dimiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
