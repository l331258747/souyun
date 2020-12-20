package com.xrwl.owner.module.friend.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.friend.bean.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2017/12/23 下午3:28.
 */

public class FriendSearchAdapter extends CommonAdapter<Friend> implements Filterable {

    private final RequestOptions mRequestOptions;
    private List<Friend> items = new ArrayList<>();
    private View mNoResultView;

    public FriendSearchAdapter(Context context, int layoutId, List<Friend> datas) {
        super(context, layoutId, datas);

        mContext = context;
        mRequestOptions = new RequestOptions().circleCrop();
    }

    public void setNoResultView(View noResultView) {
        mNoResultView = noResultView;
    }

    @Override
    protected void convert(ViewHolder holder, Friend f, int position) {
        holder.setText(R.id.friendItemNameTv, f.getName());
        holder.setText(R.id.friendItemPhoneTv, f.getPhone());

        ImageView avatarIv = holder.getView(R.id.friendItemAvatarIv);

        String avatar = f.getAvatar();
        if (!TextUtils.isEmpty(avatar)) {
            Glide.with(mContext).load(avatar).apply(mRequestOptions).into(avatarIv);
        } else {
            avatarIv.setImageResource(R.drawable.ic_default_avatar);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Friend> list = new ArrayList<>();
                for (Friend item : mDatas) {
                    if (item.getPinyin().startsWith(constraint.toString()) || item.getName().contains(constraint)) {
                        list.add(item);
                    }
                }
                FilterResults results = new FilterResults();
                results.count = list.size();
                results.values = list;
                return results;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<Friend> list = (ArrayList<Friend>) results.values;
                items.clear();
                items.addAll(list);
                if (mNoResultView != null) {
                    if (results.count == 0) {
                        mNoResultView.setVisibility(View.VISIBLE);
                    } else {
                        mNoResultView.setVisibility(View.INVISIBLE);
                    }
                }
                setDatas(items);
            }
        };
    }
}
