package com.xrwl.owner.module.home.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Order;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:20.
 */
public class HistoryOrderAdapter extends CommonAdapter<Order> {

    public HistoryOrderAdapter(Context context, int layoutId, List<Order> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Order order, int position) {
        holder.setText(R.id.historyOrderItemStartTv, order.getStartPos());
        holder.setText(R.id.historyOrderItemEndTv, order.getEndPos());
        holder.setText(R.id.historyOrderItemProductNameTv, "货名：" + order.getProductName());
        if(TextUtils.isEmpty(order.getTruck()))
        {
            holder.setText(R.id.historyOrderItemTruckTv, "车型：无车型需求");
        }
        else
        {
            holder.setText(R.id.historyOrderItemTruckTv, "车型：" + order.getTruck());
        }

        String dundun="0";String fangfang="0";String jianjian="0";
        if(TextUtils.isEmpty(order.getWeight())||order.getWeight().length()==0)
        {
            dundun="0";
        }
        else
        {
            dundun=order.getWeight();
        }
        if(TextUtils.isEmpty(order.getArea())||order.getArea().length()==0)
        {
            fangfang="0";
        }
        else
        {
            fangfang=order.getArea();
        }
        if(TextUtils.isEmpty(order.getNum())||order.getNum().length()==0)
        {
            jianjian="0";
        }
        else
        {
            jianjian=order.getNum();
        }


        //holder.setText(R.id.historyOrderItemPropertyTv, order.getWeight() + "吨/" + order.getArea() + "方/" + order.getNum() + "件");
        holder.setText(R.id.historyOrderItemPropertyTv, dundun + "吨/" + fangfang + "方/"+jianjian+ "件");
    }
}
