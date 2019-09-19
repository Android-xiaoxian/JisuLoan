package com.zxy.jisuloan.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.fragment.app.FragmentActivity;

import com.githang.statusbar.StatusBarCompat;
import com.zxy.jisuloan.view.dialog.TipsDialog;

import butterknife.ButterKnife;


public abstract class BaseActivity extends FragmentActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityList.addActiviy(this);//创建时将Activity加入队列
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    protected int getLayoutId() {
        return 0;
    }

    /**
     * 设置白底黑字状态栏
     * @param activity
     */
    public void setWhiteStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(activity, Color.WHITE, true);
        }
    }

    /**
     * 透明状态栏
     * @param activity
     */
    public void setTransparentStatusBar(Activity activity){
        StatusBarUtils.with(activity).init();
    }


    public abstract void processMessage(Message message);

    protected void showTipsDialog(String tips) {
        new TipsDialog(this, tips).show();
    }

    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ActivityList.getLastActivity().processMessage(msg);
        }
    };

    public static void sendMsg(Message msg) {
        handler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        ActivityList.removeActivity(this);//Activity结束时从队列中去除
        super.onDestroy();
    }

//    @Override
//    public boolean isBaseOnWidth() {
//        return false;
//    }
//
//    @Override
//    public float getSizeInDp() {
//        return 667;
//    }
}
