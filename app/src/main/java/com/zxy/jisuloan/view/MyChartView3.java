package com.zxy.jisuloan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.ViewUtil;

/**
 * Create by Fang ShiXian
 * on 2019/9/10
 */
public class MyChartView3 extends View {

    private int[] colors = new int[]{0xFFF9CC7E, 0xFF7DD177, 0xFFFFBA38, 0xFFF6766C, 0xFF60B9E7};

    private float[] floats;
    private Paint mPaint, txtPaint;
    private Context context;
    private float moneyCount;

    public MyChartView3(Context context, float[] floats) {
        super(context);
        this.floats = floats;
        this.context = context;
        mPaint = new Paint();
        mPaint.setStrokeWidth(ViewUtil.dp2px(25));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        txtPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 这是一个居中的圆
         */
        int width = getWidth();
        int height = getHeight();

        int x = (width - height / 2) / 2 - 20;
        int y = height / 4 - 20;

        RectF oval = new RectF(x, y, width - x, height - y);

        float[] angles = getProportion(floats, 360);
        int cur = -90;
        int anglesCount = 0;
        if (angles.length != 0) {
            for (int i = 0; i < angles.length; i++) {
                if (i == angles.length - 1 && i % colors.length == 0) {
                    mPaint.setColor(colors[1]);
                } else {
                    mPaint.setColor(colors[i % colors.length]);
                }
                if (i == angles.length - 1) {
                    canvas.drawArc(oval, (float) cur, anglesCount - 360 - angles.length, false, mPaint);
                } else {
                    canvas.drawArc(oval, (float) cur, -angles[i], false, mPaint);
                    cur -= angles[i] - 1;// -1 把每次绘制圆环的缝连上
                    anglesCount += angles[i];
                }
            }
        } else {
            mPaint.setColor(context.getResources().getColor(R.color.lineColor));
            canvas.drawArc(oval, 0, 360 - angles.length, false, mPaint);
        }

        int dp12 = ViewUtil.dp2px(12);
        txtPaint.setTextSize(dp12);
        txtPaint.setColor(context.getResources().getColor(R.color.text_99));
        String text = "总金额";
        float textWidth = txtPaint.measureText(text);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 - dp12 / 2 - 10, txtPaint);

        int dp16 = ViewUtil.dp2px(16);
        txtPaint.setTextSize(dp16);
        txtPaint.setColor(0xFFF8984E);
        String text2 = moneyCount + "";
        String[] strings = text2.split("\\.");
        if (strings[1].equals("0")) {
            text2 = strings[0];
        }
        float textWidth2 = txtPaint.measureText(text2);
        canvas.drawText(text2, width / 2 - textWidth2 / 2, height / 2 + dp16 / 2 + ViewUtil.dp2px(10), txtPaint);
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
        moneyCount = floatsCount;
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
