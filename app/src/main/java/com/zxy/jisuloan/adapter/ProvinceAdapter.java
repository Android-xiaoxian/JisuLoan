package com.zxy.jisuloan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.vo.Cityinfo;

import java.util.List;

/**
 * create by Fang Shixian
 * on 2019/2/14 0014
 */
public class ProvinceAdapter extends BaseAdapter {

    private Context context;
    private List<Cityinfo> list;
    private int selectItem = -1;

    public ProvinceAdapter(Context context, List<Cityinfo> list) {
        this.context = context;
        this.list = list;
    }

    public ProvinceAdapter(Context context, List<Cityinfo> list, int type) {
        this.context = context;
        if (list != null)
            this.list = list.subList(1, list.size());
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelcetItem(int selectItem) {
        this.selectItem = selectItem;
        update(list);
    }

    public void update(List<Cityinfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void update(List<Cityinfo> list, int type) {
        if (list != null)
            this.list = list.subList(1, list.size());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_district, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.district_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(list.get(position).getCity_name());
        if (selectItem == position) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView textView;
    }
}
