package com.zxy.jisuloan.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.adapter.QuestionAdapter;
import com.zxy.jisuloan.adapter.QuestionAdapter0;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.view.ListViewForScrollView;
import com.zxy.jisuloan.vo.QuestionClassVO;
import com.zxy.jisuloan.vo.QuestionVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Create by Fang ShiXian
 * on 2019/8/28
 */
public class HelpCenterActivity extends BaseActivity implements CustomAdapt {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.lv_question)
    ListViewForScrollView lv_question;

    private List<QuestionClassVO.Data> list;//问题分类
    private QuestionAdapter adapter;//问题分类
    private QuestionAdapter0 adapter2; //热门问题
    private List<QuestionVO.Data> listHot, list1, list2, list3, list4;
    //热门问题，申请问题，授信问题，借款问题，还款问题
    private QuestionVO question1, question2, question3, question4;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_center;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();
        initData();
        initMeiQia();
    }


    private void initView() {
        tv_title.setText("帮助中心");
        adapter = new QuestionAdapter(this, list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HelpCenterActivity.this, QuestionActivity.class);
                switch (i) {
                    case 0:
                        intent.putExtra(Config.QUESTION_ClASS, question1);
                        break;
                    case 1:
                        intent.putExtra(Config.QUESTION_ClASS, question2);
                        break;
                    case 2:
                        intent.putExtra(Config.QUESTION_ClASS, question3);
                        break;
                    case 3:
                        intent.putExtra(Config.QUESTION_ClASS, question4);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });

        adapter2 = new QuestionAdapter0(this, listHot);
        lv_question.setAdapter(adapter2);
        lv_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QuestionVO.Data data = listHot.get(i);
                Intent intent = new Intent(HelpCenterActivity.this, QuestionDetailActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });


    }

    /**
     * 美恰客服
     */
    private void initMeiQia() {
        MQConfig.init(this, Config.MEIQIA_KEY, new OnInitCallback() {
            @Override
            public void onSuccess(String clientId) {
//                Toast.makeText(HelpCenterActivity.this, "init success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String message) {
                Toast.makeText(HelpCenterActivity.this, "客服系统初始化失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initData() {
        //问题分类
        BaseApi.request(BaseApi.createApi(IService.class).getQuestionClass(), new BaseApi.IResponseListener<QuestionClassVO>() {
            @Override
            public void onSuccess(QuestionClassVO data) {
                list = data.getData();
                adapter.upDateAdapter(list);
            }

            @Override
            public void onFail() {

            }
        });

        //全部问题
        BaseApi.request(BaseApi.createApi(IService.class).getQuestions(),
                new BaseApi.IResponseListener<QuestionVO>() {
                    @Override
                    public void onSuccess(QuestionVO data) {
                        if (data != null) {
                            List<QuestionVO.Data> listData = data.getData();
                            listHot = new ArrayList<>();
                            list1 = new ArrayList<>();
                            list2 = new ArrayList<>();
                            list3 = new ArrayList<>();
                            list4 = new ArrayList<>();
                            for (QuestionVO.Data d : listData) {
                                if ("1".equals(d.getHot())) {
                                    listHot.add(d);
                                }
                                if ("申请问题".equals(d.getClassname())) {
                                    list1.add(d);
                                } else if ("授信问题".equals(d.getClassname())) {
                                    list2.add(d);
                                } else if ("借款问题".equals(d.getClassname())) {
                                    list3.add(d);
                                } else if ("还款问题".equals(d.getClassname())) {
                                    list4.add(d);
                                }
                            }
                            question1 = new QuestionVO();
                            question2 = new QuestionVO();
                            question3 = new QuestionVO();
                            question4 = new QuestionVO();
                            question1.setData(list1);
                            question2.setData(list2);
                            question3.setData(list3);
                            question4.setData(list4);
                            adapter2.upDateAdapter(listHot);
//                            ScrollDisabledListView.getListViewHeight(adapter2, lv_question);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }

    @OnClick({R.id.img_title_back, R.id.bottom, R.id.btn_kefu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.bottom:
            case R.id.btn_kefu:
                // 兼容Android6.0动态权限
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
                .setCustomizedId(Config.getPhone() + 1) // 相同的 id 会被识别为同一个顾客
//                .setClientInfo(clientInfo)
                .updateClientInfo(clientInfo)
                .build();
        startActivity(intent);
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
