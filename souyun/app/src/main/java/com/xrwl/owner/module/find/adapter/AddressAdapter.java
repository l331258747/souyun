package com.xrwl.owner.module.find.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/9 下午2:27.
 */
public class AddressAdapter extends CommonAdapter<Address> {

    private Resources mRes;

    public AddressAdapter(Context context, int layoutId, List<Address> datas) {
        super(context, layoutId, datas);

        mRes = context.getResources();
    }

    @Override
    protected void convert(ViewHolder holder, Address address, int position) {
        TextView tv = holder.getView(R.id.caItemTv);
        tv.setText(address.getName());
        if (address.isSelected()) {
            tv.setTextColor(mRes.getColor(R.color.colorPrimary));
        } else {
            tv.setTextColor(mRes.getColor(R.color.black));
        }
    }
}
