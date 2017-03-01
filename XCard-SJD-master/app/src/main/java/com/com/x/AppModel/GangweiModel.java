package com.com.x.AppModel;

import java.io.Serializable;

/**
 * Created by X on 2016/10/6.
 */

public class GangweiModel implements Serializable {


    /**岗位
     * id : 5
     * name : 测试1
     * power : 1,2,3
     * 序列化。
     */

    private String id;
    private String name;
    private String power="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        if(power == null){power="";}
        return power;
    }

    public void setPower(String power) {
        if(power == null){return;}
        this.power = power;
    }


    @Override
    public String toString() {
        return "GangweiModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", power='" + power + '\'' +
                '}';
    }
}
