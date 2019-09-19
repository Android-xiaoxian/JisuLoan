package com.zxy.jisuloan.fragmrnt;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.ResetPwdActivity;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.AccountValidatorUtils;
import com.zxy.jisuloan.utils.BaseFragment;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.LoadDialog;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.TimeCount;
import com.zxy.jisuloan.view.dialog.LoginCodeDialog;
import com.zxy.jisuloan.view.dialog.TipsDialog;
import com.zxy.jisuloan.vo.BaseResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Create by Fang ShiXian
 * on 2019/8/16
 */
public class ResetPwdGetCodeFragment extends BaseFragment {

    @BindView(R.id.et_login_phone)
    EditText et_phone;
    @BindView(R.id.et_login_code)
    EditText et_code;
    @BindView(R.id.txt_login_getCode)
    TextView tv_getCode;
    @BindView(R.id.ll_xieyi)
    LinearLayout ll_xieyi;
    @BindView(R.id.btn_login_code)
    Button button;

    private View view;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_login_code, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    private void initView() {
        button.setText("下一步");
        ll_xieyi.setVisibility(View.INVISIBLE);
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0 && et_code.getText().length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && et_phone.getText().length() > 0) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
        et_code.setText(ResetPwdActivity.code);
        et_phone.setText(ResetPwdActivity.phone);
    }

    @OnClick({R.id.txt_login_getCode, R.id.btn_login_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_login_getCode:
                getCode();
                break;
            case R.id.btn_login_code:
                nextStep();
                break;

            default:
                break;
        }
    }

    private void nextStep() {
        if (!AccountValidatorUtils.checkPhone(et_phone)) {
            new TipsDialog(getContext(), "请输入正确的手机号").show();
            return;
        }
        ResetPwdActivity.code = et_code.getText().toString();
        ResetPwdActivity.phone = et_phone.getText().toString();
        Message message = Message.obtain();
        message.what = Config.CHANGE_FRAGMENT;
        ResetPwdActivity.sendMsg(message);
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        if (!AccountValidatorUtils.checkPhone(et_phone)) {
            new TipsDialog(getContext(), "请输入正确的手机号").show();
            return;
        }
        LoginCodeDialog dialog = new LoginCodeDialog(getContext());
        dialog.setClickListenerInterface(() -> {//  jvm1.8以后可以用() ->代替new回调接口
            MyLog.e("test", "验证码正确");
            dialog.dismiss();
            //开始获取验证码
            LoadDialog.show(getContext());
            String deviceNo = SharePrefsUtils.getInstance().getString(Config.SP_DEVICES_ID, "");
            BaseApi.request(BaseApi.createApi(IService.class).getLoginCode(et_phone.getText().toString(), 1, deviceNo), new BaseApi.IResponseListener<BaseResponse>() {
                @Override
                public void onSuccess(BaseResponse data) {
                    if ("0".equals(data.getError())) {
                        new TimeCount(60000, 1000, tv_getCode, "获取验证码").start();
                    } else {
                        new TipsDialog(getContext(), data.getMsg()).show();
                    }
                }

                @Override
                public void onFail() {

                }
            });

        });
        dialog.setCancelable(false);
        dialog.show();

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
