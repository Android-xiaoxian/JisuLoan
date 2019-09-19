package com.zxy.jisuloan.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Create by Fang ShiXian
 * on 2019/8/30
 */
public class BrowseRecordsVO extends BaseResponse {

    private List<BrowseRecord> data;

    public List<BrowseRecord> getBrowseRecList() {
        return data;
    }

    @Override
    public String toString() {
        return "BrowseRecordsVO{" +
                "data=" + data +
                ", error='" + error + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static class BrowseRecord extends ProductVo.Products implements Serializable {
        private String itemType;
        private String orderStatus;

        @Override
        public String toString() {
            return "BrowseRecord{" +
                    "createDate='" + createdate + '\'' +
                    ", itemType='" + itemType + '\'' +
                    ", orderStatus='" + orderStatus + '\'' +
                    ", amountmax='" + amountmax + '\'' +
                    ", amountmin='" + amountmin + '\'' +
                    ", borrowperiodmax='" + borrowperiodmax + '\'' +
                    ", borrowperiodmin='" + borrowperiodmin + '\'' +
                    ", loanplatformname='" + loanplatformname + '\'' +
                    ", clickcount='" + clickcount + '\'' +
                    ", borrowperiodunit='" + borrowperiodunit + '\'' +
                    ", platformurl='" + platformurl + '\'' +
                    ", avgloantime='" + avgloantime + '\'' +
                    ", id='" + id + '\'' +
                    ", interestrate='" + interestrate + '\'' +
                    ", avgloanmoney='" + avgloanmoney + '\'' +
                    ", price='" + price + '\'' +
                    ", settle='" + settle + '\'' +
                    ", status='" + status + '\'' +
                    ", platformimg='" + platformimg + '\'' +
                    '}';
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setCreateDate(String createDate) {
            this.createdate = createDate;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getCreateDate() {
            if (createdate == null) {
                return "";
            }
            return createdate;
        }

        public String getItemType() {
            return itemType;
        }

    }
}
