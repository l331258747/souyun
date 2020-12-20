package com.ldw.library.utils;

import java.lang.reflect.Field;

/**
 * Created by www.longdw.com on 2018/3/6 下午1:22.
 */

public class ReflectUtil {
    /**
     * 根据属性名获取属性值
     * */
    public static Object get(String fieldName, Object o) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception e) {
            return null;
        }
    }
}
