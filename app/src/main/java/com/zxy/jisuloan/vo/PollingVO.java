package com.zxy.jisuloan.vo;

/**
 * Create by Fang ShiXian
 * on 2019/8/23
 */
public class PollingVO extends BaseResponse {

    public Result result;

    public Result getBody() {
        return result;
    }

    @Override
    public String toString() {
        return "PollingVO{" +
                "body=" + result +
                ", error='" + error + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static class Result {

        private String code;
        private String error;

        private String tid;
        private String lastUpdateTime;
        private String status;
        private String phase;
        private String need;
        private String failCode;
        private String reason;
        private Object result;

        public String getTid() {
            return tid;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public String getStatus() {
            return status;
        }

        public String getPhase() {
            return phase;
        }

        public String getNeed() {
            return need;
        }

        public String getFailCode() {
            return failCode;
        }

        public String getReason() {
            return reason;
        }

        public Object getResult() {
            return result;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "code='" + code + '\'' +
                    ", error='" + error + '\'' +
                    ", tid='" + tid + '\'' +
                    ", lastUpdateTime='" + lastUpdateTime + '\'' +
                    ", status='" + status + '\'' +
                    ", phase='" + phase + '\'' +
                    ", need='" + need + '\'' +
                    ", failCode='" + failCode + '\'' +
                    ", reason='" + reason + '\'' +
                    ", result='" + result + '\'' +
                    '}';
        }

        public String getCode() {
            return code;
        }

        public String getError() {
            return error;
        }

    }
}
