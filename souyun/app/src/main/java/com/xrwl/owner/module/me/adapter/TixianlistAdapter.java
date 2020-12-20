package com.xrwl.owner.module.me.adapter;

import android.content.Context;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.me.bean.Bank;
import com.xrwl.owner.module.me.bean.Tixianlist;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/5 上午12:13.
 */
public class TixianlistAdapter extends CommonAdapter<Tixianlist> {
    public TixianlistAdapter(Context context, int layoutId, List<Tixianlist> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Tixianlist tixianlist, int position) {
        holder.setText(R.id.bankItemBankNameTv, tixianlist.id);
        holder.setText(R.id.bankItemCategoryTv, tixianlist.userid);
        holder.setText(R.id.bankItemNumTv, tixianlist.jine);
        holder.setText(R.id.bankItemNumTv, tixianlist.addtime);
        holder.setText(R.id.bankItemNumTv, tixianlist.type);
    }
}
