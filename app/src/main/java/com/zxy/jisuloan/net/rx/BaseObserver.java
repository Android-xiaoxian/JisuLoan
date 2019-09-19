package com.zxy.jisuloan.net.rx;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.zxy.jisuloan.net.bean.BaseEntity;
import com.zxy.jisuloan.utils.MyLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by hs on 2017/8/10.
 */

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {
    private Context mContext;
    private ProgressDialog mDialog;
    private Disposable mDisposable;


    public BaseObserver(Context context) {
        mContext = context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(BaseEntity<T> value) {
        if(value==null){
            MyLog.e("onNext","BaseEntity is null");
            Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_LONG).show();
            return;
        }
        if (value.isSuccess()) {
            T t = value.resultData;
            if(t!=null){
                onHandleSuccess(t);
            }

        } else {
            onHandleError(value.getCode(), value.getMessage());
        }
        onHandle(value);
    }

    @Override
    public void onError(Throwable e) {
        MyLog.d(" ", "error:" + e.toString());

        Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete() {
        MyLog.d(" ", "onComplete");

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public abstract void onHandleSuccess(T t);
    public void onHandle(BaseEntity<T> baseEntity){

    };
    public void onHandleError(String code, String message) {
        Toast.makeText(mContext, message + code, Toast.LENGTH_LONG).show();
    }
}
