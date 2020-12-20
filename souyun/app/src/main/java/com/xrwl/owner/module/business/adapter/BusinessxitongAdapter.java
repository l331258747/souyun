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
public class BusinessxitongAdapter extends CommonAdapter<Business> {
    public BusinessxitongAdapter(Context context, int layoutId, List<Business> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Business business, int position) {
            ImageView iv = holder.getView(R.id.businessItemIv);

            iv.setImageResource(R.drawable.owner);

            holder.setText(R.id.businessItemTitleTv, "系统消息");

           holder.setText(R.id.businessItemTimeTv, business.title);

        String bbb = "";
        try {
            bbb = URLDecoder.decode(business.date, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("00", e.getMessage(), e);
        }
        holder.setText(R.id.businessItemTimeshijianTv, "时间："+bbb);






         View dotView = holder.getView(R.id.businessItemDotView);
        if ("1".equals(business.type)) {
            dotView.setVisibility(View.GONE);
        } else {
            dotView.setVisibility(View.VISIBLE);
        }

    }
}
