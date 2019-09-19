package com.zxy.jisuloan.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.zxy.jisuloan.R;

/**
 * Create by Fang ShiXian
 * on 2019/8/16
 */
public class ResetPwdDialog extends Dialog implements View.OnClickListener {

    private Context context;

    public ResetPwdDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.dialog_reset_pwd, null);
        setContentView(view);
        view.findViewById(R.id.txt_resetPwd_cancel).setOnClickListener(this);
        view.findViewById(R.id.txt_resetPwd_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_resetPwd_cancel:
                dismiss();
                break;
            case R.id.txt_resetPwd_sure:
                onClickListenerInterface.doConfirm();
                break;
            default:
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        DialogWindowManger.setDialogWindow(context, this);
    }

    private OnClickListenerInterface onClickListenerInterface;

    public void setOnClickListenerInterface(OnClickListenerInterface onClickListenerInterface) {
        this.onClickListenerInterface = onClickListenerInterface;
    }

    public interface OnClickListenerInterface {
        void doConfirm();
    }
}
