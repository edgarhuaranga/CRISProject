package com.edhuaranga.crisproject;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edhuaranga.crisproject.model.User;

import java.util.Map;

public class RESTManager {
    private static RESTManager restManager;

    RequestQueue requestQueue;
    User user;
    Context context;

    private RESTManager(Application context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.start();
    }

    public void stopConnection(Object tag){
        requestQueue.cancelAll(tag);
    }

    private void addToQueue(Request request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                Constants.REQUEST_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    static RESTManager getInstance(Application context){
        if(restManager==null)
            restManager = new RESTManager(context);

        if(context!=null && context instanceof OrtodentechApplication)
            restManager.user= ((OrtodentechApplication)context).getUsuario();

        return restManager;
    }

    void addStringRequest(int method, final String label, String url, Response.Listener<String> responseListener, Response.ErrorListener errorListener, @Nullable final Map<String, String> params){
        if(method == Request.Method.GET){
            String URL = url + "&requestType="+label;
            try {

                StringRequest request = new StringRequest(method, URL, responseListener, errorListener){
                    @Override
                    protected Map<String, String> getParams(){
                        params.put(Constants.REQUEST_TAG, label);
                        return params;
                    }
                };

                addToQueue(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(method == Request.Method.POST){
            try {

                StringRequest request = new StringRequest(method, url, responseListener, errorListener){
                    @Override
                    protected Map<String, String> getParams(){
                        params.put(Constants.REQUEST_TAG, label);
                        return params;
                    }
                };

                addToQueue(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}
