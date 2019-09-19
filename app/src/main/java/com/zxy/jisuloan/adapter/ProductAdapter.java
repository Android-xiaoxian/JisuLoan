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
import com.zxy.jisuloan.vo.ProductVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/8/26
 */
public class ProductAdapter extends BaseAdapter {

    private List<ProductVo.Products> lists = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private int type = 0;//type不为0的时候只显示两条数据


    public ProductAdapter(Context context, List<ProductVo.Products> lists) {
        if (lists != null) {
            this.lists = lists;
        }
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public ProductAdapter(Context context, List<ProductVo.Products> lists, int type) {
        this(context, lists);
        this.type = type;
    }


    public void upDateAdapter(List<ProductVo.Products> lists) {
        if (this.lists != null) {
            this.lists.clear();
        }
        this.lists.addAll(lists);
        notifyDataSetChanged();
    }
    public void upDateAdapter(List<ProductVo.Products> lists,int type) {
        this.type = type;
        upDateAdapter(lists);
    }

    @Override
    public int getCount() {
        int count;
        if (type == 0) {
            count = lists == null ? 0 : lists.size();
        } else {
            count = lists == null ? 0 : (lists.size() > 2 ? 2 : lists.size());
        }
        return count;
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
            view = inflater.inflate(R.layout.item_tuisong3, null);
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
        ProductVo.Products products = lists.get(i);
        Glide.with(context).load(products.getPlatformimg()).into(viewHolder.img_logo);
        viewHolder.tv_name.setText(products.getLoanplatformname());
//        products.setClickcount("9700001");
        int applyNum = products.getClickcount();
        //Math.ceil 进1  除以10.0保留一位小数（前面要乘相同倍数）
        String applyTxt = Math.ceil((double) applyNum * 10 / 10000) / 10.0 + "万人申请";
        viewHolder.tv_apply.setText(applyTxt);
        viewHolder.tv_fangKuan.setText(products.getAvgloanmoney());

        String rate = products.getInterestrate() + "";
        String[] split = rate.split("\\.");
        if ("0".equals(split[1])) {
            rate = split[0] + "%";
        } else {
            rate += "%";
        }
        viewHolder.tv_liLu.setText(rate);//利率

        String term = products.getBorrowperiodmin() + "~" + products.getBorrowperiodmax();
        String unit = products.getBorrowperiodunit();
        if ("1".equals(unit)) {
            term = term + "天";
        } else if ("2".equals(unit)) {
            term = term + "月";
        } else if ("3".equals(unit)) {
            term = term + "年";
        }
        viewHolder.tv_qiXian.setText(term);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra(Config.PRODUCT, lists.get(i));
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
