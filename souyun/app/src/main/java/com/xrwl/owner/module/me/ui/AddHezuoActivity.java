package com.xrwl.owner.module.me.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.event.BankListRefreshEvent;
import com.xrwl.owner.module.me.mvp.AddBankPresenter;
import com.xrwl.owner.module.me.mvp.BankContract;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我要合作加油站和加气站
 * Created by www.longdw.com on 2018/4/5 上午12:24.
 */
public class AddHezuoActivity extends BaseActivity<BankContract.IAddView, AddBankPresenter> implements BankContract.IAddView {

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

    @Override
    protected AddBankPresenter initPresenter() {
        return new AddBankPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.addhezuo_activity;
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

    @OnClick(R.id.addBankConfirmBtn)
    public void confirm() {
//        String num = mNumEt.getText().toString();
//        if (TextUtils.isEmpty(num) || num.length() < 16) {
//            showToast("请输入正确的银行卡号");
//            return;
//        }
//        String name = mNameEt.getText().toString();
//        if (TextUtils.isEmpty(name)) {
//            showToast("请输入持卡人姓名");
//            return;
//        }
//        //String account = mAccountEt.getText().toString();
//        String account ="暂时隐藏";
////        if (TextUtils.isEmpty(account)) {
////            showToast("请输入开户行");
////            return;
////        }
//        String appyinhangkaleibie=mYinhangkaleibie.getSelectedItem().toString();
//        String appsuoshuyinhang=mSuoshuyinhang.getSelectedItem().toString();
//        mProgressDialog = LoadingProgress.showProgress(this, "正在添加...");
//
//        Map<String, String> params = new HashMap<>();
//        params.put("num", num);
//        params.put("name", name);
//        params.put("account_bank", account);
//        params.put("category", appyinhangkaleibie);
//        params.put("bank", appsuoshuyinhang);
//        params.put("check", "1");
       // mPresenter.addBank(params);
    }
}
