package com.com.x.AppModel;
/**
 * Created by X on 2016/10/1.
 */

public class HttpResult<T> {

    /**网络数据请求的反馈信息数据。
     * ret : 200
     * data : {"code":0,"msg":"","info":[{"picurl":"http://7xotdz.com2.z0.glb.qiniucdn.com/2016-07-26_5796d6bda81bf.jpg","url":"http://101.201.169.38/city/news_info.php?id=5717&type=1","title":"推荐"}]}
     * msg :
     */

    private int ret;
    /**
     * code : 0
     *
     * msg :
     * info : [{"picurl":"http://7xotdz.com2.z0.glb.qiniucdn.com/2016-07-26_5796d6bda81bf.jpg","url":"http://101.201.169.38/city/news_info.php?id=5717&type=1","title":"推荐"}]
     */

    private DataBean<T> data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean<T> getData() {
        return data;
    }

    public void setData(DataBean<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class DataBean<T> {
        private int code;
        private String msg="";

        private String sum="";

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        /**
         * Data bean轻量级数据容器。
         * picurl : http://7xotdz.com2.z0.glb.qiniucdn.com/2016-07-26_5796d6bda81bf.jpg
         * url : http://101.201.169.38/city/news_info.php?id=5717&type=1
         * title : 推荐
         */

        private T info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;


        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public T getInfo() {
            return info;
        }

        public void setInfo(T info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", info=" + info.toString() +
                    '}';
            //字符转换。
        }
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "ret=" + ret +
                ", data=" + data.toString() +
                ", msg='" + msg + '\'' +
                '}';
    }





}
