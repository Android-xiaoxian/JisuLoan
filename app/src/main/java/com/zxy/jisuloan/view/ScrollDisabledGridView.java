package com.zxy.jisuloan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Create by Fang ShiXian
 * on 2019/8/27
 */
public class ScrollDisabledGridView extends GridView {

    private int mPosition;

    public ScrollDisabledGridView(Context context) {
        super(context);
    }

    public ScrollDisabledGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollDisabledGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            // 记录手指按下时的位置
            mPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            return super.dispatchTouchEvent(ev);
        }

        if (actionMasked == MotionEvent.ACTION_MOVE) {
            // 最关键的地方，忽略MOVE 事件
            // ListView onTouch获取不到MOVE事件所以不会发生滚动处理
            return true;
        }

        // 手指抬起时
        if (actionMasked == MotionEvent.ACTION_UP
                || actionMasked == MotionEvent.ACTION_CANCEL) {
            // 手指按下与抬起都在同一个视图内，交给父控件处理，这是一个点击事件
            if (pointToPosition((int) ev.getX(), (int) ev.getY()) == mPosition) {
                super.dispatchTouchEvent(ev);
            } else {
                // 如果手指已经移出按下时的Item，说明是滚动行为，清理Item pressed状态
                setPressed(false);
                invalidate();
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }




    /**
     * 重新计算listview的高度
     *
     * @param adapter
     */
    public static void getListViewHeight(BaseAdapter adapter ,ListView listView) {
        //重新计算listview的高度
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}