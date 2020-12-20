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
public class BankyueAdapter extends CommonAdapter<Tixianlist> {
    public BankyueAdapter(Context context, int layoutId, List<Tixianlist> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Tixianlist tixianlist, int position) {
        holder.setText(R.id.tixianid, tixianlist.id);
        holder.setText(R.id.tixianuserid, tixianlist.userid);
        holder.setText(R.id.tixianjine, "金额："+tixianlist.jine+"元");
        holder.setText(R.id.tixiantype, "状态："+zhuangtai(tixianlist.type));
        holder.setText(R.id.tixianshijian, "时间："+tixianlist.addtime);

    }
    protected String zhuangtai(String cs)
    {
        String abc="";
        if(cs.equals("0"))
        {
            abc="请求提现";
        }
        else if(cs.equals("1"))
        {
            abc="请求提现";
        }
        else if(cs.equals("2"))
        {
            abc="提现成功";
        }
        else
        {
            abc = "有异常";
        }
        return  abc;
    }

}
