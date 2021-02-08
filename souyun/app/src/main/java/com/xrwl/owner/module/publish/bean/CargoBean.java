package com.xrwl.owner.module.publish.bean;

import java.util.List;

public class CargoBean {


    /**
     * oneType : {"id":1,"name":"蔬菜"}
     * twoTypes : [{"id":170,"name":"叶菜","listid":"1"},{"id":171,"name":"根菜","listid":"1"},{"id":172,"name":"蒜葱","listid":"1"},{"id":173,"name":"薯芋","listid":"1"},{"id":174,"name":"豆类","listid":"1"},{"id":175,"name":"茄果","listid":"1"},{"id":176,"name":"瓜类蔬菜","listid":"1"},{"id":177,"name":"水生蔬菜","listid":"1"},{"id":178,"name":"鲜艳菌菇","listid":"1"},{"id":179,"name":"其他蔬菜","listid":"1"}]
     */

    private int id;
    private String name;
    private String listid;
    private CargoBean oneType;
    private List<CargoBean> twoTypes;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getListid() {
        return listid;
    }

    public CargoBean getOneType() {
        return oneType;
    }

    public List<CargoBean> getTwoTypes() {
        return twoTypes;
    }
}
