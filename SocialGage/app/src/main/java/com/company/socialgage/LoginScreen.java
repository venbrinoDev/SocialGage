package com.company.socialgage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class LoginScreen extends AppCompatActivity implements AutheticationListener{
RevealAnimation mRevealAnimation;
LinearLayout loginButton ,notAUser;
AutheticationDialog autheticationDialog=null;
private AppPrefrences appPrefrences ;
private String token =null;
RequestQueue requestQueue ;
public ImageButton Help;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_screen);
        Intent intent = this.getIntent();

        loginButton =findViewById(R.id.login_button);
        RelativeLayout rootLayout = findViewById(R.id.rootLayout);
        notAUser = findViewById(R.id.notAUser);

        mRevealAnimation = new RevealAnimation(rootLayout,intent,this);


        autheticationDialog = new AutheticationDialog(this,this);
        appPrefrences= new AppPrefrences(this);

        requestQueue = MySingleton.getInstance(this.getApplication()).getRequestQueue();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autheticationDialog.setCancelable(true);
              autheticationDialog.show();
            }
        });
        AllClicks();



    }

    public void AllClicks(){
        notAUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this,SignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAffinity();
        System.exit(0);
    }

    @Override
    public void onTokenRecieved(String auth_token) {
        if (auth_token != null) {
            String url = "https://api.instagram.com/oauth/access_token";
           FecthInstagramToken(auth_token,url);
        }
    }

    public void FecthInstagramToken(final String authCode,String url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("access_token" ,"onResponse: "+response);

                        if (response !=null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String user_token = jsonObject.getString("access_token");
                               String id = jsonObject.getString("user_id");

                                appPrefrences.putString(AppPrefrences.TOKEN, user_token);
                                appPrefrences.putString(AppPrefrences.USER_ID, id);

                                loginIn(user_token,id);

                                Log.d("access_token", "access_token = " + token);
                                Log.d("access_token", "user_id = " + id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("access_token" ,"onResponse: "+error);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("client_id", getResources().getString(R.string.client_id));
                params.put("client_secret", getResources().getString(R.string.client_secret_id));
                params.put("grant_type", "authorization_code");
                params.put("redirect_uri", getResources().getString(R.string.redirect_url));
                params.put("code",authCode);
                return params;
            }

        };
        requestQueue.add(stringRequest);

    }

    private void loginIn(String userToken,String user_id) {
        if (!userToken.isEmpty() && !user_id.isEmpty()) {
            Intent HomeActivity = new Intent(LoginScreen.this, com.company.socialgage.HomeActivity.class);
            HomeActivity.putExtra(AppPrefrences.TOKEN, userToken);
            HomeActivity.putExtra(AppPrefrences.USER_ID, user_id);
            startActivity(HomeActivity);
        }
    }
}
