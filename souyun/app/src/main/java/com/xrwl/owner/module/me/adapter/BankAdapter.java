package com.xrwl.owner.module.me.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.module.me.bean.Bank;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/4/5 上午12:13.
 */
public class BankAdapter extends CommonAdapter<Bank> {
    public BankAdapter(Context context, int layoutId, List<Bank> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Bank bank, int position) {
        String str= bank.num.substring(bank.num.length ()- 4,bank.num.length()); //最后四个字符
        holder.setText(R.id.bankItemBankNameTv, bank.bank);
        holder.setText(R.id.bankItemCategoryTv, bank.category);
        holder.setText(R.id.bankItemNumTv, "**** "+str);
         Glide.with(mContext).load(bank.pic).into((ImageView) holder.getView(R.id.yhbj));
    }
}
