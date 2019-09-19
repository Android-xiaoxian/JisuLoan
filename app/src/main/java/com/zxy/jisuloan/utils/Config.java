package com.zxy.jisuloan.utils;

import com.zxy.jisuloan.net.BaseApi;

/**
 * Create by Fang ShiXian
 * on 2019/8/14
 */
public class Config {

    //美恰
    public static final String MEIQIA_KEY = "766938e9352a20b79e8396fab5ba1353";

    //    创蓝253实人认证
    public static final String APPID_253 = "vtuW70I0";
    public static final String APPKEY_253 = "qV2wH0TU";
    public static final String LICENSE_ID_253 = "jisushandai-face-android";

    //    public static final String APPID_253 = "fKmC8qtG";
//    public static final String APPKEY_253 = "EIoHqf3o";


    public static final int CODE_UPDATE_DIALOG = 0;
    public static final int CODE_URL_ERROR = 1;
    public static final int CODE_NET_ERROR = 2;
    public static final int CODE_JSON_ERROR = 3;
    public static final int CODE_ENTER_HOME = 4;

    public static final String SP_FIRST_RUN = "first_run";
    public static final String SP_IS_LOGIN = "isLogin";
    public static final String SP_LAST_LOGIN_TIME = "last_login_time";
    public static final String SP_DEVICES_ID = "device_id";
    public static final String SP_USER_CREATE_TIME = "create_time";
    public static final String SP_USER_ENABLE = "enable";
    public static final String SP_USER_ID = "user_id";
    public static final String SP_USER_NAME = "user_name";
    public static final String SP_USER_MOBILEPHONE = "user_mobile_phone";
    public static final String SP_USER_VIP_TYPE = "user_vip_type";
    public static final String SP_REAL_NAME_STATUS = "read_info_status";
    public static final String SP_USER_INFO_STATUS = "user_info_status";
    public static final String SP_USER_CONT_STATUS = "cont_info_status";
    public static final String SP_USER_YYS_STATUS = "yys_info_status";
    public static final String SP_USER_REAL_NAME = "real_name";
    public static final String SP_USER_CAREER = "user_career";
    public static final String SP_USER_IDCARD_NO = "idCard_no";
    public static final String SP_USER_JOB = "user_job";
    public static final String AUTH_STATE = "auth_state";
    public static final String PROVINCE = "province";
    public static final String PRODUCT = "product";
    public static final String PRODUCT_ID = "product_id";
    public static final String CHANGE_FRAGMENT_BORROW = "change_to_borrow";
    public static final String ITEM_TYPE1 = "date_type";
    public static final String ITEM_TYPE2 = "normal_type";
    public static final String QUESTION_ClASS = "question_class";
    public static final String WHICH_QUESTION = "WHICH_QUESTION";
    public static final String WEB_URL = "WEB_URL";
    public static final String WEB_TITLE = "WEB_TITLE";
    public static final String AGRESS_WITH_PRIVACY_POLICY = "I_had_agree_with_the_privacy_policy";


    public static final int MESSAGE_WHAT = 1;


    public static final int REQUEST_CODE_1 = 100;
    public static final int RESULT_CODE_1 = 101;
    public static final int REQUEST_CODE_2 = 102;
    public static final int RESULT_CODE_2 = 103;
    public static final int PERMISSION_REQUEST_CODE = 105;
    public static final int REQUEST_CODE_SERVICE_SMS = 106;
    public static final int REQUEST_CODE_3 = 107;
    public static final int REQUEST_CODE_4 = 108;
    public static final int SELF_PERMISSION_REQUEST_CODE = 109;
    public static final int IDCARD_REQUESTCODE_1 = 110;
    public static final int IDCARD_REQUESTCODE_2 = 111;
    public static final int LIVENESS_REQUEST_CODE = 112;


    public static final int CHANGE_FRAGMENT = 1001;
    public static final int CONTACTS_REQUEST_CODE = 1002;
    public static final int FIREND_REQUEST_CODE = 1003;
    public static final int WORKMATE_REQUEST_CODE = 1004;
    public static final int CHANGE_TO_BORROW = 1005;
    public static final int MOXIE_REQUEST_CODE = 1006;
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 1007;
    public static final int REQUEST_CODE_UNKNOWN_APP = 1008;
    public static final int APP_INSTALL = 1009;


    public static String SMS_URL = BaseApi.KEY_BASE_URL + "/servlet/current/user/UserAction?function=AddMessageList";//短信上传接口
    public static String CONTACTS_URL = BaseApi.KEY_BASE_URL + "/servlet/current/user/UserAction?function=AddContactBook";//联系人上传接口
    public static String PRIVACY_URL = BaseApi.KEY_BASE_URL + "/h5/privacy.html";//隐私协议
    public static String USER_AGGREEMENT_URL = BaseApi.KEY_BASE_URL + "/h5/userAgreement.html";//用户协议


    public static String getUserId() {
        return SharePrefsUtils.getInstance().getString(SP_USER_ID, "");
    }

    public static String getDeviceId() {
        return SharePrefsUtils.getInstance().getString(SP_DEVICES_ID, "");
    }

    public static String getRealName() {
        return SharePrefsUtils.getInstance().getString(SP_USER_REAL_NAME, "");
    }

    public static String getIDCardNo() {
        return SharePrefsUtils.getInstance().getString(SP_USER_IDCARD_NO, "");
    }

    public static String getPhone() {
        return SharePrefsUtils.getInstance().getString(SP_USER_MOBILEPHONE, "");
    }

    public static String getJob() {
        return SharePrefsUtils.getInstance().getString(SP_USER_JOB, "");
    }

    public static boolean getIsLogin() {
        return SharePrefsUtils.getInstance().getBoolean(SP_IS_LOGIN, false);
    }

    /**
     * 身份认证状态
     *
     * @return
     */
    public static String getIDCardStatus() {
        return SharePrefsUtils.getInstance().getString(SP_REAL_NAME_STATUS, "0");
    }

    /**
     * 个人信息认证状态
     *
     * @return
     */
    public static String getUserInfoStatus() {
        return SharePrefsUtils.getInstance().getString(SP_USER_INFO_STATUS, "0");
    }

    /**
     * 联系人认证状态
     *
     * @return
     */
    public static String getContactsStatus() {
        return SharePrefsUtils.getInstance().getString(SP_USER_CONT_STATUS, "0");
    }

    /**
     * 运营商认证状态
     *
     * @return
     */
    public static String getYYSStatus() {
        return SharePrefsUtils.getInstance().getString(SP_USER_YYS_STATUS, "0");
    }

    public static void putIDCardNo(String IDCardNo) {
        SharePrefsUtils.getInstance().putString(SP_USER_IDCARD_NO, IDCardNo);
    }

    public static void putRealName(String name) {
        SharePrefsUtils.getInstance().putString(SP_USER_REAL_NAME, name);
    }

    public static void putCareer(String career) {
        SharePrefsUtils.getInstance().putString(SP_USER_CAREER, career);
    }



}
