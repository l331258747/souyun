package com.xrwl.owner.module.find.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.BusModel;

import java.util.List;

/**
 * 高级搜索的车型
 * Created by www.longdw.com on 2018/4/12 下午5:18.
 */
public class BusModelAdapter extends CommonAdapter<BusModel> {

    private Resources mRes;

    public BusModelAdapter(Context context, int layoutId, List<BusModel> datas) {
        super(context, layoutId, datas);
        mRes = context.getResources();
    }

    @Override
    protected void convert(ViewHolder holder, BusModel busModel, int position) {

        View view = holder.getConvertView();
        TextView tv = holder.getView(R.id.psItemTv);

        if (busModel.isSelected()) {
            view.setBackgroundResource(R.drawable.productsearch_item_selected_bg);
            tv.setTextColor(mRes.getColor(R.color.colorPrimary));
        } else {
            view.setBackgroundResource(R.drawable.productsearch_item_bg);
            tv.setTextColor(Color.BLACK);
        }

        tv.setText(busModel.getName());
    }
}
