package com.zxy.jisuloan.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/28
 */
public class CreditScoreActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_credit_score;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();
    }

    private void initView() {
        tv_title.setText("信用分");
    }


    @OnClick({R.id.img_title_back,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void processMessage(Message message) {

    }
}
