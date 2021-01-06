package com.xrwl.owner.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.xrwl.owner.module.publish.bean.Photo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/28 上午10:08.
 */
public class OrderDetail {

    /**
     * 如果是货主，按如下规则
     * 0--取消订单显示，定位司机和确认收获不显示
     * 1--取消订单不显示，定位司机和确认收获显示
     * 2--取消订单不显示，定位司机和确认收获显示
     * 3--都不显示
     *
     * 如果是司机，按如下规则
     * 0--抢单显示，其他不显示
     * 1--取消、线路导航、中转显示，确认到货、抢单不显示
     * 2--线路导航、中转、确认到货显示，抢单、取消订单不显示
     * 3--都不显示
     */

    public String type;
    public String isbzj_type;
    @SerializedName("start_pos")
    public String startPos;
    @SerializedName("end_pos")
    public String endPos;
    @SerializedName("product_name")
    public String productName;

    public String start_desc;
    public String end_desc;
    public String truck;
    public String freight;

    @SerializedName("total_price")
    public String price;
    public String weight;
    public String area;
    public String num;
    public String kilo;
    public String pic;
    public String driver;
    @SerializedName("Receiving")

    public String upload;//司机端 是否显示上传图片

    public String lon;
    public String lat;

    public String times;//获得高德地图的时间
    public String tixing;


    /** 以下针对司机导航 */
    @SerializedName("category")
    public String category;
    @SerializedName("start_lon")
    public String startLon;
    @SerializedName("start_lat")
    public String startLat;
    @SerializedName("end_lon")
    public String endLon;
    @SerializedName("end_lat")
    public String endLat;
    /** ~~~~~~ */

    @SerializedName("publish_phone")
    public String consignorPhone;//发货电话
    @SerializedName("get_phone")
    public String consigneePhone;//收货电话

    public String remark;//备注

    public String pay;//是否显示支付

    public String onpay;//只针对司机详情是否显示微信或支付宝支付

    @SerializedName("id")
    public String orderId;//订单编号

    @SerializedName("status")
    public String orderStatus;//抢单是否成功1：成功 0：有人已经抢走订单了

    public String packingtype;//包装类型

    public String overtotal_price;//剩余付款金额

    public String huozhudianji;//货主点击开始运输

    public String sijidianji;
    public String ddbh;
    public String total_prices;//helpget_price
    public String daishoutype;//代收货款
    public String shouhuorenid;//代收人id
    public String userid;//代收人id
     public  String isquerenshouhuo;

    public String jieguolo;
    public String date;
    public String hongbao;
    public String renmibi;
    public String is_sendbyself;
    public String is_pickbyself;
    public String drivername;
    public String drivertel;
    public String drviercar;


    /**
     * 点评需要返回的数据显示*/
    public String username;
    public String dianhua;
    public String chexing;
    public String chehao;
    public String pingfen;

    public String getDrivername() {
        return drivername;
    }

    public String getDrivertel() {
        return drivertel;
    }

    public String getDrivertelXing() {
        if(TextUtils.isEmpty(drivertel)){
            return "";
        }
        if(drivertel.length() == 11){
            StringBuilder sb = new StringBuilder(drivertel);
            sb.replace(3, 7, "****");
            sb.substring(7, 11);
            return sb.toString();
        }else {
            return drivertel;
        }
    }

    public String getDrviercar() {
        return drviercar;
    }

    public String getDrviercarStr() {
        try {
            return  (URLDecoder.decode(drviercar, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public String WaterId;//退款的那个值

    public String Final_payment;//剩余尾款
    public String PaymentMethod;//支付方式
    public List<Photo> getPics() {
        if (!TextUtils.isEmpty(pic)) {

            String[] paths = pic.split(",");
            List<Photo> datas = new ArrayList<>();
            for (String path : paths) {
                Photo photo = new Photo();
                photo.setCanDelete(false);
                photo.setPath(path);
                photo.setItem();

                datas.add(photo);
            }
            return datas;
        }
        return new ArrayList<>();
    }

    public boolean canUpload() {
        return !"0".equals(upload);
    }

    public boolean showPay() {
        return "1".equals(pay);
    }

    public boolean showOnPay() {
        return "1".equals(onpay);
    }


    public String getFinal_Payment() {
        return Final_payment;
    }

    public String getUsername() {
        return username;
    }
    public String getDianhua() {
        return dianhua;
    }

    public String getChexing() {
        return chexing;
    }

    public String getChehao() {
        return chehao;
    }

    public String getPingfen() {
        return pingfen;
    }
    public String duqu;//交易金额
    public String getDuqu() {
        return duqu;
    }

}
