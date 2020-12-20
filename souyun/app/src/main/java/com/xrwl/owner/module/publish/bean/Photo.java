package com.xrwl.owner.module.publish.bean;

/**
 * Created by www.longdw.com on 2018/4/4 下午3:05.
 */
public class Photo {
    public static int SECTION_ITEM = 1;
    public static int SECTION_ADD = 2;

    private boolean canDelete;
    private int type = SECTION_ITEM;
    public String path;

    public Photo() {

    }

    public Photo(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setItem() {
        this.type = SECTION_ITEM;
    }

    public void setAdd() {
        this.type = SECTION_ADD;
    }

    public boolean isItem() {
        return this.type == SECTION_ITEM;
    }

    public boolean isAdd() {
        return this.type == SECTION_ADD;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }


}
