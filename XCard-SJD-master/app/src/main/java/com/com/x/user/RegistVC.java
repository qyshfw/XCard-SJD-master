package com.com.x.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XAPPUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 2016/10/12.
 */

public class RegistVC extends BaseActivity {

    private EditText tel;
    private EditText name;

    @Override
    protected void setupUi() {
        setContentView(R.layout.user_regist);
        setPageTitle("会员注册");
        tel= (EditText) findViewById(R.id.user_regist_tel);
        name= (EditText) findViewById(R.id.user_regist_name);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.containsKey("tel"))
        {
            tel.setText(bundle.getString("tel"));
        }

    }

    @Override
    protected void setupData() {

    }

    public void submit(final View v)
    {
        if(!XAPPUtil.isNull(tel) || !XAPPUtil.isNull(name))
        {
            return;
        }

        if(!XAPPUtil.isPhone(tel))
        {
            doShowToast("请输入正确的手机号码!");
            return;
        }

        //获得注册的会员信息，
        String t = tel.getText().toString().trim();
        String n = name.getText().toString().trim();

        //提示框
        final SVProgressHUD hud = XActivityindicator.create(mContext);
        hud.show();
        v.setClickable(false);
        //Handle是用来传递的信息
        XNetUtil.Handle(APPService.userRegister(t, n), "会员注册成功","会员注册失败",new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                v.setClickable(true);
            }
            @Override
            public void onSuccess(Boolean res) {
                v.setEnabled(true);
                if(res)
                {
                    //通知中心获得实例，放入通知。
                    XNotificationCenter.getInstance().postNotice("RegistSuccess",null);
                    XActivityindicator.getHud().setOnDismissListener(new OnDismissListener() {
                        @Override
                        //对话框结束时的操作。
                        public void onDismiss(SVProgressHUD hud) {
                            //结束动画
                            doPop();
                        }
                    });
                }

            }
        }                 );

    }

}
