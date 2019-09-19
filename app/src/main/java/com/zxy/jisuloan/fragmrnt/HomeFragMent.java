package com.zxy.jisuloan.fragmrnt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sunfusheng.marqueeview.MarqueeView;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.LoginActivity;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseFragment;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.vo.FelictateVO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Create by Fang ShiXian
 * on 2019/8/12
 * “首页”Fragment
 */
public class HomeFragMent extends BaseFragment {

    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;

    private View view;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
            initData();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }


    private void initView() {
//        List<Spanned> strings = new ArrayList<>();
//        SpannableString spannableString = new SpannableString("桂林 葛女士（尾号6952）获得额度45000元");
//        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#e63f3f")), spannableString.length() - 6, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        SpannableString spannableString2 = new SpannableString("北京 赵女士（尾号7382）获得额度9000元");
//        spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#e63f3f")), spannableString2.length() - 5, spannableString2.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        String s = "桂林 葛女士（尾号6952）获得额度<font color=#e63f3f>45000</font>元";
//        String s2 = "北京 赵女士（尾号7382）获得额度<font color=#e63f3f>36000</font>元";
//        Spanned spanned1 = Html.fromHtml(s);
//        Spanned spanned2 = Html.fromHtml(s2);
//        strings.add(spanned1);
//        strings.add(spanned2);
//        marqueeView.startWithList(strings);

    }

    private void initData() {
        BaseApi.request(BaseApi.createApi2(IService.class).getFelicitate(),
                new BaseApi.IResponseListener<List<FelictateVO>>() {
                    @Override
                    public void onSuccess(List<FelictateVO> data) {
                        if (isDestroy) {
                            return;
                        }
                        if (data != null && data.size() != 0) {
                            List<Spanned> spanneds = new ArrayList<>();
                            for (FelictateVO vo : data) {
                                String s = vo.getRegion() + " " + vo.getName() + " （尾号" + vo.getNumber()
                                        + "） 获得额度<font color=#e63f3f>" + vo.getMoney() + "</font>元";
                                Spanned spanned = Html.fromHtml(s);
                                spanneds.add(spanned);
                                if (data.size() == 1) {
                                    spanneds.add(spanned);
                                }

                            }
                            marqueeView.startWithList(spanneds);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }

    @OnClick({R.id.btn_borrow_now})
    public void onClick(View view) {
        boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
        if (!isLogin) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            case R.id.btn_borrow_now:

                break;
            default:
                break;
        }
    }

    private boolean isDestroy;

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        marqueeView.stopFlipping();
    }

    @Override
    public void onResume() {
        super.onResume();
        marqueeView.startFlipping();
    }
}
