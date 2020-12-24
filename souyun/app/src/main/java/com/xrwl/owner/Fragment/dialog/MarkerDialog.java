package com.xrwl.owner.Fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.services.core.PoiItem;
import com.xrwl.owner.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MarkerDialog extends Dialog {

    Context mContext;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    MarkerAdapter mAdapter;

    public MarkerDialog(Context context,List<PoiItem> list) {
        super(context, R.style.mdialog);
        mContext = context;
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.marker_dialog, null);
        this.setContentView(layout);
        recyclerView = layout.findViewById(R.id.recycler_view);
        initRecycler();

    }

    //初始化recyclerview
    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MarkerAdapter(mContext, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setData(list);

        mAdapter.setOnItemClickListener(position -> {
            if(mOnItemClickListener != null){
                mOnItemClickListener.onClick(position);
                dismiss();
            }
        });

    }


    List<PoiItem> list;

    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public MarkerDialog setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }
}
