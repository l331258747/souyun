package com.xrwl.owner.module.friend.adapter;

import android.view.View;

import com.ldw.library.adapter.recycler.base.ItemViewDelegate;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;

/**
 * 显示搜索和组织架构
 * Created by www.longdw.com on 2017/12/13 下午4:19.
 */

public class FriendSearchDelegate implements ItemViewDelegate<EntityWrapper<Friend>> {

    private OnAddressSearchListener mListener;

    public FriendSearchDelegate(OnAddressSearchListener l) {
        mListener = l;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.friend_search_recycler_item;
    }

    @Override
    public boolean isForViewType(EntityWrapper<Friend> item, int position) {
        return item.isHeader();
    }

    @Override
    public void convert(ViewHolder holder, EntityWrapper<Friend> addressEntityWrapper, int position) {
        holder.getView(R.id.friendItemSearchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSearch();
                }
            }
        });
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public interface OnAddressSearchListener {
        /** 点击搜索 */
        void onSearch();
    }
}
