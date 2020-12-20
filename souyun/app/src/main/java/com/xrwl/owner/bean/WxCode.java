package com.xrwl.owner.bean;

import java.io.Serializable;

/**
 * Created by www.longdw.com on 2018/5/27 下午4:28.
 */
public class WxCode implements Serializable {
    //0：失败 1：成功
    public String status;
    public String code;
    public String access_token;
    public String openid;
}
