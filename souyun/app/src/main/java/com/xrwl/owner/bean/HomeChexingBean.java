package com.xrwl.owner.bean;

import com.xrwl.owner.module.publish.bean.Truck;

import java.io.Serializable;

public class HomeChexingBean implements Serializable {
    String chexing;
    Truck truck;
    int chexingType;

    public int getChexingType() {
        return chexingType;
    }

    public void setChexingType(int chexingType) {
        this.chexingType = chexingType;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public String getChexing() {
        return chexing;
    }

    public void setChexing(String chexing) {
        this.chexing = chexing;
    }
}
