package com.com.x.AppModel;

import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.x.custom.XAPPUtil;
import com.x.custom.XNetUtil;
import com.x.custom.XNotificationCenter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static com.example.x.xcard.ApplicationClass.APPService;

/**
 * Created by X on 2016/10/1.
 */

public class UserModel implements Serializable {


    /**
     * uid : 5
     * username : cheng
     * mobile : 18037975855
     * truename : 李先生
     * shopid : 1
     * shopname : 怀府网
     * power : 1,2,3,4,5,6,7,8,9,10,11
     * jobname : 老板
     * shopcategory : 汽车服务
     * logo : http://7xw3a7.com2.z0.glb.clouddn.com/2016-07-07_577e1db0d4f13.png
     * tel : 0391-5288100
     * address : 沁阳市文化路与香港街交叉口合庆楼六楼
     */
    private String id;
    private String uid;
    private String username;
    private String mobile;
    private String truename;
    private String shopid;
    private String shopname;
    private String power = "";
    private String jobname;
    private String shopcategory;
    private String logo;
    private String tel;
    private String address;
    private String pass;
    private String info;
    private String nickname;
    private String banner;
    private String token;

    public String getToken() {
        token = token == null ? "" : token;
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    private List<String> powerArr;

    public List<String> getPowerArr() {

        if(power == null)
        {
            power = "";
        }
       //将字符串分解。
        powerArr =  Arrays.asList(power.split(","));

        return powerArr;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public String getShopcategory() {
        return shopcategory;
    }

    public void setShopcategory(String shopcategory) {
        this.shopcategory = shopcategory;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        //强制类型的转化，。
        return "UserModel{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", truename='" + truename + '\'' +
                ", shopid='" + shopid + '\'' +
                ", shopname='" + shopname + '\'' +
                ", power='" + power + '\'' +
                ", jobname='" + jobname + '\'' +
                ", shopcategory='" + shopcategory + '\'' +
                ", logo='" + logo + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    public void save()
    {
        XNetUtil.APPPrintln(this.toString());
        boolean b = XAPPUtil.SaveAPPCache("UserModel",this);
        XNetUtil.APPPrintln("保存Model: "+b);
        XNotificationCenter.getInstance().postNotice("UserChanged",null);
    }


    public void reSet()
    {
        ModelUtil.reSet(this);
        save();
    }


    public void requestPower()
    {
        if(shopid == null || uid == null || shopid.equals("") || uid.equals(""))
        {
            return;
        }

        XNetUtil.Handle(APPService.userGetuserpower(shopid, uid), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {
                power = "";
            }

            @Override
            public void onSuccess(List<UserModel> arrs) {

                if(arrs.size()>0)
                {
                    power = arrs.get(0).getPower();
                }
                else
                {
                    power = "";
                }

            }
        });
    }

    public void unRegistNotice()
    {
        PushServiceFactory.getCloudPushService().unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                XNetUtil.APPPrintln("removeAlias success!!!!!!");
            }

            @Override
            public void onFailed(String s, String s1) {
                XNetUtil.APPPrintln("removeAlias fail!!!!!! "+s+" | "+s1);
            }
        });
    }

    public void registNotice()
    {
        if(getToken().equals(""))
        {
            return;
        }

        PushServiceFactory.getCloudPushService().bindAccount(getToken(), new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                XNetUtil.APPPrintln("addAlias success!!!!!!");
            }

            @Override
            public void onFailed(String s, String s1) {
                XNetUtil.APPPrintln("addAlias fail!!!!!! "+s+" | "+s1);
            }
        });

    }

    public void checkToken()
    {
        if(getToken().equals(""))
        {
            return;
        }

        XNetUtil.HandleReturnAll(APPService.userGetOrLine(getToken()), new XNetUtil.OnHttpResult<HttpResult<Object>>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(HttpResult<Object> res) {

                if(res.getData().getCode() == 1)
                {
                    XNotificationCenter.getInstance().postNotice("AccountLogout",null);
                }
            }
        });


    }

}
