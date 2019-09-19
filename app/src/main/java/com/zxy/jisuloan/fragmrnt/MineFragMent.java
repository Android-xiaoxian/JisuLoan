package com.zxy.jisuloan.fragmrnt;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.activity.BrowseRecordsActivity;
import com.zxy.jisuloan.activity.CollectionActivity;
import com.zxy.jisuloan.activity.LoginActivity;
import com.zxy.jisuloan.activity.MsgCenterActivity;
import com.zxy.jisuloan.activity.MyInfoActivity;
import com.zxy.jisuloan.activity.OrdersManagerActivity;
import com.zxy.jisuloan.activity.ProductActivity;
import com.zxy.jisuloan.activity.RepayManageActivity;
import com.zxy.jisuloan.activity.SettingActivity;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseFragment;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.vo.ProductVo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Create by Fang ShiXian
 * on 2019/8/12
 * “我的”Fragment
 */
public class MineFragMent extends BaseFragment {

    @BindView(R.id.img_logo)
    ImageView img_logo;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_quota)
    TextView tv_quota;
    @BindView(R.id.tv_term)
    TextView tv_term;


    private View view;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);
//            view = inflater.inflate(R.layout.item_tuisong2, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
            initData();
        } else {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return view;
    }

    private void initView() {
        boolean isLogin = Config.getIsLogin();
        if (isLogin) {
            String name = Config.getRealName();
            if (!TextUtils.isEmpty(name)) {
                tv_login.setText(name);
            } else {
                String phone = Config.getPhone();
                tv_login.setText(phone);
            }
        }
    }

    ProductVo.Products products;

    private void initData() {
        BaseApi.request(BaseApi.createApi(IService.class).getProducts("clickCount", "desc"),
                new BaseApi.IResponseListener<ProductVo>() {
                    @Override
                    public void onSuccess(ProductVo data) {
                        if (isDestroy) {
                            return;
                        }
                        if ("0".equals(data.getError())) {
                            products = data.getProductList().get(0);
                            Glide.with(getContext()).load(products.getPlatformimg()).into(img_logo);
                            tv_name.setText(products.getLoanplatformname());
                            String qouta = products.getAmountmin() + "-" + products.getAmountmax();
                            tv_quota.setText(qouta);
                            String term = products.getBorrowperiodmin() + "-" + products.getBorrowperiodmax();
                            switch (products.getBorrowperiodunit()) {
                                case "1":
                                    term += "天";
                                    break;
                                case "2":
                                    term += "个月";
                                    break;
                                case "3":
                                    term += "年";
                                    break;
                                default:
                                    break;
                            }
                            tv_term.setText(term);


                        } else {
                            ToastUtils.showToast(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }

    @OnClick({R.id.img_setting, R.id.img_msg, R.id.ll_credit_score, R.id.img_touXiang, R.id.tv_login,
            R.id.tv_browse_record, R.id.tv_house, R.id.ll_evaluate, R.id.tv_repayment,
            R.id.tv_order, R.id.tv_apply_now})
    public void onClick(View view) {
        //未登录拦截点击
        boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
        MyLog.e("test", "isLogin = " + isLogin);
        if (!isLogin) {
            startActivity(new Intent(getContext(), LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            case R.id.img_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.img_msg:
                startActivity(new Intent(getContext(), MsgCenterActivity.class));
                break;
            case R.id.img_touXiang:
            case R.id.tv_login:
                startActivity(new Intent(getContext(), MyInfoActivity.class));
                break;
            case R.id.ll_credit_score:
//                startActivity(new Intent(getContext(), CreditScoreActivity.class));
                break;
            case R.id.tv_browse_record:
                startActivity(new Intent(getContext(), BrowseRecordsActivity.class));
                break;
            case R.id.tv_house:
                startActivity(new Intent(getContext(), CollectionActivity.class));
                break;
            case R.id.ll_evaluate:
//                startActivity(new Intent(getContext(), EvaluateActivity.class));
                break;
            case R.id.tv_repayment:
                startActivity(new Intent(getContext(), RepayManageActivity.class));
                break;
            case R.id.tv_order:
                startActivity(new Intent(getContext(), OrdersManagerActivity.class));
                break;
            case R.id.tv_apply_now:
                Intent intent = new Intent(getContext(), ProductActivity.class);
                intent.putExtra(Config.PRODUCT, products);
                startActivity(intent);
                break;
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
