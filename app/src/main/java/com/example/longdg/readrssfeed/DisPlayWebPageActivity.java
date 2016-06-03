package com.example.longdg.readrssfeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Longdg on 01/06/2016.
 */
public class DisPlayWebPageActivity extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.website);

        Intent in = getIntent();
        String page_url = in.getStringExtra("page_url");
        webView = (WebView) findViewById(R.id.webpage);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(page_url);
        webView.setWebViewClient(new DisplayWebPageActivityClient());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK)&& webView.canGoBack()){
            webView.goBack();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private class DisplayWebPageActivityClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

