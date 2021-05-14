package com.example.zoompe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebView;
import android.widget.Toast;

public class paymentPage extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        getSupportActionBar().hide();
        webView = findViewById(R.id.webview);
        Intent i = getIntent();
        String page = i.getStringExtra("page");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(page, "text/html; charset=utf-8", "UTF-8");

    }
    int backButtonCount=0;
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
           Intent i = new Intent(getApplicationContext(),home.class);
           startActivity(i);
        }
        else
        {
            Toast.makeText(this, "Don't press back until transaction is completed", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}