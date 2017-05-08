package com.example.root.trackr;

/**
 * Created by joy on 8/5/17.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context myContext;

    private MySingleton(Context context) {
        myContext= context;
        requestQueue= getRequestQueue();
    }

    //method to get request queue
    public RequestQueue getRequestQueue() {
        if(requestQueue== null) {
            requestQueue= Volley.newRequestQueue(myContext.getApplicationContext());
        }
        return requestQueue;
    }

    //method to get instance of this class
    public static synchronized MySingleton getInstance(Context context) {
        if(mInstance==  null) {
            mInstance= new MySingleton(context);
        }
        return mInstance;
    }

    //method to add request to RequestQueue
    public<T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }
}


