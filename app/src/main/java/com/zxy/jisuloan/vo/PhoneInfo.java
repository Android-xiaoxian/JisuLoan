package com.zxy.jisuloan.vo;

/**
 * create by Fang Shixian
 * on 2018/9/28 0028
 */
public class PhoneInfo {
    private String name;
    private String phoneNo;
//    private String sortKey;
//    private int id;
    public PhoneInfo(String name, String number, String sortKey, int id) {
        this.name = name;
        this.phoneNo = number;
//        this.sortKey = sortKey;
//        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

        @Override
    public String toString() {
        return "PhoneInfo{" +
                "name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }
}
