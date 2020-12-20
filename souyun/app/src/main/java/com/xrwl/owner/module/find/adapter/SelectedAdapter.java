package com.xrwl.owner.module.find.adapter;

import android.content.Context;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/10 下午2:58.
 */
public class SelectedAdapter extends CommonAdapter<Address> {
    public SelectedAdapter(Context context, int layoutId, List<Address> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Address address, int position) {
        holder.setText(R.id.caSelectedItemTv, address.getName());
    }
}
