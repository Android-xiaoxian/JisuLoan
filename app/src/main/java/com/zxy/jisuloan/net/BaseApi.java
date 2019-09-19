package com.zxy.jisuloan.net;


import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zxy.jisuloan.BuildConfig;
import com.zxy.jisuloan.utils.BaseApplication;
import com.zxy.jisuloan.utils.LoadDialog;
import com.zxy.jisuloan.utils.MyLog;
import com.zxy.jisuloan.utils.NetUtils;
import com.zxy.jisuloan.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * create by Fang Shixian
 * on 2018/8/8 0008
 */
public class BaseApi {

    private static final int TIMEOUT_READ = 5;
    private static final int TIMEOUT_CONNECTION = 5;
    private static OkHttpClient mOkHttpClient;

    public static String KEY_BASE_URL = "http://192.168.50.169:8080"; //线下测试地址
//    public static String KEY_BASE_URL = "http://7rsk8e.natappfree.cc"; //线下测试地址
//    public static String KEY_BASE_URL = "http://www.recycling-community.com"; //线上测试地址


    private static OkHttpClient genericClient() {

        if (mOkHttpClient != null) {
            return mOkHttpClient;
        }

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level level = BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.HEADERS :
                HttpLoggingInterceptor.Level.NONE;
        loggingInterceptor.setLevel(level);
        ClearableCookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(),
                new SharedPrefsCookiePersistor(BaseApplication.getAppContext()));


        return mOkHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .build();
    }

    // 创建网络接口请求实例
    public static <T> T createApi(Class<T> service) {
        final String url = KEY_BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(genericClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }


    // 修改访问超时时间为60秒

    /**
     * 创建post请求
     * 并且不复用 mOkHttpClient
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T createApi2(Class<T> service) {
        final String url = KEY_BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(genericClient2())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }



    /**
     * 不复用OkHttpClient
     *
     * @return
     */
    private static OkHttpClient genericClient2() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level level = BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.HEADERS :
                HttpLoggingInterceptor.Level.NONE;
        loggingInterceptor.setLevel(level);
        ClearableCookieJar cookieJar = new PersistentCookieJar(
                new SetCookieCache(),
                new SharedPrefsCookiePersistor(BaseApplication.getAppContext()));


        return new OkHttpClient.Builder().cookieJar(cookieJar)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .build();
    }


    /**
     * 发起网络请求，请求结束自动取消LoadDialog
     *
     * @param observable
     * @param listener
     * @param <T>
     */
    public static <T> void request(Observable<T> observable,
                                   final IResponseListener<T> listener) {
        if (!NetUtils.isConnect(BaseApplication.getAppContext())) {
            ToastUtils.showToast("网络不可用,请检查网络");
            if (listener != null) {
                listener.onFail();
                LoadDialog.dimiss();
            }
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                                   ToastUtils.showToast("服务器错误，请稍后重试");
                                   if (listener != null) {
                                       listener.onFail();
                                       LoadDialog.dimiss();
                                   }
                               }

                               @Override
                               public void onComplete() {

                               }

                               @Override
                               public void onSubscribe(Disposable disposable) {

                               }

                               @Override
                               public void onNext(T data) {
                                   if (listener != null) {
                                       LoadDialog.dimiss();
                                       MyLog.e("test", data.toString());
                                       listener.onSuccess(data);

                                   }
                               }
                           }
                );
    }

    /**
     * 发起网络请求，不自动取消LoadDialog
     *
     * @param observable
     * @param listener
     * @param <T>
     */
    public static <T> void request2(Observable<T> observable,
                                    final IResponseListener<T> listener) {
        if (!NetUtils.isConnect(BaseApplication.getAppContext())) {
            ToastUtils.showToast("网络不可用,请检查网络");
            if (listener != null) {
                listener.onFail();
            }
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {

                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                                   ToastUtils.showToast("服务器错误，请稍后重试");
                                   if (listener != null) {
                                       listener.onFail();
                                   }
                               }

                               @Override
                               public void onComplete() {

                               }

                               @Override
                               public void onSubscribe(Disposable disposable) {

                               }

                               @Override
                               public void onNext(T data) {
                                   if (listener != null) {
                                       MyLog.e("test", data.toString());
                                       listener.onSuccess(data);

                                   }
                               }
                           }
                );
    }


    public interface IResponseListener<T> {
        void onSuccess(T data);

        void onFail();
    }
}
