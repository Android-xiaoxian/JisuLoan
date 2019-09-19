package com.zxy.jisuloan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.vo.ProductVo;

import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/8/30
 */
public class PushAdapter extends BaseAdapter {


    private Context context;
    private List<ProductVo.Products> list;
    private LayoutInflater inflater;

    public PushAdapter(Context context, List<ProductVo.Products> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void upDateAdapter(List<ProductVo.Products> lists) {
        if (this.list != null) {
            this.list.clear();
        }
        this.list = lists;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_tuisong1,null);
        ImageView imageView = view.findViewById(R.id.img_product_logo);
        TextView tv_name = view.findViewById(R.id.tv_product_name);
        TextView tv_quota = view.findViewById(R.id.tv_quota);
        TextView tv_status = view.findViewById(R.id.tv_status);
        Button button = view.findViewById(R.id.btn_check_detail);

        Glide.with(context).load(list.get(i).getPlatformimg()).into(imageView);
        tv_name.setText(list.get(i).getLoanplatformname());
//        tv_quota.setText(list.get(i).getAvgloanmoney());
        tv_status.setText(list.get(i).getStatus());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyLog.e("test","点击了");
            }
        });
        return view;
    }
}
