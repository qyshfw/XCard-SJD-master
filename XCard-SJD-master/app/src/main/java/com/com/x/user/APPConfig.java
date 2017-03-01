package com.com.x.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.x.custom.FileSizeUtil;
import com.x.custom.XAPPUtil;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import static com.example.x.xcard.ApplicationClass.APPDataCache;

/**
 * Created by X on 16/9/2.
 */
public class APPConfig extends BaseActivity {

    private TextView tel;
    private TextView cacheSize;
    private Button btn;

    @Override
    protected void setupUi() {
        setContentView(R.layout.app_config);
        setPageTitle("设置");

        tel = (TextView)findViewById(R.id.app_config_tel);
        cacheSize = (TextView)findViewById(R.id.app_config_size);
        btn = (Button)findViewById(R.id.app_config_btn);

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void refreshUI()
    {
        double size = FileSizeUtil.getFileOrFilesSize(ImageLoader.getInstance().getDiskCache().getDirectory().getPath(),3);

        cacheSize.setText(size+"M");

        XNetUtil.APPPrintln("Cache Size: "+size);

        if(XAPPUtil.APPCheckIsLogin())
        {
            String tel = APPDataCache.User.getMobile();
            this.tel.setText(tel.substring(0,3)+"****"+tel.substring(7,11));
            btn.setText("退出登录");
        }
        else
        {
            tel.setText("请先登录");
            btn.setText("登录");
        }
    }

    @Override
    protected void setupData() {

    }


    public void cleanCache(View v){
        ImageLoader.getInstance().getDiskCache().clear();
        double size = FileSizeUtil.getFileOrFilesSize(ImageLoader.getInstance().getDiskCache().getDirectory().getPath(),3);
        cacheSize.setText(size+"M");
    }

    public void toChangePass(View v){

        if(checkIsLogin())
        {
            pushVC(UpdatePassVC.class);
        }

    }

    public void toFeed(View v){

        if(checkIsLogin())
        {
            pushVC(FeedBack.class);
        }

    }



    public void toUpdateTel(View v)
    {
        if(checkIsLogin())
        {
            pushVC(UpdateTelVC.class);
        }
    }

    public void doLogout(View v)
    {
        if(checkIsLogin())
        {
            AlertView alert = new AlertView("注销登录", "确定要登出账户吗?", null, null,
                    new String[]{"取消", "确定"},
                    mContext, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    System.out.println("点击了: "+position);

                    if(position == 1)
                    {
                        APPDataCache.User.unRegistNotice();
                        APPDataCache.User.reSet();

                        refreshUI();
                    }

                }
            });

            alert.show();
        }

    }


    public void btnClick(View v)
    {
        pushVC(FindPWVC.class);
    }



}
