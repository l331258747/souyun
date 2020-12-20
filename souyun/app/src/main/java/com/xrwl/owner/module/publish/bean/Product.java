package com.xrwl.owner.module.publish.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by www.longdw.com on 2018/4/2 下午9:28.
 */

@Entity
public class Product {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    @Generated(hash = 1475182087)
    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1890278724)
    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
