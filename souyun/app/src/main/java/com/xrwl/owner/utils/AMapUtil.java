package com.xrwl.owner.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.blankj.utilcode.util.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2018/5/15 上午8:40.
 *
 * https://github.com/MZCretin/ExternalMapUtils/
 */
public class  AMapUtil {
    public static String getFriendlyLength(int lenMeter) {
        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
            return dis + "";
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dstr = fnum.format(dis);
            return dstr;
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + "";
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + "";
    }
    public static String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";

    }
    public static boolean hasGaodeApp(Context context) {
        return isAppInstalled(context, "com.autonavi.minimap");
    }

    private static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(uri,PackageManager.GET_ACTIVITIES);
            installed =true;
        } catch(PackageManager.NameNotFoundException e) {
            installed =false;
        }
        return installed;
    }
    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }
    /**
     * 把集合体的LatLonPoint转化为集合体的LatLng
     */
    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = AMapUtil.convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }
    /**
     * 调起高德客户端 路径规划
     * 支持版本 V4.2.1 起。
     *
     * @param activity
     * @param sLongitude
     * @param sLatitude
     * @param sName
     * @param dLongitude
     * @param dLatitude
     * @param dName
     * @param appName
     */
    public static void openGaodeRouteMap(Context activity, String sLongitude, String sLatitude, String sName,
                                         String dLongitude, String dLatitude, String dName, String appName) {
        Intent intent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse("amapuri://route/plan/?sourceApplication=" + appName +
                        "&sid=&slat=" + sLatitude + "&slon=" +
                        sLongitude + "&sname=" + sName + "&did=&dlat=" +
                        dLatitude + "&dlon=" + dLongitude + "&dname=" + dName + "&dev=1&t=0"));
        intent.setPackage("com.autonavi.minimap");
        activity.startActivity(intent);
    }

    /**
     * 调起高德客户端 导航
     * 输入终点，以用户当前位置为起点开始路线导航，提示用户每段行驶路线以到达目的地。支持版本V4.1.3 起。
     * lat,lng (先纬度，后经度)
     * 40.057406655722,116.2964407172
     *
     * @param activity
     * @param appName
     */
    public static void openGaodeNaviMap(Context activity, String appName, String poiname,
                                        String latitude, String longitude) {
        Intent intent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse("androidamap://navi?sourceApplication=" + appName + "&poiname=" +
                        poiname + "&lat=" + latitude + "&lon=" + longitude + "&dev=1&style=2"));
        intent.setPackage("com.autonavi.minimap");
        activity.startActivity(intent);
    }

}
