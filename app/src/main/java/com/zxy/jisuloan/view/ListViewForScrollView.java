package com.zxy.jisuloan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Create by Fang ShiXian
 * on 2019/9/18
 * 重新测量listView的高度，可在scrollview里面使用，
 * 可复用viewHolder
 */
public class ListViewForScrollView extends ListView {
    public ListViewForScrollView(Context context) {
        super(context);
    }
    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListViewForScrollView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
