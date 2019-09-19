package com.zxy.jisuloan.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.master.permissionhelper.PermissionHelper;
import com.rpc.enumerate.LivenessTypeEnum;
import com.rpc.manager.IRPCLister;
import com.rpc.manager.RPCSDKManager;
import com.zxy.jisuloan.R;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.HttpUtils;
import com.zxy.jisuloan.utils.LoadDialog;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.PermissionUtils;
import com.zxy.jisuloan.utils.StatusBarUtils;
import com.zxy.jisuloan.view.ApplyStepView;
import com.zxy.jisuloan.view.dialog.PermissionDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by Fang ShiXian
 * on 2019/8/17
 * 实名认证
 */
public class IDCardAuthActivity extends BaseActivity {
    @BindView(R.id.txt_public_title)
    TextView tv_title;
    @BindView(R.id.asv_user_info)
    ApplyStepView applyStepView;
    @BindView(R.id.img_IDcard_f)
    ImageView img_IDCard_f;
    @BindView(R.id.img_IDcard_b)
    ImageView img_IDCard_b;
    @BindView(R.id.ll_IDCard_info)
    LinearLayout ll_IDCard_info;
    @BindView(R.id.txt_idcard_name)
    TextView tv_name;
    @BindView(R.id.txt_idcard_no)
    TextView tv_IDCardNo;
    @BindView(R.id.txt_idcard_face)
    TextView tv_face;
    @BindView(R.id.btn_auth_next)
    Button btn_next;
    @BindView(R.id.txt_kefu)
    TextView tv_kefu;

    private PermissionHelper permissionHelper;

    private String userId;
    private boolean finishAuth = false;//是否认证完成
    private String status2, status3, status4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.with(this).init();

