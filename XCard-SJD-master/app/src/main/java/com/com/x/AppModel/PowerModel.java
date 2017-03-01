package com.com.x.AppModel;

/**
 * Created by X on 2016/10/6.
 */

public class PowerModel {


    /**
     * id : 1
     * name : 新用户开卡
     */

    private String id;
    private String name;
    private Boolean checked=false;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

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
}
