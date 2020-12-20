package com.xrwl.owner.event;

/**
 * Created by www.longdw.com on 2018/8/16 下午8:48.
 */
public class BusinessTabCountEvent {
    private String count;
    public BusinessTabCountEvent(String count) {
        this.count = count;
    }

    public String getCount() {
        return count;
    }
}
