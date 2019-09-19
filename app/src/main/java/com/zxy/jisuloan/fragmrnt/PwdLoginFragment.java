package com.zxy.jisuloan.fragmrnt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.MainActivity;
import com.zxy.jisuloan.activity.MyInfoActivity;
import com.zxy.jisuloan.activity.ResetPwdActivity;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.AccountValidatorUtils;
import com.zxy.jisuloan.utils.BaseFragment;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.LoadDialog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.view.dialog.ResetPwdDialog;
import com.zxy.jisuloan.view.dialog.TipsDialog;
import com.zxy.jisuloan.vo.CodeLoginVO;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Create by Fang ShiXian
 * on 2019/8/14
 * 密码登录fragment
 */
public class PwdLoginFragment extends BaseFragment {

    @BindView(R.id.et_login_phone2)
    EditText et_phone;
    @BindView(R.id.et_login_pwd2)
    EditText et_pwd;
    @BindView(R.id.btn_login_pwd)
    Button button;

    private View view;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_login_pwd, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    private void initView() {
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0 && et_pwd.getText().length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0
                        && et_phone.getText().length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
    }


    @OnClick({R.id.txt_forget_pwd, R.id.btn_login_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_forget_pwd:
                startActivity(new Intent(getContext(), ResetPwdActivity.class));
                break;
            case R.id.btn_login_pwd:
                doLoginByPwd();
                break;
            default:
                break;
        }
    }

    private void doLoginByPwd() {
        String phone = et_phone.getText().toString();
        String pwd = et_pwd.getText().toString();
        if (!AccountValidatorUtils.isMobile(phone)) {
            new TipsDialog(getContext(), "请输入正确的手机号码吗").show();
        }
        String deviceId = SharePrefsUtils.getInstance().getString(Config.SP_DEVICES_ID, "");
        LoadDialog.show(getContext());
        BaseApi.request(BaseApi.createApi(IService.class).loginByPwd(phone, pwd, deviceId), new BaseApi.IResponseListener<CodeLoginVO>() {
            @Override
            public void onSuccess(CodeLoginVO data) {
                if ("0".equals(data.getError())) {
                    loginSuccess(getContext(), data);

                } else if ("4".equals(data.getError())) {
                    new TipsDialog(getContext(),"设备号不一致，无法登录").show();
                } else {
                    toResetPwd();
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    /**
     * 登录成功后做的处理
     *
     * @param context
     * @param data
     */
    public static void loginSuccess(Context context, CodeLoginVO data) {
        CodeLoginVO.SdUser userData = data.getSdUser();
        SharePrefsUtils.getInstance().putString(Config.SP_USER_CREATE_TIME, userData.getCreateTime());
        SharePrefsUtils.getInstance().putString(Config.SP_USER_ENABLE, userData.getEnable());
        SharePrefsUtils.getInstance().putString(Config.SP_USER_ID, userData.getId());
        SharePrefsUtils.getInstance().putString(Config.SP_USER_MOBILEPHONE, userData.getMobilePhone());
        SharePrefsUtils.getInstance().putString(Config.SP_USER_NAME, userData.getUserName());
        SharePrefsUtils.getInstance().putString(Config.SP_REAL_NAME_STATUS, userData.getRealInfoStatus());
        SharePrefsUtils.getInstance().putString(Config.SP_USER_INFO_STATUS, userData.getUserInfoStatus());
        SharePrefsUtils.getInstance().putString(Config.SP_USER_CONT_STATUS, userData.getContactInfoStatus());
        SharePrefsUtils.getInstance().putString(Config.SP_USER_YYS_STATUS, userData.getOperatorInfoStatus());
        SharePrefsUtils.getInstance().putBoolean(Config.SP_IS_LOGIN, true);
        SharePrefsUtils.getInstance().putLong(Config.SP_LAST_LOGIN_TIME, System.currentTimeMillis());
        String realInfoStatus = userData.getRealInfoStatus();
        String userInfoStatus = userData.getUserInfoStatus();
        String contactInfoStatus = userData.getContactInfoStatus();
        String yysStatus = userData.getOperatorInfoStatus();
        if (
//                !"1".equals(realInfoStatus) ||
                         !"1".equals(userInfoStatus) || !"1".equals(contactInfoStatus)
//                        || !"1".equals(yysStatus)
        ) {
            Intent intent = new Intent(context, MyInfoActivity.class);
            intent.putExtra("from","FirstActivity");
            context.startActivity(intent);
        } else {
            context.startActivity(new Intent(context, MainActivity.class));
        }
        ((Activity) context).finish();
    }


    /**
     * 密码错误等情况弹出dialog提示去重置密码
     */
    private void toResetPwd() {
        ResetPwdDialog dialog = new ResetPwdDialog(getContext());
        dialog.setCancelable(false);
        dialog.show();
        dialog.setOnClickListenerInterface(() -> {
            dialog.dismiss();
            startActivity(new Intent(getContext(), ResetPwdActivity.class));
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
