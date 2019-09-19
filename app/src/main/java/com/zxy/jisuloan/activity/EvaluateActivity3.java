package com.zxy.jisuloan.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Create by Fang ShiXian
 * on 2019/9/3
 */
public class EvaluateActivity3 extends BaseActivity implements CustomAdapt {

    @BindView(R.id.txt_public_title)
    TextView tv_title;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_evaluate3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar(this);
        initView();
    }

    private void initView() {
        tv_title.setText("");

    }


    @OnClick({R.id.img_title_back, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
            case R.id.btn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void processMessage(Message message) {

    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
