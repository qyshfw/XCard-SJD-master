package com.com.x.AppModel;

import java.io.Serializable;

/**
 * Created by X on 2016/10/7.
 */

public class CardTypeModel implements Serializable {


    /**
     * id : 80
     * logo : http://7xw3a7.com2.z0.glb.clouddn.com/2016-07-07_577e1db0d4f13.png
     * shopname : 怀府网
     * type : 计次卡
     * color : 446ab4
     * shopid : 1
     * uid : 5
     * orlq : 1
     * hcmid : 387
     * 卡类型序列化
     */

    private String id;
    private String logo;
    private String shopname;
    private String type;
    private String color;
    private String shopid;
    private String uid;
    private int orlq;
    private String hcmid;
    private String mcardid;
    private String info;
    private String cardid;
    private String values;

    public String getMcardid() {
        return mcardid;
    }

    public void setMcardid(String mcardid) {
        this.mcardid = mcardid;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        color = color.replace("#","");
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getOrlq() {
        return orlq;
    }

    public void setOrlq(int orlq) {
        this.orlq = orlq;
    }

    public String getHcmid() {
        return hcmid;
    }

    public void setHcmid(String hcmid) {
        this.hcmid = hcmid;
    }
}
