package com.npdeas.b1k3labapp.Webserver;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.npdeas.b1k3labapp.Webserver.Error.ConnError;
import com.npdeas.b1k3labapp.Webserver.Error.Error;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by NPDEAS on 8/23/2018.
 */

public class Comm implements Response.Listener<JSONObject>, Response.ErrorListener {

    //private static Comm thisObject = null;

    private RequestQueue mRequestQueue;
    private JSONObject response;
    private Bitmap imageResponse = null;
    private String encapsulament;

    protected Error error;
    protected Context context;

    private static final String URL = "http://200.17.218.227:5000/mobile/";
//    private static final String URL = "http://10.162.9.5:5000/mobile/";
//    private static final String URL = "http://10.164.29.82:5000/mobile/";
//    private static final String URL = "http://192.168.1.32:5000/mobile/";

    public Comm(@NonNull Context context) {
        error = new Error(Error.NO_ERROR);
        this.context = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public boolean isOnline() {
        /*ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();*/
        return false;
    }

    public synchronized JSONObject sendJSON(JSONObject request, final String command, String encapsulament) {
        response = null;
        this.encapsulament = encapsulament;
        String url = URL + command;
        error = new Error(Error.NO_ERROR);
        try {
            JSONObject msg = new JSONObject();
            msg.put(encapsulament, request);
            if (msg.toString().length() <= 150) {
                Log.i("REQUEST: ", msg.toString());
            } else {
                Log.i("REQUEST: ", msg.toString().substring(0, 150));
            }

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,
                    msg, this, this);
            req.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(req);
        } catch (Exception e) {
        }
        try {
            this.wait();
        } catch (Exception e) {
            Log.e("CONN_ERROR", e.getMessage());
        }
        return response;
    }

    public Bitmap getImage(String cmd) {
        final Thread thisThread = Thread.currentThread();
        String url = URL + cmd;
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageResponse = response;
                thisThread.notify();
            }
        },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        imageResponse = null;
                        thisThread.notify();
                    }
                });
        mRequestQueue.add(imageRequest);

        try {
            thisThread.wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageResponse;
    }


    @Override
    public void onResponse(JSONObject response) {
//        if (response.toString().length() <= 200) {
        Log.i("RESPONSE: ", response.toString());
//        } else {
//            Log.i("RESPONSE: ", response.toString().substring(0, 200));
//        }
        try {
            int status = response.getInt("status");
            if (status == ConnError.NO_ERROR.value) {
                this.response = response.getJSONObject(encapsulament);
            } else {
                this.response = null;
                error = new Error(status);
            }
        } catch (JSONException e) {

        }
        //queue.notifyAll();
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (error.getMessage() != null) {
            Log.i("HTTP_ERROR", error.getMessage());
        }
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Comm.this.error = new Error(ConnError.ERROR_ON_SERVER_CONN.value);
        } else if (error instanceof NetworkError) {
            Comm.this.error = new Error(ConnError.ERROR_ON_SERVER_CONN.value);
        }
        synchronized (this) {
            notify();
        }
    }

    public String getErrorString() {
        return Error.getErrorString(error.getError());
    }

    public int getError() {
        return error.getIntError();
    }

}
