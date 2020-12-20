package com.xrwl.owner.module.business.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Business;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by www.longdw.com on 2018/8/16 下午7:30.
 */
public class BusinessqianbaoAdapter extends CommonAdapter<Business> {
    public BusinessqianbaoAdapter(Context context, int layoutId, List<Business> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Business business, int position) {
            ImageView iv = holder.getView(R.id.businessItemIv);
            // Glide.with(mContext).load(business.pic).into(iv);
            iv.setImageResource(R.drawable.owner);
            // holder.setText(R.id.businessItemTitleTv, business.title);
            holder.setText(R.id.businessItemTitleTv, "钱包消息");
            
//            String aaa = "";
//            try {
//                aaa = URLDecoder.decode(business.title, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                Log.e("0", e.getMessage(), e);
//            }
           holder.setText(R.id.businessItemTimeTv,business.title);

        String bbb = "";
        try {
            bbb = URLDecoder.decode(business.date, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("00", e.getMessage(), e);
        }
        holder.setText(R.id.businessItemTimeshijianTv, "时间："+bbb);


        String ccc = "";
        try {
            ccc = URLDecoder.decode(business.total_price, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("00", e.getMessage(), e);
        }
        holder.setText(R.id.businessItemTimejiageTv, "共计："+ccc+"元");


        String ddd = "";
        try {
            ddd = URLDecoder.decode(business.start_province+business.start_city+" > "+business.end_province+business.end_city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("000", e.getMessage(), e);
        }
        holder.setText(R.id.chufade, ddd);
         View dotView = holder.getView(R.id.businessItemDotView);
        if ("1".equals(business.type)) {
            dotView.setVisibility(View.GONE);
        } else {
            dotView.setVisibility(View.VISIBLE);
        }

//        new LBadgeView(mContext).setGravityOffset(0, true).bindTarget(view)
//                .setBadgeText(event.getCount());
    }
}
