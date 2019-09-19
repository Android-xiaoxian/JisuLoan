package com.zxy.jisuloan.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.fragmrnt.ResetPwdFragment;
import com.zxy.jisuloan.fragmrnt.ResetPwdGetCodeFragment;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;

import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/15
 * 重置密码
 */
public class ResetPwdActivity extends BaseActivity  {


    public static String phone = "";
    public static String code = "";
    public static  Activity activity;
    private boolean first_fragment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    private void initView() {
        changeFragment(new ResetPwdGetCodeFragment());
    }

    @OnClick({R.id.img_login_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_login_back:
                onBack();
                break;
            default:
                break;
        }
    }



    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        if (first_fragment) {
            finish();
        } else {
            changeFragment(new ResetPwdGetCodeFragment());
            first_fragment = true;
        }
    }

    /**
     * 切换fragment
     *
     * @param targetFragment
     */
    private void changeFragment(Fragment targetFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_login, targetFragment, "reset_pwd_fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void processMessage(Message message) {
        if (message.what == Config.CHANGE_FRAGMENT) {
            changeFragment(new ResetPwdFragment());
            first_fragment = false;
        }
    }


}
