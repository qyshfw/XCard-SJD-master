package com.com.x.xiaoxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.com.x.card.ChooseCardTypeVC;
import com.com.x.user.FindPWNewVC;
import com.com.x.user.FindPWVC;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XAPPUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/7.
 */
public class MSGSendMSGVC extends BaseActivity {
    private EditText mtitle;
    private EditText info;

    String uid = ApplicationClass.APPDataCache.User.getUid();
    String username = ApplicationClass.APPDataCache.User.getUsername();
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    @Override
    protected void setupUi() {
        setContentView(R.layout.msg_sendmsg);
        setPageTitle("发布消息");

        mtitle = (EditText)findViewById(R.id.msg_sendmsg_title);
        info = (EditText)findViewById(R.id.msg_sendmsg_info);


    
    }

    @Override
    protected void setupData() {

    }


    public void btnClick(final View v)
    {
        if(!XAPPUtil.isNull(mtitle) || !XAPPUtil.isNull(info))
        {
            return;
        }

        final String t = mtitle.getText().toString().trim();
        final String i = info.getText().toString().trim();

        v.setEnabled(false);

        XNetUtil.Handle(APPService.shopaAddMessages(uid,username,t, i,sid), "消息发送成功", "消息发送失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
                v.setEnabled(true);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                v.setEnabled(!aBoolean);
                if(aBoolean)
                {
                    XNotificationCenter.getInstance().postNotice("SendMSGSuccessed",null);
                    XActivityindicator.getHud().setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(SVProgressHUD hud) {
                            doPop();
                        }
                    });
                }
            }
        });

    }


}
