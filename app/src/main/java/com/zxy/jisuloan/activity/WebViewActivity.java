package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.LoadDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.webView)
    WebView home_webview;

    private String url = "http://www.lvzbao.com/androidHtml/gsdcjwt_app.html";
    private WebSettings webSettings;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_problem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();
    }

    private void initView() {
        LoadDialog.show(this);
        Intent intent = getIntent();
        url = intent.getStringExtra(Config.WEB_URL);
        tv_title.setText(intent.getStringExtra(Config.WEB_TITLE));

        //重写长按事件，避免用户复制内容
        home_webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webSettings = home_webview.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        home_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        home_webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
                if (progress == 100) {
                    LoadDialog.dimiss();
                }
                super.onProgressChanged(view, progress);
            }
        });

        home_webview.loadUrl(url);
    }

    @OnClick({R.id.img_title_back})
    public void onClick(View view) {
        finish();
    }


    @Override
    public void processMessage(Message message) {
        // TODO Auto-generated method stub

    }

}
