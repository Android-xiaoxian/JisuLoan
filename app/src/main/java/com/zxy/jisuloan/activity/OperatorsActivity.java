package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.LoadDialog;
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
 * on 2019/8/22
 */
public class OperatorsActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.asv_user_info)
    ApplyStepView applyStepView;
    @BindView(R.id.name)
    EditText et_name;
    @BindView(R.id.id_card)
    EditText et_id_card;
    @BindView(R.id.phone)
    EditText et_phone;
    @BindView(R.id.password)
    EditText et_password;
    @BindView(R.id.code)
    EditText et_code;
    @BindView(R.id.check)
    CheckedTextView ct_check;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private String carrier = "";
    private String[] carriers = new String[]{"cmcc", "telecom", "unicom"};
    private String tid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this).init();
        initView();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_operators;
    }

    private void initView() {
        tv_title.setText("运营商认证");
        applyStepView.changeStep(3);
        String phone = SharePrefsUtils.getInstance().getString(Config.SP_USER_MOBILEPHONE, "");
        et_phone.setText(phone);
        et_phone.setEnabled(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb1:
                        carrier = carriers[0];
                        break;
                    case R.id.rb2:
                        carrier = carriers[1];
                        break;
                    case R.id.rb3:
                        carrier = carriers[2];
                        break;
                    default:
                        carrier = "";
                        break;
                }
            }
        });
    }

    @OnClick({R.id.img_title_back, R.id.check, R.id.btn_auth_next, R.id.txt_kefu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.check:
                if (ct_check.isChecked()) {
                    ct_check.setChecked(false);
                    radioGroup.setVisibility(View.GONE);
                    radioGroup.clearCheck();
                } else {
                    ct_check.setChecked(true);
                    radioGroup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_auth_next:
                doOperatorsAuth();
                break;
            case R.id.txt_kefu:
                startActivity(new Intent(this, HelpCenterActivity.class));
                break;
            default:
                break;
        }
    }

    private void doOperatorsAuth() {
        String name = et_name.getText().toString();
        String IDCardNo = et_id_card.getText().toString();
        String phone = et_phone.getText().toString();
        String password = et_password.getText().toString();
        if (name.length() == 0 || IDCardNo.length() == 0 || password.length() == 0) {
            showTipsDialog("请完善资料");
            return;
        }
        String userId = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");
        LoadDialog.show(this);
        BaseApi.request(BaseApi.createApi(IService.class).startYYSAuth(userId, phone, carrier, password,
                name, IDCardNo), new BaseApi.IResponseListener<YYSResuleVO>() {
            @Override
            public void onSuccess(YYSResuleVO data) {
                if ("0".equals(data.getError())) {
                    if (data.getResult().getCode() == null) {
                        LoadDialog.show(OperatorsActivity.this);
                        tid = data.getResult().getTid();
                        //延时3秒后开始第一次查询
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                startPolling();
                            }
                        }, 3000);
                    } else {
                        showTipsDialog(data.getResult().getError());
                    }
                } else {
                    showTipsDialog(data.getMsg());
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
                                    startPolling();
                                }
                            }, 3000);
                        } else {
                            LoadDialog.dimiss();
                            checkResult(body);
                        }
                    } else {
                        LoadDialog.dimiss();
                        showTipsDialog(body.getError());
                    }
                } else {
                    LoadDialog.dimiss();
                    showTipsDialog(data.getMsg());
                }
            }

            @Override
            public void onFail() {
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
                case "SMSJilinTelecom":
                case "loginSMS":
                case "SMSAgain":
                case "newSMS":
                case "loginSMSAgain":
                case "newLoginSMS":
                    startNewActivity(body.getNeed());
                    break;
                case "nameAndID":
                    showTipsDialog("姓名或身份证有误，请重新输入");
                    break;
                case "password":
                    showTipsDialog("服务密码误，请重新输入");
                    break;
                case "passwordInOne":
                    showTipsDialog("密码错误， 还有一次重试机会");
                    break;
                case "passwordInTwo":
                    showTipsDialog("密码错误，还有两次重试机会");
                    break;
                case "passwordInThree":
                    showTipsDialog("密码错误， 还有三次重试机会");
                    break;
                case "passwordInFour":
                    showTipsDialog("密码错误，还有四次重试机会");
                    break;
                case "passwordInFive":
                    showTipsDialog("密码错误，还有五次重试机会");
                    break;
                case "passwordTooSimple":
                    showTipsDialog("密码太简单，请重试。");
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
            }
        } else if ("done".equals(body.getStatus())) {
            startActivity(new Intent(this, QuotaDatermineActivity.class));
            SharePrefsUtils.getInstance().putString(Config.SP_USER_YYS_STATUS, "1");
            finish();
        }
    }

    private void startNewActivity(String need) {
        Intent intent = new Intent(OperatorsActivity.this, OperatorsSMSActivity.class);
        intent.putExtra("tid", tid);
        intent.putExtra("need", need);
        startActivity(intent);
        finish();
    }

    @Override
    public void processMessage(Message message) {

    }

}
