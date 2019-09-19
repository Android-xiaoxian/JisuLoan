package com.zxy.jisuloan.utils;

import com.alibaba.fastjson.JSONArray;
import com.zxy.jisuloan.net.BaseApi;
import com.zxy.jisuloan.net.moxie.network.HttpRequestUtils;
import com.zxy.jisuloan.net.moxie.network.NetworkCallback;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpUtils {

    // 调用上传图片文件接口
    public static void uploadPicture(String userId, File[] files, Map<String, String> data, final INetworkCallback callback) {
        if (files != null) {
            postFileRequest(BaseApi.KEY_BASE_URL + "/servlet/current/user/AuthOcrAction?function=SendIdCard", files, data, new NetworkCallback() {

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

    private static OkHttpClient okHttpClient;


    public static OkHttpClient getOkhttp() {
        if (okHttpClient == null) {
            OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
            okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
            okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS);
            okhttpBuilder.readTimeout(60, TimeUnit.SECONDS);
            okhttpBuilder.addInterceptor(new HttpLoggingInterceptor(new HttpRequestUtils.MyLog()).setLevel(HttpLoggingInterceptor.Level.BODY));
            okHttpClient = okhttpBuilder.build();
        }

        return okHttpClient;
    }

    public static void postFileRequest(String url, File[] files, Map<String, String> data, final NetworkCallback callback) {
        okHttpClient = getOkhttp();

        if (url == null || "".equals(url)) {
            sendFailResult(callback, 404, "URL无效");
            return;
        }
        MultipartBody.Part[] parts = new MultipartBody.Part[files.length];

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < files.length; i++) {
            parts[i] = MultipartBody.Part.createFormData("file" + i, files[i].getName(),
                    RequestBody.create(MediaType.parse("image/jpg"), files[i]));
            if (i == 0)
                builder.addFormDataPart("idCardFace", files[0].getName(), parts[0].body());
            if (i == 1)
                builder.addFormDataPart("idCardBack", files[1].getName(), parts[1].body());
            if (i == 2)
                builder.addFormDataPart("idCardPortrait", files[2].getName(), parts[2].body());
//            if (i == 3)
//                builder.addFormDataPart("Eye", files[3].getName(), parts[3].body());
//            if (i == 4)
//                builder.addFormDataPart("Mouth", files[4].getName(), parts[4].body());
//            if (i == 5)
//                builder.addFormDataPart("ShakingHead", files[5].getName(), parts[5].body());
        }

        for (Map.Entry<String, String> entry : data.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
            MyLog.e("test",entry.getKey()+"="+ entry.getValue());
        }
//        builder.addFormDataPart("data", data);

        Request request = new Request.Builder().url(url)
                .post(builder.build())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                sendFailResult(callback, 404, "网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dealRequestResponse(response, callback);
            }
        });


    }


    // 调用上传JsonArray接口
    public static void uploadJsonArray(String url, String name, JSONArray array, final INetworkCallback callback) {
        if (array != null) {
            postJsonArray(url, name, array, new NetworkCallback() {

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

    public static void postJsonArray(String url, String name, JSONArray array, final NetworkCallback callback) {
        OkHttpClient okHttpClient = getOkhttp2();

        if (url == null || "".equals(url)) {
            sendFailResult(callback, 404, "URL无效");
            return;
        }
        try {
            //post方式提交的数据
            FormBody formBody = new FormBody.Builder()
                    .add(name, URLEncoder.encode(array.toJSONString(), "UTF-8"))
                    .build();

            Request request = new Request.Builder().url(url)
                    .post(formBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    sendFailResult(callback, 404, "网络请求失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    dealRequestResponse(response, callback);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static void dealRequestResponse(Response response, final NetworkCallback callback) throws IOException {
        if (response != null) {
            int code = response.code();
            if (code == 200) {
                String result = response.body().string();
                try {
                    org.json.JSONObject resultObj = new org.json.JSONObject(result);

                    String error = resultObj.getString("error");
                    if ("0".equals(error)) {
                        sendSuccessResult(callback, result);
                    } else {
//                        sendFailResult(callback, Integer.parseInt(resultCode), msg);
                        sendFailResult(callback, Integer.parseInt(error), "没有回传msg");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                sendFailResult(callback, code, response.message());
            }
        } else {
            sendFailResult(callback, 0, "");
        }
    }

    /**
     * 不复用okHttpClient2
     *
     * @return
     */
    private static OkHttpClient getOkhttp2() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.addInterceptor(new HttpLoggingInterceptor(new HttpRequestUtils.MyLog()).setLevel(HttpLoggingInterceptor.Level.BODY));
//        okHttpClient2 = okhttpBuilder.build();
        return okhttpBuilder.build();
    }


    public static <T> void sendFailResult(final NetworkCallback callback, final int errorCode, final String errorString) {
        if (callback != null) {
            callback.failed(errorCode, errorString);
        }
    }

    public static void sendSuccessResult(final NetworkCallback callback, final String response) {

        if (callback != null) {
            callback.completed(response);
        }
    }


    public interface INetworkCallback {
        void callback(String result);

        void fail(String error);
    }
}
