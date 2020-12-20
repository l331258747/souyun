package com.xrwl.owner.module.me.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.event.BankListRefreshEvent;
import com.xrwl.owner.module.me.mvp.AddBankPresenter;
import com.xrwl.owner.module.me.mvp.BankContract;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我要合作加油站和加气站
 * Created by www.longdw.com on 2018/4/5 上午12:24.
 */
public class ZaiXianGouMai_Activity extends BaseActivity<BankContract.IAddView, AddBankPresenter> implements BankContract.IAddView {

//    @BindView(R.id.addBankNumEt)
//    EditText mNumEt;//账号
//    @BindView(R.id.addBankNameEt)
//    EditText mNameEt;//持卡人姓名
//    @BindView(R.id.addBankAccountEt)
//    EditText mAccountEt;//开户行
//    @BindView(R.id.yinhangkaleibie)
//    Spinner mYinhangkaleibie;//银行卡类别
//    @BindView(R.id.suoshuyinhang)
//    Spinner mSuoshuyinhang;//所属银行
//    private ProgressDialog mProgressDialog;

       @BindView(R.id.jiezhangBtn)
       Button maddJieZhangBtn;


    @Override
    protected AddBankPresenter initPresenter() {
        return new AddBankPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.zaixiangoumai_activity;
    }

    @Override
    protected void initViews() {
    }

    @Override
    public void onRefreshSuccess(BaseEntity entity) {
       // mProgressDialog.dismiss();
        startActivity(new Intent(mContext, RuZhuSuccessActivity.class));
        EventBus.getDefault().post(new BankListRefreshEvent());
        finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
      //  mProgressDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
       // mProgressDialog.dismiss();
        handleError(entity);
    }

    @OnClick({R.id.jiezhangBtn})
    public void onClick(View v) {
        if (v.getId() == R.id.jiezhangBtn) {
            startActivity(new Intent(mContext, Success_erweimaActivity.class));
        }

    }
}
