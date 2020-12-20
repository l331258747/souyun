package com.xrwl.owner.module.friend.adapter;


import com.ldw.library.adapter.recycler.base.ItemViewDelegate;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;

/**
 * section标题
 * Created by www.longdw.com on 2017/12/13 下午4:21.
 */

public class FriendTitleDelegate implements ItemViewDelegate<EntityWrapper<Friend>> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.friend_sectiontitle_recycler_item;
    }

    @Override
    public boolean isForViewType(EntityWrapper<Friend> item, int position) {
        return item.isTitle();
    }

    @Override
    public void convert(ViewHolder holder, EntityWrapper<Friend> value, int position) {
        holder.setText(R.id.friendItemTitleTv, value.getIndex());
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
