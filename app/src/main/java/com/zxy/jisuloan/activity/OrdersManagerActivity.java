package com.zxy.jisuloan.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.adapter.ProductAdapter;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.utils.ViewUtil;
import com.zxy.jisuloan.view.ListViewForScrollView;
import com.zxy.jisuloan.vo.ProductVo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/9/3
 * 订单管理
 */
public class OrdersManagerActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.listView)
    ListView listView;//订单数据列表

    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.include)
    LinearLayout include;
    @BindView(R.id.lv_tuisong2)
    ListViewForScrollView listView2;//无订单时显示的推荐数据

    private String type = "全部账单";
    private ProductAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();
    }

    private void initView() {

        tv_title.setText("订单管理");
        tv_no_data.setText("您还没有订单哦");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton1) {
                    type = "待提现";
                } else if (i == R.id.radioButton2) {
                    type = "待还款";
                } else if (i == R.id.radioButton3) {
                    type = "可复借";
                } else {
                    type = "全部账单";
                }
                MyLog.e("test", type);
            }
        });
        View view = LayoutInflater.from(this).inflate(R.layout.include_bottom, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewUtil.dp2px(45));
        view.setLayoutParams(params);
        listView.addFooterView(view);
//        adapter = new ProductAdapter(this,null);
//        listView.setAdapter(adapter);

        adapter = new ProductAdapter(this, null,1);
        listView2.setAdapter(adapter);
        radioGroup.setVisibility(View.GONE);
        ll_no_data.setVisibility(View.VISIBLE);
        include.setVisibility(View.GONE);
        initData();
    }


    @OnClick({R.id.img_title_back, R.id.ll_no_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.ll_no_data:
                initData();
                break;
            default:
                break;
        }
    }

    private void initData() {
        initRecommendData();
    }


    private List<ProductVo.Products> products;

    private void initRecommendData() {

        BaseApi.request(BaseApi.createApi(IService.class).getProducts( "clickCount", "desc"),
                new BaseApi.IResponseListener<ProductVo>() {
                    @Override
                    public void onSuccess(ProductVo data) {
                        if ("0".equals(data.getError())) {
                            products = data.getProductList();
                            adapter.upDateAdapter(products);
//                            ScrollDisabledListView.getListViewHeight(adapter, listView);
                            ll_no_data.setVisibility(View.VISIBLE);
                            include.setVisibility(View.VISIBLE);
                            listView2.setVisibility(View.VISIBLE);
                        } else {
                            ToastUtils.showToast(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });

    }

    @Override
    public void processMessage(Message message) {

    }
}
