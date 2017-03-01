package com.com.x.user;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.com.x.AppModel.UserModel;
import com.example.x.xcard.ApplicationClass;
import com.example.x.xcard.BaseActivity;
import com.example.x.xcard.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.x.custom.DensityUtil;
import com.x.custom.XAPPUtil;
import com.x.custom.XGetPhoto;
import com.x.custom.XNetUtil;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 16/9/8.
 */
@RuntimePermissions
public class ShopSetupVC extends BaseActivity {

    private EditText address;
    private EditText tel;
    private RoundedImageView img;
    private TextView name;
    private TextView type;
    private TextView btn;
    private ImageView banner;
    private Bitmap bannerImg;
    private Bitmap headerImg;
    String sid = ApplicationClass.APPDataCache.User.getShopid();

    XNetUtil.OnHttpResult result =  new XNetUtil.OnHttpResult<Boolean>() {
        @Override
        public void onError(Throwable e) {
            XNetUtil.APPPrintln(e);
            btn.setEnabled(true);
        }

        @Override
        public void onSuccess(Boolean aBoolean) {
            btn.setEnabled(true);
        }
    };

    private boolean isHeader = true;

    private XGetPhoto.onGetPhotoListener imgListener = new XGetPhoto.onGetPhotoListener() {
        @Override
        public void getPhoto(Bitmap img) {

            XNetUtil.APPPrintln("W: "+img.getWidth());
            XNetUtil.APPPrintln("H: "+img.getHeight());

            if(isHeader)
            {
                headerImg = img;
                Drawable drawable =new BitmapDrawable(getResources(), img);
                ShopSetupVC.this.img.setImageBitmap(img);
            }
            else
            {
                bannerImg = img;
                Drawable drawable =new BitmapDrawable(getResources(), img);
                banner.setImageBitmap(img);
            }

        }
    };

    @Override
    protected void setupUi() {
        setContentView(R.layout.shop_setup);
        setPageTitle("店铺设置");

        address = (EditText)findViewById(R.id.shop_setup_address);
        tel = (EditText)findViewById(R.id.shop_setup_tel);

        img = (RoundedImageView)findViewById(R.id.shop_setup_img);
        name = (TextView)findViewById(R.id.shop_setup_name);
        type = (TextView)findViewById(R.id.shop_setup_type);
        btn = (TextView)findViewById(R.id.shop_setup_btn);

        banner = (ImageView)findViewById(R.id.shop_setup_banner);


        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();

        int w = ApplicationClass.SW - DensityUtil.dip2px(mContext,36);
        int h = (int)(w*7.0/16.0);

        layoutParams.height = h;
        banner.setLayoutParams(layoutParams);

        XNetUtil.Handle(APPService.shopdGetShopInfo(sid), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {
                doShowToast(e.toString());
            }

            @Override
            public void onSuccess(List<UserModel> userModels) {

                if(userModels.size() > 0)
                {
                    UserModel model = userModels.get(0);
                    ApplicationClass.APPDataCache.User.setLogo(model.getLogo());
                    ApplicationClass.APPDataCache.User.setShopname(model.getShopname());
                    ApplicationClass.APPDataCache.User.setTel(model.getTel());
                    ApplicationClass.APPDataCache.User.setAddress(model.getAddress());
                    ApplicationClass.APPDataCache.User.setInfo(model.getInfo());
                    ApplicationClass.APPDataCache.User.setBanner(model.getBanner());
                    ApplicationClass.APPDataCache.User.save();

                    show();

                }




            }
        });

    }

    private void show()
    {
        ImageLoader.getInstance().displayImage(ApplicationClass.APPDataCache.User.getLogo(),img);
        name.setText(ApplicationClass.APPDataCache.User.getShopname());
        type.setText(ApplicationClass.APPDataCache.User.getShopcategory());
        address.setText(ApplicationClass.APPDataCache.User.getAddress());
        tel.setText(ApplicationClass.APPDataCache.User.getTel());

        ImageLoader.getInstance().displayImage(ApplicationClass.APPDataCache.User.getBanner(),banner);

    }

    @Override
    protected void setupData() {

    }

    public void chooseHeaderImg(final View v)
    {
        isHeader = true;
        ShopSetupVCPermissionsDispatcher.MethodAWithCheck(this);
    }

    public void chooseBannerImg(final View v)
    {
        isHeader = false;
        ShopSetupVCPermissionsDispatcher.MethodAWithCheck(this);
    }

    public void submit(final View v)
    {
        if(!XAPPUtil.isNull(address) || !XAPPUtil.isNull(tel))
        {
            return;
        }

        final String t = tel.getText().toString().trim();
        final String a = address.getText().toString().trim();

        v.setEnabled(false);

        XNetUtil.Handle(APPService.shopdUpdateShopInfo(sid,a,t), "信息修改成功", "信息修改失败", result);

        if(headerImg != null)
        {
            String sid = ApplicationClass.APPDataCache.User.getShopid();

            Map<String , RequestBody> params = new HashMap<>();
            params.put("id",createBody(sid));
            params.put("file\"; filename=\"xtest.jpg",createBody(headerImg));

            v.setEnabled(false);

            XNetUtil.Handle(APPService.shopdUpdateShopLogo(params), null, "LOGO修改失败", result);

        }

        if(bannerImg != null)
        {
            String sid = ApplicationClass.APPDataCache.User.getShopid();

            Map<String , RequestBody> params = new HashMap<>();
            params.put("id",createBody(sid));
            params.put("file\"; filename=\"xtest.jpg",createBody(bannerImg));

            v.setEnabled(false);

            XNetUtil.Handle(APPService.shopdUpdateShopBanner(params), null, "Banner修改失败", result);

        }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XGetPhoto.handleResult(requestCode,resultCode,data);
    }


    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void MethodA() {
        if(isHeader)
        {
            XGetPhoto.show(this,new XGetPhoto.XPhotoCrapOption(1,1),imgListener);
        }
        else
        {
            XGetPhoto.show(this,new XGetPhoto.XPhotoCrapOption(16,7),imgListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ShopSetupVCPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
