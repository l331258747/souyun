package com.xrwl.owner.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class CommTools {

    public final static String LINE_SEPARATOR = System
            .getProperty("line.separator");

    /**
     * MD5加密
     *
     * @param
     * @return
     */
    public static String md5(String s) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 可逆的加密算法
    public static String reMd5Code(String inStr) {
        // String s = new String(inStr);
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 'x');
        }
        String s = new String(a);
        return s;
    }

    // 加密后解密
    public static String remd5DeCode(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 'x');
        }
        String k = new String(a);
        return k;
    }

    /**
     * 将流转换为字符串
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                is));
        final StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + LINE_SEPARATOR);
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {

            }
        }
        return sb.toString();
    }

    /**
     * 根据手机分辨率从dp转成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f) - 15;
    }

    /**
     * 获取手机状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 判断字符串是否为 空，如果有一个为 空。则返回 @return true。否则 @return false
     */
    public static boolean isStrEmpty(String... param) {

        for (String str : param) {
            if (TextUtils.isEmpty(str))
                return true;
        }
        return false;
    }

    /**
     * 正则匹配是否符合手机号格式
     */
    public static boolean isPhoneNumber(String number) {
        return Pattern.matches("^1[0-9]{10}$",
                number);
    }

    /**
     * 正则匹配是否符合数字格式
     */
    public static boolean isNumber(String number) {
        return Pattern.matches("^[0-9]+(.[0-9]+)?$",
                number);
    }

    /**
     * 正则匹配是否符合省份证格式
     */
    public static boolean isIDCard(String idCard) {

        if (Pattern.matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$", idCard)
                || Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$", idCard)) {
            return true;
        }
        return false;

    }

}
