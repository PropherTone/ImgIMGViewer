package com.example.imgviewer.Web;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MyWebViewClient extends WebViewClient {

    public ProgressBar progressBar;
    public String url;
    public ImageView I_wanna_star;
    public ImageView I_get_star;
    public Context context;

    public MyWebViewClient() {
    }

    public MyWebViewClient(Context context,ProgressBar progressBar, ImageView i_wanna_star, ImageView i_get_star) {
        this.context = context;
        this.progressBar = progressBar;
        I_wanna_star = i_wanna_star;
        I_get_star = i_get_star;
    }

    public MyWebViewClient(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        this.url = url;
        view.loadUrl(url);
        CheckStar();
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void CheckStar() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (CheckStar(url)){
                    I_get_star.setVisibility(View.VISIBLE);
                    I_wanna_star.setClickable(false);
                }else {
                    I_get_star.setVisibility(View.INVISIBLE);
                    I_wanna_star.setClickable(true);
                }
            }
        });
    }

    public boolean CheckStar(String url){
        List<String> starUrl = WebDB.getInstance(context).getWebDAO().QueryAllUrlNotLive();
        return starUrl.contains(url);
    }

}
