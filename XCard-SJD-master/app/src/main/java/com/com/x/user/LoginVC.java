package com.com.x.user;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.com.x.AppModel.UserModel;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.MainActivity;
import com.example.x.xcard.R;
import com.x.custom.XActivityindicator;
import com.x.custom.XNetUtil;

import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPDataCache;
import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/1.
 */
public class LoginVC extends BaseActivity {

    private EditText mob;
    private EditText pass;
    private Intent intent;

    @Override
    protected void setupUi() {
        setContentView(R.layout.user_login);
        setPageTitle("登录");
        mob= (EditText) findViewById(R.id.user_login_mob);
        pass= (EditText) findViewById(R.id.user_login_pass);

    }

    @Override
    protected void setupData() {

    }

    public void doLogin(View v)
    {
        //获取输入的登录名和密码
        String m = mob.getText().toString().trim();
        String p = pass.getText().toString().trim();

        if(m.length() == 0 || p.length()==0)
        {
            doShowToastLong("请输入帐号和密码");
            return;
        }


        final View btn = v;

        final SVProgressHUD hud = XActivityindicator.create(mContext);
        hud.show();
        btn.setClickable(false);
        XNetUtil.Handle(APPService.doLogin(m, p), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {
                btn.setClickable(true);
                hud.dismiss();
            }
            //正确登录
            @Override
            public void onSuccess(List<UserModel> userModels) {
                APPDataCache.User = userModels.get(0);
                APPDataCache.User.save();
                XNetUtil.APPPrintln("token: "+APPDataCache.User.getToken());
                APPDataCache.User.registNotice();
                hud.dismiss();
                intent =new Intent(LoginVC.this, MainActivity.class);
                startActivity(intent);
                doPop();

            }
        });




    }

    public void toFindPWVC(View v)
    {
        pushVC(FindPWVC.class);
    }

}