package com.zxy.jisuloan.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.vo.QuestionVO;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/9/4
 */
public class QuestionDetailActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.tv_wt1)
    TextView tv_question;
    @BindView(R.id.ll_da1)
    LinearLayout ll_answer;
    @BindView(R.id.tv_da1)
    TextView tv_answer;
    @BindView(R.id.tv_useful)
    TextView tv_useful;
    @BindView(R.id.tv_unUse)
    TextView tv_unUse;

    private String questionId ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        iniView();
    }

    private void iniView() {
        tv_title.setText(R.string.str42);
        ll_answer.setVisibility(View.VISIBLE);
        tv_question.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        Intent intent = getIntent();
        QuestionVO.Data data = (QuestionVO.Data) intent.getSerializableExtra("data");
        String question = data.getProblem();
        String answer = data.getAnswer();
        tv_question.setText(question);
        tv_answer.setText(answer);
    }

    private boolean evaluate = false;

    @OnClick({R.id.img_title_back, R.id.tv_useful, R.id.tv_unUse, R.id.bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.tv_useful:
                if (!evaluate) {
                    evaluate = true;
                    Drawable left = getResources().getDrawable(R.mipmap.icon_zan);
                    tv_useful.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    tv_useful.setTextColor(getResources().getColor(R.color.color_red_txt2));
                }

                break;
            case R.id.tv_unUse:
                if (!evaluate) {
                    evaluate = true;
                    Drawable left2 = getResources().getDrawable(R.mipmap.icon_wy_2);
                    tv_unUse.setCompoundDrawablesWithIntrinsicBounds(left2, null, null, null);
                    tv_unUse.setTextColor(getResources().getColor(R.color.color_red_txt2));
                }
                break;
            case R.id.bottom:
                conversationWrapper();
                break;
            default:
                break;
        }
    }



    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    conversationWrapper();
                } else {
                    MQUtils.show(this, com.meiqia.meiqiasdk.R.string.mq_sdcard_no_permission);
                }
                break;
            }
        }

    }

    private void conversationWrapper() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            conversation();
        }
    }

    private void conversation() {

        HashMap<String, String> clientInfo = new HashMap<>();
//        clientInfo.put("name", "富坚义博");
//        clientInfo.put("avatar", "https://s3.cn-north-1.amazonaws.com.cn/pics.meiqia.bucket/1dee88eabfbd7bd4");
//        clientInfo.put("gender", "男");
        clientInfo.put("tel", Config.getPhone());
        clientInfo.put("用户id", Config.getUserId());
        Intent intent = new MQIntentBuilder(this)
                .setCustomizedId( Config.getPhone()) // 相同的 id 会被识别为同一个顾客
                .setClientInfo(clientInfo)
                .build();
        startActivity(intent);
    }


    @Override
    public void processMessage(Message message) {

    }
}
