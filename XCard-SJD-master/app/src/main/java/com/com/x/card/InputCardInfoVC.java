package com.com.x.card;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;

/**
 * Created by X on 16/9/4.
 */
public class InputCardInfoVC extends BaseActivity {

    private String type = "";

    private TextView typeTV;

    @Override
    protected void setupUi() {
        setContentView(R.layout.card_inputinfo);
        setPageTitle("填写卡片信息");

        typeTV = (TextView)findViewById(R.id.card_inputinfo_type);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey("type"))
        {
            type = bundle.getString("type");
            typeTV.setText("已选择: "+type);
        }

    }

    @Override
    protected void setupData() {

    }

    public void btnClick(View v)
    {

    }

}
