package com.xrwl.owner.module.publish.bean;

import android.text.TextUtils;

import com.xrwl.owner.bean.Account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 发布货源 所有字段
 * Created by www.longdw.com on 2018/4/16 下午9:31.
 */
 public class PublishBean implements Serializable
{


//    //新街口返回人民币的接口
//    public String xrtoken;
//    //运输类型
//    public int xrtypes;
//    //时间 (小时)
//    public String xrtime;
//    //距离 (公里)
//    public String xrdistances;
//    //车型(对应type,车型只有整车有)
//    public int xrcar_type;
//    // 吨
//    public String xrtons;
//    //方
//    public String xrsquares;
//    //件
//    public String xrpiece;
//    //返回数据的价格
//    public int xrpay;



    public String qinyanwei;
     public int category = -1;//配送类型
    //    public String truckDes;//车型描述
//    public String truckId;//已选车型
    public Truck truck;
    public String truckIds;
    /**
     * 同城配送状态下 发货经纬度
     */
    public double defaultStartLon;
    public double defaultStartLat;
    /**
     * 同城配送状态下 到货经纬度
     */
    public double defaultEndLon;
    public double defaultEndLat;

    /**
     * 同城配送状态下 发货和收货位置描述信息
     */
    public String defaultStartPosDes;
    public String defaultEndPosDes;

    public String productName;//货物名称
    public String company;
    public String shouhuodanweiname;
    /**
     * 吨 方 件
     */
    public String defaultWeight;
    public String defaultArea;
    public String defaultNum;

    public String defaultNo;

    public String zhesunlv;
    public String danjia;

    public String consignorName;//发货人
    public String consignorId;//发货人id
    public String consignorPhone;//发货人号码

    public String consigneeName;//收货人
    public String consigneeId;//收货人id
    public String consigneePhone;//收货人号码
    public String remark;//备注
    public String datapay;//总钱叔
    /**
     * 长途状态下 件
     */
    public String longNum;
    /**
     * 长途状态下 发货地址 省市区的id
     */
    public int longStartProvinceId = -1;
    public int longStartCityId = -1;
    public int longStartAreaId = -1;
    /**
     * 长途状态下 收货地址 省市区的id
     */
    public int longEndProvinceId = -1;
    public int longEndCityId = -1;
    public int longEndAreaId = -1;

    /**
     * 长途状态下 发货和收货省市区描述信息
     */
    public String longStartProvinceDes;
    public String longStartCityDes;
    public String longStartAreaDes;
    public String longEndProvinceDes;
    public String longEndCityDes;
    public String longEndAreaDes;

    public String startDesc;//发货详细地址
    public String endDesc;//收货详细地址

    public String distance;//公里数 由服务器计算得出
    public String duration;//用的时间 由服务器计算得出
    public String start;//公里数 由服务器计算得出
    public String end;//用的时间 由服务器计算得出

    public String startarea;//发货所在的区域
    public String endarea;//到货所在的区域

    /**
     * 长途状态下 货物重量：吨、单价、金额
     */
    public String longWeight;
    public String singleTonPrice;
    public String totalTonPrice;
    /**
     * 长途状态下 货物体积：方、单价、金额
     */
    public String longArea;
    public String singleAreaPrice;
    public String totalAreaPrice;

    public String freight;//运费
    public String isReceipt;//是否需要发票 0或1
    public String taxNum;//税号
    public String unitName;//单位名称
    public String email;

    public String isHelpPay;//是否代付  0或1
    public String helpPayId;//代付人id

    public String isHelpGet;//是否代收  0或1
    public String helpGetId;//代收人id
    public String getPrice;//代收货款
    public String nidaye;//......
    public String insurance = "0";//0：没有保险 50元，以此类推
    public String coupon = "0";//0：不优惠 1：满1000减50，以此类推

    public String finalPrice;//最终价格 合计价格
    public String date;//订单日期

    public String isSendBySelf;//0、1 自送
    public String isPickBySelf;//0、1 自提
    public String display;
    /**
     * 计算城市的经纬度 发货起始点
     */
    public String startX;//经度
    public String startY;//纬度
    public String endX;
    public String endY;

    public String time;

    public ArrayList<String> imagePaths;

    public String packingType;

    public String capacity;

    public Account mAccount;

    public String sendend_lon;
    public String sendend_lat;
    public String pickstart_lon;
    public String pickstart_lat;


