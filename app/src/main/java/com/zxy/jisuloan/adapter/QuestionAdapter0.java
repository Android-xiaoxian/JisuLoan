package com.zxy.jisuloan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.vo.QuestionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/9/9
 */
public class QuestionAdapter0 extends BaseAdapter {

    private Context context;
    private List<QuestionVO.Data> list = new ArrayList<>();
    private LayoutInflater inflater;

    public QuestionAdapter0(Context context, List<QuestionVO.Data> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void upDateAdapter(List<QuestionVO.Data> list) {
        this.list = list;
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
        view = inflater.inflate(R.layout.item_question_hot, null);
        TextView textView = view.findViewById(R.id.tv_wt);
        textView.setText(list.get(i).getProblem());
        return view;
    }
}
