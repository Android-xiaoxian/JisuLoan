package com.zxy.jisuloan.vo;

/**
 * Create by Fang ShiXian
 * on 2019/8/22
 */
public class YYSResuleVO extends BaseResponse{

    private Result result;

    public Result getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "YYSResuleVO{" +
                "result=" + result +
                ", error='" + error + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public class Result{
        private String tid;
        private String status;
        private String phase;
        private String type;
        private String lastUpdateTime;
        private String code;
        private String error;

        @Override
        public String toString() {
            return "Result{" +
                    "tid='" + tid + '\'' +
                    ", status='" + status + '\'' +
                    ", phase='" + phase + '\'' +
                    ", type='" + type + '\'' +
                    ", lastUpdateTime='" + lastUpdateTime + '\'' +
                    ", code='" + code + '\'' +
                    ", error='" + error + '\'' +
                    '}';
        }

        public String getCode() {
            return code;
        }

        public String getError() {
            return error;
        }

        public String getPhase() {
            return phase;
        }

        public String getType() {
            return type;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public String getTid() {
            return tid;
        }

        public String getStatus() {
            return status;
        }

    }
}
