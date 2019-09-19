package com.zxy.jisuloan.fragmrnt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.LoginActivity;
import com.zxy.jisuloan.utils.BaseFragment;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.view.SportProgressView;
import com.zxy.jisuloan.viewpager.CreditPagerAdapter;
import com.zxy.jisuloan.viewpager.CreditViewPagerListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Create by Fang ShiXian
 * on 2019/8/12
 * “信用”Fragment
 */
public class CreditFragment extends BaseFragment {

    @BindView(R.id.sportProgressView)
    SportProgressView sportProgressView;
    @BindView(R.id.txt_credit)
    TextView txt_credit;
    @BindView(R.id.txt_vip_open)
    TextView txt_vip_open;
    @BindView(R.id.credit_include1)
    LinearLayout credit_include1;
    @BindView(R.id.credit_include2)
    LinearLayout credit_include2;
    @BindView(R.id.credit_include3)
    LinearLayout credit_include3;
    @BindView(R.id.viewPage_credit)
    ViewPager viewPager;
    @BindView(R.id.ll_point_group)
    LinearLayout ll_point_group;

    private View view;
    private Unbinder unbinder;
    private int[] imgs = new int[]{R.mipmap.tab_icon_sy_xz,
            R.mipmap.tab_icon_jq_xz,
            R.mipmap.tab_icon_xy_xz,
            R.mipmap.tab_icon_wd_xz,
            R.mipmap.tab_icon_sy_wxz,
            R.mipmap.tab_icon_jq_wxz,
            R.mipmap.tab_icon_xy_wxz,
            R.mipmap.tab_icon_wd_wxz};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_credit, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        return view;
    }

    private void initView() {
        sportProgressView.setProgress(888);
        initData();

        String str = "会员免费测，立省<font color='#F8984E'>218</font>元，立即开通";
        txt_vip_open.setText(Html.fromHtml(str));

        ((TextView) credit_include2.findViewById(R.id.text_1)).setText("便携工具");
        ((TextView) credit_include2.findViewById(R.id.text_2)).setText("产品PK");
        ((TextView) credit_include2.findViewById(R.id.text_3)).setText("淘宝消费评测");
        ((TextView) credit_include2.findViewById(R.id.text_4)).setText("公积金分析");
        ((TextView) credit_include2.findViewById(R.id.text_5)).setText("贷款计算器");
        ((TextView) credit_include3.findViewById(R.id.text_1)).setText("便利生活");
        ((TextView) credit_include3.findViewById(R.id.text_2)).setText("手机充值");
        ((TextView) credit_include3.findViewById(R.id.text_3)).setText("赚钱");
        ((TextView) credit_include3.findViewById(R.id.text_4)).setText("信用卡");
        ((TextView) credit_include3.findViewById(R.id.text_5)).setText("金币商城");

        ((ImageView) credit_include2.findViewById(R.id.img_1)).setImageResource(R.mipmap.icon_bj_cp);
        ((ImageView) credit_include2.findViewById(R.id.img_2)).setImageResource(R.mipmap.icon_bj_tb);
        ((ImageView) credit_include2.findViewById(R.id.img_3)).setImageResource(R.mipmap.icon_bj_gjj);
        ((ImageView) credit_include2.findViewById(R.id.img_4)).setImageResource(R.mipmap.icon_bj_dk);
        ((ImageView) credit_include3.findViewById(R.id.img_1)).setImageResource(R.mipmap.icon_bl_sj);
        ((ImageView) credit_include3.findViewById(R.id.img_2)).setImageResource(R.mipmap.icon_bl_zq);
        ((ImageView) credit_include3.findViewById(R.id.img_3)).setImageResource(R.mipmap.icon_bl_jb);
        ((ImageView) credit_include3.findViewById(R.id.img_4)).setImageResource(R.mipmap.icon_bl_jb_2);
        credit_include2.findViewById(R.id.ll_1).setVisibility(View.GONE);
        credit_include3.findViewById(R.id.ll_1).setVisibility(View.GONE);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = Config.getIsLogin();
                if (!isLogin) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                switch (view.getId()) {
                    case R.id.ll_2:
                        MyLog.e("test", "这是黑名单");
                        break;
                    case R.id.ll_3:
                        MyLog.e("test", "这是通话风险");
                        break;
                    case R.id.ll_4:
                        MyLog.e("test", "多头借贷");

                        break;
                    case R.id.ll_5:
                        MyLog.e("test", "联系人风险");

                        break;
                }
            }
        };
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
                if (!isLogin) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                switch (view.getId()) {
                    case R.id.ll_2:
                        MyLog.e("test", "产品PK");
                        break;
                    case R.id.ll_3:
                        MyLog.e("test", "淘宝");

                        break;
                    case R.id.ll_4:
                        MyLog.e("test", "公积金");

                        break;
                    case R.id.ll_5:
                        MyLog.e("test", "计算器");
                        break;
                }
            }
        };
        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin = Config.getIsLogin();
                if (!isLogin) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                switch (view.getId()) {
                    case R.id.ll_2:
                        MyLog.e("test", "手机充值");
                        break;
                    case R.id.ll_3:
                        MyLog.e("test", "赚钱");

                        break;
                    case R.id.ll_4:
                        MyLog.e("test", "信用卡");

                        break;
                    case R.id.ll_5:
                        MyLog.e("test", "金币");

                        break;
                }
            }
        };
        credit_include1.findViewById(R.id.ll_2).setOnClickListener(listener);
        credit_include1.findViewById(R.id.ll_3).setOnClickListener(listener);
        credit_include1.findViewById(R.id.ll_4).setOnClickListener(listener);
        credit_include1.findViewById(R.id.ll_5).setOnClickListener(listener);
        credit_include2.findViewById(R.id.ll_2).setOnClickListener(listener2);
        credit_include2.findViewById(R.id.ll_3).setOnClickListener(listener2);
        credit_include2.findViewById(R.id.ll_4).setOnClickListener(listener2);
        credit_include2.findViewById(R.id.ll_5).setOnClickListener(listener2);
        credit_include3.findViewById(R.id.ll_2).setOnClickListener(listener3);
        credit_include3.findViewById(R.id.ll_3).setOnClickListener(listener3);
        credit_include3.findViewById(R.id.ll_4).setOnClickListener(listener3);
        credit_include3.findViewById(R.id.ll_5).setOnClickListener(listener3);
    }

    private int size;//viewPager的页数
    private CreditPagerAdapter adapter;


    //加载底部viewpager内容
    private void initData() {
        if (imgs != null) {
            size = (imgs.length - 1) / 3 + 1;//页数
        }
        if (imgs.length > 3) {
            CreditViewPagerListener listener = new CreditViewPagerListener(getContext(),
                    ll_point_group, size);
            ll_point_group.setVisibility(View.VISIBLE);
            viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener) listener);
        }
        adapter = new CreditPagerAdapter(getContext(), imgs);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(size * 100000);
    }

    @OnClick({R.id.sportProgressView,})
    public void OnClick2(View view) {
        boolean isLogin = Config.getIsLogin();
        if (!isLogin) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        switch (view.getId()) {
//            case R.id.sportProgressView:
//                int a = new Random().nextInt(1000);
//                MyLog.e("test", "点击了信用  a = " + a);
//                sportProgressView.setProgress(a);
//                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