    public boolean check() {
        if (category == -1) {
            return false;
        }

        if (TextUtils.isEmpty(consigneeName) || TextUtils.isEmpty(consigneePhone) ||
                TextUtils.isEmpty(consignorPhone)) {
            return false;
        }

//        if (TextUtils.isEmpty(time)) {
//            return false;
//        }

        if (category == 0) {
            if (defaultStartLat == 0 || defaultStartLon == 0) {
                return false;
            }
            if (defaultEndLat == 0 || defaultEndLon == 0) {
                return false;
            }

            return true;
        }
       else if (category == 6) {
            if (defaultStartLat == 0 || defaultStartLon == 0) {
                return false;
            }
            if (defaultEndLat == 0 || defaultEndLon == 0) {
                return false;
            }

            return true;
        }
        else if(category==5)
        {
            if (defaultStartLat == 0 || defaultStartLon == 0) {
                return false;
            }
            if (defaultEndLat == 0 || defaultEndLon == 0) {
                return false;
            }
            return true;
        }
        else {
            if (longStartProvinceDes == null || longStartCityDes == null || longStartAreaDes == null) {
                return false;
            }
            if (longEndProvinceDes == null || longEndCityDes == null || longEndAreaDes == null) {
                return false;
            }
            return true;
        }
    }

    public String getStartPos() {
        if (category == 0) {
            return defaultStartPosDes;
        }
        else if(category==5)
        {
            return defaultStartPosDes;
        }
        else if(category==6)
        {
            return defaultStartPosDes;
        }
        else {
            return longStartProvinceDes + longStartCityDes + longStartAreaDes;
        }
    }



    public String getStartPosshengshi() {

            return longStartProvinceDes + longStartCityDes;

    }

    public String getStartPosdezhi() {

            return longStartAreaDes;

    }


    public String getDatapay()
    {
        return  datapay;
    }
    public String getEndPos() {
        if (category == 0) {
            return defaultEndPosDes;
        }
        else if(category==5)
        {
            return  defaultEndPosDes;
        }
        else if(category==6)
        {
            return  defaultEndPosDes;
        }
        else {
            return longEndProvinceDes + longEndCityDes + longEndAreaDes;
        }
    }


    public String getEndPosshengshi() {

            return longEndProvinceDes + longEndCityDes;

    }

    public String getEndPosdezhi() {

            return longEndAreaDes;

    }
    public String getWeight() {
       if(TextUtils.isEmpty(defaultWeight))
       {
           return "0";
       }
       else
       {
           return defaultWeight;
       }

    }
    public String getGet_person() {
        if(TextUtils.isEmpty(consigneeName))
        {
            return "0";
        }
        else
        {
            return consigneeName;
        }

    }
    public String getArea() {
        if(TextUtils.isEmpty(defaultArea))
        {
            return "0";
        }
        else
        {
            return defaultArea;
        }
    }

    public String getDistance() {
//        if (category == 0) {
//            LatLng ll1 = new LatLng(defaultStartLat, defaultStartLon);
//            LatLng ll2 = new LatLng(defaultEndLat, defaultEndLon);
//
//            return String.valueOf((int)(AMapUtil.calculateLineDistance(ll1, ll2) * 0.001));
//        } else {
//            return "";
//        }
        return distance;
    }
    public String getDuration() {
//        if (category == 0) {
//            LatLng ll1 = new LatLng(defaultStartLat, defaultStartLon);
//            LatLng ll2 = new LatLng(defaultEndLat, defaultEndLon);
//
//            return String.valueOf((int)(AMapUtil.calculateLineDistance(ll1, ll2) * 0.001));
//        } else {
//            return "";
//        }
        return duration;
    }
    public String getTruckIds()
    {
        return  truck.getId();
    }
//    public String getStartarea() {
//        return startarea;
//    }
//    public String getEndarea() {
//        return endarea;
//    }
    public String getStart() {
//        if (category == 0) {
//            LatLng ll1 = new LatLng(defaultStartLat, defaultStartLon);
//            LatLng ll2 = new LatLng(defaultEndLat, defaultEndLon);
//
//            return String.valueOf((int)(AMapUtil.calculateLineDistance(ll1, ll2) * 0.001));
//        } else {
//            return "";
//        }
        return start;
    }
    public String getEnd() {
//        if (category == 0) {
//            LatLng ll1 = new LatLng(defaultStartLat, defaultStartLon);
//            LatLng ll2 = new LatLng(defaultEndLat, defaultEndLon);
//
//            return String.valueOf((int)(AMapUtil.calculateLineDistance(ll1, ll2) * 0.001));
//        } else {
//            return "";
//        }
        return end;
    }
    private float getDistanceFloat() {
        if (TextUtils.isEmpty(distance)) {
            distance = "0";
        }
        return Float.parseFloat(distance);
    }
    private float getDurationFloat() {
        if (TextUtils.isEmpty(duration)) {
            duration = "0";
        }
        return Float.parseFloat(duration.replace("小时",""));
    }
    private float getStartFloat() {
        if (TextUtils.isEmpty(start)) {
            start = "0";
        }
        return Float.parseFloat(start);
    }
    private float getEndFloat() {
        if (TextUtils.isEmpty(end)) {
            end = "0";
        }
        return Float.parseFloat(end);
    }
    public String getNum() {

        if(TextUtils.isEmpty(defaultNum))
        {
            return  "0";
        }
        else
        {
            return defaultNum;
        }
//        if (category == 0) {
//            return defaultNum;
//        } else {
//            return longNum;
//        }
       // return defaultNum.replace("——","");
    }

