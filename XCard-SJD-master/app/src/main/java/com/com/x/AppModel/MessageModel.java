package com.com.x.AppModel;

import com.x.custom.DateUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by X on 2016/10/7.
 */

public class MessageModel implements Serializable {


    /**序列化
     * 信息模式
     * id : 735
     * title : 没有标题
     * content : 哈哈哈哈哈哈哈哈哈
     * create_time : 1475026275
     */

    private String id;
    private String title;
    private String content;
    private String create_time;
    private String description;

    private Boolean isGonggao = false;
    /**
     * source :
     * view : 0
     * comment : 0
     * update_time : 1474882955
     */

    private String source;
    private String view;
    private String comment;
    private String update_time;
    /**
     * picsid : 6912,
     * name : 怀府网
     * picList : [{"url":"http://7xotdz.com2.z0.glb.qiniucdn.com/2016-09-26_57e8ed5c1f2ca.jpg"}]
     */

    private String picsid;
    private String name;
    /**
     * url : http://7xotdz.com2.z0.glb.qiniucdn.com/2016-09-26_57e8ed5c1f2ca.jpg
     */

    private List<PicListBean> picList;


    public Boolean getGonggao() {
        return isGonggao;
    }

    public void setGonggao(Boolean gonggao) {
        isGonggao = gonggao;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {

        if(!create_time.contains("-"))
        {
            //当前的时间格式转化。
            create_time = DateUtils.unixToStr(create_time,"yyyy-MM-dd HH:mm:ss");
        }

        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getPicsid() {
        return picsid;
    }

    public void setPicsid(String picsid) {
        this.picsid = picsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PicListBean> getPicList() {
        return picList;
    }

    public void setPicList(List<PicListBean> picList) {
        this.picList = picList;
    }

    public static class PicListBean implements Serializable {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
