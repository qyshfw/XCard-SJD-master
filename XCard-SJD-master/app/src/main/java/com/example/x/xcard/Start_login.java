package com.example.x.xcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.com.x.user.LoginVC;

public class Start_login extends AppCompatActivity {
    Button mlogin_btn,mzhuce_btn;
    Intent mintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_login);
        mlogin_btn=(Button)findViewById(R.id.login_btn);
        mzhuce_btn=(Button)findViewById(R.id.zhuce_btn);
        mlogin_btn.setOnClickListener(new Start_onClick());
        mzhuce_btn.setOnClickListener(new Start_onClick());
    }
    class Start_onClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_btn:
                    mintent=new Intent(Start_login.this, LoginVC.class);
                    startActivity(mintent);
                    break;
                case R.id.zhuce_btn:
                    mintent=new Intent(Start_login.this,Zhuceactivity.class);
                    startActivity(mintent);
                    break;
            }
        }
    }
}
