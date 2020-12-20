package com.ldw.library.utils;

import android.content.Context;

/**
 * Created by www.longdw.com on 2018/1/14 上午10:46.
 */

public class DisplayUtil {

    public static int dp2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }

    /**
     * get Pixel size by sp
     *
     * @param sp sp
     * @return the value of px
     */
    public static float getPixelSizeBySp(Context context, int sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

}
