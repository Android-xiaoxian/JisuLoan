package com.zxy.jisuloan.vo;

/**
 * Create by Fang ShiXian
 * on 2019/9/3
 */
public class SMSVO {
    private String address;
    private String body;
    private String date;
    private String type;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SMSVO{" +
                "address='" + address + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
