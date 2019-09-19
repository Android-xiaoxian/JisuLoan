package com.zxy.jisuloan.viewpager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.LoginActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.SharePrefsUtils;

/**
 * Create by Fang ShiXian
 * on 2019/8/13
 * 信用页面viewpager的适配器
 */
public class CreditPagerAdapter extends PagerAdapter {

    private Context context;
    private int[] imgs;
    private int size;

    public CreditPagerAdapter(Context context, int[] imgs) {
        this.imgs = imgs;
        this.context = context;
        size = imgs.length / 3 + 1;//页数
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.ll_viewpager_credit, null);
        ImageView imageView1 = view.findViewById(R.id.img1);
        ImageView imageView2 = view.findViewById(R.id.img2);
        ImageView imageView3 = view.findViewById(R.id.img3);
        LinearLayout ll_1 = view.findViewById(R.id.ll_1);
        LinearLayout ll_2 = view.findViewById(R.id.ll_2);
        LinearLayout ll_3 = view.findViewById(R.id.ll_3);
        TextView tv_1 = view.findViewById(R.id.text1);
        TextView tv_2 = view.findViewById(R.id.text2);
        TextView tv_3 = view.findViewById(R.id.text3);

        imageView1.setImageResource(imgs[(position % 3) * size]);
        tv_1.setText((position % 3) * size + "");

        if ((position % 3) * size + 1 < imgs.length) {
            imageView2.setImageResource(imgs[(position % 3) * size + 1]);
            tv_2.setText((position % 3) * size + 1 + "");
        }

        if ((position % 3) * size + 2 < imgs.length) {
            imageView3.setImageResource(imgs[(position % 3) * size + 2]);
            tv_3.setText((position % 3) * size + 2 + "");
        }
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
                if (!isLogin) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return;
                }
            }
        });
        ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
                if (!isLogin) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return;
                }
            }
        });
        ll_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
                if (!isLogin) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return;
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
