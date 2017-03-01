package com.com.x.user;

import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;

/**
 * Created by X on 16/9/1.
 */
public class APPContact extends BaseActivity {

    private WebView web;
    @Override
    protected void setupUi() {
        setContentView(R.layout.app_contact);
        setPageTitle("联系我们");
        web = (WebView) findViewById(R.id.app_contact_web);
        web.loadUrl("http://www.baidu.com");

        web.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return true;
            }

        });

    }

    @Override
    protected void setupData() {

    }

}
