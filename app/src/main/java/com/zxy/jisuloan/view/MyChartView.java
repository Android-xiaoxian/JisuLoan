package com.zxy.jisuloan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxy.jisuloan.R;

/**
 * Create by Fang ShiXian
 * on 2019/8/31
 */
public class MyChartView extends RelativeLayout {

    private double[] number;
    private Paint mPaint;
    private Context context;
    private int mWidth; // 控件的宽高
    private int mHeight;
    private int childCount;
    private int grayNumber = 3;
    private int lastVisibility = 3;
    private View[] views;

    private LayoutInflater inflater;

    public MyChartView(Context context) {
        super(context);
    }

    public MyChartView(Context context, double[] numbers) {
        this(context);
        this.context = context;
        this.number = numbers;
        inflater = LayoutInflater.from(context);
        initView();

    }


    public MyChartView(Context context, double[] numbers, int grayNumber) {
        this(context, numbers);
        this.grayNumber = grayNumber;
        lastVisibility = grayNumber;
    }

    private void initView() {
        views = new View[number.length];
        TextView textView;
        View view;
        for (int i = 0; i < number.length * 2; i++) {
            if (i < number.length) {
                if (i < 3) {
                    textView = (TextView) inflater.inflate(R.layout.textview, null);
                } else {
                    textView = (TextView) inflater.inflate(R.layout.textview_002, null);
                }
                String txt = (number[i] + "");
                String[] strings = txt.split("\\.");
                if (strings[1].equals("0")) {
                    txt = strings[0];
                }
                textView.setText(txt);
                addView(textView);
            } else {
                view = new View(context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        dp2px(10),
                        dp2px(10));
                view.setLayoutParams(params);
                view.setBackgroundResource(R.drawable.ring);

                if (i == number.length + grayNumber) {
                    view.setVisibility(VISIBLE);
                } else {
                    view.setVisibility(GONE);
                }
                views[i - number.length] = view;
                addView(view);
            }
        }

    }

    public void setVisibility(int i) {
        if (i != lastVisibility) {
            views[lastVisibility].setVisibility(GONE);
            views[i].setVisibility(VISIBLE);
            lastVisibility = i;
        }
    }

    @Override
    protected void onLayout(boolean b, int l, int i1, int i2, int i3) {
        int cl;//最左边x轴位置
        int ct;//最上边y轴位置
        int cr;//最右边x轴位置
        int cb;//最下边y轴位置
        //为每一个子View指定位置  所需效果：查看mipmap里面的效果图
        for (int i = 0; i < childCount; i++) {
            if (i < 7) {
                TextView childView = (TextView) getChildAt(i);
                //修改view的内容、大小要在childView.layout(cl, ct, cr, cb)之前
                //否则一直报onLayout调用不当
                // requestLayout() improperly called by android.widget.TextView{...} during layout: running second layout pass
//                String txt = (number[i] + "");
//                String[] strings = txt.split("\\.");
//                if (strings[1].equals("0")) {
//                    txt = strings[0];
//                }
//                childView.setText(txt);
                cl = (int) (x[i] - childView.getWidth() / 2);
                ct = (int) (y[i] - childView.getHeight() - dp2px(6));
                cr = cl + childView.getMeasuredWidth();
                cb = ct + childView.getMeasuredHeight();
                childView.layout(cl, ct, cr, cb);

            } else {
                View view = getChildAt(i);
                cl = (int) (x[i - 7] - view.getWidth() / 2);
                ct = (int) (y[i - 7] - view.getHeight() / 2);
                cr = cl + dp2px(10);
                cb = ct + dp2px(10);
                view.layout(cl, ct, cr, cb);
            }
        }
    }

    private double[] x, y;

    private void init() {

        float width = mWidth / 15f;
        double max = getMax(number);
        y = new double[number.length];
        for (int i = 0; i < number.length; i++) {

            if (number[i] > 0) {
                y[i] = mHeight - (number[i] / (max * 1.5f)) * mHeight - 20;
            } else {
                y[i] = mHeight - 20;
            }
        }
        x = new double[]{width * 1.5, width * 3.5, width * 5.5, width * 7.5,
                width * 9.5, width * 11.5, width * 13.5};
        mPaint = new Paint();
        mPaint.setStrokeWidth(dp2px(1));
        mPaint.setColor(Color.parseColor("#999999"));
        mPaint.setAntiAlias(true);
    }


    public int dp2px(float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < number.length; i++) {
            if (i == 3) {
                mPaint.setColor(Color.parseColor("#F8984E"));
            }
            canvas.drawCircle((float) (x[i]), (float) (y[i]), dp2px(3), mPaint);
            if (i < number.length - 1) {
                canvas.drawLine((float) (x[i]), (float) (y[i]), (float) (x[i + 1]), (float) (y[i + 1]), mPaint);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = getWidth();
        mHeight = getHeight();
        init();
        int width = 0;
        int height = 0;
        int mWidthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);//初始化所有子View的宽高
        childCount = getChildCount();
        if (mWidthMeasureMode == MeasureSpec.AT_MOST) {//Wrap_content的情况
            int maxWidth = 0;
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                width = childView.getMeasuredWidth();
                maxWidth = (width > maxWidth) ? width : maxWidth;
            }
            width = maxWidth;
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        int mHeightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        if (mHeightMeasureMode == MeasureSpec.AT_MOST) {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                height += childView.getMeasuredHeight();
            }
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    private double getMax(double[] array) {
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
}
