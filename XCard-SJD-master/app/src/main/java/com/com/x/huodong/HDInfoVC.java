package com.com.x.huodong;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.com.x.AppModel.ActivityModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.x.custom.DensityUtil;

/**
 * Created by X on 2016/10/11.
 */

public class HDInfoVC extends BaseActivity {

    private ImageView img;
    private TextView viewNum;

    private TextView title;
    private TextView time;
    private TextView tel;
    private TextView address;

    private WebView info;

    private ActivityModel model ;

    String BaseHtml = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" +
            "<head>\r\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n" +
            "<meta http-equiv=\"Cache-Control\" content=\"no-cache\" />\r\n" +
            "<meta content=\"telephone=no\" name=\"format-detection\" />\r\n" +
            "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\">\r\n" +
            "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />\r\n" +
            "<title>活动简介</title>\r\n" +
            "<style>\r\n" +
            "body {background-color: #ffffff}\r\n" +
            "table {border-right:1px dashed #D2D2D2;border-bottom:1px dashed #D2D2D2}\r\n" +
            "table td{border-left:1px dashed #D2D2D2;border-top:1px dashed #D2D2D2}\r\n" +
            "img {width:100%;height: auto}\r\n" +
            "</style>\r\n</head>\r\n<body>\r\n"+"[XHTMLX]"+"\r\n</body>\r\n</html>";

    @Override
    protected void setupUi() {
        setContentView(R.layout.hd_info);
        setPageTitle("详情");

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("model"))
        {
            model = (ActivityModel)bundle.getSerializable("model");
        }

        img = (ImageView) findViewById(R.id.hd_info_img);
        viewNum = (TextView) findViewById(R.id.hd_info_view);

        title = (TextView) findViewById(R.id.hd_info_title);
        time = (TextView) findViewById(R.id.hd_info_time);
        tel = (TextView) findViewById(R.id.hd_info_tel);
        address = (TextView) findViewById(R.id.hd_info_address);

        info = (WebView)findViewById(R.id.hd_info_info);


        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();

        int w = ApplicationClass.SW;
        int h = (int)(w * 0.435F);

        layoutParams.height = h;
        img.setLayoutParams(layoutParams);

        ImageLoader.getInstance().displayImage(model.getUrl(),img);
        viewNum.setText("浏览: "+model.getView());
        title.setText(model.getTitle());
        time.setText(model.getS_time()+"至"+model.getE_time());
        tel.setText(model.getTel());
        address.setText(model.getAddress());


        info.getSettings().setDefaultTextEncodingName("utf-8");
        info.loadDataWithBaseURL(null, BaseHtml.replace("[XHTMLX]",model.getContent()), "text/html", "utf-8", null);


    }

    @Override
    protected void setupData() {

    }
}
