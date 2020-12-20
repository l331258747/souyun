package com.xrwl.owner.module.friend.bean;

import java.io.Serializable;

public class BaseContact implements Serializable {

    //这个类型主要用在RV分组使用
    public enum BaseContactType {
        HEADER, CONTENT, SECTION
    }

    public String id;
    public String name;
    private boolean isSelected;

    private BaseContactType contactType;

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

    public BaseContactType getContactType() {
        return contactType;
    }

    public void setContactType(BaseContactType contactType) {
        this.contactType = contactType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}