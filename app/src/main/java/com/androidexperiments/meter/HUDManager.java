package com.androidexperiments.meter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by zachmathews on 12/22/15.
 */
public class HUDManager {
    private static HUDManager instance;
    private long refreshInterval = 50; //minutes
    private Handler handler;
    private Context context;
    private Point screenSize;

    private Pair<Integer, String> url_1;
    private Pair<Integer, String> url_2;

    private String[] results;
    private SharedPreferences pref;

    public HUDManager() {
        handler = new Handler();
    }

    public static HUDManager instance() {
        if (instance == null) {
            instance = new HUDManager();
        }
        return instance;
    }

    public String[] getData() {
        if (results == null) { //hardcode 2 params for now. Add more later.
            results = new String[2];
            results[0] = "";
            results[1] = "";
        }
        return results;
    }

    Runnable getParams = new Runnable() {
        @Override
        public void run() {
            if (context != null) {
                pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                url_1 = new Pair(0, pref.getString("url_1", ""));
                url_2 = new Pair(1, pref.getString("url_2", ""));

                if (!url_1.getValue().equals("")) {
                    getHUDParametersFromURL(context, url_1);
                }

                if (!url_2.getValue().equals("")) {
                    getHUDParametersFromURL(context, url_2);
                }
                handler.postDelayed(getParams, refreshInterval * 100);
            }
        }
    };

        private RequestQueue queue;
        public void start(Context context) {
            this.context = context;
            queue = Volley.newRequestQueue(context);
            queue.start();
            handler = new Handler();
            handler.postDelayed(getParams, refreshInterval * 100);
        }
        public void stop(Context context){

            handler.removeCallbacks(getParams);
            if(queue != null) {
                queue.stop();
                queue.getCache().clear();
                queue = null;
            }
        }


        public void getHUDParametersFromURL(Context context, final Pair url) {

            // Instantiate the RequestQueue.
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            screenSize = new Point();
            display.getSize(screenSize);


            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, (String) url.getValue(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String temp = "";
                            int tempIndex;
                            for (int i = (screenSize.x / 8); i < response.length(); i += (screenSize.x / 8)) {
                                tempIndex = response.indexOf(" ", i);
                                if (tempIndex == -1) {
                                    tempIndex = i;
                                } else {
                                    i = tempIndex;
                                }
                                temp = response.substring(tempIndex);
                                response = response.substring(0, tempIndex) + "\n" + temp;
                            }
                            results[(int) url.getKey()] = response;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        queue.add(stringRequest);
    }
}

