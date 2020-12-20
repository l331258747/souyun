package com.xrwl.owner.module.publish.adapter;

import android.content.Context;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.publish.bean.Insurance;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/14 下午9:15.
 */
public class InsuranceAdapter extends CommonAdapter<Insurance> {
    public InsuranceAdapter(Context context, int layoutId, List<Insurance> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Insurance insurance, int position) {
        holder.setText(R.id.idItemTitleTv, insurance.title);
        holder.setText(R.id.idItemDesTv, insurance.des);
        holder.setText(R.id.idItemPriceTv, insurance.price + "元");
    }
}
