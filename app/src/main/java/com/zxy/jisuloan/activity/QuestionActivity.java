package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.view.MyQuestionView;
import com.zxy.jisuloan.vo.QuestionVO;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/9/3
 */
public class QuestionActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.ll_question_list)
    LinearLayout linearLayout;

    private MyQuestionView[] views;

    private int lastShow = -1;//记录上一个打开的答案

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        Intent intent = getIntent();
        QuestionVO questionVO = (QuestionVO) intent.getSerializableExtra(Config.QUESTION_ClASS);
        List<QuestionVO.Data> list = questionVO.getData();
        if (list == null || list.size() == 0) {
            ToastUtils.showToast("没有获取到数据");
            finish();
            return;
        }
        String title = list.get(0).getClassname();
        tv_title.setText(title);
        tv_title.setText(title);

        views = new MyQuestionView[list.size()];
        MyQuestionView myQuestionView;
        for (int i = 0; i < list.size(); i++) {
            myQuestionView = new MyQuestionView(this);
            myQuestionView.setContent(list.get(i).getProblem(), list.get(i).getAnswer());
            linearLayout.addView(myQuestionView, i);
            views[i] = myQuestionView;//将每个myQuestionView添加到数组里
            myQuestionView.setmInterface(i, new MyQuestionView.OnClickListenerInterface() {
                @Override
                public void closeLast(int number) {
                    if (lastShow == -1) {
                        views[number].open();//打开当前点击的
                        lastShow = number;
                    } else if (lastShow != number) {
                        views[number].open();//打开当前点击的
                        views[lastShow].close();//关闭上一个显示的
                        lastShow = number;
                    } else {
                        //点击已打开的就直接关闭
                        views[number].close();
                        lastShow = -1;
                    }


                }

                @Override
                public void doUseful(int number) {
                    MyLog.e("test", "第" + number + "个点赞了");
                }

                @Override
                public void doUnUse(int number) {
                    MyLog.e("test", "第" + number + "个点踩了");
                }
            });
        }
    }

    @OnClick({R.id.img_title_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_title_back) {
            finish();
        }
    }

    @Override
    public void processMessage(Message message) {

    }
}
