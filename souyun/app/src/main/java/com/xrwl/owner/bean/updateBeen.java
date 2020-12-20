package com.xrwl.owner.bean;

public class updateBeen {

    /**
     * status_code : 200
     * msg : 发现新版本
     * data : {"update":"1","url":"http://39.104.49.122:810/XingRongAppServer/apk/xrwl_app_4.0.1.73.apk","remark":"请及时更新到最新版"}
     */

    private String status_code;
    private String msg;
    private DataBean data;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * update : 1
         * url : http://39.104.49.122:810/XingRongAppServer/apk/xrwl_app_4.0.1.73.apk
         * remark : 请及时更新到最新版
         */
        private String update;
        private String url;
        private String remark;

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
