package com.zxy.jisuloan.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zxy.jisuloan.activity.DistinctPickerActivity;
import com.zxy.jisuloan.activity.UserInfoActivity;
import com.zxy.jisuloan.fragmrnt.HomeLoginedFragment;


/**
 * 百度定位工具类
 * create by Fang Shixian
 * on 2019/2/15 0015
 */
public class LocateUtil {

    private Activity activity;
    private int type = -1;
    private LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();//自定义方法
    public MyLocationListener2 myListener2 = new MyLocationListener2();//自定义方法

    public LocateUtil(Activity activity) {
        this.activity = activity;
    }


    public void startLoacte(int type) {
        //声明LocationClient类
        mLocationClient = new LocationClient(activity.getApplicationContext());
        //注册监听函数
        if (type == 1) {//主页静默定位
            mLocationClient.registerLocationListener(myListener2);
        } else {//主动开启定位进入定位选择页面
            mLocationClient.registerLocationListener(myListener);
        }
        LocationClientOption option = new LocationClientOption();
        int span = 1000 * 3000;
        option.setScanSpan(span);//设置发起定位请求的间隔
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                MyLog.e("定位测试", "定位失败");
                return;
            }
            int locType = location.getLocType();
            MyLog.e("定位测试", "定位结果locType = " + locType);
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            Intent intent = new Intent(activity, DistinctPickerActivity.class);
            intent.putExtra(Config.PROVINCE, province);
            intent.putExtra("city", city);
            intent.putExtra("district", district);
            activity.startActivityForResult(intent, Config.REQUEST_CODE_1);
            UserInfoActivity.locating = false;
            HomeLoginedFragment.locating = false;
        }
    }

    public class MyLocationListener2 extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                MyLog.e("定位测试", "定位失败");
                unRegister();
                return;
            }
            int locType = location.getLocType();
            MyLog.e("定位测试", "定位结果locType = " + locType);//打印定位情况
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            if (!TextUtils.isEmpty(city)) {
                HomeLoginedFragment.setArea(city);
            }
            HomeLoginedFragment.locating = false;
            if (mLocationClient != null) {
                mLocationClient.unRegisterLocationListener(myListener2);
            }
        }
    }

    /**
     * 取消百度定位组件的注册
     */
    public void unRegister() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
        }
    }
}
