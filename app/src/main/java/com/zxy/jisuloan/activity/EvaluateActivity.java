package com.zxy.jisuloan.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/9/2
 */
public class EvaluateActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.lv_evaluate)
    ListView listView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_evaluate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        tv_title.setText("评价拿奖励");
        String[] strings = new String[20];
        for (int i = 0; i < 20; i++) {
            strings[i] = "第" + i + "个";
        }

        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, strings));
        View view = LayoutInflater.from(this).inflate(R.layout.include_bottom, null);
        view.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(40));
        view.setLayoutParams(params);
        listView.addFooterView(view);

    }

    @OnClick({R.id.img_title_back})
    public void onClick(View view) {
        finish();
    }

    @Override
    public void processMessage(Message message) {

    }
}
