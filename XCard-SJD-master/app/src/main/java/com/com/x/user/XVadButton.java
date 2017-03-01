package com.com.x.user;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robin.lazy.cache.CacheLoaderManager;
import com.x.custom.DensityUtil;
import com.x.custom.XAPPUtil;
import com.x.custom.XNetUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 2016/10/3.
 */

public class XVadButton extends Button {

    private class XVadTimeModel implements Serializable
    {
        private long time = 60;
        private long start = 0;

        public void getTimeOff()
        {
            if (start == 0) {return;}

            time = 60 - (System.currentTimeMillis()/1000 - start);
            if(time < 0)
            {
                time = 60;
                start = 0;
                save();
            }
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
            if(time == 60)
            {
                start = 0;
            }
            save();
        }

        private void save()
        {
            XAPPUtil.SaveAPPCache("XVadTimeModel",XVadTimeModel.this);
        }

        @Override
        public String toString() {
            return "XVadTimeModel{" +
                    "time=" + time +
                    ", start=" + start +
                    '}';
        }
    }

    private WeakReference<EditText> telEdTXT;
    public boolean needHas;
    private XVadTimeModel timeModel = new XVadTimeModel();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long n = timeModel.getTime()-1;
            if (n > 0)
            {
                timeModel.setTime(n);
                XVadButton.this.setText(n+"秒后重试");
                XVadButton.this.setEnabled(false);
            }
            else
            {
                timer.cancel();
                timeModel.setTime(60);
                XVadButton.this.setText("点击获取验证码");
                XVadButton.this.checkTel(telEdTXT.get().getText().toString());
            }


        }
    };

    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    };

    private boolean sending = false;

    private XNetUtil.OnHttpResult<Boolean> result = new XNetUtil.OnHttpResult<Boolean>() {
        @Override
        public void onError(Throwable e) {
            sending = false;
            XNetUtil.APPPrintln(e);
        }

        @Override
        public void onSuccess(Boolean aBoolean) {
            sending = false;
            if(aBoolean)
            {
                timeModel.setStart(System.currentTimeMillis() / 1000);
                changeState();
            }

        }
    };

    public EditText getTelEdTXT() {
        return telEdTXT.get();
    }

    public void setTelEdTXT(EditText telEdTXT) {
        this.telEdTXT = new WeakReference<EditText>(telEdTXT);

        this.telEdTXT.get().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkTel(editable.toString());
            }
        });

    }

    private void checkTel(String str)
    {
        if(timeModel.time != 60){return;}
        this.setEnabled(XAPPUtil.isPhone(str));
    }

    private void changeState()
    {
        timer.schedule(timerTask, 1000, 1000);
        this.setText(timeModel.getTime()+"秒后重试");
        this.setEnabled(false);
    }

    private void click()
    {
        if(sending)
        {
            return;
        }

        sending = true;

        String mobile = telEdTXT.get().getText().toString();
        String type = needHas ? "2" : "1";

        XNetUtil.Handle(APPService.userSmsSend(mobile, type), "短信发送成功", "短信发送失败", result);

    }

    private void initSelf()
    {
        XVadTimeModel model=CacheLoaderManager.getInstance().loadSerializable("XVadTimeModel");

        if(model != null)
        {
            timeModel = model;
        }

        timeModel.getTimeOff();


        XNetUtil.APPPrintln("timeModel: "+timeModel.toString());

        if(timeModel.getTime() < 60)
        {
            changeState();
        }


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                click();

            }
        });


    }

    public XVadButton(Context context) {
        super(context);
        initSelf();
    }

    public XVadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSelf();
    }

    public XVadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSelf();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        telEdTXT = null;

        timer.cancel();
        timer = null;

        XNetUtil.APPPrintln("XVadButton 销毁!!!!!!");

    }
}
