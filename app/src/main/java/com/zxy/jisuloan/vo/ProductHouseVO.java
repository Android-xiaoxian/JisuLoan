package com.zxy.jisuloan.vo;

import java.io.Serializable;

/**
 * Create by Fang ShiXian
 * on 2019/9/10
 */
public class ProductHouseVO implements Serializable {
    private String amountmax;//最大金额
    private String amountmin;//最小金额
    private String avgloanmoney;//平均放款金额
    private String borrowperiodmax;//借期最大值
    private String borrowperiodmin;//借期最小值
    private String borrowperiodunit;//借期单位
    private String collectdate;//创建日日期
    private String iconpath;//logo链接
    private String id;//id
    private String interestrate;//利率
    private String productid;//产品id
    private String productname;//产品名称


    public String getAmountmax() {
        return amountmax;
    }

    public String getAmountmin() {
        return amountmin;
    }

    public String getAvgloanmoney() {
        return avgloanmoney;
    }

    public String getBorrowperiodmax() {
        return borrowperiodmax;
    }

    public String getBorrowperiodmin() {
        return borrowperiodmin;
    }

    public String getBorrowperiodunit() {
        return borrowperiodunit;
    }

    public String getCollectdate() {
        return collectdate;
    }

    public String getIconpath() {
        return iconpath;
    }

    public String getId() {
        return id;
    }

    public String getInterestrate() {
        return interestrate;
    }

    public String getProductid() {
        return productid;
    }

    public String getProductname() {
        return productname;
    }

    @Override
    public String toString() {
        return "ProductHouseVO{" +
                "amountmax='" + amountmax + '\'' +
                ", amountmin='" + amountmin + '\'' +
                ", avgloanmoney='" + avgloanmoney + '\'' +
                ", borrowperiodmax='" + borrowperiodmax + '\'' +
                ", borrowperiodmin='" + borrowperiodmin + '\'' +
                ", borrowperiodunit='" + borrowperiodunit + '\'' +
                ", collectdate='" + collectdate + '\'' +
                ", iconpath='" + iconpath + '\'' +
                ", id='" + id + '\'' +
                ", interestrate='" + interestrate + '\'' +
                ", productid='" + productid + '\'' +
                ", productname='" + productname + '\'' +
                '}';
    }
}
