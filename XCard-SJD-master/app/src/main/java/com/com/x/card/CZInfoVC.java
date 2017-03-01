package com.com.x.card;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.com.x.AppModel.CardTypeModel;
import com.com.x.AppModel.UserModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.RoundAngleImageView;
import com.x.custom.XAPPUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/5.
 */
public class CZInfoVC extends BaseActivity {

    private RoundAngleImageView img;
    private TextView nameTv;
    private TextView telTv;
    private TextView typeTv;
    private TextView yuTv;
    private TextView yuTitle;
    private TextView lt;
    private TextView rt;
    private EditText montyET;
    private EditText valueET;
    private EditText info;

    private UserModel user;
    private CardTypeModel card;

    @Override
    protected void setupUi() {
        setContentView(R.layout.card_cz_info);
        setPageTitle("充值");

        img = (RoundAngleImageView)findViewById(R.id.card_cz_info_img);
        nameTv = (TextView) findViewById(R.id.card_cz_info_name);
        telTv = (TextView)findViewById(R.id.card_cz_info_tel);
        typeTv = (TextView)findViewById(R.id.card_cz_info_type);
        yuTv = (TextView)findViewById(R.id.card_cz_info_yu);
        lt = (TextView)findViewById(R.id.card_cz_info_lt);
        rt = (TextView)findViewById(R.id.card_cz_info_rt);
        yuTitle = (TextView)findViewById(R.id.card_cz_info_yutitle);

        montyET = (EditText) findViewById(R.id.card_cz_info_money);
        valueET = (EditText) findViewById(R.id.card_cz_info_value);
        info = (EditText) findViewById(R.id.card_cz_info_info);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("user"))
        {
            user = (UserModel) bundle.getSerializable("user");
            card = (CardTypeModel) bundle.getSerializable("card");
        }

        typeTv.setText("卡类型："+card.getType());
        img.setBackgroundColor(Color.parseColor("#"+card.getColor()));
        nameTv.setText(user.getTruename());
        telTv.setText(user.getMobile());


        if (card.getType().equals("计次卡"))
        {
            yuTv.setText(card.getValues()+"次");
            lt.setText("充值次数");
        }
        else
        {
            yuTv.setText("￥"+card.getValues());
        }

    }

    @Override
    protected void setupData() {

    }


    public void btnClick(final View v)
    {
        if(!XAPPUtil.isNull(montyET))
        {
            return;
        }

        v.setEnabled(false);

        XActivityindicator.create(mContext).show();

        String value = montyET.getText().toString().trim();
        String money = valueET.getText().toString().trim();
        String bak = info.getText().toString().trim();
        String uid = ApplicationClass.APPDataCache.User.getUid();

        XNetUtil.Handle(APPService.hykAddValues(user.getUid(), user.getUsername(), card.getMcardid(),money,value,bak,uid), "充值成功", "充值失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
                v.setEnabled(true);
                XActivityindicator.hide();
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                v.setEnabled(!aBoolean);
                if(aBoolean)
                {
                    XNotificationCenter.getInstance().postNotice("CZMainChanged",null);
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
