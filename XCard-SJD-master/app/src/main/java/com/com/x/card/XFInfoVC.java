package com.com.x.card;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.DecimalFormat;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/5.
 */
public class XFInfoVC extends BaseActivity {

    private RoundAngleImageView img;
    private TextView nameTv;
    private TextView telTv;
    private TextView typeTv;
    private TextView yuTv;
    private TextView yuTitle;
    private TextView lt;
    private TextView rt;

    private TextView ly;
    private TextView ry;

    private EditText montyET;
    private EditText valueET;
    private EditText info;

    private UserModel user;
    private CardTypeModel card;
//修改UI
    @Override
    protected void setupUi() {
        setContentView(R.layout.card_xf_info);
        //设置消费标题
        setPageTitle("消费");
        //自定义圆角图片
        img = (RoundAngleImageView)findViewById(R.id.card_xf_info_img);
        //卡主姓名
        nameTv = (TextView) findViewById(R.id.card_xf_info_name);

        telTv = (TextView)findViewById(R.id.card_xf_info_tel);
        typeTv = (TextView)findViewById(R.id.card_xf_info_type);
        yuTv = (TextView)findViewById(R.id.card_xf_info_yu);
        lt = (TextView)findViewById(R.id.card_xf_info_lt);
        rt = (TextView)findViewById(R.id.card_xf_info_rt);

        ly = (TextView)findViewById(R.id.card_xf_info_leftY);
        ry = (TextView)findViewById(R.id.card_xf_info_rightY);

        yuTitle = (TextView)findViewById(R.id.card_xf_info_yutitle);

        montyET = (EditText) findViewById(R.id.card_xf_info_money);
        valueET = (EditText) findViewById(R.id.card_xf_info_value);
        info = (EditText) findViewById(R.id.card_xf_info_info);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("user"))
        {
            user = (UserModel) bundle.getSerializable("user");
            card = (CardTypeModel) bundle.getSerializable("card");
        }

        valueET.setEnabled(false);
        valueET.setFocusable(false);

        typeTv.setText("卡类型："+card.getType());
        img.setBackgroundColor(Color.parseColor("#"+card.getColor()));
        nameTv.setText(user.getTruename());
        telTv.setText(user.getMobile());


        if (card.getType().equals("计次卡"))
        {
            yuTv.setText(card.getValues()+"次");
            lt.setText("消费次数");
            rt.setText("实扣次数");

            ly.setVisibility(View.GONE);
            ry.setVisibility(View.GONE);

        }
        else if (card.getType().equals("打折卡"))
        {
            yuTitle.setText("折扣: ");
            yuTv.setText(card.getValues());
            lt.setText("消费金额");
            rt.setText("实扣金额");
        }
        else if(card.getType().equals("积分卡"))
        {
            yuTv.setText("￥"+card.getValues());
            lt.setText("消费金额");
            rt.setText("积分");
        }
        else
        {
            yuTv.setText("￥"+card.getValues());
            lt.setText("消费金额");
            rt.setText("实扣金额");
        }

        montyET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(card.getType().equals("打折卡"))
                {
                    try {
                        double zk = Double.parseDouble(card.getValues());
                        double v = Double.parseDouble(editable.toString().trim()) * zk;
                        DecimalFormat df   =new   java.text.DecimalFormat("#.00");

                        valueET.setText(df.format(v));
                    }
                    catch (Exception e)
                    {
                        valueET.setText("");
                    }

                }
                else
                {
                    valueET.setText(editable.toString().trim());
                }

            }
        });


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
        String bak = info.getText().toString().trim();
        String uid = ApplicationClass.APPDataCache.User.getUid();

        XNetUtil.Handle(APPService.hykAddCost(user.getUid(), user.getUsername(), card.getMcardid(),value,bak,uid), "消费成功", "消费失败", new XNetUtil.OnHttpResult<Boolean>() {
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