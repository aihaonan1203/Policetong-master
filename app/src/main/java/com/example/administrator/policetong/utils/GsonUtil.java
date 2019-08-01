package com.example.administrator.policetong.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/21.
 */

public class GsonUtil {

    public static boolean verifyResult_show(String result){
        if (getCode(result)==200) {
            return true;
        }
        UIUtils.t(getMsg(result),false,UIUtils.T_ERROR);
        return false;
    }

    public static boolean verifyResult(String result){
        if (getCode(result)==200) {
            return true;
        }
        return false;
    }

    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        try {
            Gson gson = new Gson();
            T result = gson.fromJson(jsonData, type);
            return result;
        }catch (Exception e){
            return null;
        }
    }

    public static <T> List<T> parseJsonArrayWithGson(String jsonData,
                                                     Class<T> type) {
        try {
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(jsonData).getAsJsonArray();
            Gson gson = new Gson();
            List<T> list = new ArrayList<>();
            for (JsonElement jsonElement : jsonArray) {
                list.add(gson.fromJson(jsonElement,type)); //cls
            }
            return list;
        }catch (Exception e){
            return new ArrayList<>();
        }
    }


    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    public static String getString(String json,String type){
        try {
            JSONObject jsonObject=new JSONObject(json);
            return jsonObject.getString(type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getResult(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            return jsonObject.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMsg(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            return jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getCode(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            return jsonObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static boolean getSuccess(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            return jsonObject.getBoolean("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static <T> ArrayList<T> resultToArrayList(String result, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(getResult(result), type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

}
