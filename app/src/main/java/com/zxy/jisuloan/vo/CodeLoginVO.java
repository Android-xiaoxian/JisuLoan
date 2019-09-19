package com.zxy.jisuloan.vo;

/**
 * Create by Fang ShiXian
 * on 2019/8/14
 */
public class CodeLoginVO extends BaseResponse {

    private SdUser sdUser;

    public CodeLoginVO.SdUser getSdUser() {
        return sdUser;
    }

    public void setSdUser(CodeLoginVO.SdUser sdUser) {
        this.sdUser = sdUser;
    }

    @Override
    public String toString() {
        return "CodeLoginVO{" +
                "sdUser=" + sdUser +
                ", error='" + error + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class SdUser {
        private String createTime;
        private String deviceNo;
        private String enable;
        private String id;
        private String mobilePhone;
        private String userName;
        private String contactInfoStatus;//联系人认证状态
        private String ipAddress;//ip地址
        private String realInfoStatus;//实名认证状态
        private String userInfoStatus;//个人信息状态
        private String operatorInfoStatus;//运营商认证状态

        public String getOperatorInfoStatus() {
            return operatorInfoStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public String getEnable() {
            return enable;
        }

        public String getId() {
            return id;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public String getUserName() {
            return userName;
        }

        public String getContactInfoStatus() {
            return contactInfoStatus;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getRealInfoStatus() {
            return realInfoStatus;
        }

        public String getUserInfoStatus() {
            return userInfoStatus;
        }

        @Override
        public String toString() {
            return "SdUser{" +
                    "createTime='" + createTime + '\'' +
                    ", deviceNo='" + deviceNo + '\'' +
                    ", enable='" + enable + '\'' +
                    ", id='" + id + '\'' +
                    ", mobilePhone='" + mobilePhone + '\'' +
                    ", userName='" + userName + '\'' +
                    ", contactInfoStatus='" + contactInfoStatus + '\'' +
                    ", ipAddress='" + ipAddress + '\'' +
                    ", realInfoStatus='" + realInfoStatus + '\'' +
                    ", userInfoStatus='" + userInfoStatus + '\'' +
                    ", operatorInfoStatus='" + operatorInfoStatus + '\'' +
                    '}';
        }
    }
}
