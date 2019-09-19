package com.zxy.jisuloan.fragmrnt;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
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
import com.zxy.jisuloan.utils.SharePrefsUtils;
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
public class ResetPwdFragment extends BaseFragment {

    @BindView(R.id.txt_phone)
    TextView tv_phone;
    @BindView(R.id.txt_code)
    TextView tv_code;
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
        button.setText("确定");
        tv_phone.setText("新密码");
        tv_code.setText("确认密码");
        et_phone.setHint("请输入新密码");
        et_code.setHint("请确认密码");
        et_phone.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_code.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //最大输入长度
        et_code.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //最大输入长度
//        et_code.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框

        ll_xieyi.setVisibility(View.INVISIBLE);
        tv_getCode.setVisibility(View.GONE);
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
    }


    @OnClick({R.id.btn_login_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_code:
                nextStep();
                break;

            default:
                break;
        }
    }

    /**
     * 确认修改密码
     */
    private void nextStep() {
        String pwd = et_phone.getText().toString();
        String pwd2 = et_code.getText().toString();
        if ( !AccountValidatorUtils.isPassword(pwd)) {
            new TipsDialog(getContext(), "请输入6~18位字母加数字组合密码").show();
            return;
        }

        if (!pwd.equals(pwd2)) {
            new TipsDialog(getContext(), "两次密码输入不一致，请重新输入").show();
            return;
        }

        String deviceNo = SharePrefsUtils.getInstance().getString(Config.SP_DEVICES_ID, "");
        LoadDialog.show(getContext());
        BaseApi.request(BaseApi.createApi(IService.class).resetPwd(ResetPwdActivity.phone,pwd,
                ResetPwdActivity.code,  deviceNo), new BaseApi.IResponseListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse data) {
                if ("0".equals(data.getError())) {
                    ((Activity) getContext()).finish();
                } else {
                    new TipsDialog(getContext(), data.getMsg()).show();
                }
            }

            @Override
            public void onFail() {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
