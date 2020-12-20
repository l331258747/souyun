package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ldw.library.adapter.recycler.base.ItemViewDelegate;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.publish.bean.Photo;

/**
 * Created by www.longdw.com on 2018/4/4 下午3:04.
 */
public class PhotoRvItemDelegate implements ItemViewDelegate<Photo> {

    private final OnItemDeleteListener mOnItemDeleteListener;
    private final Context mContext;

    public PhotoRvItemDelegate(Context context, OnItemDeleteListener l) {
        mContext = context;
        mOnItemDeleteListener = l;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.add_showphotos_recyclerview_item;
    }

    @Override
    public boolean isForViewType(Photo item, int position) {
        return item.isItem();
    }

    @Override
    public void convert(ViewHolder holder, final Photo photo, int position) {
        ImageView deleteIv = holder.getView(R.id.addShowPhotosItemDeleteIv);
        if (photo.isCanDelete()) {
            deleteIv.setVisibility(View.VISIBLE);
            deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemDeleteListener.onItemDelete(photo);
                }
            });
        } else {
            deleteIv.setVisibility(View.GONE);
        }
        ImageView iv = holder.getView(R.id.addShowPhotosItemIv);
        Glide.with(mContext).load(photo.path).into(iv);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public interface OnItemDeleteListener {
        void onItemDelete(Photo photo);
    }
}
