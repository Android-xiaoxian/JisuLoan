package com.zxy.jisuloan.fragmrnt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.LoginActivity;
import com.zxy.jisuloan.adapter.ProductAdapter;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.LoadDialog;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.view.ListViewForScrollView;
import com.zxy.jisuloan.view.ObservableScrollView;
import com.zxy.jisuloan.vo.ProductVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Create by Fang ShiXian
 * on 2019/8/12
 * “借钱”Fragment
 */
public class BorrowFragMent extends Fragment {

    @BindView(R.id.tv_borrow_title)
    TextView tv_tltle;
    @BindView(R.id.sv_borrow)
    ObservableScrollView scrollView;
    @BindView(R.id.rg_borrow)
    RadioGroup rg_borrow;
    @BindView(R.id.rg_borrow2)
    RadioGroup rg_borrow2;
    @BindView(R.id.rb1)
    RadioButton radioButton1;
    @BindView(R.id.rb2)
    RadioButton radioButton2;
    @BindView(R.id.rb3)
    RadioButton radioButton3;
    @BindView(R.id.rb12)
    RadioButton radioButton12;
    @BindView(R.id.rb22)
    RadioButton radioButton22;
    @BindView(R.id.rb32)
    RadioButton radioButton32;
    @BindView(R.id.tv_tips1)
    TextView tv_tip1;
    @BindView(R.id.tv_tips2)
    TextView tv_tip2;
    @BindView(R.id.lv_borrow)
    ListViewForScrollView listView;
    @BindView(R.id.ll_borrow)
    LinearLayout ll_hover;

    private View view;
    private Unbinder unbinder;
    private ProductAdapter adapter;
    private List<ProductVo.Products> list = new ArrayList<>();//智能排序
    private List<ProductVo.Products> list2 = new ArrayList<>();//高通过率
    private List<ProductVo.Products> list3 = new ArrayList<>();//低利率
    private int curPage = 1;
    private int numPerPage = 100;
    private String condition = "clickCount";//clickCount（智能排序） highPassRate（高通过率）interestRate（低利率）
    private static final String condition1 = "clickCount";
    private static final String condition2 = "highPassRate";
    private static final String condition3 = "interestRate";
    private String sort = "desc";//desc(降序 高-->低) asc(低-->高)
    private boolean getAllData = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_borrow, container, false);
//            view = inflater.inflate(R.layout.activity_product, null);
            unbinder = ButterKnife.bind(this, view);
            initView();
            initData();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }


    private void initView() {
        String tips = "申请<font color=#E63F3F>5个以上</font>产品，可大幅度提高<font color=#E63F3F>成功率至95%</font>";
        tv_tip1.setText(Html.fromHtml(tips));
        tv_tip2.setText(Html.fromHtml(tips));
        radioButton1.setChecked(true);
        radioButton12.setChecked(true);

        adapter = new ProductAdapter(getContext(), null);
        listView.setAdapter(adapter);

        rg_borrow.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb1:
                        condition = condition1;
                        radioButton12.setChecked(true);
                        break;
                    case R.id.rb2:
                        condition = condition2;
                        radioButton22.setChecked(true);
                        break;
                    case R.id.rb3:
                        condition = condition3;
                        radioButton32.setChecked(true);
                        break;
                    case R.id.rb4:
                        break;
                    default:
                        break;
                }
                initData();
            }
        });

        rg_borrow2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb12:
                        radioButton1.setChecked(true);
                        break;
                    case R.id.rb22:
                        radioButton2.setChecked(true);
                        break;
                    case R.id.rb32:
                        radioButton3.setChecked(true);
                        break;
                    case R.id.rb4:
                        break;
                    default:
                        break;
                }
            }
        });
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                int[] location = new int[2];
                rg_borrow.getLocationOnScreen(location);
                int xPosition = location[0];
                int yPosition = location[1];
                int title_height = tv_tltle.getHeight();
                if (yPosition <= title_height) {
                    ll_hover.setVisibility(View.VISIBLE);
                } else {
                    ll_hover.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        if (list.size() != 0 && condition.equals(condition1)) {//只能排序
            Collections.sort(list, new ProductVo.MyComparator2());
            adapter.upDateAdapter(list);
            return;
        } else if (list.size() != 0 && condition.equals(condition2)) {//高通过率排序
            Collections.sort(list, new ProductVo.MyComparator3());
            adapter.upDateAdapter(list);
            return;
        } else if (list.size() != 0 && condition.equals(condition3)) {//低利率排序
            Collections.sort(list, new ProductVo.MyComparator());
            adapter.upDateAdapter(list);
            return;
        }

        LoadDialog.show(getContext());
        BaseApi.request(BaseApi.createApi(IService.class).getProducts(condition, sort),
                new BaseApi.IResponseListener<ProductVo>() {
                    @Override
                    public void onSuccess(ProductVo data) {
                        if (isDestroy) {
                            MyLog.e("test", "网络加载完成，借款页面已经不存在");
                            return;
                        }
                        if ("0".equals(data.getError())) {
                            list.addAll(data.getProductList());
                            adapter.upDateAdapter(list);
                        } else {
                            ToastUtils.showToast(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }


    @OnClick({})
    public void onClick(View view) {
        boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
        if (!isLogin) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            default:
                break;
        }
    }

    private boolean isDestroy = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        unbinder.unbind();
    }
}
