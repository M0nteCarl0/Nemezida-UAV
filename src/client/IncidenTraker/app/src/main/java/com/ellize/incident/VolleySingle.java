package com.ellize.incident;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingle {
    private static VolleySingle instance;
    private RequestQueue requestQueue;
    private static Application app;

    private VolleySingle(Application app) {
        this.app = app;
        requestQueue = getRequestQueue();


    }

    public static synchronized VolleySingle getInstance(Application app) {
        if (instance == null) {
            instance = new VolleySingle(app);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(app.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
