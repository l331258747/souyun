package com.xrwl.owner.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:20.
 */
public class Order implements Serializable {
    public String id;
    @SerializedName("publish_phone")
    private String phone;

    @SerializedName("start_pos")
    public String startPos;
    @SerializedName("end_pos")
    public String endPos;
    @SerializedName("product_name")
    public String productName;
    @SerializedName("truck")
    public String truck;
    public String weight;
    public String area;
    public String num;
    public String driver;
    public String date;
    @SerializedName("driverid")
    public String driverId;
    public String packingtype;
    public int hit;


    public String start_provinces;//山西省
    public String start_poss;//临汾市
    public String start_areas;//尧都区
    public String start_descss;//兴荣物流联盟综合创业
    public String end_provinces;//山西省
    public String end_poss;//临汾市
    public String end_areas;//襄汾县
    public String end_descss;//荷花公园

    public String kilo;
    public String freight;
    public String getKilo() {
        return kilo;
    }

    public void setKilo(String kilo) {
        this.kilo = kilo;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }



    public String getStart_provinces() {
        return start_provinces;
    }

    public void setStart_provinces(String start_provinces) {
        this.start_provinces = start_provinces;
    }

    public String getStart_poss() {
        return start_poss;
    }

    public void setStart_poss(String start_poss) {
        this.start_poss = start_poss;
    }

    public String getStart_areas() {
        return start_areas;
    }

    public void setStart_areas(String start_areas) {
        this.start_areas = start_areas;
    }

    public String getStart_descss() {
        return start_descss;
    }

    public void setStart_descss(String start_descss) {
        this.start_descss = start_descss;
    }

    public String getEnd_provinces() {
        return end_provinces;
    }

    public void setEnd_provinces(String end_provinces) {
        this.end_provinces = end_provinces;
    }

    public String getEnd_poss() {
        return end_poss;
    }

    public void setEnd_poss(String end_poss) {
        this.end_poss = end_poss;
    }

    public String getEnd_areas() {
        return end_areas;
    }

    public void setEnd_areas(String end_areas) {
        this.end_areas = end_areas;
    }

    public String getEnd_descss() {
        return end_descss;
    }

    public void setEnd_descss(String end_descss) {
        this.end_descss = end_descss;
    }

    public int getHit() {
        return hit;
    }


    public void setHit(int hit) {
        this.hit = hit;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getPackingtype() {
        return packingtype;
    }
    public void setPackingtype(String packingtype) {
        this.packingtype = packingtype;
    }

}
