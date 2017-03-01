package com.com.x.huodong;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.x.custom.DensityUtil;
import com.x.custom.XAPPUtil;
import com.x.custom.XActivityindicator;
import com.x.custom.XGetPhoto;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.example.x.xcard.ApplicationClass.APPService;
import static com.robin.lazy.util.DateKit.getDate;
import static com.robin.lazy.util.DateKit.getTime;

/**
 * Created by X on 16/9/7.
 */


@RuntimePermissions
public class HDCreateVC  extends BaseActivity {

    private EditText title;
    private EditText address;
    private EditText tel;
    private EditText info;

    private TextView stime;
    private TextView etime;

    private Button header;
    private Bitmap headerImg;
    private TimePickerView datePicker;

    private boolean isStime = false;
    private  Date sdate;
    private  Date edate;

    @Override
    protected void setupUi() {
        setContentView(R.layout.hd_create);
        setPageTitle("发布活动");

        header = (Button) findViewById(R.id.hd_create_header);

        title = (EditText) findViewById(R.id.hd_create_title);
        address = (EditText) findViewById(R.id.hd_create_address);
        tel = (EditText) findViewById(R.id.hd_create_tel);
        info = (EditText) findViewById(R.id.hd_create_info);

        stime = (TextView) findViewById(R.id.hd_create_stime);
        etime = (TextView) findViewById(R.id.hd_create_etime);

        ViewGroup.LayoutParams layoutParams = header.getLayoutParams();

        int w = ApplicationClass.SW - DensityUtil.dip2px(mContext,36);
        int h = (int)(w*7.0/16.0);

        layoutParams.height = h;
        header.setLayoutParams(layoutParams);


        //时间选择器
        datePicker = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
//        Calendar calendar = Calendar.getInstance();
//        datePicker.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦

        datePicker.setTime(new Date());
        datePicker.setCyclic(false);
        datePicker.setCancelable(true);
        //时间选择后回调
        datePicker.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if(isStime)
                {
                    sdate = date;
                    stime.setText(getDate(date));
                }
                else
                {
                    edate = date;
                    etime.setText(getDate(date));
                }
                //tvTime.setText(getTime(date));
            }
        });
    }

    @Override
    protected void setupData() {

    }

    public void doSubmit(final View v)
    {
        if(headerImg == null)
        {
            doShowToast("请选择活动图片!");
            return;
        }

        if(!XAPPUtil.isNull(title) || !XAPPUtil.isNull(address)
                || !XAPPUtil.isNull(tel) || !XAPPUtil.isNull(info))
        {
            doShowToast("请完善活动信息!");
            return;
        }

        if(sdate == null || edate == null)
        {
            doShowToast("请选择开始和结束时间!");
            return;
        }

        if(sdate.getTime()>edate.getTime())
        {
            doShowToast("结束时间不能小余开始时间!");
            return;
        }

        XActivityindicator.create(mContext).show();

        String sid = ApplicationClass.APPDataCache.User.getShopid();
        String uid = ApplicationClass.APPDataCache.User.getUid();

        Map<String , RequestBody> params = new HashMap<>();

        params.put("uid",createBody(uid));
        params.put("shopid",createBody(sid));
        params.put("title",createBody(title.getText().toString().trim()));
        params.put("stime",createBody(sdate.getTime()/1000+""));
        params.put("etime",createBody(edate.getTime()/1000+""));
        params.put("address",createBody(address.getText().toString().trim()));
        params.put("tel",createBody(tel.getText().toString().trim()));
        params.put("content",createBody(info.getText().toString().trim()));

        params.put("file\"; filename=\"xtest.jpg",createBody(headerImg));

        v.setEnabled(false);

        XNetUtil.Handle(APPService.shopaAddShopHD(params), "发布活动成功", "发布活动失败", new XNetUtil.OnHttpResult<Boolean>() {
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
                    XNotificationCenter.getInstance().postNotice("CreateHDSuccessed",null);

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

    private RequestBody createBody(Object obj)
    {
        if(obj instanceof String)
        {
           return RequestBody.create(MediaType.parse("text/plain"), (String) obj);
        }
        else
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ((Bitmap)obj).compress(Bitmap.CompressFormat.JPEG, 50, baos);
            return RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
        }
    }

    public void chooseStartTime(View v)
    {
        isStime = true;
        datePicker.show();
    }

    public void chooseEndTime(View v)
    {
        isStime = false;
        datePicker.show();
    }

    public void chooseHeaderImg(View v)
    {
        HDCreateVCPermissionsDispatcher.MethodAWithCheck(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XGetPhoto.handleResult(requestCode,resultCode,data);
    }


    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void MethodA() {

                XGetPhoto.show(this,new XGetPhoto.XPhotoCrapOption(16,7), new XGetPhoto.onGetPhotoListener() {
            @SuppressLint("NewApi")
            @Override
            public void getPhoto(Bitmap img) {

                headerImg = img;
                Drawable drawable =new BitmapDrawable(getResources(), img);
                header.setBackground(drawable);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HDCreateVCPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);


    }

    @OnShowRationale({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void MethodB(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void MethodC() {

        Toast.makeText(this, "被拒绝了~~~", Toast.LENGTH_SHORT).show();

    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void MethodD() {

        Toast.makeText(this, "不再询问", Toast.LENGTH_SHORT).show();
    }
}
