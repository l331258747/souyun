package com.xrwl.owner.module.publish.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 支付结果---php
 * Created by www.longdw.com on 2018/4/23 下午1:23.
 */
public class WxPayResult implements Serializable {
   private Config config;
    public Config getConfig() {
        return config;
    }
    public void setConfig(Config config) {
        this.config = config;
    }
    @Override
    public String toString() {
        return "data{" +
                "config=" + config.toString()+'}';
    }

}