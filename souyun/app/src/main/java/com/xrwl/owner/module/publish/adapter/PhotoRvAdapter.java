package com.xrwl.owner.module.publish.adapter;

import android.content.Context;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.xrwl.owner.module.publish.bean.Photo;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/4 下午3:00.
 */
public class PhotoRvAdapter extends MultiItemTypeAdapter<Photo> {
    public PhotoRvAdapter(Context context, List<Photo> datas, PhotoRvAddDelegate.OnAddListener addListener, PhotoRvItemDelegate
            .OnItemDeleteListener deleteListener) {
        super(context, datas);

        addItemViewDelegate(new PhotoRvAddDelegate(addListener));
        addItemViewDelegate(new PhotoRvItemDelegate(context, deleteListener));

    }

}
