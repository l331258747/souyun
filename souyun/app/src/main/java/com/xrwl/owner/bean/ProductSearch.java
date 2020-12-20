package com.xrwl.owner.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by www.longdw.com on 2018/5/22 上午7:45.
 */
public class ProductSearch implements Serializable {
    public String productName;
    public String shortTrucks;
    public String longTrucks;
    public int category = -1;
    public String startDate;
    public String endDate;

    public List<String> shortNames;
    public List<String> longNames;
}
