package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.publish.bean.Coupon;

import java.util.List;

/**
 * 优惠券
 * Created by www.longdw.com on 2018/4/14 下午9:15.
 */
public class CouponAdapter extends CommonAdapter<Coupon> {

    private final int mPrice;
    private Resources mRes;

    public CouponAdapter(Context context, int layoutId, List<Coupon> datas, int price) {
        super(context, layoutId, datas);
        mRes = context.getResources();
        mPrice = price;
    }

    @Override
    protected void convert(ViewHolder holder, Coupon coupon, int position) {
        TextView tv = holder.getView(R.id.cdItemTv);
        int full = Integer.parseInt(coupon.full);
        if (mPrice < full) {
            tv.setTextColor(mRes.getColor(R.color.light_gray));
        } else {
            tv.setTextColor(mRes.getColor(R.color.black));
        }
        tv.setText(mRes.getString(R.string.publish_confirm_fullreduce, coupon.full, coupon.reduce));
    }
}
