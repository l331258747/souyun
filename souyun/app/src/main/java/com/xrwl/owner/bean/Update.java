package com.xrwl.owner.bean;

/**
 * Created by www.longdw.com on 2018/5/10 下午7:50.
 */
public class Update {
    public String version;
    public String url;
    public String remark;
    public String update;

    public boolean canUpdate() {
        return "1".equals(update);
    }
}
