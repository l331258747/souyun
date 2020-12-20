package com.xrwl.owner.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/8/16 下午9:51.
 */
public class BadgeUtil {

    private int MQTT_IM_NOTIFICATION_ID=1007;
    public static void setBadge(Context context, String num) {
        if (TextUtils.isEmpty(num)) {
            return;
        }
        setBadge(context, Integer.parseInt(num));
    }
    public static void setBadge(Context context, int num) {
        String  OSName=android.os.Build.BRAND.trim().toUpperCase();
    }
 //广播
    private static boolean canResolveBroadcast(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> receivers = packageManager.queryBroadcastReceivers(intent, 0);
        return receivers != null && receivers.size() > 0;
    }
    //获取类名
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        return info.activityInfo.name;
    }
}
