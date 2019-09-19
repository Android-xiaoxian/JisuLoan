package com.zxy.jisuloan.vo;

import com.zxy.jisuloan.net.BaseApi;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/8/26
 */
public class ProductVo extends BaseResponse {

    public List<Products> data;

    public List<Products> getProductList() {
        return data;
    }

    public void setProductList(List<Products> productList) {
        this.data = productList;
    }

    @Override
    public String toString() {
        return "ProductVo{" +
                "data=" + data +
                ", error='" + error + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static class Products implements Serializable {
        public String amountmax;//最高额度
        public String amountmin;//最低额度
        public String borrowperiodmax;//最大期限
        public String borrowperiodmin;//最小期限
        public String loanplatformname;//平台名称
        public int clickcount;//申请人数
        public String borrowperiodunit;//期限单位
        public String platformurl;//平台链接
        public String avgloantime;//评价放款时间

        public String id;//
        public String productid;//
        public float interestrate;//利率
        public String avgloanmoney;//平均放款金额

        public String price;//
        public int priority;//
        public String settle;//
        public String createdate;//创建时间
        public String status;//状态
        public String platformimg;//平台logo

        public String getAmountmax() {
            return amountmax;
        }

        public String getAmountmin() {
            return amountmin;
        }

        public String getBorrowperiodmax() {
            return borrowperiodmax;
        }

        public String getBorrowperiodmin() {
            return borrowperiodmin;
        }

        public String getLoanplatformname() {
            return loanplatformname;
        }

        public String getId() {
            return id == null ? productid : id;
        }

        public float getInterestrate() {
            return interestrate;
        }

        public String getPrice() {
            return price;
        }

        public String getSettle() {
            return settle;
        }

        public String getCreatedate() {
            return createdate;
        }

        public String getStatus() {
            return status;
        }

        public String getPlatformimg() {
            return BaseApi.KEY_BASE_URL + "/cms/logo/" + platformimg;
        }

        public int getClickcount() {
            return clickcount;
        }

        public int getPriority() {
            return priority;
        }

        public void setClickcount(int clickcount) {
            this.clickcount = clickcount;
        }

        public String getBorrowperiodunit() {
            return borrowperiodunit;
        }

        public String getPlatformurl() {
            return platformurl;
        }

        public String getAvgloantime() {
            return avgloantime;
        }


        public String getAvgloanmoney() {
            return avgloanmoney;
        }

        @Override
        public String toString() {
            return "Products{" +
                    "amountmax='" + amountmax + '\'' +
                    ", amountmin='" + amountmin + '\'' +
                    ", borrowperiodmax='" + borrowperiodmax + '\'' +
                    ", borrowperiodmin='" + borrowperiodmin + '\'' +
                    ", loanplatformname='" + loanplatformname + '\'' +
                    ", clickcount='" + clickcount + '\'' +
                    ", borrowperiodunit='" + borrowperiodunit + '\'' +
                    ", platformurl='" + platformurl + '\'' +
                    ", avgloantime='" + avgloantime + '\'' +
                    ", id='" + id + '\'' +
                    ", productid='" + productid + '\'' +
                    ", interestrate='" + interestrate + '\'' +
                    ", avgloanmoney='" + avgloanmoney + '\'' +
                    ", price='" + price + '\'' +
                    ", priority='" + priority + '\'' +
                    ", settle='" + settle + '\'' +
                    ", createdate='" + createdate + '\'' +
                    ", status='" + status + '\'' +
                    ", platformimg='" + platformimg + '\'' +
                    '}';
        }

    }

    /**
     * 利率比较
     */
    public static class MyComparator implements Comparator<Products> {

        @Override
        public int compare(Products o1, Products o2) {
            return (int) Math.ceil(o1.interestrate - o2.interestrate);
        }

    }

    /**
     * 智能排序
     * 优先级比较
     */
    public static class MyComparator2 implements Comparator<Products> {

        @Override
        public int compare(Products o1, Products o2) {
            return o1.priority - o2.priority;
        }

    }

    /**
     * 高通过率排序
     */
    public static class MyComparator3 implements Comparator<Products> {

        @Override
        public int compare(Products o1, Products o2) {
            return o2.clickcount - o1.clickcount;
        }

    }
}
