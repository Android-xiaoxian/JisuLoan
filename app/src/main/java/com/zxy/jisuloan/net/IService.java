package com.zxy.jisuloan.net;

import com.zxy.jisuloan.vo.AppInfoVO;
import com.zxy.jisuloan.vo.BaseResponse;
import com.zxy.jisuloan.vo.BrowseRecordsVO;
import com.zxy.jisuloan.vo.CodeLoginVO;
import com.zxy.jisuloan.vo.FelictateVO;
import com.zxy.jisuloan.vo.PollingVO;
import com.zxy.jisuloan.vo.ProductDetailVO;
import com.zxy.jisuloan.vo.ProductVo;
import com.zxy.jisuloan.vo.QuestionClassVO;
import com.zxy.jisuloan.vo.QuestionVO;
import com.zxy.jisuloan.vo.UserStatusVO;
import com.zxy.jisuloan.vo.YYSResuleVO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Create by Fang ShiXian
 * on 2019/8/14
 */
public interface IService {

    @POST("/servlet/current/user/VersionAction?function=getVersion")
    Observable<AppInfoVO> download();

    //获取登录或重置密码的验证码（登录flag=0,重置flag=1）
    @POST("/servlet/current/user/UserAction?function=SendMsg")
    Observable<BaseResponse> getLoginCode(@Query("mobile") String mobilePhone, @Query("flag") int flag,
                                          @Query("deviceNo") String deviceNo);

    //验证码登录
    @POST("/servlet/current/user/UserAction?function=RegOrLogin&rating=3")
    Observable<CodeLoginVO> codeLogin(@Query("mobilePhone") String mobilePhone,
                                      @Query("verCode") String verCode, @Query("deviceNo") String deviceNo);

    //密码登录
    @POST("/servlet/current/user/UserAction?function=Login")
    Observable<CodeLoginVO> loginByPwd(@Query("mobilePhone") String mobilePhone, @Query("password") String password,
                                       @Query("deviceNo") String deviceNo);

    //重置密码校验验证码
    @POST("/servlet/current/user/UserAction?function=RegOrLogin&rating=3")
    Observable<CodeLoginVO> resetPwdCode(@Query("mobilePhone") String mobilePhone,
                                         @Query("verCode") String verCode, @Query("deviceNo") String deviceNo);

    //重置密码
    @POST("/servlet/current/user/UserAction?function=RePassword")
    Observable<BaseResponse> resetPwd(@Query("mobilePhone") String mobilePhone, @Query("password") String password,
                                      @Query("verCode") String verCode, @Query("deviceNo") String deviceNo);


    //添加用户信息
    @POST("/servlet/current/user/UserAction?function=AddUserInfo")
    Observable<BaseResponse> addUserInfo(@Query("userId") String userId, @Query("education") int education,
                                         @Query("career") int career, @Query("monAvgIncome") int monAvgIncome,
                                         @Query("expectBorrow") int expectBorrow, @Query("residentAddress") String residentAddress,
                                         @Query("company") String company, @Query("workAddress") String workAddress);

    //查询用户认证状态
    @POST("/servlet/current/user/UserAction?function=CheckComplateInfo")
    Observable<UserStatusVO> getUserAuthStatus(@Query("userId") String userId);


    //添加联系人信息
    @POST("/servlet/current/user/UserAction?function=AddUserContact")
    Observable<BaseResponse> addContactInfo(@Query("userId") String userId, @Query("maritalStatus") String maritalStatus,
                                            @Query("kinship") String kinship,
                                            @Query("name") String name, @Query("phone") String phone,
                                            @Query("name1") String name1, @Query("phone1") String phone1,
                                            @Query("name2") String name2, @Query("phone2") String phone2);

    //提交全部通讯录
    @POST("/servlet/current/user/UserAction?function=AddContactBook")
    Observable<BaseResponse> addContactsBook(@Query("userId") String userId, @Query("contactInfo") String contactInfo);
//    Observable<BaseResponse> addContactsBook(@Query("userId") String userId, @QueryMap() Map<String,String> map);

    //提交短信记录
    @POST("/servlet/current/user/UserAction?function=AddMessageList")
    Observable<BaseResponse> addSMSData(@Query("userId") String userId, @Query("data") String data);
//    Observable<BaseResponse> addSMSData(@Query("userId") String userId, @Body() RequestBody data);


    //开始运营商认证
    @POST("/servlet/current/user/OperatorAction?function=SendOPeratorInfo")
    Observable<YYSResuleVO> startYYSAuth(@Query("userId") String userId, @Query("phoneNo") String phoneNo,
                                         @Query("carrier") String carrier,
                                         @Query("password") String password, @Query("userName") String userName,
                                         @Query("userID") String userID);

    //发送运营商短信验证码
    @POST("/servlet/current/user/OperatorAction?function=UpdateTaskStatus")
    Observable<YYSResuleVO> sendSMS(@Query("tid") String tid, @Query("smsCode") String smsCode);

    //轮询运营商认证进度状态
    @GET("/servlet/current/user/OperatorAction?function=GetTaskStatus")
    Observable<PollingVO> polling(@Query("userid") String userId, @Query("tid") String tid);

    //产品数据
    @POST("/servlet/current/user/ProductAction?function=ProductList")
    Observable<ProductVo> getProducts(@Query("condition") String condition, @Query("sort") String sort);

    //添加浏览记录
    @POST("/servlet/current/user/ProductAction?function=AddProBrowseRec")
    Observable<BaseResponse> addBrowseRecord(@Query("userId") String userId, @Query("productId") String productId,
                                             @Query("device") String device);

    //获取浏览记录
    @POST("/servlet/current/user/ProductAction?function=GetBrowseRec")
    Observable<BrowseRecordsVO> getBrowseRecord(@Query("userId") String userId);

    //删除浏览记录
    @POST("/servlet/current/user/ProductAction?function=DelBrowseRec")
    Observable<BaseResponse> delBrowseRecords(@Query("userId") String userId);

    //获取信用分评价
    @POST("/servlet/current/user/MoxieAction?function=ScodeAuth")
    Observable<BaseResponse> getAuthStatus(@Query("userId") String userId);

    //查询产品是否已收藏
    @POST("/servlet/current/user/ProductAction?function=QueryCollect")
    Observable<BaseResponse> queryCollection(@Query("userId") String userId, @Query("productId") String productId);

    //收藏产品
    @POST("/servlet/current/user/ProductAction?function=AddProCollect")
    Observable<BaseResponse> collection(@Query("userId") String userId, @Query("productId") String productId);

    //获取收藏的产品
    @POST("/servlet/current/user/UserAction?function=getCollect")
    Observable<List<ProductVo.Products>> getCollection(@Query("userId") String userId);

    //问题分类
    @POST("/servlet/current/user/ProblemAction?function=getProblemClass")
    Observable<QuestionClassVO> getQuestionClass();

    //热门问题
    @POST("/servlet/current/user/ProblemAction?function=getProblem")
    Observable<QuestionVO> getQuestions();

    //获取首页轮播内容
    @POST("/servlet/current/user/ProductAction?function=GetFelicitate")
    Observable<List<FelictateVO>> getFelicitate();

    //获取产品详情
    @POST("/servlet/current/user/ProductAction?function=QueryDetails")
    Observable<ProductDetailVO> QueryDetails(@Query("productId") String productId);
}
