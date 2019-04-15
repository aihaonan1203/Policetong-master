// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.administrator.policetong.network.api;

import com.example.administrator.policetong.base.BaseBean;
import com.example.administrator.policetong.new_bean.GongGaoBean;
import com.example.administrator.policetong.new_bean.JingBaoBean;
import com.example.administrator.policetong.new_bean.UserBean;
import com.example.administrator.policetong.new_bean.ZDBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PoliceApi {

    @POST("user/login")//登录
    Observable<BaseBean<UserBean>> login(@Body RequestBody test);

    @POST("notice/getNotice")//公告查询
    Observable<BaseBean<List<GongGaoBean>>> getNotice(@Body RequestBody test);

    @POST("user/changePwd")//修改密码
    Observable<BaseBean> changePwd(@Body RequestBody test);

    @GET("squInfo/getSqu")//获取中队列表
    Observable<BaseBean<List<ZDBean>>> getSqu();

    @POST("guard/addGuard")//添加警保台账
    Observable<BaseBean> addGuard(@Body RequestBody test);

    @POST("guard/getGuard")//查询警保台账
    Observable<BaseBean<List<JingBaoBean>>> getGuard(@Body RequestBody test);



}
