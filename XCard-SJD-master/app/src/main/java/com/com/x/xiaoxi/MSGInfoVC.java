package com.com.x.xiaoxi;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.com.x.AppModel.MessageModel;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XNetUtil;

import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 2016/10/7.
 */

public class MSGInfoVC extends BaseActivity
{
    private TextView mtitle;
    private TextView time;
    private WebView web;

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

    private MessageModel model;

    @Override
    protected void setupUi() {
        setContentView(R.layout.msg_info);
        setPageTitle("消息详情");

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("model"))
        {
            model = (MessageModel)bundle.getSerializable("model");
        }

        mtitle = (TextView)findViewById(R.id.msg_info_title);
        time = (TextView)findViewById(R.id.msg_info_time);
        web = (WebView)findViewById(R.id.msg_info_web);


        mtitle.setText(model.getTitle());
        time.setText(model.getCreate_time());

        if(model.getGonggao())
        {
            getInfo();
        }
        else
        {
            web.getSettings().setDefaultTextEncodingName("utf-8");
            web.loadDataWithBaseURL(null, BaseHtml.replace("[XHTMLX]",model.getContent()), "text/html", "utf-8", null);
        }

    }

    @Override
    protected void setupData() {

    }

    private void getInfo()
    {
        XNetUtil.Handle(APPService.shopdGetArticle(model.getId()), new XNetUtil.OnHttpResult<List<MessageModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<MessageModel> messageModels) {

                if(messageModels.size() > 0)
                {
                    model.setContent(messageModels.get(0).getContent());

                    web.getSettings().setDefaultTextEncodingName("utf-8");
                    web.loadDataWithBaseURL(null, BaseHtml.replace("[XHTMLX]",model.getContent()), "text/html", "utf-8", null);
                }
            }
        });
    }
}
