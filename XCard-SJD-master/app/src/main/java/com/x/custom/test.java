package com.x.custom;

import java.util.List;

/**
 * Created by X on 16/8/29.
 */
public class test {

    /**
     * status : 0
     * statusMsg : 请求成功
     * list : [{"Area_Code":1,"EndTime":"2017-12-15 00:00:00","StartTime":"2016-08-15 00:00:00","StoreID":65995,"StoreName":"洛阳同昌钨钼材料有限公司洛阳同昌洛阳同昌钨钼材料有限公司洛阳同昌钨钼材料洛阳同昌钨钼材料有限公司洛阳","isauth":2,"isvip":0,"Title":"优惠3优惠3优惠3优惠3优优惠3优惠3优惠3优惠3优优惠3优惠3优惠3优惠3优优惠3优惠3优惠3优惠3优优惠3优惠3优惠3优惠惠3优惠3优惠3优惠3优惠3优惠3优惠3优惠优惠3优惠3优惠3优惠3优惠3","ID":985527},{"Area_Code":1,"EndTime":"2017-08-30 00:00:00","StartTime":"2016-08-15 00:00:00","StoreID":65995,"StoreName":"洛阳同昌钨钼材料有限公司洛阳同昌洛阳同昌钨钼材料有限公司洛阳同昌钨钼材料洛阳同昌钨钼材料有限公司洛阳","isauth":2,"isvip":0,"Title":"优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优惠2优优惠2优惠2优惠2优惠2","ID":985526},{"Area_Code":1,"EndTime":"2017-10-15 00:00:00","StartTime":"2016-08-15 00:00:00","StoreID":65995,"StoreName":"洛阳同昌钨钼材料有限公司洛阳同昌洛阳同昌钨钼材料有限公司洛阳同昌钨钼材料洛阳同昌钨钼材料有限公司洛阳","isauth":2,"isvip":0,"Title":"优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠1优惠","ID":985525}]
     */

    private int status;
    private String statusMsg;
    /**
     * Area_Code : 1
     * EndTime : 2017-12-15 00:00:00
     * StartTime : 2016-08-15 00:00:00
     * StoreID : 65995
     * StoreName : 洛阳同昌钨钼材料有限公司洛阳同昌洛阳同昌钨钼材料有限公司洛阳同昌钨钼材料洛阳同昌钨钼材料有限公司洛阳
     * isauth : 2
     * isvip : 0
     * Title : 优惠3优惠3优惠3优惠3优优惠3优惠3优惠3优惠3优优惠3优惠3优惠3优惠3优优惠3优惠3优惠3优惠3优优惠3优惠3优惠3优惠惠3优惠3优惠3优惠3优惠3优惠3优惠3优惠优惠3优惠3优惠3优惠3优惠3
     * ID : 985527
     */

    private List<ListBean> list;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int Area_Code;
        private String EndTime;
        private String StartTime;
        private int StoreID;
        private String StoreName;
        private int isauth;
        private int isvip;
        private String Title;
        private int ID;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getArea_Code() {
            return Area_Code;
        }

        public void setArea_Code(int Area_Code) {
            this.Area_Code = Area_Code;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public int getStoreID() {
            return StoreID;
        }

        public void setStoreID(int StoreID) {
            this.StoreID = StoreID;
        }

        public String getStoreName() {
            return StoreName;
        }

        public void setStoreName(String StoreName) {
            this.StoreName = StoreName;
        }

        public int getIsauth() {
            return isauth;
        }

        public void setIsauth(int isauth) {
            this.isauth = isauth;
        }

        public int getIsvip() {
            return isvip;
        }

        public void setIsvip(int isvip) {
            this.isvip = isvip;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }
    }
}
