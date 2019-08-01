package com.example.administrator.policetong.utils;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Hjb on 2019/8/1.
 */

public class RequestBodyUtils {


    public static RequestBody getRequestBody(JSONObject jsonObject){
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
    }


}
