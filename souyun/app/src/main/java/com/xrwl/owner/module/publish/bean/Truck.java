package com.xrwl.owner.module.publish.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by www.longdw.com on 2018/3/31 下午11:49.
 */

public class Truck implements Serializable {

    public String id;
    @SerializedName("car_name")
    public String title;

    @SerializedName("car_img")
    public String picUrl;


    @SerializedName("car_info") //史的长宽高
    public String length;

    public String width;
    public String height;

    @SerializedName("start_price")
    public String startPrice;//起步价

    @SerializedName("overflow_distance")
    public String startKilometre;//起步公里

    @SerializedName("overflow_price")
    public String exceedPrice;//超出价格

    @SerializedName("car_ton")
    public String capacity;



    public String zcdunkilo;//整车每公里每吨的价格
    public String zcfangkilo;//整车每公里每方的价格


    public String zccapacityfang;



    public String getZccapacityfang() {
        return zccapacityfang;
    }
    public void setZccapacityfang(String zccapacityfang) {
        this.zccapacityfang = zccapacityfang;
    }
    public String getZcdunkilo() {
        return zcdunkilo;
    }
    public void setZcdunkilo(String zcdunkilo) {
        this.zcdunkilo = zcdunkilo;
    }
    public String getZcfangkilo() {
        return zcfangkilo;
    }
    public void setZcfangkilo(String zcfangkilo) {
        this.zcfangkilo = zcfangkilo;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getStartKilometre() {
        return startKilometre;
    }

    public void setStartKilometre(String startKilometre) {
        this.startKilometre = startKilometre;
    }

    public String getExceedPrice() {
        return exceedPrice;
    }

    public void setExceedPrice(String exceedPrice) {
        this.exceedPrice = exceedPrice;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
