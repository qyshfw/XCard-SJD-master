package com.com.x.user;

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
 * Created by X on 2016/10/3.
 */

public class UpdatePassVC extends BaseActivity {

    private EditText old;
    private EditText new1;
    private EditText new2;

    @Override
    protected void setupUi() {
        setContentView(R.layout.user_update_pass);
        setPageTitle("修改密码");

        old = (EditText)findViewById(R.id.user_update_pass_old);
        new1 = (EditText)findViewById(R.id.user_update_pass_new1);
        new2 = (EditText)findViewById(R.id.user_update_pass_new2);

    }

    @Override
    protected void setupData() {

    }

    public void submit(final View v) {

        if(!XAPPUtil.isNull(old) || !XAPPUtil.isNull(new1) || !XAPPUtil.isNull(new2))
        {
            return;
        }

        String tel = ApplicationClass.APPDataCache.User.getMobile();
        String o = old.getText().toString().trim();
        String n1 = new1.getText().toString().trim();
        String n2 = new2.getText().toString().trim();

        if(!n1.equals(n2))
        {
            doShowToast("新密码和确认新密码不一致");
            return;
        }

        v.setEnabled(false);

        XNetUtil.Handle(APPService.userUpdatePass2(tel, o, n1), "修改密码成功", "手机号码修改失败", new XNetUtil.OnHttpResult<Boolean>() {
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
