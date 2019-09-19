package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.fragmrnt.CodeLoginFragment;
import com.zxy.jisuloan.fragmrnt.PwdLoginFragment;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.MyLog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/14
 */
public class LoginActivity extends BaseActivity  {

    @BindView(R.id.img_login_back)
    ImageView back;
    @BindView(R.id.txt_login_code)
    TextView tv_code;
    @BindView(R.id.txt_login_pwd)
    TextView tv_pwd;
    @BindView(R.id.fl_login)
    FrameLayout frameLayout;

    private TextView[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        textViews = new TextView[]{tv_code, tv_pwd};
        changeFragment(new CodeLoginFragment());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.img_login_back, R.id.txt_login_code, R.id.txt_login_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_login_back:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.txt_login_code:
                selectLoginMode(0);
                break;
            case R.id.txt_login_pwd:
                selectLoginMode(1);
                break;
            default:
                break;
        }
    }

    private int curMode = 0;

    private void selectLoginMode(int i) {
        MyLog.e("test", "curMode == " + curMode + "  i == " + i);
        if (i == curMode) {
            return;
        }
        textViews[i].setTextAppearance(this, R.style.login_text1);
        textViews[i].setBackgroundResource(R.drawable.line_underline);
        textViews[curMode].setTextAppearance(this, R.style.login_text2);
        textViews[curMode].setBackgroundResource(R.drawable.shape_tran);
        if (i == 0) {
            changeFragment(new CodeLoginFragment());
        } else {
            changeFragment(new PwdLoginFragment());
        }
        curMode = i;
    }

    private void changeFragment(Fragment targetFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_login, targetFragment, "login_fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void processMessage(Message message) {

    }

}
