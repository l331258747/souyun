package com.xrwl.owner.module.me.adapter;

import android.content.Context;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.xrwl.owner.module.me.bean.Me;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/3/31 下午3:12.
 */

public class MeAdapter extends MultiItemTypeAdapter<Me> {
    public MeAdapter(Context context, List<Me> datas) {
        super(context, datas);

        addItemViewDelegate(new MeHeaderDelegate(context));
        addItemViewDelegate(new MeDelegate());
    }
}
