package com.xrwl.owner.module.me.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseEventActivity;
import com.xrwl.owner.event.BankListRefreshEvent;
import com.xrwl.owner.module.me.adapter.BankAdapter;
import com.xrwl.owner.module.me.bean.Bank;
import com.xrwl.owner.module.me.mvp.BankContract;
import com.xrwl.owner.module.me.mvp.BankPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 银行卡
 * Created by www.longdw.com on 2018/4/5 上午12:11.
 */
public class BankcanshuActivity extends BaseEventActivity<BankContract.IView, BankPresenter> implements BankContract.IView {
    private int mSelectedPos = -1;
    public static final int RESULT_POSITION = 100;
    @BindView(R.id.bankRv)
    RecyclerView mRv;
    @BindView(R.id.totalPriceTv)
    TextView mPriceTv;
    @BindView(R.id.addTixianBtn)
    Button mTixianBtn;
    @BindView(R.id.tianjiall)
    LinearLayout tianjiall;
    private BankAdapter mAdapter;
    private HeaderAndFooterWrapper mWrapper;
    private ProgressDialog mTixianDialog;
    private List<Bank> mDatas;

    @Override
    protected BankPresenter initPresenter() {
        return new BankPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bank_activity;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BankListRefreshEvent event) {
        getData();
    }


    @Override
    protected void getData() {
        mPresenter.getBankList();

    }

    //到账银行卡
    @OnClick(R.id.tianjiall)
    public void tianjiaClick() {
        startActivity(new Intent(mContext, AddBankActivity.class));
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAdapter = new BankAdapter(this, R.layout.bank_recycler_item, new ArrayList<Bank>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

//        View view = LayoutInflater.from(this).inflate(R.layout.bank_footer_view, mRv, false);
//        mWrapper.addFootView(view);


        mRv.setAdapter(mWrapper);


        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                setSelectedPos(position);
                Bank item = mDatas.get(position);
                Intent intent = new Intent();
                intent.putExtra("title", item.num);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {


                return false;
            }
        });
        getData();
    }

    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Bank>> entity) {
        mAdapter.setDatas(entity.getData());
        mWrapper.notifyDataSetChanged();
        mDatas = entity.getData();
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
        mPriceTv.setText("0");

    }

    @Override
    public void onTixianError(BaseEntity entity) {
        mTixianDialog.dismiss();

        handleError(entity);
    }

    @Override
    public void onTixianException(Throwable e) {
        mTixianDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onBanksanyuansuSuccess() {

    }

    @Override
    public void onBanksanyuansuError(BaseEntity entity) {

    }

    @Override
    public void onBanksanyuansuException(Throwable e) {

    }
}
