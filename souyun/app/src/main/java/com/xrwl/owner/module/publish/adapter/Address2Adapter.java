package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.widget.RadioButton;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address2;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/7/11 下午7:50.
 */
public class Address2Adapter extends CommonAdapter<Address2> {

    private int mSelectedPos = -1;

    public Address2Adapter(Context context, int layoutId, List<Address2> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Address2 address2, int position) {
        holder.setText(R.id.addressItemTv, address2.des);

        RadioButton rb = holder.getView(R.id.addressRb);
        if (mSelectedPos == position) {
            rb.setSelected(true);
        } else {
            rb.setSelected(false);
        }
    }

    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
    }
}
