package com.com.x.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XAPPUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XNetUtil;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/1.
 */
public class FindPWNewVC extends BaseActivity {

    private EditText pass1;
    private EditText pass2;

    private String tel = "";
    private String code = "";


    @Override
    protected void setupUi() {
        setContentView(R.layout.user_findpwnew);
        setPageTitle("找回密码");

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("tel"))
        {
            tel = bundle.getString("tel");
            code = bundle.getString("code");
        }

        pass1 = (EditText)findViewById(R.id.user_findpwnew_p1);
        pass2 = (EditText)findViewById(R.id.user_findpwnew_p2);
    }

    @Override
    protected void setupData() {

    }

    public void submit(final View v)
    {
        if(!XAPPUtil.isNull(pass1) || !XAPPUtil.isNull(pass2))
        {
            return;
        }
        String p1 = pass1.getText().toString().trim();
        String p2 = pass2.getText().toString().trim();

        if(!p1.equals(p2))
        {
            doShowToast("新密码和确认密码不一致");
            return;
        }

        v.setEnabled(false);

        XNetUtil.Handle(APPService.userUpdatePass(tel, code, p1), "密码重置成功", "密码重置失败", new XNetUtil.OnHttpResult<Boolean>() {
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