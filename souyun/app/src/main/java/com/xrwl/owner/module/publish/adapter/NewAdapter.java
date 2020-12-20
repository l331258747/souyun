package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.widget.RadioButton;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address2;
import com.xrwl.owner.bean.New;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/7/11 下午7:50.
 */
public class NewAdapter extends CommonAdapter<New> {

    private int mSelectedPos = -1;

    public NewAdapter(Context context, int layoutId, List<New> datas) {
        super(context, layoutId, datas);
    }



    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
    }

    @Override
    protected void convert(ViewHolder holder, New aNew, int position) {
        holder.setText(R.id.addressItemTv, aNew.title);

        RadioButton rb = holder.getView(R.id.addressRb);
        if (mSelectedPos == position) {
            rb.setSelected(true);
        } else {
            rb.setSelected(false);
        }
    }
}
