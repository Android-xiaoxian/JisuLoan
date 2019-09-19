package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.vo.ProductDetailVO;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/27
 */
public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.tv_1)
    TextView tv_1;
    @BindView(R.id.tv_2)
    TextView tv_2;
    @BindView(R.id.tv_3)
    TextView tv_3;
    @BindView(R.id.tv_4)
    TextView tv_4;
    @BindView(R.id.tv_condition)
    TextView tv_condition;
    @BindView(R.id.tv_samples)
    TextView tv_samples;
    @BindView(R.id.tv_instructions)
    TextView tv_instructions;
    @BindView(R.id.tv_company_info)
    TextView tv_company_info;

    private String product_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWhiteStatusBar(this);
        initView();
        initData();
    }

    private void initView() {
        tv_title.setText("产品详情");
        Intent intent = getIntent();
        product_id = intent.getStringExtra(Config.PRODUCT_ID);
    }

    private void initData() {
        BaseApi.request(BaseApi.createApi(IService.class).QueryDetails(product_id),
                new BaseApi.IResponseListener<ProductDetailVO>() {
                    @Override
                    public void onSuccess(ProductDetailVO data) {
                        if (data != null) {
                            ProductDetailVO.Data detail = data.getData();
                            if (detail == null) {
                                return;
                            }
                            String conditions = data.getData().getConditions();
                            String materials = data.getData().getMaterials();
                            String explains = data.getData().getExplains();
                            String organization = data.getData().getOrganization();
                            if (!TextUtils.isEmpty(conditions))
                                tv_condition.setText(conditions);
                            if (!TextUtils.isEmpty(materials))
                                tv_samples.setText(materials);
                            if (!TextUtils.isEmpty(explains))
                                tv_instructions.setText(explains);
                            if (!TextUtils.isEmpty(organization))
                                tv_company_info.setText(organization);
                        }

                    }

                    @Override
                    public void onFail() {

                    }
                });
    }


    @OnClick({R.id.img_title_back})
    public void onClick() {
        finish();
    }


    @Override
    public void processMessage(Message message) {

    }
}
