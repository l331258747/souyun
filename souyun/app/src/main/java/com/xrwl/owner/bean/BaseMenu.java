package com.xrwl.owner.bean;

/**
 * 如果界面有滑动导航 此类作为bean 如果字段不够另行拓展
 * Created by www.longdw.com on 2018/1/23 下午2:53.
 */

public class BaseMenu {
    public String id;
    public String name;

    public BaseMenu(String name) {
        this.name = name;
    }

    public BaseMenu(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
