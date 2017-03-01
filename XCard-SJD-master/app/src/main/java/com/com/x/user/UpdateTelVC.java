package com.com.x.user;

import android.view.View;
import android.widget.EditText;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.DataCache;
import com.example.x.xcard.R;
import com.x.custom.XAPPUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XNetUtil;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 2016/10/3.
 */

public class UpdateTelVC extends BaseActivity {

    private EditText telEdTXT;
    private EditText oldTel;
    private EditText code;
    private XVadButton vadBtn;

    @Override
    protected void setupUi() {
        setContentView(R.layout.user_update_tel);
        setPageTitle("修改手机号码");

        telEdTXT = (EditText)findViewById(R.id.user_update_tel_new);
        oldTel = (EditText)findViewById(R.id.user_update_tel_old);
        code = (EditText)findViewById(R.id.user_update_tel_code);
        vadBtn = (XVadButton)findViewById(R.id.user_update_tel_vadbtn);

        vadBtn.setTelEdTXT(telEdTXT);
        vadBtn.needHas = false;

    }

    @Override
    protected void setupData() {

    }

    public void submit(final View v) {

    if(!XAPPUtil.isNull(telEdTXT) || !XAPPUtil.isNull(oldTel) || !XAPPUtil.isNull(code))
    {
        return;
    }
        String tel = telEdTXT.getText().toString().trim();
        String old = oldTel.getText().toString().trim();
        String c = code.getText().toString().trim();

        if(!ApplicationClass.APPDataCache.User.getMobile().equals(old))
        {
            doShowToast("原手机号码有误");
            return;
        }

        if(tel.equals(old))
        {
            doShowToast("新手机号码不能和原手机号码一样");
            return;
        }

        v.setEnabled(false);

        XNetUtil.Handle(APPService.userUpdateMobile(tel, old, c), "手机号码修改成功", "手机号码修改失败", new XNetUtil.OnHttpResult<Boolean>() {
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
