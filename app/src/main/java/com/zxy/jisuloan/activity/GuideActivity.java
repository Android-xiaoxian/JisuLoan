package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.StatusBarUtils;
import com.zxy.jisuloan.viewpager.GuidePagerListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/14
 */
public class GuideActivity extends BaseActivity {

    @BindView(R.id.vp_guide)
    ViewPager viewPager;
    @BindView(R.id.ll_point_group)
    LinearLayout linearLayout;
    @BindView(R.id.btn_start)
    Button button;

    private ArrayList<View> views;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this).init();

        initView();

    }

    private void initView() {
        views = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.layout_guide1,null);
        View view2 = inflater.inflate(R.layout.layout_guide2,null);
        View view3 = inflater.inflate(R.layout.layout_guide3,null);
        views.add(view1);
        views.add(view2);
        views.add(view3);
        GuidePagerListener listener = new GuidePagerListener(this,
                linearLayout, views.size(), button);
        viewPager.addOnPageChangeListener(listener);
        viewPager.setAdapter(new GuideAdapter(views));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @OnClick({R.id.btn_start})
    public void onClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void processMessage(Message message) {

    }

    class GuideAdapter extends PagerAdapter {
        private ArrayList<View> views;

        public GuideAdapter(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
