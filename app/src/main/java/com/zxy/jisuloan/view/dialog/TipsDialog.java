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
 * 提示弹窗
 */
public class TipsDialog extends Dialog {

    private String content;
    private Context context;

    public TipsDialog(Context context, String content) {
        super(context, R.style.MyDialog);
        this.content = content;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_tips, null);
        setContentView(view);
//        setContentView(R.layout.dialog_tips);
        initView();
    }


    private void initView() {
        ((TextView) findViewById(R.id.tv_dialog_tips)).setText(content);
        findViewById(R.id.btn_dialog_tips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        //设置dialog弹窗大小
        DialogWindowManger.setDialogWindow(context,this);
    }
}
