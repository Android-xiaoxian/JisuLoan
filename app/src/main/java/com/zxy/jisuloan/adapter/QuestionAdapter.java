package com.zxy.jisuloan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.vo.QuestionClassVO;

import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/9/6
 */
public class QuestionAdapter extends BaseAdapter {

    private List<QuestionClassVO.Data> list;
    private Context context;
    private LayoutInflater inflater;

    public QuestionAdapter(Context context, List<QuestionClassVO.Data> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void upDateAdapter(List<QuestionClassVO.Data> list1) {
        if (list != null) {
            list.clear();
        }
        list = list1;
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
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_gridview, null);
            holder.imageView = view.findViewById(R.id.img_icon);
            holder.textView = view.findViewById(R.id.tv_question);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(list.get(i).getClassname());
        if (list.get(i).getIcon() != null) {
            Glide.with(context).load(list.get(i).getIcon()).into(holder.imageView);
        }
        return view;
    }


    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
