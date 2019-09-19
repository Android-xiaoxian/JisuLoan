package com.zxy.jisuloan.net.moxie.data;


public class ResUploadFile {
    private boolean success;
    private int code;
    private String msg;
    private FileData data;


    public boolean isSuccess() {
        return success;
    }


    public void setSuccess(boolean success) {
        this.success = success;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FileData getData() {
        return data;
    }

    public void setData(FileData data) {
        this.data = data;
    }


    public class FileData{
        private String fid;

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        @Override
        public String toString() {
            return "FileData{" +
                    "fid='" + fid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ResUploadFile{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }


}
