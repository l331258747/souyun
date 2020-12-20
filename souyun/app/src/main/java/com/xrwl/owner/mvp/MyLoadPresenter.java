package com.xrwl.owner.mvp;

import android.content.Context;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.utils.AccountUtil;

/**
 * Created by www.longdw.com on 2018/4/21 下午1:24.
 */
public class MyLoadPresenter<V extends BaseMVP.IBaseLoadView> extends BaseMVP.BaseLoadPresenter<V> {
    public MyLoadPresenter(Context context) {
        super(context);
    }
    public Account getAccount() {
        return AccountUtil.getAccount(mContext);
    }

}
