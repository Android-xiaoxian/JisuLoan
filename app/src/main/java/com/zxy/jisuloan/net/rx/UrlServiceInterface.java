package com.zxy.jisuloan.net.rx;

import com.zxy.jisuloan.net.bean.BaseEntity;
import com.zxy.jisuloan.net.bean.Bean;
import com.zxy.jisuloan.vo.BaseResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hs on 2017/8/10.
 */

public interface UrlServiceInterface {

    String SMS_URL = "servlet/current/user/UserAction?function=AddMessageList";
    String data = "data";
    /**
     * 登录
     * 添加参数 mobile
     * 添加参数 verifyCode
     */
    @POST(SMS_URL)
    Observable<BaseEntity<BaseResponse>> addSMSData(@Query("userId") String userId);

    @POST(SMS_URL)
    Call<BaseEntity<Bean>> goLoginCall();


}
