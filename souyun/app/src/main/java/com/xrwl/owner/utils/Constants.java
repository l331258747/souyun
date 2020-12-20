package com.xrwl.owner.utils;

import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:31.
 */
public class Constants {

    /** App打包时要分司机端和货主端，修改此字段来打包 */
    public static final String APP_TYPE = "2";//1：货主，2：司机

    public static final String HOST = "http://39.104.49.122:810/XingRongAppServer/";

    public static final String APP_SECRET="8880e4c916396424408d4a442d1d5dc5";
    /** 客服电话 */
    public static final String PHONE_SERVICE = "0357-2591666";

    /** 用户协议 */
    public static final String URL_PROTOCAL = "https://mp.weixin.qq" +
            ".com/s?__biz=MzA4NDkyMTA4NA==&mid=2650571550&idx=1&sn=e3b322c9403667aee50d39b0ca61a3db&chksm" +
            "=87d7fc8db0a0759b59b9bdfaee147f39cf8121f34f456de51f0d8d68f69654d648796c3a59a1#rd";

    /** 关于我们 */
    public static final String URL_ABOUT = "file:///android_asset/about.html";

    public static final String WEIXIN_KEY = "wx40197add197dfbf6";
    /** 微信支付成功后通知action */
    public static final String WX_P_SUCCESS_ACTION = "com.longdw.kb.wx.p.success";

    public static final String APP_ID = "wx40197add197dfbf6"; //替换为申请到的app id
    public static IWXAPI wx_api; //全局的微信api对象


}
