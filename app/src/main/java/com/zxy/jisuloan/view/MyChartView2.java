package com.zxy.jisuloan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.ViewUtil;

/**
 * Create by Fang ShiXian
 * on 2019/9/10
 */
public class MyChartView2 extends LinearLayout {

    private Context context;
    private float[] floats;
    private String[] strings;
    private int[] colors = new int[]{0xFFF9CC7E, 0xFF7DD177, 0xFFFFBA38, 0xFFF6766C, 0xFF60B9E7};
    private int[] resouse = new int[]{R.drawable.oval_004, R.drawable.oval_005, R.drawable.oval_006,
            R.drawable.oval_007, R.drawable.oval_008};
    private LayoutInflater inflater;
    private Paint mPaint;
    private Canvas canvas;
    private int mWidth, mHeight;
    private LinearLayout linearLayout0, linearLayout;
    private MyChartView3 myChartView3;

    //代码动态生成调用这个方法
    public MyChartView2(Context context, float[] floats, String[] strings) {
        super(context);
        this.floats = floats;
        this.strings = strings;
        this.context = context;
        inflater = LayoutInflater.from(context);
        initView();
    }

    //在布局中直接用MyChartView2调用这个方法
    public MyChartView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);
        initView();
    }


    private LayoutParams params;

    private void initView() {

        linearLayout0 = new LinearLayout(context);
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        linearLayout0.setLayoutParams(params);
        addView(linearLayout0);

        linearLayout = new LinearLayout(context);

        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setGravity(Gravity.BOTTOM);

        MyScrollView scrollView = new MyScrollView(context);
        LayoutParams params2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params2.weight = 1;
        scrollView.setLayoutParams(params2);
        scrollView.setFillViewport(true);
        scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
        scrollView.setScrollBarSize(0);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setPadding(ViewUtil.dp2px(30), ViewUtil.dp2px(30), 0, ViewUtil.dp2px(30));

        scrollView.addView(linearLayout);

        addView(scrollView);
        showDate();
    }


    public void updateView(float[] floats, String[] strings) {
        this.floats = floats;
        this.strings = strings;
        showDate();
    }


    private void showDate() {
        linearLayout0.removeAllViews();
        myChartView3 = new MyChartView3(context, floats);
        myChartView3.setLayoutParams(params);
        linearLayout0.addView(myChartView3);
        linearLayout.removeAllViews();
        if (strings != null) {
            float[] proportion = getProportion(floats, 100);
            LayoutParams params3 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params3.topMargin = ViewUtil.dp2px(6);
            if (proportion.length != 0) {
                for (int i = 0; i < proportion.length; i++) {
                    LinearLayout childView = (LinearLayout) inflater.inflate(R.layout.item_repay, null);
                    childView.setLayoutParams(params3);
                    if (i == proportion.length - 1 && i % colors.length == 0) {
                        childView.getChildAt(0).setBackgroundResource(resouse[1]);
                    } else {
                        childView.getChildAt(0).setBackgroundResource(resouse[i % resouse.length]);
                    }
                    String txt = (proportion[i] + "");
                    String[] split = txt.split("\\.");
                    if (split[1].equals("0")) {
                        txt = split[0];
                    }
                    String s = strings[i] + " " + txt + "%";
                    ((TextView) childView.getChildAt(1)).setText(s);
                    linearLayout.addView(childView);
                }
            } else {
                LinearLayout childView = (LinearLayout) inflater.inflate(R.layout.item_repay, null);
                childView.setLayoutParams(params3);
                childView.getChildAt(0).setBackgroundResource(R.drawable.oval_009);
                ((TextView) childView.getChildAt(1)).setText("无应还贷款");
                linearLayout.addView(childView);
            }
        }
    }


    /**
     * 传入double数组返回各个 元素/元素和 的比列
     * floats元素全为0会出错
     *
     * @param floats
     * @param count
     * @return
     */
    private float[] getProportion(float[] floats, int count) {
        float floatsCount = 0;
        for (float d : floats) {
            floatsCount += d;
        }

        if (floatsCount == 0) {//floats元素全为0
            return new float[0];
        }

        float[] proportion = new float[floats.length];
        float proportionCount = 0;
        for (int i = 0; i < floats.length; i++) {
            float cur = (floats[i]) * count / floatsCount;
            proportion[i] = Math.round(cur * 100) / 100.0f;
            proportionCount += proportion[i];
        }
        return proportion;
    }


}
