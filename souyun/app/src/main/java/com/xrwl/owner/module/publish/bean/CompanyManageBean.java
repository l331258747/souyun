package com.xrwl.owner.module.publish.bean;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CompanyManageBean {


    /**
     * id : 22
     * userid : null
     * danweidezhi : null
     * sheng : null
     * shi : null
     * xian : null
     * xiangxidezhi : null
     * lianxidianhua : null
     */

    private String id;
    private String userid;
    private String danweidezhi;
    private String sheng;
    private String shi;
    private String xian;
    private String xiangxidezhi;
    private String lianxidianhua;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDanweidezhi() {
        try {
            return  (URLDecoder.decode(danweidezhi, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void setDanweidezhi(String danweidezhi) {
        this.danweidezhi = danweidezhi;
    }

    public String getSheng() {
        try {
            return  (URLDecoder.decode(sheng, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        try {
            return  (URLDecoder.decode(shi, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public String getShengshixina(){
        String str = "";
        if(!TextUtils.isEmpty(getSheng()))
            str = str + getSheng();
        if(!TextUtils.isEmpty(getShi()))
            str = str + getShi();
        if(!TextUtils.isEmpty(getXian()))
            str = str + getXian();
        return str;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getXian() {
        try {
            return  (URLDecoder.decode(xian, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void setXian(String xian) {
        this.xian = xian;
    }

    public String getXiangxidezhi() {
        try {
            return  (URLDecoder.decode(xiangxidezhi, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void setXiangxidezhi(String xiangxidezhi) {
        this.xiangxidezhi = xiangxidezhi;
    }

    public String getLianxidianhua() {
        return lianxidianhua;
    }

    public void setLianxidianhua(String lianxidianhua) {
        this.lianxidianhua = lianxidianhua;
    }
}
