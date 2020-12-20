package com.xrwl.owner.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 实名认证
 * Created by www.longdw.com on 2018/4/29 下午8:43.
 */
public class Auth {
    /**
     * type_review=0  提交审核后，提交按钮由原来的   “ 确认”  改成  “审核中”      变成灰色，图片和认证的信息正常显示
     * type_review=1  审核通过后，提交按钮由原来的   “ 确认”  改成  “审核通过”   变成灰色，图片和认证的信息正常显示
     * type_review=2 审核未通过，提交按钮正常显示，图片和认证的信息都不显示
     * type_review=3 新注册的用户
     */
    @SerializedName("type_review")
    public String review;
    public String caozuo;

    @SerializedName("type_carload")
    public String carload;//是否是整车  0：不是  1：是
    public String name;
    @SerializedName("pic_id")

    public String picId;//身份证
    @SerializedName("pic_avatar")
    public String picAvatar;//本人照片
    @SerializedName("pic_licence")
    public String picLicence;//营业执照
    public String unit;
    public String picidshenfenzheng;//这个是身份证号码
    public String invitePhones;
    /** 以下三个字段针对司机端 */
    @SerializedName("pic_driver")
    public String picDriver;//驾驶证
    @SerializedName("pic_train")
    public String picBook;//行车本
    public String category;


    public String renzheng;

    public String birth;


    public String Identity_comparison;//身份证比对通过1
    public String Driving_comparison;//驾驶本比对通过1
    public String Car_comparison;//行车本比对通过1

    /**
     * 是否是整车
     * @return
     */
    public boolean isCarLoad() {
     //   if (!TextUtils.isEmpty(carload) && carload.equals("1")) {
            return true;
//        }
//        return false;
    }
}
