package com.zxy.jisuloan.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 倒计时工具类
 * 
 * @author jy qiu
 * 
 */
public class TimeCount extends CountDownTimer {
	private TextView mBtn;
	private String title;

	public TimeCount(long millisInFuture, long countDownInterval, TextView btn,
                     String title) {
		super(millisInFuture, countDownInterval);
		this.mBtn = btn;
		this.title = title;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		mBtn.setText(millisUntilFinished / 1000 + "秒后重新获取");
		mBtn.setTextColor(Color.parseColor("#999999"));
		mBtn.setEnabled(false);

	}

	@Override
	public void onFinish() {
		mBtn.setTextColor(Color.parseColor("#F8984E"));
		mBtn.setText(title);
		mBtn.setEnabled(true);
	}

}
