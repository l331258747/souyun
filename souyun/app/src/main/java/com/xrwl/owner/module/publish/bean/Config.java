package com.xrwl.owner.module.publish.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 支付结果---php
 * Created by www.longdw.com on 2018/4/23 下午1:23.
 */
public class Config implements Serializable {
    public String appid;
    public String partnerid;
    public String prepayid;
    @SerializedName("package")
    public String packagestr;
    public String noncestr;
    public String timestamp;
    public String sign;

    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getAppid() {
        return appid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }
    public String getPartnerid() {
        return partnerid;
    }


    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }
    public String getPrepayid() {
        return prepayid;
    }


    public void setPackagestr(String packagestr) {
        this.partnerid = packagestr;
    }
    public String getPackagestr() {
        return packagestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }
    public String getNoncestr() {
        return noncestr;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getTimestamp() {
        return timestamp;
    }


    public void setSign(String sign) {
        this.partnerid = sign;
    }
    public String getSign() {
        return sign;
    }


}
