package com.zxy.jisuloan.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zxy.jisuloan.R;

/**
 * Create by Fang ShiXian
 * on 2019/8/14
 * 申请权限时提示弹窗
 */
public class PermissionDialog extends Dialog {

    private Context context;
        private View view;
    private TextView tv_title, tv_content, tv_cancel, tv_confirm;

    public PermissionDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_permission, null);
        setContentView(view);
        initView();
    }

    /**
     * 要在show之后调用
     * @param title
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }
    public void setTitle(int title) {
        tv_title.setText(title);
    }

    /**
     * 要在show之后调用
     * @param content
     */
    public void setContent(String content) {
        tv_content.setText(content);
    }

    /**
     * 要在show之后调用
     * @param cancel
     * @param confirm
     */
    public void setBtnText(String cancel, String confirm) {
        tv_cancel.setText(cancel);
        tv_confirm.setText(confirm);
    }


    private void initView() {
        tv_title = view.findViewById(R.id.tv_dialog_permission_title);
        tv_content = view.findViewById(R.id.txt_permission_content);
        tv_cancel = view.findViewById(R.id.btn_dialog_permission_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tv_confirm = view.findViewById(R.id.btn_dialog_permission_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListenerInterface.doConfirm();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        //设置dialog弹窗大小
        DialogWindowManger.setDialogWindow(context, this);
        setCancelable(false);
    }


    private OnClickListenerInterface onClickListenerInterface;

    public void setOnClickListenerInterface(OnClickListenerInterface onClickListenerInterface) {
        this.onClickListenerInterface = onClickListenerInterface;
    }

    public interface OnClickListenerInterface {
        void doConfirm();
    }
}
