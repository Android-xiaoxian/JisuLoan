package com.zxy.jisuloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.zxy.jisuloan.R;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.IService;
import com.zxy.jisuloan.utils.BaseActivity;
import com.zxy.jisuloan.utils.Config;
import com.zxy.jisuloan.utils.GetDeviceId;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.SharePrefsUtils;
import com.zxy.jisuloan.utils.StatusBarUtils;
import com.zxy.jisuloan.utils.ToastUtils;
import com.zxy.jisuloan.vo.UserStatusVO;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Create by Fang ShiXian
 * on 2019/8/14
 * 启动页
 */
public class FirstActivity extends BaseActivity {

    private boolean first_run = true;//是否第一次运行
    private String status1, status2, status3, status4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusTransparent(this);
//        SharePrefsUtils.getInstance().putBoolean(Config.SP_IS_LOGIN,true);
//        SharePrefsUtils.getInstance().putString(Config.SP_USER_ID,"190829185356350");
//        SharePrefsUtils.getInstance().putLong(Config.SP_LAST_LOGIN_TIME,System.currentTimeMillis());
//        SharePrefsUtils.getInstance().putString(Config.SP_USER_MOBILEPHONE,"15878586007");
        initData();
        turnToWhere();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_first;
    }

    private void initData() {
        getDeviceId();
    }

    /**
     * 延时2秒后判断跳转到哪个页面
     */
    private void turnToWhere() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //判断是否第一次打开APP
                first_run = SharePrefsUtils.getInstance().getBoolean(Config.SP_FIRST_RUN, true);
                if (first_run) {//第一次启动
                    startActivity(new Intent(FirstActivity.this, GuideActivity.class));
                    SharePrefsUtils.getInstance().putBoolean(Config.SP_FIRST_RUN, false);
                    finish();
                } else {
                    //不是第一次启动，判断是否已经登录
                    boolean isLogin = SharePrefsUtils.getInstance().getBoolean(Config.SP_IS_LOGIN, false);
                    if (isLogin) {
                        //已经登录判断上次使用时间是否7天之前
                        long last_login_time = SharePrefsUtils.getInstance().getLong(Config.SP_LAST_LOGIN_TIME, 0);
                        if (System.currentTimeMillis() - last_login_time > 7 * 24 * 60 * 60 * 1000) {
                            //距离上次使用时间超过7天
                            SettingActivity.logOut();
                            //删除登录状态后进入主页
                            startActivity(new Intent(FirstActivity.this, MainActivity.class));
                            finish();
                        } else {
                            //保存最近使用时间
                            SharePrefsUtils.getInstance().putLong(Config.SP_LAST_LOGIN_TIME, System.currentTimeMillis());
                            //检查是否完成四项认证
                            chedkDate();
                        }

                    } else {
                        //未登录进入主页
                        startActivity(new Intent(FirstActivity.this, MainActivity.class));
//                        startActivity(new Intent(FirstActivity.this, QuotaDatermineActivity.class));
                        finish();
                    }
                }

            }
        }, 2000);
    }

    /**
     * 查询是否通过四项认证
     */
    private void chedkDate() {
        String userId = SharePrefsUtils.getInstance().getString(Config.SP_USER_ID, "");
        BaseApi.request(BaseApi.createApi(IService.class).getUserAuthStatus(userId),
                new BaseApi.IResponseListener<UserStatusVO>() {
                    @Override
                    public void onSuccess(UserStatusVO data) {
                        if ("0".equals(data.getError())) {
                            status1 = data.getRealInfoStatus();
                            status2 = data.getUserInfoStatus();
                            status3 = data.getContactInfoStatus();
                            status4 = data.getOperatorInfoStatus();
                            String cardNum = data.getCardNum();
                            if (cardNum != null) {
                                cardNum = cardNum.substring(0, 3) + "***********" + cardNum.substring(cardNum.length() - 4);
                            }
                            Config.putIDCardNo(cardNum);
                            Config.putCareer(data.getCareer());
                            Config.putRealName(data.getName());
                            saveData();
                            if (!"1".equals(status2) || !"1".equals(status3)
                                    || !"1".equals(status1) || !"1".equals(status4)
                            ) {
                                //未认证完成进入我的资料
                                Intent intent = new Intent(FirstActivity.this, MyInfoActivity.class);
                                intent.putExtra("from", "FirstActivity");
                                startActivity(intent);
//                                startActivity(new Intent(FirstActivity.this, MainActivity.class));
                            } else {
                                //认证完成进入首页
                                startActivity(new Intent(FirstActivity.this, MainActivity.class));
                            }
                        } else {
                            startActivity(new Intent(FirstActivity.this, MainActivity.class));
                        }
                        finish();
                    }

                    @Override
                    public void onFail() {
                        ToastUtils.cancel();
                        startActivity(new Intent(FirstActivity.this, MainActivity.class));
                        finish();
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

    @Override
    public void processMessage(Message message) {

    }

    /**
     * 生成过更新设备唯一标识符
     */
    private void getDeviceId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取保存在sd中的 设备唯一标识符
                    String readDeviceID = GetDeviceId.readDeviceID(FirstActivity.this);
                    //获取缓存在  sharepreference 里面的 设备唯一标识
                    String string = SharePrefsUtils.getInstance().getString(Config.SP_DEVICES_ID, readDeviceID);
                    //判断 app 内部是否已经缓存,  若已经缓存则使用app 缓存的 设备id
                    if (string != null) {
                        //app 缓存的和SD卡中保存的不相同 以app 保存的为准, 同时更新SD卡中保存的 唯一标识符
                        if (TextUtils.isEmpty(readDeviceID) && !string.equals(readDeviceID)) {
                            // 取有效地 app缓存 进行更新操作
                            if (TextUtils.isEmpty(readDeviceID) && !TextUtils.isEmpty(string)) {
                                readDeviceID = string;
                                GetDeviceId.saveDeviceID(readDeviceID, FirstActivity.this);
                            }
                        }
                    }
                    // app 没有缓存 (这种情况只会发生在第一次启动的时候)
                    if (TextUtils.isEmpty(readDeviceID)) {
                        //保存设备id
                        readDeviceID = GetDeviceId.getDeviceId(FirstActivity.this);

                    }
                    MyLog.e("test", readDeviceID);
                    //最后再次更新app 的缓存
                    SharePrefsUtils.getInstance().putString(Config.SP_DEVICES_ID, readDeviceID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

    }
}
