package com.xrwl.owner.module.friend.adapter;

import android.widget.CheckBox;

import com.ldw.library.adapter.recycler.base.ItemViewDelegate;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;

public class FriendAddHeaderDelegate implements ItemViewDelegate<EntityWrapper<Friend>> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.friendadd_header_recycler_item;
    }

    @Override
    public boolean isForViewType(EntityWrapper<Friend> item, int position) {
        return item.isHeader();
    }

    @Override
    public void convert(ViewHolder holder, EntityWrapper<Friend> f, int position) {
        CheckBox cb = holder.getView(R.id.contactHeaderItemCb);
        cb.setChecked(f.getData().isSelected());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}