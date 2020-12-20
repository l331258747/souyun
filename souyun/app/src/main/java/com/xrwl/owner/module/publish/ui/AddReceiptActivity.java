package com.xrwl.owner.module.publish.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Receipt;
import com.xrwl.owner.event.BankListRefreshEvent;
import com.xrwl.owner.module.me.mvp.AddBankPresenter;
import com.xrwl.owner.module.me.ui.BankyuejinduActivity;
import com.xrwl.owner.module.publish.adapter.ReceiptAdapter;
import com.xrwl.owner.module.publish.map.SearchLocationActivity;
import com.xrwl.owner.module.publish.mvp.ReceiptContract;
import com.xrwl.owner.module.publish.mvp.ReceiptPresenter;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发票列表
 * Created by www.longdw.com on 2018/7/11 下午7:47.
 */
public class AddReceiptActivity extends BaseActivity<ReceiptContract.IView, ReceiptPresenter> implements ReceiptContract.IView {

    @BindView(R.id.addnumberEt)
    EditText maddnumberEt;
    @BindView(R.id.addcompanyEt)
    EditText maddcompanyEt;
    @BindView(R.id.addaddressEt)
    EditText maddaddressEt;
    @BindView(R.id.addtelEt)
    EditText maddtelEt;
    @BindView(R.id.addemailEt)
    EditText maddemailEt;
    private ProgressDialog mProgressDialog;
    private ProgressDialog mPostDialog;
    @Override
    protected ReceiptPresenter initPresenter() {
        return new ReceiptPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.addreceipt_activity;
    }

    @Override
    protected void initViews() {
    }

    @Override
    public void onRefreshSuccess(BaseEntity entity) {
        mProgressDialog.dismiss();
        EventBus.getDefault().post(new BankListRefreshEvent());
        finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
        mProgressDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        mProgressDialog.dismiss();
        handleError(entity);
    }

    @OnClick(R.id.addBankConfirmBtn)
    public void confirm() {
        String number = maddnumberEt.getText().toString();
        if (TextUtils.isEmpty(number) || number.length() < 18) {
            showToast("请输入正确的税号");
            return;
        }
        String company = maddcompanyEt.getText().toString();
        if (TextUtils.isEmpty(company)) {
            showToast("请输入单位名称");
            return;
        }
        String address = maddaddressEt.getText().toString();
        if (TextUtils.isEmpty(address)) {
            showToast("请输入单位地址");
            return;
        }
        String tel = maddtelEt.getText().toString();
        if (TextUtils.isEmpty(tel) || tel.length() < 11) {
            showToast("请输入联系电话");
            return;
        }
        String email = maddemailEt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            showToast("请输入邮箱");
            return;
        }
        mProgressDialog = LoadingProgress.showProgress(this, "正在添加...");



        HashMap<String, String> params = new HashMap<>();
        try {
            params.put("number", URLEncoder.encode(number,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("company", URLEncoder.encode(company,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("address", URLEncoder.encode(address,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("tel",  URLEncoder.encode(tel,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("userid", mAccount.getId());
        try {
            params.put("email",  URLEncoder.encode(email,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("addtime", "2018-11-28");
        mPresenter.postData(params);
        startActivity(new Intent(mContext, ReceiptActivity.class));

    }

    @Override
    public void onPostSuccess(BaseEntity entity) {
       // mPostDialog.dismiss();
       // showToast("添加成功");

    }

    @Override
    public void onPostError(BaseEntity entity) {
//        mPostDialog.dismiss();
//        showToast("添加失败");
//        handleError(entity);
    }

    @Override
    public void onPostError(Throwable e) {
//        mPostDialog.dismiss();
//        showNetworkError();
    }
}
