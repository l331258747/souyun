package com.ldw.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by www.longdw.com on 16/9/7 下午2:35.
 */
public class Utils {
//    public synchronized static String getLocationStr(AMapLocation location, final int index) {
//        if (null == location) {
//            return null;
//        }
//        StringBuffer sb = new StringBuffer();
//        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
//        if (location.getErrorCode() == 0) {
//            sb.append("定位成功" + "\n");
//            StringBuffer append = sb.append("定位类型: " + location.getLocationType() + "\n");
//            sb.append("经    度    : " + location.getLongitude() + "\n");
//            sb.append("纬    度    : " + location.getLatitude() + "\n");
//            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//            sb.append("提供者    : " + location.getProvider() + "\n");
//
//            if (location.getProvider().equalsIgnoreCase(
//                    android.location.LocationManager.GPS_PROVIDER)) {
//                // 以下信息只有提供者是GPS时才会有
//                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//                sb.append("角    度    : " + location.getBearing() + "\n");
//                // 获取当前提供定位服务的卫星个数
//                sb.append("星    数    : "
//                        + location.getSatellites() + "\n");
//            } else {
//                // 提供者是GPS时是没有以下信息的
//                sb.append("国    家    : " + location.getCountry() + "\n");
//                sb.append("省            : " + location.getProvince() + "\n");
//                sb.append("市            : " + location.getCity() + "\n");
//                sb.append("城市编码 : " + location.getCityCode() + "\n");
//                sb.append("区            : " + location.getDistrict() + "\n");
//                sb.append("区域 码   : " + location.getAdCode() + "\n");
//                sb.append("地    址    : " + location.getAddress() + "\n");
//                sb.append("兴趣点    : " + location.getPoiName() + "\n");
//                return (location.getAddress() + "," + location.getLongitude() + "," + location.getLatitude());
//            }
//        } else {
//            //定位失败
//            sb.append("定位失败" + "\n");
//            sb.append("错误码:" + location.getErrorCode() + "\n");
//            sb.append("错误信息:" + location.getErrorInfo() + "\n");
//            sb.append("错误描述:" + location.getLocationDetail() + "\n");
//            return sb.toString();
//        }
//        return sb.toString();
//    }
    /**
     * 开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    public static final String TAG = Utils.class.getSimpleName();

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);//InputMethodManager.SHOW_IMPLICIT
    }

    private static long lastClickTime=System.currentTimeMillis();
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 500;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取assets下文件中的字符串
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssetsString(Context context, String fileName) {

        String result = null;
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];

            is.read(buffer);

            is.close();

            result  = new String(buffer, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return result;
    }

    public static String getFileNameFromUrl(String url) {
        // 通过 ‘？’ 和 ‘/’ 判断文件名'
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }

        if (filename == null || "".equals(filename.trim())) {// 如果获取不到文件名称
            filename = UUID.randomUUID() + ".doc";// 默认取一个文件名
        }
        return filename;
    }

    /**
     * 比较时间
     * true：startDate < endDate
     */
    public static boolean compareDate(String startDate, String endDate) {
        DateFormat format = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        try {
            Date start = format.parse(startDate);
            Date end = format.parse(endDate);

            int result = start.compareTo(end);
            return result <= 0;
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 比较时间
     * true：startDate < endDate
     */
    public static boolean compareDate(String formatStr, String startDate, String endDate) {
        DateFormat format = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            Date start = format.parse(startDate);
            Date end = format.parse(endDate);

            int result = start.compareTo(end);
            return result <= 0;
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return false;
    }

    public static String convertWeekNum(int num) {
        switch (num) {
            case 0:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            default:
                return "未知";
        }
    }

    /**
     * 保存图片为JPEG
     *
     * @param bitmap
     * @param path
     */
    public static String savePic(Bitmap bitmap, String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)) {
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return file.getAbsolutePath();
    }


    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return versionCode;
    }

    /**
     * 删除指定文件下下所有文件和文件夹
     * @param folderPath
     */
    public static void deleteFolderFiles(String folderPath) {
        File file = new File(folderPath);
        if (file.exists()) {
            deleteFile(file, file.getName());
        }
    }

    private static void deleteFile(File file, String rootName){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++){
                deleteFile(files[i], rootName);
            }
        }
        if (!file.getName().equals(rootName)) {
            file.delete();
        }
    }
    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @return
//     */
//    public synchronized static String getLocationStr(AMapLocation location, final int index) {
//        if (null == location) {
//            return null;
//        }
//        StringBuffer sb = new StringBuffer();
//        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
//        if (location.getErrorCode() == 0) {
//            sb.append("定位成功" + "\n");
//            sb.append("定位类型: " + location.getLocationType() + "\n");
//            sb.append("经    度    : " + location.getLongitude() + "\n");
//            sb.append("纬    度    : " + location.getLatitude() + "\n");
//            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//            sb.append("提供者    : " + location.getProvider() + "\n");
//
//            if (location.getProvider().equalsIgnoreCase(
//                    android.location.LocationManager.GPS_PROVIDER)) {
//                // 以下信息只有提供者是GPS时才会有
//                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//                sb.append("角    度    : " + location.getBearing() + "\n");
//                // 获取当前提供定位服务的卫星个数
//                sb.append("星    数    : "
//                        + location.getSatellites() + "\n");
//            } else {
//                // 提供者是GPS时是没有以下信息的
//                sb.append("国    家    : " + location.getCountry() + "\n");
//                sb.append("省            : " + location.getProvince() + "\n");
//                sb.append("市            : " + location.getCity() + "\n");
//                sb.append("城市编码 : " + location.getCityCode() + "\n");
//                sb.append("区            : " + location.getDistrict() + "\n");
//                sb.append("区域 码   : " + location.getAdCode() + "\n");
//                sb.append("地    址    : " + location.getAddress() + "\n");
//                sb.append("兴趣点    : " + location.getPoiName() + "\n");
//                return (location.getAddress() + "," + location.getLongitude() + "," + location.getLatitude());
//            }
//        } else {
//            //定位失败
//            sb.append("定位失败" + "\n");
//            sb.append("错误码:" + location.getErrorCode() + "\n");
//            sb.append("错误信息:" + location.getErrorInfo() + "\n");
//            sb.append("错误描述:" + location.getLocationDetail() + "\n");
//            return sb.toString();
//        }
//        return sb.toString();
//    }
}