        initView();
        initListener();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_idcard_auth;
    }

    private void initView() {
        Intent intent = getIntent();
        status2 = intent.getStringExtra("status2");
        status3 = intent.getStringExtra("status3");
        status4 = intent.getStringExtra("status4");
        tv_title.setText("实名认证");
        btn_next.setEnabled(false);
        userId = Config.getUserId();

    }


    @OnClick({R.id.img_title_back, R.id.img_IDcard_f, R.id.img_IDcard_b,
            R.id.rl_body, R.id.btn_auth_next, R.id.txt_kefu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.img_IDcard_f:
                startAuth();
                break;
            case R.id.img_IDcard_b:
                startAuth();
                break;
            case R.id.rl_body:
                //活体检测
//                startAuth();
                break;
            case R.id.btn_auth_next:
//                if (!finishAuth) {//未认证
//                    new TipsDialog(this, "请先完成身份认证").show();
//                    return;
//                }
//                startActivity(new Intent(this, UserInfoActivity.class));
//                finish();
                commitData();
                break;
            case R.id.txt_kefu:
                startActivity(new Intent(this, HelpCenterActivity.class));

                break;
            default:
                break;
        }
    }

    /**
     * 上传认证数据到服务器
     */
    private void commitData() {

        LoadDialog.show(this);
        resultObject.put("userId", userId);
        HttpUtils.uploadPicture(userId, files, resultObject, new HttpUtils.INetworkCallback() {
            @Override
            public void callback(String result) {
                LoadDialog.dimiss();
//                try {
//                    JSONObject object = new JSONObject(result);
//                    if ("0".equals(object.getString("error"))) {
//                        SharePrefsUtils.getInstance().putString(Config.SP_REAL_NAME_STATUS, "1");
//                        Intent intent = new Intent();
//                        if ("0".equals(status2)|| !"1".equals(status2)) {
//                            intent.setClass(IDCardAuthActivity.this, UserInfoActivity.class);
//                            intent.putExtra("status3", status3);
//                            intent.putExtra("status4", status4);
//                        } else if (!"1".equals(status3)) {
//                            intent.setClass(IDCardAuthActivity.this, ContactsActivity.class);
//                            intent.putExtra("status4", status4);
//                        } else if (!"1".equals(status4)) {
//                            intent.setClass(IDCardAuthActivity.this, OperatorsActivity.class);
//                        }
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        showTipsDialog(object.getString("msg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void fail(String error) {
                LoadDialog.dimiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showTipsDialog("上传失败:" + error);
                    }
                });

            }
        });
    }


    /**
     * 开始认证
     */
    private void startAuth() {
        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE}, Config.MOXIE_REQUEST_CODE);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                MyLog.e("test", "申请到权限了");
                startLivenessAuth();
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

                final PermissionDialog dialog = new PermissionDialog(IDCardAuthActivity.this);
                dialog.show();
                dialog.setTitle("极速闪贷想要访问权限");
                dialog.setContent("您已经关闭读写文件、相机、读取手机状态权限并不再提醒,请手动打开权限");
                dialog.setOnClickListenerInterface(new PermissionDialog.OnClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        PermissionUtils.toSelfSetting(IDCardAuthActivity.this);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private String tiker = "";

    private void startLivenessAuth() {
        //设置活体检测动作
        List<LivenessTypeEnum> list = new ArrayList<>();
        list.add(LivenessTypeEnum.Eye);
        list.add(LivenessTypeEnum.Mouth);
        list.add(LivenessTypeEnum.HeadLeftOrRight);
        RPCSDKManager.getInstance().setLivenessTypeEnum(list);
        RPCSDKManager.getInstance().setModifiedIdcardMsg(true, true, true);
        RPCSDKManager.getInstance().getTiker(new RPCSDKManager.TikerCallBack() {
            @Override
            public void onSuccess(String s) {
                tiker = s;
                MyLog.e("test onSuccess", "tiker = " + s);
                //获取tickterID成功，可启动实人认证方法
                RPCSDKManager.getInstance().startAuthentication(IDCardAuthActivity.this);
            }

            @Override
            public void onFail(String s, String s1) {
                showTipsDialog(s + ":" + s1);
                MyLog.e("test onFail", "code = " + s + "       msg =" + s1);
            }
        });


    }


    private Bitmap bitmap1, bitmap2;
    private Map<String, String> resultObject = new HashMap<>();
    //    private StringBuilder stringBuilder = new StringBuilder();
    private File[] files;

    /**
     * 回调监听
     */
    private void initListener() {
        RPCSDKManager.getInstance().setRPCLister(new IRPCLister() {
            @Override
            public void onFail(String s, String s1) {//调用失败
                showTipsDialog(s + ":" + s1);
                MyLog.e("test 回调onFail", "code = " + s + "       msg =" + s1);
                RPCSDKManager.getInstance().finishActivity();
            }

            @Override
            public void onVerifaction(String s, String s1) {//调用成功
//                showTipsDialog(s + ":" + s1);
                MyLog.e("test 回调 onVerifaction", "code = " + s + "       msg =" + s1);
                RPCSDKManager.getInstance().finishActivity();
//                try {
//                stringBuilder.append("&score=" + s1);

                resultObject.put("score", s1);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                btn_next.setEnabled(true);
            }

            @Override
            public void onIdentityCardAuth(String s, String s1) {
                MyLog.e("test 回调 onIdentityCardAuth", "code = " + s + "       msg =" + s1);

                //身份核验成功

                try {
                    JSONObject object = new JSONObject(s1);
                    JSONObject data = object.getJSONObject("data");
                    String result = data.optString("result");
                    if (result.equals("01")) {
                        //返回结果 01-认证一致(收费) 02-认证不一致(收费) 03-认证不确定（不收费） 04-认证失败(不收费)
//                        showTipsDialog(result+":"+s1);
                        tv_face.setText("已认证");
                    } else {
//                        showTipsDialog(result + ":认证不通过");
                        tv_face.setText("认证不通过");
                    }
//                    stringBuilder.append("&orderNo=" + data.getString("orderNo"))
//                            .append("&handleTime=" + data.getString("handleTime"))
//                            .append("&result=" + data.getString("result"))
//                            .append("&remark=" + data.getString("remark"))
//                            .append("&province=" + data.getString("province"))
//                            .append("&city=" + data.getString("city"))
//                            .append("&country=" + data.getString("country"))
//                            .append("&birthday=" + data.getString("birthday"))
//                            .append("&age=" + data.getString("age"))
//                            .append("&gender=" + data.getString("gender"));

                    resultObject.put("orderNo", data.getString("orderNo"));
                    resultObject.put("handleTime", data.getString("handleTime"));
                    resultObject.put("result", data.getString("result"));
                    resultObject.put("remark", data.getString("remark"));
                    resultObject.put("province", data.getString("province"));
                    resultObject.put("city", data.getString("city"));
                    resultObject.put("country", data.getString("country"));
                    resultObject.put("birthday", data.getString("birthday"));
                    resultObject.put("age", data.getString("age"));
                    resultObject.put("gender", data.getString("gender"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVerifiedPic(String s, Map<String, String> map) {
                //图片地址回调
                MyLog.e("test 回调 onVerifiedPic", "code = " + s + "     " + map.toString());
                if (s.equals("1000")) {
                    String idcard_front = map.get("rpc_sdk_idcard_front");
                    String idcard_back = map.get("rpc_sdk_idcard_back");
                    String bestImg = map.get("bestImage0");
                    String Eye = map.get("Eye");
                    String Mouth = map.get("Mouth");
                    String HeadLeftOrRight = map.get("HeadLeftOrRight");
                    bitmap1 = BitmapFactory.decodeFile(idcard_front);
                    bitmap2 = BitmapFactory.decodeFile(idcard_back);
                    img_IDCard_f.setImageBitmap(bitmap1);
                    img_IDCard_b.setImageBitmap(bitmap2);
                    File file1 = new File(idcard_front);
                    File file2 = new File(idcard_back);
                    File file3 = new File(bestImg);
//                    File file4 = new File(Eye);
//                    File file5 = new File(Mouth);
//                    File file6 = new File(HeadLeftOrRight);
//                    files = new File[]{file1, file2, file3, file4, file5, file6};
                    files = new File[]{file1, file2, file3};
                }
            }

            @Override
            public void onIdcardMsg(String s, String s1, String s2) {
                MyLog.e("test 回调 onIdcardMsg", "code = " + s + "\ns1 =" + s1
                        + "\ns2 = " + s2);

                //身份证正反面ocr回调
                if (s.equals("1000")) {
                    if (s1 != null) {
                        try {
                            JSONObject IdcardFront = new JSONObject(s1);
                            if ("1".equals(IdcardFront.getString("chargeStatus"))) {
                                JSONObject frontData = IdcardFront.getJSONObject("data");
                                String name = frontData.getString("name");
                                String cardNum = frontData.getString("cardNum");

//                                stringBuilder.append("&tradeNo=" + frontData.getString("tradeNo"))
//                                        .append("&code=" + frontData.getString("code"))
//                                        .append("&riskType=" + frontData.getString("riskType"))
//                                        .append("&address=" + frontData.getString("address"))
//                                        .append("&birth=" + frontData.getString("birth"))
//                                        .append("&name=" + frontData.getString("name"))
//                                        .append("&cardNum=" + frontData.getString("cardNum"))
//                                        .append("&sex=" + frontData.getString("sex"))
//                                        .append("&nation=" + frontData.getString("nation"));


                                resultObject.put("tradeNo", frontData.getString("tradeNo"));
                                resultObject.put("code", frontData.getString("code"));
                                resultObject.put("riskType", frontData.getString("riskType"));
                                resultObject.put("address", frontData.getString("address"));
                                resultObject.put("birth", frontData.getString("birth"));
                                resultObject.put("name", name);
                                resultObject.put("cardNum", cardNum);
                                resultObject.put("sex", frontData.getString("sex"));
                                resultObject.put("nation", frontData.getString("nation"));

                                //将名字和身份证号码存到SharedPreferences
                                Config.putIDCardNo(cardNum.substring(0, 3) + "***********"
                                        + cardNum.substring(cardNum.length() - 4));
                                Config.putRealName(name);

                                tv_name.setText(name);
                                tv_IDCardNo.setText(cardNum);
                                ll_IDCard_info.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (s2 != null) {
                        try {
                            JSONObject IdcardFront = new JSONObject(s1);
                            if ("1".equals(IdcardFront.getString("chargeStatus"))) {
                                JSONObject backData = IdcardFront.getJSONObject("data");

//                                stringBuilder.append("&issuingDate=" + backData.getString("issuingDate"))
//                                        .append("&expiryDate=" + backData.getString("expiryDate"))
//                                        .append("&issuingAuthority=" + backData.getString("issuingAuthority"));
                                resultObject.put("issuingDate", backData.getString("issuingDate"));
                                resultObject.put("expiryDate", backData.getString("expiryDate"));
                                resultObject.put("issuingAuthority", backData.getString("issuingAuthority"));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onIdcardModifiedMsg(String s, String s1, String s2) {
                MyLog.e("test 回调 onIdcardModifiedMsg", "code = " + s + "\ns1 =" + s1
                        + "\ns2 = " + s2);
                //用户修改后的身份证信息回调
                //当身份证信息页设置能修改时有值，否则为空
                if (("1000").equals(s)) {
                    if (s1 != null) {
                        try {
                            JSONObject IdcardFront = new JSONObject(s1);
                            if ("1".equals(IdcardFront.getString("chargeStatus"))) {
                                JSONObject frontData = IdcardFront.getJSONObject("data");

                                String name = frontData.getString("name");
                                String cardNum = frontData.getString("cardNum");

//                                stringBuilder.append("&tradeNo=" + frontData.getString("tradeNo"))
//                                        .append("&code=" + frontData.getString("code"))
//                                        .append("&riskType=" + frontData.getString("riskType"))
//                                        .append("&address=" + frontData.getString("address"))
//                                        .append("&birth=" + frontData.getString("birth"))
//                                        .append("&name=" + frontData.getString("name"))
//                                        .append("&cardNum=" + frontData.getString("cardNum"))
//                                        .append("&sex=" + frontData.getString("sex"))
//                                        .append("&nation=" + frontData.getString("nation"));
                                resultObject.put("tradeNo", frontData.getString("tradeNo"));
                                resultObject.put("code", frontData.getString("code"));
                                resultObject.put("riskType", frontData.getString("riskType"));
                                resultObject.put("address", frontData.getString("address"));
                                resultObject.put("birth", frontData.getString("birth"));
                                resultObject.put("name", name);
                                resultObject.put("cardNum", cardNum);
                                resultObject.put("sex", frontData.getString("sex"));
                                resultObject.put("nation", frontData.getString("nation"));

                                //将名字和身份证号码存到SharedPreferences
                                Config.putIDCardNo(cardNum.substring(0, 3) + "***********"
                                        + cardNum.substring(cardNum.length() - 4));
                                Config.putRealName(name);

                                tv_name.setText(name);
                                tv_IDCardNo.setText(cardNum);
                                ll_IDCard_info.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void processMessage(Message message) {

    }
}
