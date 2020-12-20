package com.xrwl.owner.module.me.bean;

/**
 * Created by www.longdw.com on 2018/3/31 下午3:10.
 */

public class Me {
    public interface onItemClickListener {
        void onItemClick();
    }

    public static final int TYPE_HEADER = 1;
    public static final int TYPE_CONTENT = 2;

    private int icon;
    private String title;
    private onItemClickListener listener;
    private int type;

    public Me() {

    }

    public Me(int icon, String title, int type) {
        this.icon = icon;
        this.title = title;
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public onItemClickListener getListener() {
        return listener;
    }

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setItemType(int type) {
        this.type = type;
    }

    public boolean isHeader() {
        return this.type == TYPE_HEADER;
    }

    public boolean isContent() {
        return this.type == TYPE_CONTENT;
    }
}
