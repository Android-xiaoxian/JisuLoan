package com.zxy.jisuloan.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.view.RoundedProgressBar;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Create by Fang ShiXian
 * on 2019/9/3
 */
public class EvaluateActivity2 extends BaseActivity implements CustomAdapt {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.progress)
    RoundedProgressBar progressBar;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.et)
    EditText editText;

    private String feel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_evaluate2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        tv_title.setText("写评价");
        progressBar.setColorProgress("#F8984E");
        progressBar.setColorBcg("#EEEEEE");
        progressBar.setProgress(10);
        progressBar.setMaxProgress(100);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton1) {
                    feel = "很糟糕";
                } else if (i == R.id.radioButton2) {
                    feel = "一般般";
                } else {
                    feel = "棒极了";
                }
                MyLog.e("test", feel);
            }
        });
    }


    @OnClick({R.id.img_title_back, R.id.tv_rule, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.tv_rule:
                MyLog.e("test", "活动规则");
                break;
            case R.id.btn:
                MyLog.e("test", "提交按钮");
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
