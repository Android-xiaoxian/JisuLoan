package com.zxy.jisuloan.utils;

import android.app.Application;
import android.content.Context;

import com.rpc.manager.RPCSDKManager;


public class BaseApplication extends Application {
	private static Context mContext;
	private static BaseApplication mApplication;

	public synchronized static BaseApplication getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		mContext = this.getApplicationContext();
		mApplication = this;
		super.onCreate();
		init();
	}

	private void init() {

		RPCSDKManager.getInstance().init(this, Config.APPID_253, Config.APPKEY_253,Config.LICENSE_ID_253);
	}

	public static Context getAppContext() {
		return mContext;
	}

}
