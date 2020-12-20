package com.xrwl.owner.module.publish.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpinnerDialog extends DialogFragment {

    @BindView(R.id.spinnerDialogRv)
    RecyclerView mRv;
    private List<Address> mDatas;
    private CustomSpinner.OnCustomSpinnerItemClickListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spinnerdialog_layout, container, false);

        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), R.layout.spinnerdialog_recycler_item, mDatas);
        mRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mListener.onItemClick(mDatas.get(position));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public void setData(List<Address> datas) {
        mDatas = datas;
    }

    private class SpinnerAdapter extends CommonAdapter<Address> {

        public SpinnerAdapter(Context context, int layoutId, List<Address> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, Address address, int position) {
            holder.setText(R.id.spinnerDialogItemTv, address.getName());
        }
    }

    public void setOnCustomSpinnerItemClickListener(CustomSpinner.OnCustomSpinnerItemClickListener l) {
        mListener = l;
    }


    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);


        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = (int)(dm.widthPixels * 0.5);
//        lp.height = (int)(dm.heightPixels * 0.7);
        lp.windowAnimations = R.style.PopAlphaAnimStyle;
        lp.alpha = 1.0f;
        lp.dimAmount = 0.4f;
        dialogWindow.setAttributes(lp);
    }
}