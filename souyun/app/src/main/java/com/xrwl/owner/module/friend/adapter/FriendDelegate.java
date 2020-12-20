package com.xrwl.owner.module.friend.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ldw.library.adapter.recycler.base.ItemViewDelegate;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;

/**
 * Created by www.longdw.com on 2018/3/29 下午11:00.
 */

public class FriendDelegate implements ItemViewDelegate<EntityWrapper<Friend>> {

    private final boolean mIsAdd;
    private final OnFriendInviteListener mListener;

    public FriendDelegate(boolean isAdd, OnFriendInviteListener l) {
        mIsAdd = isAdd;

        mListener = l;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.friend_recycler_item;
    }

    @Override
    public boolean isForViewType(EntityWrapper<Friend> item, int position) {
        return item.isContent();
    }

    @Override
    public void convert(ViewHolder holder, final EntityWrapper<Friend> friend, int position) {
        holder.setText(R.id.friendItemNameTv, friend.getData().name);
        holder.setText(R.id.friendItemPhoneTv, friend.getData().phone);
        ImageView avatarIv = holder.getView(R.id.friendItemAvatarIv);
        CheckBox cb = holder.getView(R.id.friendItemCb);

        Button inviteBtn = holder.getView(R.id.friendItemInviteBtn);

        if (mIsAdd) {
            avatarIv.setVisibility(View.GONE);
            cb.setVisibility(View.VISIBLE);
            inviteBtn.setVisibility(View.GONE);
        } else {
            avatarIv.setVisibility(View.VISIBLE);
            cb.setVisibility(View.GONE);
            inviteBtn.setVisibility(View.VISIBLE);
            String zhucetype="";String yaoqing="邀请";
             if("1".equals(friend.getData().is_registqubie))
             {
                 zhucetype="司机";
             }
             else
             {
                 zhucetype="货主";
             }
            if (friend.getData().isRegister()) {
                inviteBtn.setText(""+zhucetype+"");
                inviteBtn.setOnClickListener(null);
            } else {

                     inviteBtn.setText("未注册");


                inviteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onFriendInvite(friend.getData());
                        }
                    }
                });
            }


        }

        cb.setChecked(friend.getData().isSelected());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public interface OnFriendInviteListener {
        void onFriendInvite(Friend f);
    }
}
