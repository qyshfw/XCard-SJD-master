package com.example.x.xcard;
import android.webkit.WebView;

import com.x.custom.DragImage;

/**
 * Created by X on 16/8/23.
 */
public class TestVC extends BaseActivity
{
    private WebView webview;
    private DragImage dw;




    @Override
    protected void setupUi() {
        setContentView(R.layout.test_vc);
//        webview = (WebView) findViewById(R.id.about_webview);
//        doSetTitle(R.id.about_include, "关于我们");

        dw = (DragImage) findViewById(R.id.drawImage);

        setRightImg(R.drawable.icon_user_white);
        setPageTitle("123456");


    }

    @Override
    protected void setupData()
    {

    }

}
