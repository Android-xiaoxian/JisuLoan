package com.zxy.jisuloan.activity;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_title.setText("关于我们");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @OnClick({R.id.img_title_back})
    public void onClick() {
        finish();
    }

    @Override
    public void processMessage(Message message) {

    }

}
