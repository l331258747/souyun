package com.ldw.library.utils;

public class DoubleUtils {
    private static long lastClickTime;
    private static final long TIME = 300L;

    public DoubleUtils() {
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if(time - lastClickTime < TIME) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
}
