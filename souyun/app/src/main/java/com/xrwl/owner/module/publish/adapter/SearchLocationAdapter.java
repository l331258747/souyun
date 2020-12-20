package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.amap.api.services.core.PoiItem;
import com.ldw.library.adapter.listview.CommonAdapter;
import com.ldw.library.adapter.listview.ViewHolder;
import com.xrwl.owner.R;

import java.util.List;

/**
 * Created by www.longdw.com on 2017/11/25 上午8:27.
 */

public class SearchLocationAdapter extends CommonAdapter<PoiItem> {

    private OnPoiItemClickListener mListener;

    public SearchLocationAdapter(Context context, int layoutId, List<PoiItem> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final PoiItem item, int position) {

        viewHolder.setText(R.id.slItemTitleTv, item.getTitle());
        viewHolder.setText(R.id.slItemSubTitleTv, item.getSnippet());

        Button selectBtn = viewHolder.getView(R.id.slItemSelectBtn);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.poiItemClick(item);
                }
            }
        });
    }

    public interface OnPoiItemClickListener {
        void poiItemClick(PoiItem pi);
    }

    public void setOnPoiItemClickListener(OnPoiItemClickListener l) {
        mListener = l;
    }

}
