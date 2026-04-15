package com.example.jpl2.activities;

import static android.widget.Toast.LENGTH_LONG;

import static com.example.jpl2.api.ApiClient.BASE_URL;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.R;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            webView = findViewById(R.id.webview);

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webView.setWebViewClient(new WebViewClient());

            webView.loadUrl(BASE_URL);
        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.toString(), LENGTH_LONG).show();
        }

    }
}