package com.zxy.jisuloan.viewpager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/8/14
 */
public class GuidePagerListener implements ViewPager.OnPageChangeListener {
    private Context context;
    private LinearLayout linearLayout;
    private int size;
    private Button button;
    private List<ImageView> dotView;
    private int curPostion = 0;

    public GuidePagerListener(Context context, LinearLayout linearLayout, int size, Button button) {
        this.context = context;
        this.linearLayout = linearLayout;
        this.size = size;
        this.button = button;

        initData();
    }

    /**
     * 初始化小圆点
     */
    private void initData() {
        dotView = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            params.leftMargin = ViewUtil.dp2px(4f);
            params.rightMargin = ViewUtil.dp2px(4f);
            params.width = ViewUtil.dp2px(10);
            params.height = ViewUtil.dp2px(10);
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

    @Override
    public void onPageSelected(int position) {
        dotView.get(position).setBackgroundResource(R.drawable.circular_001);
        dotView.get(curPostion).setBackgroundResource(R.drawable.circular_002);
        curPostion = position;
        if (position != 2) {
            linearLayout.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
        }else {
            linearLayout.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
