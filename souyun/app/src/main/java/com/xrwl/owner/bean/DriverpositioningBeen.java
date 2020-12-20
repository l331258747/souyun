package com.xrwl.owner.bean;

/**
 * Created by Administrator on 2020/4/12.
 */

public class DriverpositioningBeen {


    /**
     * status_code : 200
     * msg : 读取司机坐标成功
     * data : {"userId":"3839","lon":"111.473479","lat":"36.014008","addTime":"2020-04-12"}
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
         * userId : 3839
         * lon : 111.473479
         * lat : 36.014008
         * addTime : 2020-04-12
         */

        private String userId;
        private String lon;
        private String lat;
        private String addTime;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }
    }
}
