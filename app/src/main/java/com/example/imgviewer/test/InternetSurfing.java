package com.example.imgviewer.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.imgviewer.R;
import com.example.imgviewer.Utils.ScreenPXMath;
import com.example.imgviewer.View.MainActivity;
import com.example.imgviewer.View.MyTextBook;
import com.example.imgviewer.Web.MyChromeClient;
import com.example.imgviewer.Web.MyWebViewClient;
import com.example.imgviewer.Web.Web;
import com.example.imgviewer.Web.WebDB;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InternetSurfing extends AppCompatActivity {

    public ProgressBar progressBar;
    public TextView textView;
    private WebSettings webSettings;
    private WebView webView;
    public ImageView I_wanna_star;
    public ImageView I_get_star;
    private EditText star_type;
    public String starUrl;
    private Button confirm_star;
    private MyWebViewClient myWebViewClient;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.internetsurfing);

        InitView();
        InitWebView();
        CheckStar();
        WenSettings();
        ScreenPXMath.HideBottomUIMenu(getWindow().getDecorView(),this);

    }

    private void InitView() {
        confirm_star = findViewById(R.id.confirm_star);
        star_type = findViewById(R.id.star_type);
        I_wanna_star = findViewById(R.id.I_wanna_star);
        I_get_star = findViewById(R.id.I_get_star);
        progressBar = findViewById(R.id.web_progress);
        textView = findViewById(R.id.webTitle);
        webView = findViewById(R.id.web);
        ImageView back,forward,NET_finish,show_NAV;
        NET_finish = findViewById(R.id.NET_finish);
        back = findViewById(R.id.NET_back);
        forward = findViewById(R.id.NET_forward);
        show_NAV = findViewById(R.id.showNAVGATION);

        I_wanna_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star_type.setVisibility(View.VISIBLE);
                confirm_star.setVisibility(View.VISIBLE);
            }
        });

        NET_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()){
                    webView.goBack();
                    CheckStar();
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoForward()){
                    webView.goForward();
                    CheckStar();
                }
            }
        });

        show_NAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenPXMath.ShowBottomUIMenu(getWindow().getDecorView(),InternetSurfing.this);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        ScreenPXMath.HideBottomUIMenu(getWindow().getDecorView(),InternetSurfing.this);
                    }
                };
                timer.schedule(timerTask,2000);
            }
        });
    }

    private void InitWebView() {
        Intent intent = getIntent();
        starUrl = intent.getStringExtra("WebUri");
        myWebViewClient = new MyWebViewClient(this,progressBar,I_wanna_star,I_get_star);
        MyChromeClient myChromeClient = new MyChromeClient(progressBar,textView,this);
        webView.setWebViewClient(myWebViewClient);
        webView.setWebChromeClient(myChromeClient);
        webView.loadUrl(starUrl);
    }

    private void WenSettings() {
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//支持JS
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能

        String cacheDirPath = getFilesDir().getAbsolutePath()+"/WebCache";
        Log.e("path",cacheDirPath);
//        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

//        webSettings.setAllowFileAccess(false); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
    }

    @Override
    protected void onResume() {
        super.onResume();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        webSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ViewGroup)webView.getParent()).removeView(webView);
        webView.destroy();
        webView = null;
    }

    public boolean CheckStar(String url){
        List<String> starUrl = WebDB.getInstance(this).getWebDAO().QueryAllUrlNotLive();
        return starUrl.contains(url);
    }

    //收藏按钮配置
    //检索数据库匹配收藏数据
    public void starClk(View view) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String type = star_type.getText().toString();
                 String url = myWebViewClient.getUrl();
                if (!CheckStar(url)&&!type.equals("")){
                    I_get_star.setVisibility(View.VISIBLE);
                    star_type.setVisibility(View.INVISIBLE);
                    confirm_star.setVisibility(View.INVISIBLE);
                    WebDB.getInstance(InternetSurfing.this)
                            .getWebDAO()
                            .InsertMyWebData(new Web(url,type));
                }else{
                    star_type.setVisibility(View.INVISIBLE);
                    confirm_star.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //检索数据库
    private void CheckStar() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String url;
                if (myWebViewClient.getUrl() == null){
                    url = starUrl;
                }else{
                    url = myWebViewClient.getUrl();
                }
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

    public void refresh(View view) {
        webView.reload();
        starUrl = "https://www.baidu.com";
        CheckStar();
    }
}
