package com.company.socialgage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {

    private static MySingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private MySingleton(Context ctx) {
        this.ctx=ctx;
        requestQueue=getRequestQueue();

    }
public static synchronized MySingleton getInstance(Context ctx){
        if (instance==null){
            instance=new MySingleton(ctx);
        }
        return instance;
}

public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
}
public <T> void addToRequestQueue(Request <T> req){
        getRequestQueue().add(req);
}
}
