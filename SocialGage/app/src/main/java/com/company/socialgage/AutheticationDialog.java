package com.company.socialgage;


import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;


public class AutheticationDialog extends Dialog {
    private  String redirect_url;
    private  String request_url;
    private  AutheticationListener listener;

    public AutheticationDialog(@NonNull Context context,AutheticationListener listener) {
        super(context);
        this.listener = listener;
        this.redirect_url = context.getResources().getString(R.string.redirect_url);
        this.request_url = context.getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                context.getResources().getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=code&display=touch&scope=user_profile,user_media";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);
        InitializeWebView();
    }
    public void InitializeWebView(){
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);
    }


    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(redirect_url)) {
                    Uri uri = Uri.EMPTY.parse(url);
                    String access_token = uri.getEncodedQuery();
                    access_token = access_token.substring(access_token.lastIndexOf("=") + 1);
                      Log.d("access_token", url);
                    Log.e("access_token",access_token);
                    listener.onTokenRecieved(access_token);
                    AutheticationDialog.this.dismiss();
                    return true;

            }
            return false;
        }


    };

}
