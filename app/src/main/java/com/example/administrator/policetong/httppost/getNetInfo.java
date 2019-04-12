package com.example.administrator.policetong.httppost;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.policetong.MainActivity;
import com.example.administrator.policetong.utils.UrlBean;
import com.example.administrator.policetong.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getNetInfo {
    public static void NetInfo(Context context, String url, JSONObject jsonObject, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        url="http://" + Util.loadSetting(context).getUrl() + ":"+Util.loadSetting(context).getUrlPort()+"/pointsman/"+url;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    callback.onSuccess(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(volleyError);
            }
        });
        queue.add(jsonObjectRequest);
    }


    public static void NetInfoArray(Context context, String url, JSONObject jsonObject, final VolleyArrayCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        url="http://" + Util.loadSetting(context).getUrl() + ":"+Util.loadSetting(context).getUrlPort()+"/pointsman/"+url;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray object) {
                try {
                    callback.onSuccess(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(volleyError);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public interface VolleyCallback {
        void onSuccess(JSONObject object) throws JSONException;

        void onError(VolleyError volleyError);
    }

    public interface VolleyArrayCallback {
        void onSuccess(JSONArray object) throws JSONException;

        void onError(VolleyError volleyError);
    }
}
