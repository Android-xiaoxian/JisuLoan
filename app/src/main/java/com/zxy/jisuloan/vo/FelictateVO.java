package com.zxy.jisuloan.vo;

/**
 * Create by Fang ShiXian
 * on 2019/9/12
 */
public class FelictateVO {

    private String id;
    private String money;
    private String name;
    private String number;
    private String region;

    public String getId() {
        return id;
    }

    public String getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getRegion() {
        return region;
    }

    @Override
    public String toString() {
        return "FelictateVO{" +
                "id='" + id + '\'' +
                ", money='" + money + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
