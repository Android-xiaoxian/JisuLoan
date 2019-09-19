package com.zxy.jisuloan.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/29
 */
public class MsgCenterActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.txt_msg_all)
    CheckedTextView ctv_all;
    @BindView(R.id.txt_msg_system)
    CheckedTextView ctv_system;
    @BindView(R.id.txt_msg_activity)
    CheckedTextView ctv_activity;

    CheckedTextView[] ctvs;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_center;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();
    }

    private void initView() {
        tv_title.setText("消息中心");
        ctvs = new CheckedTextView[]{ctv_all, ctv_system, ctv_activity};

    }

    @OnClick({R.id.img_title_back,R.id.txt_msg_all, R.id.txt_msg_system, R.id.txt_msg_activity,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.txt_msg_all:
                checkCtv(0);
                break;
            case R.id.txt_msg_system:
                checkCtv(1);
                break;
            case R.id.txt_msg_activity:
                checkCtv(2);
                break;
            default:
                break;
        }
    }

    private int lastCheck = 1;

    private void checkCtv(int current) {
        if (lastCheck == current) {
            return;
        }
        ctvs[current].setChecked(true);
        ctvs[lastCheck].setChecked(false);
        lastCheck = current;
    }


    @Override
    public void processMessage(Message message) {

    }
}
