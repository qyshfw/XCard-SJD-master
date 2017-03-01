package com.com.x.AppModel;

import com.x.custom.DateUtils;

import java.io.Serializable;

/**
 * Created by X on 2016/10/8.
 */

public class MoneyDetailModel implements Serializable {

    /**消费详细列表
     * id : 98
     * uid : 5
     * truename : 李先生
     * mobile : 18037975857
     * cname : 计次卡
     * value : 5
     * money : 50
     * create_time : 1474775613
     * opername : 李先生
     * bak : null
     */

    private String id;
    private String uid;
    private String truename;
    private String mobile;
    private String cname;
    private String value;
    private String money;
    private String create_time;
    private String opername;
    private String bak;
    private String status;
    /**
     * shopname : 怀府网
     * xftype : 2
     * cardtype : 4
     */

    private String shopname;
    private String xftype;
    private String cardtype;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreate_time() {

        if(!create_time.contains("-"))
        {
            create_time = DateUtils.unixToStr(create_time,"yyyy-MM-dd HH:mm:ss");
        }

        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOpername() {
        return opername;
    }

    public void setOpername(String opername) {
        this.opername = opername;
    }

    public String getBak() {
        if(bak == null)
        {
            bak = "";
        }
        return bak;
    }

    public void setBak(String bak) {
        this.bak = bak;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getXftype() {
        return xftype;
    }

    public void setXftype(String xftype) {
        this.xftype = xftype;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }
}
