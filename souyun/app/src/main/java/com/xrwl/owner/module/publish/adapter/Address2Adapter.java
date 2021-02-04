package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.Address2;
import com.xrwl.owner.utils.AccountUtil;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/7/11 下午7:50.
 */
public class Address2Adapter extends CommonAdapter<Address2> {

    private int mSelectedPos = -1;
    Context context;

    public Address2Adapter(Context context, int layoutId, List<Address2> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, Address2 address2, int position) {
        holder.setText(R.id.addressItemTv, address2.des);
        holder.setText(R.id.addressDesTv, address2.getShengshixian());

        if(TextUtils.isEmpty(address2.getName())){
            Account mAccount = AccountUtil.getAccount(context);
            holder.setText(R.id.addressNameTv, address2.getAccountName(mAccount.getNameDecode(),mAccount.getPhone()));
        }else{
            holder.setText(R.id.addressNameTv, address2.getName());
        }

        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });

        RadioButton rb = holder.getView(R.id.addressRb);
        if (mSelectedPos == position) {
            rb.setSelected(true);
        } else {
            rb.setSelected(false);
        }
    }

    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
    }

    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
