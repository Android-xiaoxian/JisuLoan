package com.zxy.jisuloan.utils;

import android.util.Log;

import java.util.LinkedList;


/**
 * Activity队列管理类
 */
public class ActivityList {
	private static LinkedList<BaseActivity> list = new LinkedList<BaseActivity>();

	/**
	 * 把Activity添加到队列里
	 * @param a
	 */
	public static void addActiviy(BaseActivity a) {
		if (!list.contains(a)) {
			list.add(a);
		}
	}

	/**
	 * 获取队列最后的一个Activity
	 * @return
	 */
	public static BaseActivity getLastActivity() {
		return list.getLast();
	}

	/**
	 * 移除队列中知道的Activity
	 * @param a
	 */
	public static void removeActivity(BaseActivity a) {
		if (!list.isEmpty()) {
			list.remove(a);
		}
	}

	/**
	 * 退出，结束程序的所有界面
	 */
	public static void tuichu() {
		int lenth = list.size();
		for (int i = 0; i < lenth; i++) {
			try {
				list.get(i).finish();
			} catch (Exception e) {
			}
		}
		if (list.size()>0) {
			list.clear();
		}
		int l = list.size();
		Log.i("wjj", l + "");
	}

	/**
	 * 退出登录，留下一个登录界面
	 */
	public static void existLogin() {
		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() - 1) {
				list.get(i).finish();
			}
		}
	}
}
