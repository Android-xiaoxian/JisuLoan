package com.zxy.jisuloan.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.master.permissionhelper.PermissionHelper;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.LocateUtil;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.PermissionUtils;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.view.ApplyStepView;
import com.zxy.jisuloan.view.dialog.BottomDialog01;
import com.zxy.jisuloan.view.dialog.PermissionDialog;
import com.zxy.jisuloan.view.dialog.TipsDialog;
import com.zxy.jisuloan.vo.BaseResponse;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/17
 * 填写个人信息
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.asv_user_info)
    ApplyStepView applyStepView;
    @BindView(R.id.txt_user_edu)
    TextView tv_edu;
    @BindView(R.id.txt_user_job)
    TextView tv_job;
    @BindView(R.id.txt_user_income)
    TextView tv_income;
    @BindView(R.id.txt_user_want_money)
    TextView tv_want_money;
    @BindView(R.id.txt_user_home_address)
    TextView tv_address;
    @BindView(R.id.et_user_address)
    EditText et_address;
    @BindView(R.id.txt_user_work_address)
    TextView tv_workAdd;
    @BindView(R.id.et_company_name)
    EditText et_companyName;
    @BindView(R.id.et_workAddress)
    EditText et_workAdd;
    @BindView(R.id.txt_user_tips1)
    TextView tv_tips1;
    @BindView(R.id.txt_user_tips2)
    TextView tv_tips2;

    //教育、职业、收入、期望借款的选项数组
    private String[] edus = new String[]{"研究生及以上", "本科（学士）", "大专", "高中/职中", "初中及以下"},
            jobs = new String[]{"工薪族", "企业族", "自由职业者"},
            incomes = new String[]{"1500以下", "1500-3000", "3000-5000", "5000-8000", "8000-12000", "12000以上"},
            wantMoneys = new String[]{"1500以下", "1500-3000", "3000-5000", "5000-8000", "8000-12000", "12000以上"};
    private PermissionHelper permissionHelper;
    private LocateUtil locateUtil;
    private int areaNo = 1;//1表示居住地址，2表示工作地址
    private int eduNum, jobNum, incomeNum, exceptNum;
    private String status3, status4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar(this);
        locateUtil = new LocateUtil(this);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        status3 = intent.getStringExtra("status3");
        status4 = intent.getStringExtra("status4");
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    private void initView() {
        tv_title.setText("个人信息");
        applyStepView.changeStep(1);
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    tv_tips1.setVisibility(View.VISIBLE);
                } else {
                    tv_tips1.setVisibility(View.GONE);
                }
            }
        });
        et_workAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    tv_tips2.setVisibility(View.VISIBLE);
                } else {
                    tv_tips2.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.img_title_back, R.id.rl_user1, R.id.rl_user2, R.id.rl_user3, R.id.rl_user4,
            R.id.rl_user5, R.id.rl_user6, R.id.btn_auth_next, R.id.txt_kefu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.rl_user1:
                showBottomDialog(edus, "选择学历", tv_edu, 1);
                break;
            case R.id.rl_user2:
                showBottomDialog(jobs, "选择职业", tv_job, 2);
                break;
            case R.id.rl_user3:
                showBottomDialog(incomes, "选择月均收入", tv_income, 3);
                break;
            case R.id.rl_user4:
                showBottomDialog(wantMoneys, "选择期望借款金额", tv_want_money, 4);
                break;
            case R.id.rl_user5:
                MyLog.e("test", "点击了");
                getPermission(1);
                break;
            case R.id.rl_user6:
                MyLog.e("test", "点击了2");
                getPermission(2);
                break;
            case R.id.btn_auth_next:
                doAddUserInfo();
                break;
            case R.id.txt_kefu:
                startActivity(new Intent(this, HelpCenterActivity.class));

                break;
            default:
                break;
        }
    }

    /**
     * 提交资料
     */
    private void doAddUserInfo() {
        String userID = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");
        String edu = tv_edu.getText().toString();
        String income = tv_income.getText().toString();
        String job = tv_job.getText().toString();
        String wantMoney = tv_want_money.getText().toString();
        String addArea = tv_address.getText().toString();
        String address = et_address.getText().toString();
        String company = et_companyName.getText().toString();
        String workAdd = tv_workAdd.getText().toString();
        String workAddress = et_workAdd.getText().toString();
        if (edu.length() == 0 || income.length() == 0 || job.length() == 0 || wantMoney.length() == 0 || addArea.length() == 0 ||
                address.length() == 0 || company.length() == 0 || workAdd.length() == 0 || workAddress.length() == 0) {
            new TipsDialog(this, "请完善资料").show();
            return;
        }
        BaseApi.request(BaseApi.createApi(IService.class).addUserInfo(userID, eduNum, jobNum,
                incomeNum, exceptNum, addArea + address, company,
                workAdd + workAddress),
                new BaseApi.IResponseListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if ("0".equals(data.error)) {
                            SharePrefsUtils.getInstance().putString(Config.SP_USER_INFO_STATUS, "1");
                            SharePrefsUtils.getInstance().putString(Config.SP_USER_JOB, job);
                            Intent intent = new Intent();
                            if (!"1".equals(status3)) {
                                intent.setClass(UserInfoActivity.this, ContactsActivity.class);
                                intent.putExtra("status4", status4);
                            } else if (!"1".equals(status4)) {
                                intent.setClass(UserInfoActivity.this, OperatorsActivity.class);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            new TipsDialog(UserInfoActivity.this, data.getMsg()).show();
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
    }


    /**
     * 获取定位权限并开始定位
     */
    public static boolean locating = false;//开始定位到页面跳转有时间延迟，防止多次定位

    private void getPermission(int i) {
        MyLog.e("test", "locating = " + locating);
        if (locating) {
            return;
        }
        locating = true;
        areaNo = i;
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, Config.PERMISSION_REQUEST_CODE);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                MyLog.e("test", "获取到权限了");
                locateUtil.startLoacte(0);
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {

            }

            @Override
            public void onPermissionDenied() {
                showTipsDialog("您拒绝了定位权限申请");
            }

            @Override
            public void onPermissionDeniedBySystem() {
                locating = false;
                if (locateUtil != null) {
                    locateUtil.unRegister();
                }
                final PermissionDialog dialog = new PermissionDialog(UserInfoActivity.this);
                dialog.show();
                dialog.setTitle("极速闪贷想要访问定位权限");
                dialog.setContent("您已经关闭定位权限并不再提醒,请手动打开定位权限");
                dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        PermissionUtils.toSelfSetting(UserInfoActivity.this);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 申请权限回调处理
     * 使用权限插件implementation 'com.master.android:permissionhelper:1.3'
     * permissionHelper类内部处理权限问题
     *
     * @param requestCode  请求权限时传入的参数，前后对应
     * @param permissions  请求权限时传入的权限数组，可同时请求多个权限
     * @param grantResults 对应每个权限的返回结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            MyLog.e("test", "权限回调");
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private BottomDialog01 dialog01;

    /**
     * @param strings
     * @param title
     * @param textView
     * @param i        区分是哪个资料
     */
    public void showBottomDialog(String[] strings, String title, TextView textView, int i) {
        dialog01 = new BottomDialog01(this, strings, title);
        dialog01.setListener(new BottomDialog01.ListViewClickListener() {
            @Override
            public void onClick(String choice, int position) {
                textView.setText(choice);
                textView.setVisibility(View.VISIBLE);
                switch (i) {
                    case 1:
                        eduNum = 5 - position;
                        break;
                    case 2:
                        jobNum = position + 1;
                        break;
                    case 3:
                        incomeNum = position + 1;
                        break;
                    case 4:
                        exceptNum = position + 1;
                        break;
                    default:
                        break;
                }
            }
        });
        dialog01.show();

    }

    @Override
    public void processMessage(Message message) {

    }


    /**
     * 定位页返回后获取位置并显示
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Config.RESULT_CODE_1 && requestCode == Config.REQUEST_CODE_1) {
            String area = data.getStringExtra("result");
            area = area.replaceAll("1市辖区1", "")
                    .replaceAll("1县1", "")
                    .replaceAll("1", "");
            switch (areaNo) {
                case 1:
                    tv_address.setText(area);
                    tv_address.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tv_workAdd.setText(area);
                    tv_workAdd.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locateUtil != null) {
            //取消注册百度定位组件，不取消会导致多次重复定位
            locateUtil.unRegister();
        }
    }
}
