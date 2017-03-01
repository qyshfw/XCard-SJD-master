package com.com.x.user;

import android.view.View;

import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;

/**
 * Created by X on 16/9/2.
 */
public class ChangePwVC extends BaseActivity {

    @Override
    protected void setupUi() {
        setContentView(R.layout.user_changepw);
        setPageTitle("修改密码");

    }

    @Override
    protected void setupData() {

    }

    public void btnClick(View v)
    {
        pushVC(FindPWVC.class);
    }

}
