package com.ldw.library.utils;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by www.longdw.com on 2016/11/14 上午8:46.
 */

public abstract class AvoidDoubleItemClickListener implements AdapterView.OnItemClickListener {

    public static final int MIN_CLICK_TIME = 600;

    private long lastClickTime = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = System.currentTimeMillis();
        long dis = currentTime - lastClickTime;
        if (dis > MIN_CLICK_TIME) {
            lastClickTime = currentTime;
            avoidDoubleItemClick(parent, view, position, id);
        }
    }

    public abstract void avoidDoubleItemClick(AdapterView<?> parent, View view, int position, long id);
}
