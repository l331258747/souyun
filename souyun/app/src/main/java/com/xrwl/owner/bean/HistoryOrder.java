package com.xrwl.owner.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/27 下午9:31.
 */
public class HistoryOrder {
    public String num;//交易次数
    @SerializedName("amount")
    public String price;//交易金额

    public String hasnext;

    public List<Order> lists;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHasnext() {
        return hasnext;
    }

    public void setHasnext(String hasnext) {
        this.hasnext = hasnext;
    }

    public List<Order> getLists() {
        return lists;
    }

    public void setLists(List<Order> lists) {
        this.lists = lists;
    }

}
