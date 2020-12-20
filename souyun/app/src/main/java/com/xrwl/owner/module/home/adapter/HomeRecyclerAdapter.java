package com.xrwl.owner.module.home.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.HomeItem;

import java.util.List;

public class HomeRecyclerAdapter extends CommonAdapter<HomeItem> {

		public HomeRecyclerAdapter(Context context, int layoutId, List<HomeItem> datas) {
            super(context, layoutId, datas);
		}

		@Override
		protected void convert(ViewHolder holder, HomeItem homeItem, int position) {
			Resources res = mContext.getResources();
//			int resId = res.getIdentifier(homeItem.getIcon(), "drawable", mContext.getPackageName());
//			holder.setImageResource(R.id.homeItemIv, resId);
			String icon = homeItem.getIcon();
			ImageView iv = holder.getView(R.id.homeItemIv);
			if (icon.startsWith("http")) {
				Glide.with(mContext).load(homeItem.getIcon()).into(iv);
			} else {
				int resId = res.getIdentifier(homeItem.getIcon(), "drawable", mContext.getPackageName());
				holder.setImageResource(R.id.homeItemIv, resId);
			}
			holder.setText(R.id.homeItemTv, homeItem.getTitle());
		}
	}