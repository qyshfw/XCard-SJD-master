package com.example.x.xcard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.x.custom.XNetUtil;

import static com.example.x.xcard.ApplicationClass.APPDataCache;

/**
 * Created by X on 2016/10/11.
 */

public class LaunchPage extends AppCompatActivity{

    private View decorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.launch);
        // 启动的欢迎图片。
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /* Create an Intent that will start the Main WordPress Activity. */
                XNetUtil.APPPrintln("APPDataCache: "+APPDataCache);
                XNetUtil.APPPrintln("APPDataCache.User: "+APPDataCache.User);
                Intent mainIntent = new Intent(LaunchPage.this, Start_login.class);
                Intent mIntent=new Intent(LaunchPage.this,MainActivity.class);

                if(APPDataCache.User.getUid().equals(""))
                {
                    //如果缓存中不存在用户，则启动登录界面
                    LaunchPage.this.startActivity(mainIntent);
                    LaunchPage.this.finish();
                }else{
                    //如果存在用户信息，则进入主界面。
                    //git 怎么使用
                    startActivity(mIntent);
                    LaunchPage.this.finish();
                }
            }
        }, 3000);
        XNetUtil.APPPrintln("$$$$$$$$$$$$$$$$$$$$");
    }


    //欢迎动画充满全屏
    public void setFullScreen()
    {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    public void setContentView(int layoutResID) {
        //decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
        //通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。间接可以计算状态栏高度。
        decorView = getWindow().getDecorView();

        setFullScreen();

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            //用于状态栏是否隐藏的监听
            public void onSystemUiVisibilityChange(int i) {

                if(i == 0)
                {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                }
                System.out.println("onSystemUiVisibilityChange: "+i);

            }
        });
        //让window占满整个手机屏幕，不留任何边界（border）
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        // window大小不再不受手机屏幕大小限制，即window可能超出屏幕之外，这时部分内容在屏幕之外。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.setContentView(layoutResID);
    }

//当前窗体得到或失去焦点的时候的时候调用
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        System.out.println("onWindowFocusChanged !!!!!!");

        if (hasFocus && Build.VERSION.SDK_INT >= 19) {

            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//Activity全屏显示，且状态栏被隐藏覆盖掉。
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏虚拟按键(导航栏)。
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);//让系统栏在一段时间后自动隐藏，
        }

    }

}
