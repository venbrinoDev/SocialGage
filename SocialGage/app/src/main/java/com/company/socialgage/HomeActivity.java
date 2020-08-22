package com.company.socialgage;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView userId,uesrName;
    AppPrefrences appPrefrences;
    RequestQueue requestQueue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        profileImage = findViewById(R.id.profile);
        userId = findViewById(R.id.userId);
        uesrName= findViewById(R.id.username);
        appPrefrences = new AppPrefrences(this);
        requestQueue=MySingleton.getInstance(this.getApplication()).getRequestQueue();

        Intent intent = getIntent();
        String user_token=intent.getStringExtra(AppPrefrences.TOKEN);
        String user_id = intent.getStringExtra(AppPrefrences.USER_ID);

        if (!user_token.isEmpty()&&!user_id.isEmpty()){
          getUserInfo(user_token);
        }
//
//        Picasso.get().load(appPrefrences.getString(AppPrefrences.PROFILE_PIC)).placeholder(R.drawable.ic_camera_enhance_black_24dp).into(profileImage);
//        userId.setText(appPrefrences.getString(AppPrefrences.USER_ID));
//        uesrName.setText(appPrefrences.getString(AppPrefrences.USER_NAME));

    }

    private void getUserInfo(final String user_token) {
        String url ="https://graph.instagram.com/me?fields=id,media_count,account_type,ig_id,username&access_token=" + user_token;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("access_token", response.toString());
                try {
                    String user_id = response.getString("ig_id");
                    bringInfo(user_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("access_token", error.toString());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Content-Type","application/json; charset=utf-8");
                return map;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void bringInfo(String uid){
        JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id",uid);
                    jsonObject.put("include_chaining",false);
                    jsonObject.put("include_reel",true);
                    jsonObject.put("include_suggested_users",true);
                    jsonObject.put("include_logged_out_extras",false);
                    jsonObject.put("include_highlight_reels",false);
                    jsonObject.put("include_live_status",true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String id =uid;

        String tryUrl ="https://www.instagram.com/graphql/query/?query_hash=d4d88dc1500312af6f937f7b804c68c3&variables="+jsonObject.toString();
                String anotherUrl = "https://www.instagram.com/graphql/query/?query_hash=d4d88dc1500312af6f937f7b804c68c3&variables=%7B%22user_id%22%3A%22"+id+"%22%2C%22include_chaining%22%3Afalse%2C%22include_reel%22%3Atrue%2C%22include_suggested_users%22%3Atrue%2C%22include_logged_out_extras%22%3Afalse%2C%22include_highlight_reels%22%3Afalse%2C%22include_live_status%22%3Atrue%7D";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, tryUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("access_token", response.toString());
                try {
                    JSONObject data = response.getJSONObject("data");
                    Log.d("access_token", "onResponse: "+data.toString());
                    JSONObject reel_User= data.getJSONObject("user");
                    JSONObject reel = reel_User.getJSONObject("reel");
                    JSONObject user= reel.getJSONObject("user");

                    String id = user.getString("id");
                    String usernameStr= user.getString("username");
                    String profile_picture = user.getString("profile_pic_url");

                    userId.setText(id);
                    uesrName.setText(usernameStr);
                    try {
                        Picasso.get().load(profile_picture).into(profileImage);
                    }catch (Exception e){

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("access_token", error.toString());
                uesrName.setText(error.toString());
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET,tryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              uesrName.setText(response);
                Log.d("access_token", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                uesrName.setText(error.toString());
                Log.d("access_token", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

        private class Content extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    String url = "http://en.wikipedia.org/wiki/Nigeria";
                    Document doc = Jsoup.connect(url).get();
                    Elements paragraphs = doc.select(".mw-content-ltr p, .mw-content-ltr li");

                    Element firstParagraph = paragraphs.first();
                    Element lastParagraph = paragraphs.last();
                    Element p;
                    int i = 1;
                    p = firstParagraph;
                    Log.d("document", p.text());
                    while (p != lastParagraph) {
                        p = paragraphs.get(i);
                        System.out.println(p.text());
                        i++;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

}
