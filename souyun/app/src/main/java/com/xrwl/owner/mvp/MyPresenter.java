package com.xrwl.owner.mvp;

import android.content.Context;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.utils.AccountUtil;

/**
 * Created by www.longdw.com on 2018/4/19 下午2:16.
 */
public abstract class MyPresenter<V extends BaseMVP.IBaseView> extends BaseMVP.BasePresenter<V> {

    private Account mAccount;

    public MyPresenter(Context context) {
        super(context);
        mAccount = AccountUtil.getAccount(mContext);
    }

    public Account getAccount() {
        return mAccount;
    }

}
