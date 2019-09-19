package com.zxy.jisuloan.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.view.RoundedProgressBar;

/**
 * Create by Fang ShiXian
 * on 2019/8/20
 */
public class UpdateDialog extends Dialog {

    private String content;
    private Context context;
    private RoundedProgressBar progressBar;

    public UpdateDialog(Context context, String content) {
        super(context, R.style.MyUpdateDialog);
        this.content = content;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        setContentView(view);
        initView();
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }


    private void initView() {
        TextView textView = findViewById(R.id.txt_update_dialog);
        textView.setText(content);
        progressBar = findViewById(R.id.rp_update_dialog);
        progressBar.setMaxProgress(100);
        findViewById(R.id.img_update_dialog_X).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onClickListenerInterface.doCancel();
            }
        });
        Button button = findViewById(R.id.btn_update_dialog);
        LinearLayout linearLayout = findViewById(R.id.ll_update_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                onClickListenerInterface.doUpdate();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        DialogWindowManger.setDialogFullWindow(context, this);
        setCancelable(false);
    }


    private OnClickListenerInterface onClickListenerInterface;

    public void setOnClickListenerInterface(OnClickListenerInterface onClickListenerInterface) {
        this.onClickListenerInterface = onClickListenerInterface;
    }

    public interface OnClickListenerInterface {
        void doCancel();

        void doUpdate();
    }
}
