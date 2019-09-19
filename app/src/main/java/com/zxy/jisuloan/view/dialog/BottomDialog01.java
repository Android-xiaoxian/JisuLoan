package com.zxy.jisuloan.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zxy.jisuloan.R;


/**
 * 底部弹出的，带有选项的dialog
 * create by Fang Shixian
 * on 2019/8/17  0015
 */
public class BottomDialog01 extends Dialog {

    private String[] list;
    private ListView listView;
    private Context context;
    private String title;

    public BottomDialog01(@NonNull Context context, String[] list, String title) {
        // 在构造方法里, 传入主题
        super(context, R.style.BottomDialogStyle);

        this.list = list;
        this.context = context;
        this.title = title;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_01);
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.tv_dialog_title)).setText(title);
        listView = findViewById(R.id.lv_bottom_dialog01);
        listView.setAdapter(new ListViewAdapter());

        findViewById(R.id.img_dialog_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                }
        );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onClick(list[i],i);
                dismiss();
            }
        });
    }


    @Override
    public void show() {
        super.show();
        // 拿到Dialog的Window, 修改Window的属性
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        // 一定要重新设置, 才能生效
        window.setAttributes(layoutParams);

    }

    public class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int i) {
            return list[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bottom_dialog01, null);
            TextView textView = view.findViewById(R.id.tv_dialog_item1);
            textView.setText(list[i]);
            return view;
        }
    }

    private ListViewClickListener listener;

    public void setListener(ListViewClickListener listener) {
        this.listener = listener;
    }

    public interface ListViewClickListener {
        void onClick(String choice,int i);
    }
}
