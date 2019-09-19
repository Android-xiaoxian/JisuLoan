package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.adapter.ProductAdapter;
import com.zxy.jisuloan.adapter.PushAdapter;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.view.ListViewForScrollView;
import com.zxy.jisuloan.vo.ProductVo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/24
 */
public class PushActivity extends BaseActivity {

    @BindView(R.id.fl_1)
    FrameLayout fl_1;
    @BindView(R.id.fl_2)
    FrameLayout fl_2;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    @BindView(R.id.tv_product_name)
    TextView tv_product_name;
    @BindView(R.id.tv_quota)
    TextView tv_quota;
    @BindView(R.id.tv_term)
    TextView tv_term;
    @BindView(R.id.lv_tuisong1)
    ListView lv_1;
    @BindView(R.id.lv_tuisong2)
    ListViewForScrollView lv_2;


    private List<ProductVo.Products> list;
    private ProductAdapter adapter;
    private PushAdapter adapter0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tuisong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar(this);
        initView();
        initData();
    }


    private void initView() {
//        fl_1.setVisibility(View.GONE);
//        fl_2.setVisibility(View.VISIBLE);
        adapter0 = new PushAdapter(this, null);
        lv_1.setAdapter(adapter0);
        adapter = new ProductAdapter(this, null, 1);
        lv_2.setAdapter(adapter);
    }

    private void initData() {

//        LoadDialog.show(getContext());
        BaseApi.request(BaseApi.createApi(IService.class).getProducts("clickCount", "desc"),
                new BaseApi.IResponseListener<ProductVo>() {
                    @Override
                    public void onSuccess(ProductVo data) {
                        if ("0".equals(data.getError())) {
                            list = data.getProductList();
                            adapter.upDateAdapter(list);
                            adapter0.upDateAdapter(list);
//                            ScrollDisabledListView.getListViewHeight(adapter, lv_2);
                        } else {
                            ToastUtils.showToast(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });

    }


    @OnClick({R.id.tv_more_loan, R.id.tv_apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_more_loan:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Config.CHANGE_FRAGMENT_BORROW, Config.CHANGE_TO_BORROW);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_apply:
                ToastUtils.showToast("点击了");
                break;
            default:
                break;
        }
    }


    @Override
    public void processMessage(Message message) {

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
