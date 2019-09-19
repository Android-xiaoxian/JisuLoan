package com.zxy.jisuloan.vo;

/**
 * Create by Fang ShiXian
 * on 2019/9/9
 */
public class AppInfoVO {
    private String versionname;//版本名1.0.1
    private int versioncode;// 1
    private String downloadurl;// 新版本安装包下载链接
    private String description;//更新内容

    public String getVersionname() {
        return versionname;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "AppInfoVO{" +
                "versionname='" + versionname + '\'' +
                ", versioncode=" + versioncode +
                ", downloadurl='" + downloadurl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
