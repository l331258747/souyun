package com.xrwl.owner.module.home.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ldw.library.utils.Utils;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.HomeItem;

import java.util.List;


public class HomeAdAdapter extends PagerAdapter {

    private final OnHomeAdListener mListener;
    private List<HomeItem> mDatas;
    private Resources mRes;
    private int mHeight;
    private Context mContext;

    public HomeAdAdapter(Context context, ViewPager viewPager, List<HomeItem> datas, OnHomeAdListener l) {
        mDatas = datas;
        mContext = context;
        mRes = mContext.getResources();
        mListener = l;

        int width = Utils.getScreenWidth(mContext);
        mHeight = width * 292 / 1080;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mHeight);
        viewPager.setLayoutParams(params);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = View.inflate(mContext, R.layout.home_ad_viewpager_item, null);
        container.addView(view);
        ImageView iv = view.findViewById(R.id.homeAdItemIv);
        int resId = mRes.getIdentifier(mDatas.get(position).getIcon(), "drawable", mContext.getPackageName());
        iv.setImageResource(resId);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mHeight);
        iv.setLayoutParams(params);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onHomeAddClick(position);
                }
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 删除
        container.removeView((View) object);
    }

    public interface OnHomeAdListener {
        void onHomeAddClick(int position);
    }
}