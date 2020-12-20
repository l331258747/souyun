package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.publish.bean.Truck;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/3/31 下午11:49.
 */

public class TruckAdapter extends CommonAdapter<Truck> {

    private Resources mRes;

    public TruckAdapter(Context context, int layoutId, List<Truck> datas) {
        super(context, layoutId, datas);
        mRes = context.getResources();
    }

    @Override
    protected void convert(ViewHolder holder, Truck truck, int position) {
        ImageView iv = holder.getView(R.id.truckItemTypeIv);
        Glide.with(mContext).load(truck.getPicUrl()).into(iv);
        holder.setText(R.id.truckItemLoadTv, truck.getTitle());
       holder.setText(R.id.truckItemCapacityTv, "载重："+truck.capacity+"吨");
       TextView remarkTv = holder.getView(R.id.truckItemRemarkTv);
       if (TextUtils.isEmpty(truck.getLength())) {
          holder.setText(R.id.truckItemAreaTv, "对于车辆大小没有要求");
            remarkTv.setVisibility(View.GONE);
        } else {
            String area = truck.getLength().replace(",","*")+"米";
            holder.setText(R.id.truckItemAreaTv, area);
            remarkTv.setVisibility(View.VISIBLE);
            String remark = mRes.getString(R.string.publish_truck_remark, truck.getStartPrice(), truck.getStartKilometre(), truck .getExceedPrice());
            remarkTv.setText(remark);
       }
    }
}