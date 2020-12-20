package com.xrwl.owner.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 实名认证
 * Created by www.longdw.com on 2018/4/29 下午8:43.
 */
public class Aliyunbank {

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getIdCard() {
        return idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    public String getAccountNo() {
        return accountNo;
    }
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public String getCardType() {
        return cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPrefecture() {
        return prefecture;
    }
    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getAddrCode() {
        return addrCode;
    }
    public void setAddrCode(String addrCode) {
        this.addrCode = addrCode;
    }
    public String getLastCode() {
        return lastCode;
    }
    public void setLastCode(String lastCode) {
        this.lastCode = lastCode;
    }
    public String status; /*状态码:01 验证通过；02 验证不通过；详见状态码说明*/
    public String msg;/*信息*/

    public String idCard; /*身份证号*/

    public String accountNo;/*银行卡号*/

    public String name;/*姓名*/
    public String bank;/*银行名称*/
    public String cardName;/*银行卡名称*/
    public String cardType;/*银行卡类型*/


    public String sex;/*性别*/
    public String area; /*身份证所在地址(参考)*/
    public String province; /*省(参考)*/
    public String city; /*市(参考)*/


    public String prefecture; /*区县(参考)*/

    public String birthday;/*出生年月*/


    public String addrCode;/*地区代码*/
    public String lastCode;   /*校验码*/



}
