package com.com.x.card;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.com.x.AppModel.UserModel;
import com.com.x.user.RegistVC;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.util.ArrayList;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/4.
 */
public class MakeCardVC extends BaseActivity {

    private EditText edit;
    private LinearLayout notic;
    private TextView msg;
    private TextView btn;
    private ImageView icon;
    //
    private String sid = ApplicationClass.APPDataCache.User.getShopid();
    private Spinner msp;
    //建立数据源
    List<String> list=new ArrayList<>();
    //bundle已经传有title
    private String title = "办卡";
    private UserModel user;
    @Override
    protected void setupUi() {
        setContentView(R.layout.card_banka);
        setPageTitle(title);

        XNotificationCenter.getInstance().addObserver("RegistSuccess", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                checkUser();
            }
        });
        list.add("计次卡");
        list.add("充值卡");
        list.add("积分卡");
        list.add("打折卡");
        //spinner的实例化
        msp=(Spinner)findViewById(R.id.spinner1) ;
        edit = (EditText)findViewById(R.id.card_make_edit);
        notic = (LinearLayout)findViewById(R.id.card_make_notic);
        msg = (TextView)findViewById(R.id.card_make_noticMsg);
        btn = (TextView)findViewById(R.id.card_make_btn);
        icon = (ImageView)findViewById(R.id.card_banka_icon);
        //建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msp.setAdapter(adapter);
        notic.setVisibility(View.GONE);
        decorView.setSystemUiVisibility(0);
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                checkUser();
            }
        });

    }

    private void checkUser()
    {
        //定义txt获得输入数字。
        String txt = edit.getText().toString().trim();
        //输入了11位数字后进行判断
        if(txt.length() == 11)
        {
            //网络查询，获得号码对应信息。
            XNetUtil.Handle(APPService.shopdGetUserInfoM(txt, sid), new XNetUtil.OnHttpResult<List<UserModel>>() {

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSuccess(List<UserModel> userModels) {
                    notic.setVisibility(View.VISIBLE);
                    //获得的会员信息序列化后，
                    if(userModels.size() > 0)
                    {

                        user = userModels.get(0);
                        msg.setText("会员编号: NO."+user.getUid()+", 姓名: "+user.getTruename());
                       //设置下一步的按钮的颜色，并且可以点击。
                        btn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.APPOrange));
                        btn.setClickable(true);
                    }
                    else
                    {
                        //如果在数据库中没有查询到会员信息，
                        user = null;
                        msg.setText("该手机号码暂时不是会员, 请先注册为怀府网会员");
                        btn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.APPOrange));
                        btn.setClickable(true);
                        //点击下一步后会跳转为会员注册。
                    }

                }
            });
        }
        //字数不够11位时
        else
        {
            user = null;
            btn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.CardBtnGray));
            btn.setClickable(false);
            notic.setVisibility(View.GONE);
        }

    }

    //设置数据
    @Override
    protected void setupData() {



    }
    //点击事件
    public void btnClick(View v)
    {
        //用户数据为空，没有注册过怀府网会员的。
        if(user == null)
        {
            String txt = edit.getText().toString().trim();
            Bundle bundle = new Bundle();
            bundle.putString("tel",txt);
            //跳转到注册界面
            pushVC(RegistVC.class,bundle);
        }
        else
        {
            //用户注册过怀府网
            Bundle bundle = new Bundle();
            //将数据序列化后放入bundle中，
            bundle.putSerializable("model",user);
            //启动卡类选择页面，并且传bundle过去
            pushVC(ChooseCardTypeVC.class,bundle);
        }

    }

}
