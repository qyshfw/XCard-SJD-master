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
public class FindPWVC extends BaseActivity {

    private EditText tel;
    private EditText code;
    private XVadButton vadBtn;

    @Override
    protected void setupUi() {
        setContentView(R.layout.user_findpw);
        setPageTitle("找回密码");

        tel = (EditText)findViewById(R.id.user_findpw_tel);
        code = (EditText)findViewById(R.id.user_findpw_code);
        vadBtn = (XVadButton)findViewById(R.id.user_findpw_vadbtn);

        vadBtn.setTelEdTXT(tel);
        vadBtn.needHas = true;

    }

    @Override
    protected void setupData() {

    }

    public void toFindPWNewVC(final View v)
    {

        if(!XAPPUtil.isNull(tel) || !XAPPUtil.isNull(code))
        {
            return;
        }

        final String t = tel.getText().toString().trim();
        final String c = code.getText().toString().trim();

        v.setEnabled(false);

        XNetUtil.Handle(APPService.userSmsVerify(t, c), null, "验证失败", new XNetUtil.OnHttpResult<Boolean>() {
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
                    Bundle bundle = new Bundle();
                    bundle.putString("tel",t);
                    bundle.putString("code",c);
                    pushVC(FindPWNewVC.class,bundle);
                    finish();
                }
            }
        });


    }

}