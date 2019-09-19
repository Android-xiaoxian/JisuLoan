package com.zxy.jisuloan.net.moxie.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
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

/**
 * 魔蝎测试接口，仅供参考
 */
public class HttpRequestUtils {
    private static OkHttpClient okHttpClient;


    public static OkHttpClient getOkhttp() {
        if (okHttpClient == null) {
            OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
            okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
            okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS);
            okhttpBuilder.readTimeout(60, TimeUnit.SECONDS);
            okhttpBuilder.addInterceptor(new HttpLoggingInterceptor(new MyLog()).setLevel(HttpLoggingInterceptor.Level.BODY));
            okHttpClient = okhttpBuilder.build();
        }

        return okHttpClient;
    }

    public static OkHttpClient getOkhttp2() {
//        if (okHttpClient == null) {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.addInterceptor(new HttpLoggingInterceptor(new MyLog()).setLevel(HttpLoggingInterceptor.Level.BODY));
//            okHttpClient = okhttpBuilder.build();
//        }

        return okhttpBuilder.build();
    }


    /**
     * 请求身份识别的接口
     *
     * @param url
     * @param fid
     * @param callback
     * @param isFront
     */
    public static void postOCRInfo(String url, String fid, NetworkCallback callback, boolean isFront) {
        ApiParameterList apiParameterList;
        if (isFront) {
            // 请求正面身份证信息接口
            apiParameterList = getCommonParameter("moxie.api.risk.orc.idcard.front");
        } else {
            apiParameterList = getCommonParameter("moxie.api.risk.orc.idcard.back");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("trans_id", UUID.randomUUID().toString());
            jsonObject.put("fid", fid);
            apiParameterList.with("biz_content", jsonObject.toString());
            postRequest(url, apiParameterList, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 请求活体检测接口
     *
     * @param url
     * @param fid
     * @param callback
     */
    public static void postSelfVerification(String url, String fid, NetworkCallback callback) {
        ApiParameterList apiParameterList;
        apiParameterList = getCommonParameter("moxie.api.risk.self-idcard.verification");
        JSONObject jsonObject = new JSONObject();
//         # error 填写身份证号码,并删除此行
        String idCard = "";
//         # error 填写姓名,并删除此行
        String name = "";
        try {
            jsonObject.put("id_card", idCard);
            jsonObject.put("name", name);
            jsonObject.put("trans_id", UUID.randomUUID().toString());
            jsonObject.put("fid", fid);
            apiParameterList.with("biz_content", jsonObject.toString());
            if (idCard.isEmpty() || name.isEmpty()) {
                return;
            } else {
                postRequest(url, apiParameterList, callback);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void postRequest(String url, ApiParameterList apiParameters, final NetworkCallback callback) {
        okHttpClient = getOkhttp();

        if (url == null || "".equals(url)) {
            sendFailResult(callback, 404, "URL无效");
            return;
        }

        Request.Builder builder = new Request.Builder();
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded;charset:utf-8");
        FormBody.Builder formBuild = new FormBody.Builder();
        for (ApiParameter param : apiParameters) {
            formBuild.add(param.name, String.valueOf(param.value));
        }
        RequestBody requestBody = formBuild.build();
        Request request = builder.url(url)
                .post(requestBody)
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

    public static void postFileRequest(String url, File file,int type, final NetworkCallback callback){
        okHttpClient=getOkhttp();

        if (url == null || "".equals(url)) {
            sendFailResult(callback, 404, "URL无效");
            return;
        }

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody fileBody = RequestBody.create(mediaType,file);
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("type",String.valueOf(type))
                .build();

        Request request=new Request.Builder().url(url)
                .post(requestBody)
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


    public static void postFileRequest(String url, File[] files, final NetworkCallback callback) {
        okHttpClient = getOkhttp();

        if (url == null || "".equals(url)) {
            sendFailResult(callback, 404, "URL无效");
            return;
        }
//        MultipartBody

        MultipartBody.Part[] parts = new MultipartBody.Part[files.length];

        for (int i = 0; i < files.length; i++) {
            parts[i] = MultipartBody.Part.createFormData("file" + i, files[i].getName(),
                    RequestBody.create(MediaType.parse("image/png"), files[i]));
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart("idCardFace", files[0].getName(),parts[0].body());
        builder.addFormDataPart("idCardBack", files[1].getName(),parts[1].body());
        builder.addFormDataPart("idCardPortrait", files[2].getName(),parts[2].body());
        builder.addFormDataPart("idCardFace2", files[3].getName(),parts[3].body());
        builder.addFormDataPart("idCardBack2", files[4].getName(),parts[4].body());

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


    private static void dealRequestResponse(Response response, final NetworkCallback callback) throws IOException {
        if (response != null) {
            int code = response.code();
            if (code == 200) {
                String result = response.body().string();

                try {
                    JSONObject resultObj = new JSONObject(result);
//                    boolean success = resultObj.getBoolean("success");
//                    String resultCode = resultObj.getString("code");
//                    String msg = resultObj.getString("msg");
                    String error = resultObj.getString("error");
                    if ("0".equals(error)) {
                        sendSuccessResult(callback, result);
                    } else {
                        sendFailResult(callback, Integer.parseInt(error), "msg");
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


    /**
     * 请求日志拦截器
     */
    public static class MyLog implements HttpLoggingInterceptor.Logger {

        @Override
        public void log(String message) {
            Log.i("moxieNetwork==", message);
        }
    }


    /**
     * 公共参数
     *
     * @param methodName
     * @return
     */
    private static ApiParameterList getCommonParameter(String methodName) {
        ApiParameterList apiParameterList = ApiParameterList.create();
        apiParameterList.with("version", "1.0");
        apiParameterList.with("method", methodName);
        apiParameterList.with("sign_type", "TOKEN");
        apiParameterList.with("timestamp", System.currentTimeMillis());
        return apiParameterList;
    }

}
