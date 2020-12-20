package com.xrwl.owner.module.me.adapter;

import com.ldw.library.adapter.recycler.base.ItemViewDelegate;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.me.bean.Me;

/**
 * Created by www.longdw.com on 2018/3/31 下午3:12.
 */

public class MeDelegate implements ItemViewDelegate<Me> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.me_recycler_item;
    }

    @Override
    public boolean isForViewType(Me item, int position) {
        return item.isContent();
    }

    @Override
    public void convert(ViewHolder holder, Me me, int position) {
        holder.setText(R.id.meItemTitleTv, me.getTitle());
        holder.setImageResource(R.id.meItemIconIv, me.getIcon());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
