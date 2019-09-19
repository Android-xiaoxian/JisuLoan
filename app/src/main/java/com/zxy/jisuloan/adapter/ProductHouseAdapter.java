package com.zxy.jisuloan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.ProductActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.vo.ProductHouseVO;

import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/8/26
 */
public class ProductHouseAdapter extends BaseAdapter {

    private List<ProductHouseVO> lists;
    private Context context;
    private int type = -1;
    private LayoutInflater inflater;


    public ProductHouseAdapter(Context context, List<ProductHouseVO> lists) {
        this.lists = lists;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public ProductHouseAdapter(Context context, List<ProductHouseVO> lists, int type) {
        this.lists = lists;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.type = type;
    }

    public void upDateAdapter(List<ProductHouseVO> lists) {
        if (this.lists != null) {
            this.lists.clear();
        }
        this.lists = lists;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder viewHolder;
        if (view == null) {
            viewHolder = new MyViewHolder();
//            if (type != -1){
                view = inflater.inflate(R.layout.item_tuisong3, null);
//            }else {
//                view = inflater.inflate(R.layout.item_tuisong2, null);
//            }
            viewHolder.img_logo = view.findViewById(R.id.img_logo);
            viewHolder.tv_name = view.findViewById(R.id.txt_product_name);
            viewHolder.tv_apply = view.findViewById(R.id.tv_apply_1);
            viewHolder.tv_fangKuan = view.findViewById(R.id.txt_fangKuan);
            viewHolder.tv_liLu = view.findViewById(R.id.txt_liLu);
            viewHolder.tv_qiXian = view.findViewById(R.id.txt_qiXian);
            viewHolder.btn_oneKey_apply = view.findViewById(R.id.btn_oneKey_apply);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) view.getTag();
        }
        ProductHouseVO products = lists.get(i);
        Glide.with(context).load(products.getIconpath()).into(viewHolder.img_logo);
        viewHolder.tv_name.setText(products.getProductname());
//        viewHolder.tv_apply.setText(products.getClickcount() + "万人申请");
//        viewHolder.tv_fangKuan.setText(products.getAvgloanmoney());
//        viewHolder.tv_liLu.setText(products.getInterestrate() + "%");
//        viewHolder.tv_qiXian.setText(products.getBorrowperiod() + "月");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra(Config.PRODUCT,lists.get(i));
                context.startActivity(intent);
            }
        });
        return view;
    }

    class MyViewHolder {
        ImageView img_logo;
        TextView tv_name;
        TextView tv_apply;
        TextView tv_fangKuan;
        TextView tv_liLu;
        TextView tv_qiXian;
        TextView btn_oneKey_apply;
    }
}
