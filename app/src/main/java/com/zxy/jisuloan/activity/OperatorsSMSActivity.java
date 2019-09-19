package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.LoadDialog;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.StatusBarUtils;
import com.zxy.jisuloan.view.ApplyStepView;
import com.zxy.jisuloan.vo.PollingVO;
import com.zxy.jisuloan.vo.YYSResuleVO;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/23
 */
public class OperatorsSMSActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.asv_user_info)
    ApplyStepView applyStepView;
    @BindView(R.id.et_sms)
    EditText et_sms;

    private String tid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this).init();
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_operators_sms;
    }

    private void initView() {
        tv_title.setText("运营商认证");
        applyStepView.changeStep(3);
        Intent intent = getIntent();
        tid = intent.getStringExtra("tid");
        String need = intent.getStringExtra("need");
        if (!TextUtils.isEmpty(need)) {
            switch (need) {
                case "SMS":
                    showTipsDialog("短信验证码已发送，请输入验证码");
                    break;
                case "SMSJilinTelecom":
                    showTipsDialog("编辑短信“cxxd”并发送到10001获取短验证码，然后提交该短信验证码。");
                    break;
                case "loginSMS":
                    showTipsDialog("短信验证码已发送，请输入验证码");
                    break;
                case "SMSAgain":
                    showTipsDialog("上次提交的短信验证码有误，请重新提交。");
                    break;
                case "newSMS":
                    showTipsDialog("短信验证码有误或已过期，新的验证码已发送，请提交新的验证码。");
                    break;
                case "loginSMSAgain":
                    showTipsDialog("上次提交的短信验证码有误， 请重新提交。");
                    break;
                case "newLoginSMS":
                    showTipsDialog("短信验证码有误或已过期，新的验证码已发送，请提交新的验证码。");
                    break;
                default:
                    break;
            }
        }
    }

    @OnClick({R.id.btn_auth_next, R.id.img_title_back, R.id.txt_kefu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_auth_next:
                sendSMS();
                break;
            case R.id.img_title_back:
                finish();
                break;
            case R.id.txt_kefu:
                startActivity(new Intent(this, HelpCenterActivity.class));
                break;
            default:
                break;
        }
    }

    private void sendSMS() {
        String smsCode = et_sms.getText().toString();
        if (TextUtils.isEmpty(smsCode)) {
            showTipsDialog("请输入验证码");
            return;
        }
        LoadDialog.show(this);
        BaseApi.request(BaseApi.createApi(IService.class).sendSMS(tid, smsCode), new BaseApi.IResponseListener<YYSResuleVO>() {
            @Override
            public void onSuccess(YYSResuleVO data) {
                if (data.getResult().getCode() == null) {
                    if ("0".equals(data.getError())) {
                        LoadDialog.show(OperatorsSMSActivity.this);
                        tid = data.getResult().getTid();
                        //延时3秒进行下一次查询
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                startPolling();
                            }
                        }, 3000);
                    } else {
                        et_sms.setText("");
                        showTipsDialog(data.getMsg());
                    }
                } else {
                    et_sms.setText("");
                    showTipsDialog(data.getResult().getError());
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private Timer timer = new Timer();
    String userId = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");

    private void startPolling() {
        BaseApi.request2(BaseApi.createApi(IService.class).polling(userId, tid), new BaseApi.IResponseListener<PollingVO>() {
            @Override
            public void onSuccess(PollingVO data) {
                if ("0".equals(data.getError())) {
                    PollingVO.Result body = data.getBody();
                    if (body.getCode() == null) {
                        if ("processing".equals(body.getStatus())) {
                            //延时3秒进行下一次查询
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    MyLog.e("test", "验证码页面processing继续查询");
                                    startPolling();
                                }
                            }, 3000);

                        } else {
                            MyLog.e("test", "验证码页面processed或者failed停止查询");
                            LoadDialog.dimiss();
                            et_sms.setText("");
                            checkResult(body);
                        }
                    } else {
                        LoadDialog.dimiss();
                        et_sms.setText("");
                        showTipsDialog(body.getError());
                    }
                } else {
                    LoadDialog.dimiss();
                    et_sms.setText("");
                    showTipsDialog(data.getMsg());
                }
            }

            @Override
            public void onFail() {
                et_sms.setText("");
                LoadDialog.dimiss();
            }
        });
    }


    /**
     * 判断处理各种情况
     *
     * @param body
     */
    public void checkResult(PollingVO.Result body) {
        if ("suspended".equals(body.getStatus())) {
            switch (body.getNeed()) {
                case "SMS":
                    showTipsDialog("短信验证码已发送，请输入验证码");
                    break;
                case "SMSJilinTelecom":
                    showTipsDialog("编辑短信“cxxd”并发送到10001获取短验证码，然后提交该短信验证码。");
                    break;
                case "loginSMS":
                    showTipsDialog("短信验证码已发送，请输入验证码");
                    break;
                case "SMSAgain":
                    showTipsDialog("上次提交的短信验证码有误， 请重新提交。");
                    break;
                case "newSMS":
                    showTipsDialog("短信验证码有误或已过期，新的验证码已发送，请提交新的验证码。");
                    break;
                case "loginSMSAgain":
                    showTipsDialog("上次提交的短信验证码有误， 请重新提交。");
                    break;
                case "newLoginSMS":
                    showTipsDialog("短信验证码有误或已过期，新的验证码已发送，请提交新的验证码。");
                    break;
                default:
                    break;
            }
        } else if ("failed".equals(body.getStatus())) {
            switch (body.getNeed()) {
                case "1000":
                    showTipsDialog("1000:不支持初始密码登录，请重置后重试");
                    break;
                case "1001":
                    showTipsDialog("1001:当前登录手机号码未设置服务密码，请您设置服务密码");
                    break;
                case "2000":
                    showTipsDialog("2000:账号暂时锁定，请稍后再试");
                    break;
                case "2001":
                    showTipsDialog("2001:号码是空号");
                    break;
                case "2002":
                    showTipsDialog("2002:已经使用其他客户端或者网页登录，请退出后重试");
                    break;
                case "3000":
                    showTipsDialog("3000:运营商发送短信随机码失败");
                    break;
                case "3001":
                    showTipsDialog("3001:运营商短信随机码发送太多，请稍后再试");
                    break;
                case "3002":
                    showTipsDialog("3002:短信随机码验证失败");
                    break;
                case "4000":
                    showTipsDialog("4000:验证用户信息失败");
                    break;
                case "4001":
                    showTipsDialog("4001:非实名认证用户，运营商不提供查询。");
                    break;
                case "5000":
                    showTipsDialog("5000:运营商务器网络超时，请稍后再试");
                    break;
                case "5001":
                    showTipsDialog("5001:运营商服务器网络错误，请稍后再试");
                    break;
                case "6000":
                    showTipsDialog("6000：运营商服务器繁忙，请稍后再试");
                    break;
                case "6001":
                    showTipsDialog("6001：运营商服务器错误，请稍后再试");
                    break;
                case "9999":
                    showTipsDialog("9999：未知错误，请稍后再试");
                    break;
                default:
                    break;
            }
        } else if ("done".equals(body.getStatus())) {
            startActivity(new Intent(this, QuotaDatermineActivity.class));
            SharePrefsUtils.getInstance().putString(Config.SP_USER_YYS_STATUS, "1");
            finish();
        }
    }


    @Override
    public void processMessage(Message message) {

    }
}
