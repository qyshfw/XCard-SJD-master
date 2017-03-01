package com.example.x.xcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Zhuceactivity extends AppCompatActivity {
   Button mScond,mGetcode,msetcode;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce);
        mScond=(Button)findViewById(R.id.xiayibu);
        mScond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(Zhuceactivity.this,Zhuce2.class);
                startActivity(intent);
            }
        });
    }

}
