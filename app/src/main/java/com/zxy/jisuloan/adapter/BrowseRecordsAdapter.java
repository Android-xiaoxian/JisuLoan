package com.zxy.jisuloan.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.ProductActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.vo.BrowseRecordsVO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by Fang ShiXian
 * on 2019/8/29
 */
public class BrowseRecordsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<BrowseRecordsVO.BrowseRecord> list;
    private static final int EMPTY_VIEW = 1;
    private static final int DATE_VIEW = 2;
    private static final int NOMAL_VIEW = 3;

    public BrowseRecordsAdapter(Context context, List<BrowseRecordsVO.BrowseRecord> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    public void upDateAdapter(List<BrowseRecordsVO.BrowseRecord> list) {
        if (this.list != null) {
            this.list.clear();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            return null;
        } else if (viewType == DATE_VIEW) {
            return new MyViewHolder(inflater.inflate(R.layout.item_test_1, parent, false));
        } else {
            return new MyViewHolder2(inflater.inflate(R.layout.item_test_2, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).textView.setText(list.get(position).getCreateDate());
        } else if (holder instanceof MyViewHolder2) {
            Glide.with(context).load(list.get(position).getPlatformimg()).into(((MyViewHolder2) holder).img_test);
            ((MyViewHolder2) holder).textView.setText(list.get(position).getLoanplatformname());
//            if ("0".equals(list.get(position).getOrderStatus())) {

                String str = "平均放款金额<font color=#E63F3F>"
                        + list.get(position).getAvgloanmoney()
                        + "</font>";
                ((MyViewHolder2) holder).textView2.setText(Html.fromHtml(str));
                ((MyViewHolder2) holder).btn_test.setText("一键申请");
                ((MyViewHolder2) holder).btn_test.setTextColor(context.getResources().getColor(R.color.white));
                ((MyViewHolder2) holder).btn_test.setBackgroundResource(R.drawable.shape_004);
//            } else {
//                ((MyViewHolder2) holder).textView2.setText("已申请");
//                ((MyViewHolder2) holder).btn_test.setText("我要评价");
//                ((MyViewHolder2) holder).btn_test.setTextColor(context.getResources().getColor(R.color.mainColor));
//                ((MyViewHolder2) holder).btn_test.setBackgroundResource(R.drawable.shape_015);
//            }
            //分割线是否显示
            if (position == list.size() - 1 || getItemViewType(position + 1) == DATE_VIEW) {
                ((MyViewHolder2) holder).view.setVisibility(View.INVISIBLE);
            } else {
                ((MyViewHolder2) holder).view.setVisibility(View.VISIBLE);
            }
        }
    }

    public void deleteAdapter() {
        list = null;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {


        if (list.size() == 0) {
            return EMPTY_VIEW;
        } else {

            if (list.get(position).getItemType().equals(Config.ITEM_TYPE1)) {
                return DATE_VIEW;

            } else if (list.get(position).getItemType().equals(Config.ITEM_TYPE2)) {
                return NOMAL_VIEW;
            }
        }

        return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_test1)
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MyViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.img_test)
        ImageView img_test;
        @BindView(R.id.tv_test2)
        TextView textView;
        @BindView(R.id.tv_test3)
        TextView textView2;
        @BindView(R.id.btn_test)
        Button btn_test;
        @BindView(R.id.view_line)
        View view;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btn_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btn_test.getText().toString().equals("一键申请")) {
                        Intent intent = new Intent(context, ProductActivity.class);
                        intent.putExtra(Config.PRODUCT, list.get(getLayoutPosition()));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

}
