package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.view.dialog.PermissionDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/27
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.switch1)
    Switch switch1;
    @BindView(R.id.txt_open)
    TextView tv_open;
    @BindView(R.id.txt_close)
    TextView tv_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }


    private void initView() {
        tv_title.setText("设置");
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tv_open.setVisibility(View.INVISIBLE);
                    tv_close.setVisibility(View.VISIBLE);
                    MyLog.e("test", "打开了");
                } else {
                    tv_close.setVisibility(View.INVISIBLE);
                    tv_open.setVisibility(View.VISIBLE);
                    MyLog.e("test", "关了");

                }
            }
        });
    }


    @OnClick({R.id.img_title_back, R.id.btn_LogOut, R.id.rl_changePassword, R.id.rl_helpCenter,
            R.id.rl_aboutUs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.rl_changePassword:
                startActivity(new Intent(this, ResetPwdActivity.class));
                break;
            case R.id.rl_helpCenter:
                startActivity(new Intent(this, HelpCenterActivity.class));
                break;
            case R.id.rl_aboutUs:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.btn_LogOut:
                PermissionDialog dialog = new PermissionDialog(this);
                dialog.show();
                dialog.setTitle("温馨提示");
                dialog.setContent("确定退出当前账号？");
                dialog.setBtnText("取消", "确定");
                dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        dialog.dismiss();
                        logOut();
                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        finish();
                    }
                });
                break;
        }
    }

    public static void logOut(){
        SharePrefsUtils.getInstance().remove(Config.SP_USER_CREATE_TIME);
        SharePrefsUtils.getInstance().remove(Config.SP_USER_ENABLE);
        SharePrefsUtils.getInstance().remove(Config.SP_USER_ID);
        SharePrefsUtils.getInstance().remove(Config.SP_USER_MOBILEPHONE);
        SharePrefsUtils.getInstance().remove(Config.SP_USER_NAME);
        SharePrefsUtils.getInstance().remove(Config.SP_REAL_NAME_STATUS);
        SharePrefsUtils.getInstance().remove(Config.SP_USER_INFO_STATUS);
        SharePrefsUtils.getInstance().remove(Config.SP_USER_CONT_STATUS);
        SharePrefsUtils.getInstance().remove(Config.SP_USER_YYS_STATUS);
        SharePrefsUtils.getInstance().remove(Config.SP_IS_LOGIN);
        SharePrefsUtils.getInstance().remove(Config.SP_LAST_LOGIN_TIME);
    }

    @Override
    public void processMessage(Message message) {

    }
}
