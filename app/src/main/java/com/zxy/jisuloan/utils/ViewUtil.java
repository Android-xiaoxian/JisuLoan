package com.zxy.jisuloan.utils;


/**
 * Create by Fang ShiXian
 * on 2019/8/13
 */
public class ViewUtil {
    /**
     * dp转换成px
     */
    public static int dp2px( float dpValue){
        float scale=BaseApplication.getInstance().getAppContext().getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }


}
