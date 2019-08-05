// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.administrator.policetong.network.api;

import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.new_bean.AccidentBean;
import com.example.administrator.policetong.new_bean.CarBean;
import com.example.administrator.policetong.new_bean.GongGaoBean;
import com.example.administrator.policetong.new_bean.JingBaoBean;
import com.example.administrator.policetong.new_bean.StudyBean;
import com.example.administrator.policetong.new_bean.UserBean;
import com.example.administrator.policetong.new_bean.VisitBean;
import com.example.administrator.policetong.new_bean.ZDBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface PoliceApi {


    @POST("oneList")//获取单条通行证列表
    Observable<ResponseBody> oneList(@Body RequestBody test);













    @Multipart
    @POST()
    Observable<ResponseBody> upload(@Url()String url,@Header ("token")String token,@Header ("user")String user,@Part MultipartBody.Part imgs);

    @Multipart
    @POST()//上传图片
    Observable<BaseBean> uploadImage(@Url()String url, @Part() MultipartBody.Part [] file);

    @POST("login")//登录
    Observable<ResponseBody> login(@Body RequestBody test);

    @POST("notice/getNotice")//公告查询
    Observable<BaseBean<List<GongGaoBean>>> getNotice(@Body RequestBody test);

    @POST("editpsw")//修改密码
    Observable<ResponseBody> changePwd(@Header ("token")String token, @Header ("user")String user, @Body RequestBody test);

    @GET("squInfo/getSqu")//获取中队列表
    Observable<BaseBean<List<ZDBean>>> getSqu();

    @POST("guard/addGuard")//添加警保台账
    Observable<BaseBean> addGuard(@Body RequestBody test);

    @POST("guard/getGuard")//查询警保台账
    Observable<BaseBean<List<JingBaoBean>>> getGuard(@Body RequestBody test);

    @POST("park/setPark")//停车场
    Observable<BaseBean> setPark(@Body RequestBody test);

    @GET("park/getPark")//读取停车场
    Observable<BaseBean<List<CarBean>>> getPark();

    @POST("accident/getAccident")//读取事故接警记录
    Observable<BaseBean<List<AccidentBean>>> getAccident(@Body RequestBody test);

    @POST("accident/addAccident")//事故接警
    Observable<BaseBean> addAccident(@Body RequestBody test);

    @POST("visit/addVisit")//走访整改台账增加/修改
    Observable<BaseBean> addVisit(@Body RequestBody test);

    @POST("visit/getVisit")//读取走访整改台账
    Observable<BaseBean<List<VisitBean>>> getVisit(@Body RequestBody test);

    @POST("study/addStudy")//学习增加/修改
    Observable<BaseBean> addStudy(@Body RequestBody test);

    @POST("study/getStudy")//读取学习台账
    Observable<BaseBean<List<StudyBean>>> getStudy(@Body RequestBody test);

}
