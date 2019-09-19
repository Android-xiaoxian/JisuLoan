package com.zxy.jisuloan.viewpager;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/8/13
 */
public class CreditViewPagerListener implements ViewPager.OnPageChangeListener {

    private Context context;
    private LinearLayout linearLayout;
    private int size;
    private List<ImageView> dotView;

    public CreditViewPagerListener(Context context, LinearLayout linearLayout, int size) {
        this.context = context;
        this.linearLayout = linearLayout;
        this.size = size;
        initData();
    }

    //初始化小圆点
    private void initData() {
        dotView = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            params.leftMargin = ViewUtil.dp2px(5);
            params.rightMargin = ViewUtil.dp2px(5);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.circular_001);
            } else {
                imageView.setBackgroundResource(R.drawable.circular_002);
            }
            linearLayout.addView(imageView, params);
            dotView.add(imageView);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    int current = 0;//当前选中的引导小圆点
    int currentPosition;//当前选择页

    @Override
    public void onPageSelected(int position) {
        if (position % 3 == current) {
            return;
        }
        dotView.get(position % 3).setBackgroundResource(R.drawable.circular_001);
        dotView.get(current).setBackgroundResource(R.drawable.circular_002);
        current = position % 3;
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
