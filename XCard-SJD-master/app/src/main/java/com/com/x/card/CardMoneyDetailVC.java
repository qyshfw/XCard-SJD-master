package com.com.x.card;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.com.x.AppModel.MoneyDetailModel;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;

/**
 * Created by X on 2016/10/8.
 */

public class CardMoneyDetailVC extends BaseActivity{

    private MoneyDetailModel model;
    private TextView time;
    private TextView state;
    private TextView type;
    private TextView value;
    private TextView money;
    private TextView bak;
    private TextView oper;

    private String title = "";

    @Override
    protected void setupUi() {
        setContentView(R.layout.money_detail);
        state = (TextView)findViewById(R.id.money_detail_state);
        time = (TextView)findViewById(R.id.money_detail_time);
        type = (TextView)findViewById(R.id.money_detail_type);
        value = (TextView)findViewById(R.id.money_detail_value);
        money = (TextView)findViewById(R.id.money_detail_money);
        bak = (TextView)findViewById(R.id.money_detail_bak);
        oper = (TextView)findViewById(R.id.money_detail_oper);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("model"))
        {
            title = bundle.getString("title");
            model = (MoneyDetailModel) bundle.getSerializable("model");
        }
        setPageTitle(title);

        time.setText(model.getCreate_time());
        type.setText("卡类型: "+model.getCname());
        bak.setText("备注: "+model.getBak());
        oper.setText("操作人: "+model.getOpername());

        if(model.getStatus().equals("-1"))
        {
            state.setText("已作废");
        }

        if(title.equals("充值明细"))
        {
            switch (model.getCname()) {
            case "计次卡":
                value.setText("充值: +"+model.getValue()+"次");
                money.setText("现金: "+model.getMoney()+"元");
                break;

            case "充值卡":
                value.setText("充值: +"+model.getValue()+"元");
                money.setText("现金: "+model.getMoney()+"元");
                break;

            default:
                break;
        }
        }
        else
        {
            int c = Color.parseColor("#bd0000");
            value.setTextColor(c);

            money.setText("");
            switch (model.getCname()) {
                case "计次卡":
                    value.setText("消费: -"+model.getValue()+"次");
                    money.setVisibility(TextView.GONE);
                    break;

                case "充值卡":
                    value.setText("消费: -"+model.getValue()+"元");
                    money.setVisibility(TextView.GONE);
                    break;
                case "打折卡":
                    money.setVisibility(TextView.VISIBLE);
                    value.setText("消费: "+model.getMoney()+"元");
                    money.setText("折扣后金额: "+model.getValue()+"元");
                    break;
                case "积分卡":
                    money.setVisibility(TextView.VISIBLE);
                    value.setText("消费: "+model.getMoney()+"元");
                    money.setText("获得积分: "+model.getValue()+"分");
                    break;
                default:
                    break;
            }
        }


    }

    @Override
    protected void setupData() {

    }
}
