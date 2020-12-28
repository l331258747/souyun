package com.xrwl.owner.module.publish.bean;

import android.text.TextUtils;

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
        return danweidezhi;
    }

    public void setDanweidezhi(String danweidezhi) {
        this.danweidezhi = danweidezhi;
    }

    public String getSheng() {
        return sheng;
    }

    public void setSheng(String sheng) {
        this.sheng = sheng;
    }

    public String getShi() {
        return shi;
    }

    public String getShengshixina(){
        String str = "";
        if(!TextUtils.isEmpty(sheng))
            str = str + sheng;
        if(!TextUtils.isEmpty(shi))
            str = str + sheng;
        if(!TextUtils.isEmpty(xian))
            str = str + sheng;
        return str;
    }

    public void setShi(String shi) {
        this.shi = shi;
    }

    public String getXian() {
        return xian;
    }

    public void setXian(String xian) {
        this.xian = xian;
    }

    public String getXiangxidezhi() {
        return xiangxidezhi;
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
