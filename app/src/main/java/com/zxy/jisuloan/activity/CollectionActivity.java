package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.adapter.ProductAdapter;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.view.ListViewForScrollView;

import com.zxy.jisuloan.vo.ProductVo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/30
 * <p>
 * 收藏
 */
public class CollectionActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;
    @BindView(R.id.tv_no_data)
    TextView tv_noData;
    @BindView(R.id.rl_recommend)
    RelativeLayout rl_recommend;
    @BindView(R.id.lv_tuisong2)
    ListViewForScrollView listView;

    private String userId;
    private ProductAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        userId = Config.getUserId();
        tv_title.setText("收藏");
        tv_noData.setText("您还没有收藏哦");
        ll_no_data.setVisibility(View.VISIBLE);
        rl_recommend.setVisibility(View.VISIBLE);

        adapter = new ProductAdapter(this, null);
        listView.setAdapter(adapter);
    }

    private void initData() {
        BaseApi.request(BaseApi.createApi(IService.class).getCollection(userId),
                new BaseApi.IResponseListener<List<ProductVo.Products>>() {
            @Override
            public void onSuccess(List<ProductVo.Products> data) {
                if (data != null && data.size() != 0) {
                    MyLog.e("test","data.size() = " +data.size());
                    adapter.upDateAdapter(data,0);
//                    ScrollDisabledListView.getListViewHeight(adapter,listView);
                    ll_no_data.setVisibility(View.GONE);
                    rl_recommend.setVisibility(View.GONE);
                } else {
                    initRecommendData();
                }
            }

            @Override
            public void onFail() {
                initRecommendData();
            }
        });
    }

    private List<ProductVo.Products> products;
    private void initRecommendData() {

        BaseApi.request(BaseApi.createApi(IService.class).getProducts( "clickCount", "desc"),
                new BaseApi.IResponseListener<ProductVo>() {
                    @Override
                    public void onSuccess(ProductVo data) {
                        if ("0".equals(data.getError())) {
                            products = data.getProductList();
                            adapter.upDateAdapter(products,1);
//                            ScrollDisabledListView.getListViewHeight(adapter, listView);
                            ll_no_data.setVisibility(View.VISIBLE);
                            rl_recommend.setVisibility(View.VISIBLE);
                        } else {
                            ToastUtils.showToast(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });

    }

    @OnClick({R.id.img_title_back,R.id.tv_more_loan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.tv_more_loan:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Config.CHANGE_FRAGMENT_BORROW, Config.CHANGE_TO_BORROW);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_no_data:
                initData();
                break;
            default:
                break;
        }
    }

    @Override
    public void processMessage(Message message) {

    }
}
