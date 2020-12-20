package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.widget.RadioButton;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Receipt;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/7/11 下午7:50.
 */
public class ReceiptAdapter extends CommonAdapter<Receipt> {

    private int mSelectedPos = -1;

    public ReceiptAdapter(Context context, int layoutId, List<Receipt> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Receipt receipt, int position) {
        holder.setText(R.id.addressItemTv, receipt.number);

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
