package com.comp.ns;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {
    /**
     * 根据手机的分辨率从dp->px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getApplicationContext()
                .getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale=context.getApplicationContext()
                .getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getApplicationContext()
                .getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getApplicationContext()
                .getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取获取系统状态栏高度。
     *
     * @param appContext APP的上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context appContext) {
        int result = 0;
        int resourceId = appContext.getResources().getIdentifier
                ("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
