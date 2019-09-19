package com.zxy.jisuloan.activity;

import android.os.Bundle;
import android.os.Message;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;

/**
 * Create by Fang ShiXian
 * on 2019/8/29
 */
public class MsgDetailActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
    }

    @Override
    public void processMessage(Message message) {

    }
}
