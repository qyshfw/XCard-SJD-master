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

/**
 * Created by X on 16/9/1.
 */
public class FeedBack extends BaseActivity {

    private EditText info;

    @Override
    protected void setupUi() {
        setContentView(R.layout.user_feed);
        setPageTitle("意见反馈");

        info = (EditText)findViewById(R.id.add_content);
    }

    @Override
    protected void setupData() {

    }

    public void btnClick(View v)
    {
        if(!XAPPUtil.isNull(info))
        {
            return;
        }

        if(info.getText().toString().length() < 10 || info.getText().toString().length() > 1000)
        {
            doShowToast("字数为10-1000,请检查");
            return;
        }

        XActivityindicator.create(ApplicationClass.context).showSuccessWithStatus("提交成功");
        XActivityindicator.getHud().setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(SVProgressHUD hud) {
                doPop();
            }
        });
    }

}
