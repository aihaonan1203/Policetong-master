package com.example.administrator.policetong.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/21.
 */

public class GsonUtil {
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

}
