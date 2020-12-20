package com.xrwl.owner.module.me.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseEventActivity;
import com.xrwl.owner.event.BankListRefreshEvent;
import com.xrwl.owner.module.me.adapter.BankyueAdapter;
import com.xrwl.owner.module.me.bean.Tixianlist;
import com.xrwl.owner.module.me.mvp.BankyueContract;
import com.xrwl.owner.module.me.mvp.BankyuePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 余额
 * Created by www.longdw.com on 2018/4/5 上午12:11.
 */
public class BankyuejinduActivity extends BaseEventActivity<BankyueContract.IView, BankyuePresenter> implements BankyueContract.IView {

    @BindView(R.id.bankRv)
    RecyclerView mRv;
    @BindView(R.id.totalPriceTv)
    TextView mPriceTv;
    @BindView(R.id.totalPriceEt)
    EditText mPriceEt;
    @BindView(R.id.addTixianBtn)
    Button mTixianBtn;
    @BindView(R.id.slectjindu)
    Button mslectjindu;
    private BankyueAdapter mAdapter;
    private HeaderAndFooterWrapper mWrapper;
    private ProgressDialog mTixianDialog;

    private Float shengyujine;
    @Override
    protected BankyuePresenter initPresenter() {
        return new BankyuePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bankyuejindu_activity;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BankListRefreshEvent event) {
        getData();
    }
    @OnClick(R.id.slectjindu)
    public void jindu() {
        startActivity(new Intent(mContext, AddBankActivity.class));
    }
    @OnClick(R.id.addTixianBtn)
    public void tixian() {
        String price = mPriceTv.getText().toString();
        float priceet=0;
        if(!TextUtils.isEmpty(mPriceEt.getText().toString()))
        {
            priceet=Float.parseFloat(mPriceEt.getText().toString());
        }
        else
        {
            priceet=0;
        }

        if (TextUtils.isEmpty(price) || price.equals("0") || price.equals("0.0")) {
            showToast("没有可提现金额");
            return;
        }
        float pricezong=Float.parseFloat(mPriceTv.getText().toString());
        if(priceet<=pricezong)
        {
            price = mPriceEt.getText().toString();
            shengyujine=(pricezong-priceet);
        }
        else {
            showToast("您输入的提现金额大于您的总额，系统驳回");
            return;
        }
        mTixianDialog = LoadingProgress.showProgress(this, "正在提现...");
        //mPresenter.tixian(price);
    }

    @Override
    protected void getData() {
        //mPresenter.getBankList();
        mPresenter.gettixianlist();
        mPresenter.getTotalPrice();
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAdapter = new BankyueAdapter(this, R.layout.bankyue_recycler_item, new ArrayList<Tixianlist>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

        View view = LayoutInflater.from(this).inflate(R.layout.bank_footer_view, mRv, false);
        mWrapper.addFootView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddBankActivity.class));
            }
        });

        mRv.setAdapter(mWrapper);

        getData();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Tixianlist>> entity) {
        mAdapter.setDatas(entity.getData());
        mWrapper.notifyDataSetChanged();
    }



    @Override
    public void onRefreshError(Throwable e) {
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
    }

    @Override
    public void onTotalPriceSuccess(String price) {
        mPriceTv.setText(price);
        mTixianBtn.setEnabled(true);
    }

    @Override
    public void onTixianSuccess() {
        mTixianDialog.dismiss();
        mPriceTv.setText(String.valueOf(shengyujine));
        showToast("提现成功,等待管理审核，请关注您的账户");
    }

    @Override
    public void onTixianError(BaseEntity entity) {
        mTixianDialog.dismiss();
        showToast("提现失败");
        handleError(entity);
    }

    @Override
    public void onTixianException(Throwable e) {
        mTixianDialog.dismiss();
        showNetworkError();
    }
}