    public int getNidaye() {



            if (TextUtils.isEmpty(nidaye)) {
                return 0;
            } else
                return Integer.valueOf(nidaye);




     }
      public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("category", category + "");//配送类型
        params.put("start_lon", defaultStartLon + "");//发货起点经度
        params.put("start_lat", defaultStartLat + "");//发货起点纬度
        params.put("end_lon", defaultEndLon + "");//到货终点经度
        params.put("end_lat", defaultEndLat + "");//到货终点纬度

        if (category == 0) {
            params.put("truckid", "0");//已选车型
            params.put("start_city", longStartCityDes);
            params.put("end_city", longEndCityDes);
            params.put("start_province", longStartProvinceDes);
            params.put("end_province", longEndProvinceDes);
//            params.put("start_area", getStartarea());
//            params.put("end_area", getEndarea());
        }
        else if(category==5)
        {
            params.put("truckid", truck.getId());//已选车型
            params.put("start_city", longStartCityDes);
            params.put("end_city", longEndCityDes);
            params.put("shijian",getDuration());//路程用时
            params.put("start_province", longStartProvinceDes);
            params.put("end_province", longEndProvinceDes);

        }
        else if(category==6)
        {
            params.put("truckid", "43");//已选车型
            params.put("start_city", longStartCityDes);
            params.put("end_city", longEndCityDes);
            params.put("shijian",getDuration());//路程用时
            params.put("start_province", longStartProvinceDes);
            params.put("end_province", longEndProvinceDes);
        }
        else if(category==2)
        {

            params.put("truckid", "0");//已选车型
            params.put("start_province", longStartProvinceDes);
            params.put("start_city", longStartCityDes);
            params.put("end_province", longEndProvinceDes);
            params.put("end_city", longEndCityDes);


            params.put("sendend_lon", sendend_lon);
            params.put("sendend_lat", sendend_lat);
            params.put("pickstart_lon", pickstart_lon);
            params.put("pickstart_lat", pickstart_lat);
        }
        else {

            params.put("truckid", "8808");//已选车型 8808代表是跑腿车型
            params.put("start_province", longStartProvinceDes);
            params.put("start_city", longStartCityDes);
            params.put("end_province", longEndProvinceDes);
            params.put("end_city", longEndCityDes);
        }

        params.put("company",company);//厂家名称
        params.put("shouhuodanweiname", shouhuodanweiname);
        params.put("zhesunlv", zhesunlv);//这损率
        params.put("danjia", danjia);//单价

        params.put("start_area", getStart());//同城起始的县或区
        params.put("end_area", getEnd());//同城终点的县或者区



        params.put("weight", defaultWeight.replace("——",""));//同城吨
        params.put("area", defaultArea.replace("——",""));//同城方
         params.put("num", defaultNum.replace("——",""));//同城件

        if(TextUtils.isEmpty(productName))
        {
            params.put("product_name", "未填写");//货物名称
        }
        else
        {
            params.put("product_name", productName);//货物名称
        }
        params.put("publish_phone", consignorPhone);//发货电话
        params.put("get_person", consigneeName);//收货人
        params.put("get_phone", consigneePhone);//收货电话
        params.put("remark", remark);
        params.put("freight", String.valueOf(freight));
        params.put("insurance", insurance);//保险
        params.put("is_receipt", isReceipt);//是否要发票
        params.put("taxnum", taxNum);//税号
        params.put("unitname", unitName);//单位名称
        params.put("email", email);//email

        params.put("is_helppay", isHelpPay);//是否代付
        params.put("helppay_id", helpPayId);//代付人id
        params.put("is_helpget", isHelpGet);//是否代收
        params.put("helpget_id", helpGetId);//代收人id
        params.put("helpget_price", getPrice);//代收货款

        params.put("coupon", coupon);//优惠券
        params.put("total_price", finalPrice);//合计总价
        params.put("kilo", getDistance());//公里数


        params.put("is_sendbyself", isSendBySelf);//自送
        params.put("is_pickbyself", isPickBySelf);//自提

        params.put("statx", startX);
        params.put("staty", startY);
        params.put("endx", endX);
        params.put("endy", endY);

        params.put("start_desc", startDesc);
        params.put("end_desc", endDesc);

        params.put("addtime", time);

        params.put("packingtype", packingType);

        params.put("capacity", capacity);

        params.put("display",display);
        return params;
  }


}
