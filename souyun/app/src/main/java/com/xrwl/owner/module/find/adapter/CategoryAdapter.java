package com.xrwl.owner.module.find.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/5/6 下午8:56.
 */
public class CategoryAdapter extends CommonAdapter<String> {
    private final Resources mRes;
    private int mSelectedPos = -1;

    public CategoryAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
        mRes = mContext.getResources();
    }

    public void setSelectedPos(int pos) {
        mSelectedPos = pos;
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        View view = holder.getConvertView();
        TextView tv = holder.getView(R.id.psItemTv);

        if (position == mSelectedPos) {
            view.setBackgroundResource(R.drawable.productsearch_item_selected_bg);
            tv.setTextColor(mRes.getColor(R.color.colorPrimary));
        } else {
            view.setBackgroundResource(R.drawable.productsearch_item_bg);
            tv.setTextColor(Color.BLACK);
        }

        tv.setText(s);
    }
}
