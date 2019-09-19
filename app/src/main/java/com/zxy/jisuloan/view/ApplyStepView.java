package com.zxy.jisuloan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zxy.jisuloan.R;


/**
 * create by Fang Shixian
 * on 2019/1/24 0024
 */
public class ApplyStepView extends LinearLayout {


    private ImageView img_1, pass_img2, pass_img3;
    private ImageView[] imageViews;

    public ApplyStepView(Context context, int step) {
        super(context, null);
    }

    public ApplyStepView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_applystep_view, this);
        img_1 = findViewById(R.id.pass_img);
        pass_img2 = findViewById(R.id.pass_img2);
        pass_img3 = findViewById(R.id.pass_img3);
        imageViews = new ImageView[]{img_1, pass_img2, pass_img3};
    }

    public void changeStep(int step) {

        for (int i = 0; i < step; i++) {
            imageViews[i].setVisibility(VISIBLE);
        }
    }

}
