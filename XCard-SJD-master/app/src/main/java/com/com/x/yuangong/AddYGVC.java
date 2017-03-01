package com.com.x.yuangong;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.com.x.AppModel.GangweiModel;
import com.com.x.AppModel.UserModel;
import com.com.x.user.FindPWVC;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.XAPPUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XGetPhoto;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.io.File;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/4.
 */
public class AddYGVC extends BaseActivity {

    String sid = ApplicationClass.APPDataCache.User.getShopid();
    private EditText tel;
    private EditText name;
    private EditText num;
    private TextView gw;

    private UserModel umodel;
    private GangweiModel gmodel;

    @Override
    protected void setupUi() {
        setContentView(R.layout.yg_manage_add);
        setPageTitle("添加员工");

        tel = (EditText)findViewById(R.id.yg_add_tel);
        name = (EditText)findViewById(R.id.yg_add_name);
        num = (EditText)findViewById(R.id.yg_add_num);
        gw = (TextView)findViewById(R.id.yg_add_gw);

        XNotificationCenter.getInstance().addObserver("AddYGVCdoChooseGW", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {

                gmodel = (GangweiModel) obj;
                gw.setText(gmodel.getName());

            }
        });

        tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                checkMember(editable.toString());

            }
        });

    }

    private void checkMember(String str)
    {
        if(str.length() != 11)
        {
            umodel = null;
            name.setText("");
            name.setFocusableInTouchMode(false);
            name.setFocusable(false);
            name.setEnabled(false);
            return;
        }

        XNetUtil.Handle(APPService.shopdGetUserInfoM(str, sid), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<UserModel> userModels) {

                if(userModels.size() > 0)
                {
                    umodel = userModels.get(0);
                    name.setText(umodel.getTruename());
                }
                else
                {
                    name.setFocusableInTouchMode(true);
                    name.setFocusable(true);
                    name.setEnabled(true);
                }

            }
        });
    }

    @Override
    protected void setupData() {

    }

    public void chooseGW(View v)
    {
        presentVC(GWChooseVC.class);
    }

    public void btnClick(final View v)
    {
        if(!XAPPUtil.isNull(tel) || !XAPPUtil.isNull(name) || !XAPPUtil.isNull(num))
        {
            return;
        }

        if(gmodel == null)
        {
            doShowToast("请选择岗位");
            return;
        }

        v.setEnabled(false);

        String tel = this.tel.getText().toString().trim();
        String num = this.num.getText().toString().trim();
        String name = this.name.getText().toString().trim();

        XNetUtil.Handle(APPService.powerAddShopWorker(tel,name,sid, gmodel.getId(),num), "员工添加成功", "员工添加失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
                v.setEnabled(true);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                v.setEnabled(!aBoolean);
                if(aBoolean)
                {
                    XNotificationCenter.getInstance().postNotice("AddYGSuccess",null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNotificationCenter.getInstance().removeObserver("AddYGVCdoChooseGW");

    }
}