package com.xrwl.owner.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 提交订单后返回的数据
 * Created by www.longdw.com on 2018/4/19 下午1:30.
 */
public class PostOrder implements Serializable {
    public String date;
    public String truck;
    @SerializedName("start_pos")
    public String startPos;
    @SerializedName("end_pos")
    public String endPos;
    @SerializedName("order_id")
    public String orderId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String getStartPos() {
        return startPos;
    }

    public void setStartPos(String startPos) {
        this.startPos = startPos;
    }

    public String getEndPos() {
        return endPos;
    }

    public void setEndPos(String endPos) {
        this.endPos = endPos;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
