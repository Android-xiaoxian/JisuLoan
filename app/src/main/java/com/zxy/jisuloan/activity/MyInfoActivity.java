package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
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
import com.zxy.jisuloan.view.SportProgressView;
import com.zxy.jisuloan.view.dialog.TipsDialog;
import com.zxy.jisuloan.vo.UserStatusVO;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/16
 * <p>
 * 我的资料
 */
public class MyInfoActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.sportProgressView)
    SportProgressView sportProgressView;
    @BindView(R.id.txt_time_assess)
    TextView tv_time_assess;
    @BindView(R.id.img_myInfo_realName)
    ImageView img_realName;
    @BindView(R.id.img_myInfo_userInfo)
    ImageView img_userInfo;
    @BindView(R.id.img_myInfo_cont)
    ImageView img_cont;
    @BindView(R.id.img_myInfo_yys)
    ImageView img_yys;
    @BindView(R.id.img_myInfo_gjj)
    ImageView img_gjj;
    @BindView(R.id.img_myInfo_taobao)
    ImageView img_taobao;


    private boolean hasDate = false;
    private boolean isFromFirstActivity = false;
    private String status1, status2, status3, status4, status5, status6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this).init();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        hasDate = false;
        initDate();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_info;
    }

    private void initView() {
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        if ("FirstActivity".equals(from)) {
            isFromFirstActivity = true;
        }
        tv_title.setText("我的资料");
        sportProgressView.setProgress(888);
    }

    private void initDate() {
        LoadDialog.show(this);
        String userId = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");
        BaseApi.request(BaseApi.createApi(IService.class).getUserAuthStatus(userId),
                new BaseApi.IResponseListener<UserStatusVO>() {
                    @Override
                    public void onSuccess(UserStatusVO data) {
                        if ("0".equals(data.getError())) {
                            hasDate = true;
                            status1 = data.getRealInfoStatus();
                            status2 = data.getUserInfoStatus();
                            status3 = data.getContactInfoStatus();
                            status4 = data.getOperatorInfoStatus();
                            showData();
                            String cardNum = data.getCardNum();
                            if (cardNum != null) {
                                cardNum = cardNum.substring(0, 3) + "***********" + cardNum.substring(cardNum.length() - 4);
                            }
                            Config.putIDCardNo(cardNum);
                            Config.putCareer(data.getCareer());
                            Config.putRealName(data.getName());
                        } else {
                            new TipsDialog(MyInfoActivity.this, data.getMsg()).show();
                        }
                    }

                    @Override
                    public void onFail() {
                        MyLog.e("test", "加载失败");
                    }
                });
    }

    private void showData() {
        if (status1 != null) {
            SharePrefsUtils.getInstance().putString(Config.SP_REAL_NAME_STATUS, status1);
        }
        if (status2 != null) {
            SharePrefsUtils.getInstance().putString(Config.SP_USER_INFO_STATUS, status2);
        }
        if (status3 != null) {
            SharePrefsUtils.getInstance().putString(Config.SP_USER_CONT_STATUS, status3);
        }
        if (status4 != null) {
            SharePrefsUtils.getInstance().putString(Config.SP_USER_YYS_STATUS, status4);
        } else {
            SharePrefsUtils.getInstance().putString(Config.SP_USER_YYS_STATUS, "0");
        }
        if ("1".equals(status1)) {
            img_realName.setImageResource(R.mipmap.icon_jb_sm_yrz);
        }
        if ("1".equals(status2)) {
            img_userInfo.setImageResource(R.mipmap.icon_jb_gr_yrz);
        }
        if ("1".equals(status3)) {
            img_cont.setImageResource(R.mipmap.icon_jb_lxr_yrz);
        }
        if ("1".equals(status4)) {
            img_yys.setImageResource(R.mipmap.icon_jb_yys_yrz);
        }
        if ("1".equals(status5)) {
            img_gjj.setImageResource(R.mipmap.icon_rz_gjj_yrz);
        }
        if ("1".equals(status6)) {
            img_taobao.setImageResource(R.mipmap.icon_rz_tb_yrz);
        }

    }

    @OnClick({R.id.sportProgressView, R.id.img_title_back, R.id.ll_myInfo_realName, R.id.ll_myInfo_userInfo,
            R.id.ll_myInfo_cont, R.id.ll_myInfo_yys, R.id.ll_myInfo_gjj, R.id.ll_myInfo_taobao})
    public void onClick(View view) {
        if (!hasDate && view.getId() != R.id.img_title_back) {
            initDate();
            return;
        }
        switch (view.getId()) {
            case R.id.img_title_back:
                doBackPressed();
                break;
            case R.id.ll_myInfo_realName:
                if (!"1".equals(status1)) {
                    Intent intent = new Intent(this, IDCardAuthActivity.class);
                    intent.putExtra("status2", status2);
                    intent.putExtra("status3", status3);
                    intent.putExtra("status4", status4);
                    startActivity(intent);
                }
                break;
            case R.id.ll_myInfo_userInfo:

                if ("1".equals(status1) && !"1".equals(status2)) {
                    Intent intent2 = new Intent(this, UserInfoActivity.class);
                    intent2.putExtra("status3", status3);
                    intent2.putExtra("status4", status4);
                    startActivity(intent2);
                } else if (!"1".equals(status1)) {
                    showTipsDialog("请先实名认证");
                }
                break;
            case R.id.ll_myInfo_cont:

                if ("1".equals(status1) && "1".equals(status2)
                        && !"1".equals(status3)) {
                    Intent intent3 = new Intent(this, ContactsActivity.class);
                    intent3.putExtra("status4", status4);
                    startActivity(intent3);
                } else if (!"1".equals(status2)) {
                    showTipsDialog("请先认证个人信息");
                }
                break;
            case R.id.ll_myInfo_yys:
//                startActivity(new Intent(this, OperatorsActivity.class));
                if ("1".equals(status1) && "1".equals(status2) && "1".equals(status3)
                        && !"1".equals(status4)) {
                    startActivity(new Intent(this, OperatorsActivity.class));
                } else if (!"1".equals(status3)) {
                    showTipsDialog("请先认证联系人");
                }
                break;
//            case R.id.ll_myInfo_gjj:
//                startActivity(new Intent(this, QuotaDatermineActivity.class));
//                break;
//            case R.id.sportProgressView:
//                int a = new Random().nextInt(1000);
//                MyLog.e("test", "点击了信用  a = " + a);
//                sportProgressView.setProgress(a);
//                break;
            default:
                break;
        }
    }

    public void doBackPressed() {
        if (isFromFirstActivity) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("isLogin", true);
            startActivity(intent);
        }
        finish();
    }


    @Override
    public void processMessage(Message message) {

    }

    @Override
    public void onBackPressed() {
        doBackPressed();
    }
}
