package com.androidexperiments.meter;

import android.content.Context;
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

import java.util.Dictionary;

/**
 * Created by zachmathews on 12/22/15.
 */
public class HUDManager {
    private static HUDManager instance;
    private long refreshInterval = 30; //minutes
    private OnHUDDataRetrievedListener mDataListener;
    private Handler handler;
    private Context context;
    private Point screenSize;
    public HUDManager(){
        handler = new Handler();
    }

    public static HUDManager instance(){
        if(instance == null){
            instance = new HUDManager();
        }
        return instance;
    }

    public void setListener(Context context, OnHUDDataRetrievedListener mDataListener) {
        this.mDataListener = mDataListener;
        this.context = context;
    }
    Runnable getParams = new Runnable() {
        @Override
        public void run() {
            getHUDParametersFromURL(context, "http://www.zackmatthews.com/stats.txt");
            handler.postDelayed(getParams, refreshInterval * 100);
        }
    };
    public void start(){
        handler.postDelayed(getParams, refreshInterval * 100);
    }

    public interface OnHUDDataRetrievedListener{
        void onDataRetrieved(String data);
    }
    public synchronized void getHUDParametersFromURL(Context context, String url){

        if(mDataListener != null) {
// Instantiate the RequestQueue.
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            screenSize = new Point();
            display.getSize(screenSize);
            RequestQueue queue = Volley.newRequestQueue(context);

// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(mDataListener != null) {
                                String temp = "";
                                int tempIndex;
                                for (int i = 0; i < response.length(); i += (screenSize.x / 4)) {
                                    tempIndex = response.indexOf(" ", i);
                                    if (tempIndex == -1) {
                                        tempIndex = i;
                                    }
                                    else{
                                        i = tempIndex;
                                    }
                                    temp = response.substring(tempIndex);
                                    response = response.substring(0, tempIndex) + "\n" + temp;

                                }
                                mDataListener.onDataRetrieved(response);
                            }
                        }
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

}
