package com.com.x.AppModel;

import com.x.custom.DateUtils;

import java.io.Serializable;

/**
 * Created by X on 2016/10/7.
 */

public class ActivityModel implements Serializable {


    /**
     * id : 6708
     * title : aaa
     * description :
     * view : 0
     * url : http://7xotdy.com2.z0.glb.qiniucdn.com/2016-09-28_57eb2acbc0496.png
     * name : 怀府网
     * create_time : 1475029707
     * s_time : 2016
     * e_time : 2016
     * content : bbbbbb
     * tel : 0376545845
     * address : dddd
     *id，标题，描述，视图，URL，名称，时间等数据的 序列化
     */

    private String id;
    private String title;
    private String description;
    private String view;
    private String url;
    private String name;
    private String create_time;
    private String s_time;
    private String e_time;
    private String content;
    private String tel;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
          //转化当前时间格式。
        if(!create_time.contains("-"))
        {
            create_time = DateUtils.unixToStr(create_time,"yyyy-MM-dd HH:mm:ss");
        }

        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getS_time() {

        if(!s_time.contains("-"))
        {
            s_time = DateUtils.unixToStr(s_time,"yyyy-MM-dd");
        }

        return s_time;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public String getE_time() {

        if(!e_time.contains("-"))
        {
            e_time = DateUtils.unixToStr(e_time,"yyyy-MM-dd");
        }

        return e_time;
    }

    public void setE_time(String e_time) {
        this.e_time = e_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
