package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.vo.BaseResponse;
import com.zxy.jisuloan.vo.ProductVo;
import com.zxy.jisuloan.vo.UserStatusVO;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/27
 */
public class ProductActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.tv_loan)
    TextView tv_loan;
    @BindView(R.id.tv_rate)
    TextView tv_rate;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_quota)
    TextView tv_quota;
    @BindView(R.id.tv_term)
    TextView tv_term;
    @BindView(R.id.tv_strategy)
    TextView tv_strategy;//攻略
    @BindView(R.id.tv_advantage)
    TextView tv_advantage;//优势
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_idCard)
    TextView tv_idCard;
    @BindView(R.id.tv_job)
    TextView tv_job;
    @BindView(R.id.ctv_house)
    CheckedTextView ctv_house;//收藏
    @BindView(R.id.ctv_apply)
    CheckedTextView ctv_apply;
    @BindView(R.id.ctv_evaluate)
    CheckedTextView ctv_evaluate;
    @BindView(R.id.tv_finish)
    TextView tv_finish;

    private String product_id;
    private String product_url;
    private String userId;
    private boolean authFinish = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar(this);
        initView();
        initData();
    }

    private void initData() {
        addBrowseRecord();//添加浏览记录
        queryCollection();//查询是否收藏
        chedkDate();//查询是否完成认证
    }

    private void initView() {
        Intent intent = getIntent();
        ProductVo.Products product = (ProductVo.Products) intent.getSerializableExtra(Config.PRODUCT);
        if (product == null) {
            ToastUtils.showToast("未获取到产品数据");
            finish();
            return;
        }
        product_id = product.getId();
        product_url = product.getPlatformurl();
        MyLog.e("test url=", product_url + "");
        userId = Config.getUserId();
        tv_loan.setText(product.getAvgloanmoney());//平均放款
        String rate = product.getInterestrate() + "";
        String[] split = rate.split("\\.");
        if ("0".equals(split[1])) {
            rate = split[0] + "%";
        } else {
            rate += "%";
        }
        tv_rate.setText(rate);//利率
        tv_time.setText(product.getAvgloantime() + "小时");//平均放款时间

        DecimalFormat df = new DecimalFormat("#.00");
        double minQuota = Double.valueOf(product.getAmountmin()) / 10000;
        double maxQuota = Double.valueOf(product.getAmountmax()) / 10000;
        String min = df.format(minQuota);
        String max = df.format(maxQuota);
        if (minQuota < 1) {//minQuota小于1的时候min=.xx ,前面要补0
            min = "0" + min;
        }
        if (maxQuota < 1) {
            max = "0" + max;
        }
        String quota = min + "~" + max + "万元";
        tv_quota.setText(quota);//放款额度

        String term = product.getBorrowperiodmin() + "~" + product.getBorrowperiodmax();
        String unit = product.getBorrowperiodunit();
        if ("1".equals(unit)) {
            term += "天";
        } else if ("2".equals(unit)) {
            term += "月";
        } else if ("3".equals(unit)) {
            term += "年";
        }
        tv_term.setText(term);//借款期限
        String strategy = product.getLoanplatformname() + "申请技巧及注意事项";
        tv_strategy.setText(strategy);
//        tv_advantage.setText();//产品优势
        String name = Config.getRealName();
        String IDCardNO = Config.getIDCardNo();
        String phone = Config.getPhone();
        String job = Config.getJob();

        tv_title.setText(product.getLoanplatformname());
        tv_name.setText(name);
        tv_idCard.setText(IDCardNO);
        tv_phone.setText(phone);
        tv_job.setText(job);
    }

    private String device = Config.getDeviceId();


    @OnClick({R.id.img_title_back, R.id.tv_pd, R.id.ctv_house, R.id.ll_strategy,
            R.id.btn_apply})
    public void onClick(View view) {
        boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
        if (!isLogin && view.getId() != R.id.img_title_back) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.tv_pd:
                Intent intent = new Intent(this, ProductDetailActivity.class);
                intent.putExtra(Config.PRODUCT_ID, product_id);
                startActivity(intent);
                break;
            case R.id.ll_strategy:
                ToastUtils.showToast("攻略");
                break;
            case R.id.ctv_house://收藏
                if (ctv_house.isChecked()) {
                    ctv_house.setChecked(false);
                    doCollection();
                } else {
                    ctv_house.setChecked(true);
                    doCollection();
                }
                break;
            case R.id.btn_apply:
//                ToastUtils.showToast("申请");
                if (!authFinish) {
                    //未认证完成跳转认证页面
                    Intent intent2 = new Intent(ProductActivity.this, MyInfoActivity.class);
                    startActivity(intent2);
                    ToastUtils.showToast("请先完成认证");
                } else {
                    //认证完成可以申请
                    Intent intent2 = new Intent();
                    intent2.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(product_url);//此处填链接
                    intent2.setData(content_url);
                    startActivity(intent2);
                }

                break;
        }
    }

    private String status1, status2, status3, status4;

    /**
     * 查询是否通过四项认证
     */
    private void chedkDate() {
        String userId = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");
        BaseApi.request(BaseApi.createApi2(IService.class).getUserAuthStatus(userId),
                new BaseApi.IResponseListener<UserStatusVO>() {
                    @Override
                    public void onSuccess(UserStatusVO data) {
                        if ("0".equals(data.getError())) {
                            MyLog.e("test 查询是否通过四项认证", data.toString());
                            //获取状态成功
                            status1 = data.getRealInfoStatus();
                            status2 = data.getUserInfoStatus();
                            status3 = data.getContactInfoStatus();
                            status4 = data.getOperatorInfoStatus();
                            saveData();
                            String cardNum = data.getCardNum();
                            if (cardNum != null) {
                                cardNum = cardNum.substring(0, 3) + "***********" + cardNum.substring(cardNum.length() - 4);
                            }
                            Config.putIDCardNo(cardNum);
                            Config.putCareer(data.getCareer());
                            Config.putRealName(data.getName());
                            tv_idCard.setText(cardNum);
                            tv_name.setText(data.getName());
                            if ("1".equals(data.getCareer())){
                                tv_job.setText("工薪族");
                            }else if ("2".equals(data.getCareer())){
                                tv_job.setText("企业族");
                            }else if ("3".equals(data.getCareer())){
                                tv_job.setText("自由职业");
                            }

                        } else {
                            //从服务器获取状态失败，从本地获取认证数据
                            getData();
                        }
                        judgeData();
                    }

                    @Override
                    public void onFail() {
                        //获取状态失败
                        getData();
                        judgeData();
                    }
                });
    }

    /**
     * 保存四项认证状态
     */
    private void saveData() {
        SharePrefsUtils.getInstance().putString(Config.SP_REAL_NAME_STATUS, status1);
        SharePrefsUtils.getInstance().putString(Config.SP_USER_INFO_STATUS, status2);
        SharePrefsUtils.getInstance().putString(Config.SP_USER_CONT_STATUS, status3);
        SharePrefsUtils.getInstance().putString(Config.SP_USER_YYS_STATUS, status4);

    }

    /**
     * 获取四项认证状态
     */
    private void getData() {
        status1 = Config.getIDCardStatus();
        status2 = Config.getUserInfoStatus();
        status3 = Config.getContactsStatus();
        status4 = Config.getYYSStatus();
    }

    /**
     * 判断认证状态是否完成并做相应操作
     */
    private void judgeData() {
        if (!"1".equals(status2) || !"1".equals(status3)
                || !"1".equals(status1) || !"1".equals(status4)) {
            //未认证完成
            tv_finish.setText("未完成");
            authFinish = false;

        } else {

            tv_finish.setText("已完成");
            authFinish = true;
        }
    }


    /**
     * 进入页面就添加浏览记录
     */
    private void addBrowseRecord() {
        String userId = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");
        BaseApi.request(BaseApi.createApi(IService.class).addBrowseRecord(userId, product_id, device),
                new BaseApi.IResponseListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        MyLog.e("test 添加浏览记录", data.toString());
                    }

                    @Override
                    public void onFail() {
                    }
                });
    }


    /**
     * 查询收藏状态
     */
    private void queryCollection() {
        BaseApi.request(BaseApi.createApi2(IService.class).queryCollection(userId, product_id),
                new BaseApi.IResponseListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        MyLog.e("test 查询收藏状态", data.toString());
                        if ("1".equals(data.getError())) {
                            ctv_house.setChecked(true);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }


    /**
     * 收藏
     */
    private void doCollection() {
        BaseApi.request(BaseApi.createApi(IService.class).collection(userId, product_id),
                new BaseApi.IResponseListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if ("0".equals(data.getError())) {
                            ctv_house.setChecked(true);
                        } else if ("1".equals(data.getError())) {
                            ctv_house.setChecked(false);
//                            ToastUtils.showToast(data.getMsg());
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
