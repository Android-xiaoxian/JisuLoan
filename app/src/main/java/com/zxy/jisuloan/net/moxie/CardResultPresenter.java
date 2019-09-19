package com.zxy.jisuloan.net.moxie;

import android.util.Log;

import com.zxy.jisuloan.net.moxie.network.HttpRequestUtils;
import com.zxy.jisuloan.net.moxie.network.NetworkCallback;

import java.io.File;

public class CardResultPresenter {
    private static final String TAG = "MXCardResultPresenter";


    public CardResultPresenter() {
        Log.i(TAG, "MXCardResultPresenter" + "***online");
    }


    // 调用上传图片文件接口(多个文件)
    public void uploadPicture(String url, File[] files, final INetworkCallback callback) {
        if (files != null) {
            HttpRequestUtils.postFileRequest(url, files, new NetworkCallback() {
//            HttpRequestUtils.postFileRequest(BaseApi.KEY_BASE_URL,file,type,new NetworkCallback(){

                @Override
                public void completed(String response) {
                    callback.callback(response);
                }

                @Override
                public void failed(int httpStatusCode, String error) {
                    super.failed(httpStatusCode, error);
                    callback.fail(error);
                }
            });
        }
    }

    // 调用上传图片文件接口（单个文件）
    public void uploadPicture(String url,File file,int type,final INetworkCallback callback){
        if (file!=null){
            HttpRequestUtils.postFileRequest(url,file,type,new NetworkCallback(){

                @Override
                public void completed(String response) {
                    callback.callback(response);
                }

                @Override
                public void failed(int httpStatusCode, String error) {
                    super.failed(httpStatusCode, error);
                    callback.fail(error);
                }
            });
        }
    }





    public interface INetworkCallback {
        void callback(String result);

        void fail(String error);
    }

}
