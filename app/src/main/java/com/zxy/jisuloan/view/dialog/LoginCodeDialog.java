package com.zxy.jisuloan.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.Code;
import com.zxy.jisuloan.utils.ToastUtils;

/**
 * Create by Fang ShiXian
 * on 2019/8/15
 * 获取验证码前输入图形验证码的弹窗
 */
public class LoginCodeDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private ClickListenerInterface clickListenerInterface;

    public LoginCodeDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
        initView();
    }

    private String code;
    private ImageView img_code;
    private EditText et_code;

    private void initView() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_graphical_code, null);
        setContentView(view);
        et_code = view.findViewById(R.id.et_tuxing_code);
        img_code = view.findViewById(R.id.img_showCode);
        img_code.setOnClickListener(this);
        view.findViewById(R.id.tv_codeDialog_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_codeDialog_sure).setOnClickListener(this);
        img_code.setImageBitmap(Code.getInstance().createBitmap());
        code = Code.getInstance().getCode();
    }

    @Override
    public void show() {
        super.show();
        DialogWindowManger.setDialogWindow(context, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_showCode:
                img_code.setImageBitmap(Code.getInstance().createBitmap());
                code = Code.getInstance().getCode();
                break;
            case R.id.tv_codeDialog_cancel:
                dismiss();
                break;
            case R.id.tv_codeDialog_sure:

                if (code.equals(et_code.getText().toString())) {
                    clickListenerInterface.doConfirm();
                } else {
                    img_code.setImageBitmap(Code.getInstance().createBitmap());
                    code = Code.getInstance().getCode();
                    ToastUtils.showToast("请输入正确的验证码");
                }
                break;
            default:
                break;
        }
    }

    public void setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public interface ClickListenerInterface {
        void doConfirm();
    }
}
