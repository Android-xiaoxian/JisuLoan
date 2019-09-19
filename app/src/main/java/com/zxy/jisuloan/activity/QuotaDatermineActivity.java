package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.ActivityList;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.view.RoundedProgressBar;
import com.zxy.jisuloan.vo.BaseResponse;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/22
 */
public class QuotaDatermineActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.txt_quota_deter)
    TextView tv_content;
    @BindView(R.id.rpb_quota_dater)
    RoundedProgressBar progressBar;
    @BindView(R.id.tv_quota_progress)
    TextView tv_progress;
    @BindView(R.id.tv_quota_1)
    TextView tv_1;
    @BindView(R.id.tv_quota_2)
    TextView tv_2;
    @BindView(R.id.tv_quota_3)
    TextView tv_3;

    private String[] steps = new String[]{"身份证信息", "人脸识别", "学历信息", "职业信息", "月均收入", "期望借款金额",
            "居住地址", "单位名称", "工作地址", "婚姻状况", "直系亲属关系", "朋友", "同事",};
    private long[] times = new long[13];
    private TextView[] textViews = new TextView[]{tv_1, tv_2, tv_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, Color.WHITE, true);
        initView();
        initData();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_quota_determine;
    }

    long countTime = 0;
    long curTime = 0;

    private void initView() {
        tv_title.setText("极速闪贷");
        tv_content.setText(Html.fromHtml("不要走开，<font color=#F8984E>额度</font>计算中"));
        progressBar.setColorBcg("#DFDFDF");
        progressBar.setColorProgress("#F8984E");
        progressBar.setMaxProgress(100);
        for (int i = 0; i < steps.length; i++) {
            double time0 = Math.random();
            long l = (long) (time0 * 500 + 1000);
            times[i] = l;
            countTime += l;
        }
        MyLog.e("test", "countTime = " + countTime);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < steps.length; i++) {

                    updateView(i);
                    try {
                        if (failStatus) {
                            break;
                        }
                        if (finishStatus) {
                            Thread.sleep(times[i] / 10);
                        } else {
                            Thread.sleep(times[i]);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private boolean finishStatus = false;
    private boolean failStatus = false;

    private void initData() {
        String userId = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");
        BaseApi.request(BaseApi.createApi(IService.class).getAuthStatus(userId),
                new BaseApi.IResponseListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if ("0".equals(data.getError())) {
                            finishStatus = true;
                        } else {
                            showTipsDialog(data.getMsg());
                            finishStatus = true;
                        }
                    }

                    @Override
                    public void onFail() {
                        failStatus = true;
//                        showTipsDialog("抱歉，出错了");
                    }
                });
    }

    @OnClick({R.id.img_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                startActivity(new Intent(this, MainActivity.class));
                ActivityList.removeActivity(new OperatorsSMSActivity());
                ActivityList.removeActivity(new MyInfoActivity());
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void processMessage(Message message) {

    }


    private void updateView(int num) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_1.setText(steps[num] + " 完成");
                if (num < steps.length - 1) {
                    tv_2.setText(steps[num + 1] + " 准备");
                } else {
                    tv_2.setText("");
                }
                if (num < steps.length - 2) {
                    tv_3.setText(steps[num + 2] + " 检测中");
                } else {
                    tv_3.setText("");
                }
                curTime += times[num];
                MyLog.e("test", "curTime = " + curTime);
                int progress = (int) ((double) curTime / countTime * 100);
                progressBar.setProgress(progress);
                tv_progress.setText(progress + "%");
                if (num == steps.length - 1 && !QuotaDatermineActivity.this.isDestroyed()) {
//                    showTipsDialog("额度计算成功");
                    startActivity(new Intent(QuotaDatermineActivity.this, PushActivity.class));
                    finish();
                }
            }
        });
    }

}
