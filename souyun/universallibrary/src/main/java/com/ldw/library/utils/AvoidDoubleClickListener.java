package com.ldw.library.utils;

import android.view.View;

/**
 * Created by www.longdw.com on 2016/11/14 上午8:46.
 */

public abstract class AvoidDoubleClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_TIME = 600;

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        long dis = currentTime - lastClickTime;
        if (dis > MIN_CLICK_TIME) {
            lastClickTime = currentTime;
            avoidDoubleClick(v);
        }
    }

    public abstract void avoidDoubleClick(View view);
}
