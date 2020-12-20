package com.xrwl.owner.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by www.longdw.com on 2018/3/25 下午4:18.
 */

public class Account {
    private boolean isFirst = true;
    public String name;//用户姓名
    public String avatar;//用户头像

    @SerializedName("user_type")
    public String type;//0：货主  1：司机

    public String phone;

    @SerializedName("user_id")
    public String id;
    @SerializedName("auth")
    public String auth;//0：未认证  1：认证过

    public String iscoupon;// 是否使用过优惠卷

    private boolean isLogin;

    private String yinhangka;
    /**
     * 软件下载文件存放的目录
     */
    @Expose
    private String downloadPath;

    private String username;
    private String password;


    public boolean isAuth() {
        return "1".equals(auth);
    }

    public boolean isOwner() {
        return "0".equals(type);
    }

    public boolean isDriver() {
        return "1".equals(type);
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIscoupon() {
        return iscoupon;
    }

    public void setIscoupon(String iscoupon) {
        this.iscoupon = iscoupon;
    }

    public String getYinhangka() {
        return yinhangka;
    }

    public void setYinhangka(String iscoupon) {
        this.yinhangka = yinhangka;
    }
}
