package com.zxy.jisuloan.vo;

/**
 * Create by Fang ShiXian
 * on 2019/8/21
 */
public class UserStatusVO extends BaseResponse {


    private String realInfoStatus;//实名认证状态
    private String userInfoStatus;//用户信息认证状态
    private String contactInfoStatus;//联系人认证状态
    private String operatorInfoStatus;//运营商认证状态
    private String cardnum;//身份证
    private String career;//职业类型
    private String name;//姓名


    public String getRealInfoStatus() {
        return realInfoStatus;
    }

    public String getUserInfoStatus() {
        return userInfoStatus;
    }


    public String getContactInfoStatus() {
        return contactInfoStatus;
    }


    public String getOperatorInfoStatus() {
        return operatorInfoStatus;
    }

    public String getCardNum() {
        return cardnum;
    }

    public String getCareer() {
        return career;
    }

    @Override
    public String toString() {
        return "UserStatusVO{" +
                "realInfoStatus='" + realInfoStatus + '\'' +
                ", userInfoStatus='" + userInfoStatus + '\'' +
                ", contactInfoStatus='" + contactInfoStatus + '\'' +
                ", operatorInfoStatus='" + operatorInfoStatus + '\'' +
                ", cardNum='" + cardnum + '\'' +
                ", career='" + career + '\'' +
                ", name='" + name + '\'' +
                ", error='" + error + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

}
