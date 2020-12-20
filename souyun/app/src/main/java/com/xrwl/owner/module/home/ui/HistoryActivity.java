package com.xrwl.owner.module.home.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.LoadMoreWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.CustomLoadMoreView;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.module.home.adapter.HistoryOrderAdapter;
import com.xrwl.owner.module.home.mvp.HistoryContract;
import com.xrwl.owner.module.home.mvp.HistoryPresenter;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderDetailActivity;
import com.xrwl.owner.utils.AccountUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 历史交易
 * Created by www.longdw.com on 2018/4/4 下午10:07.
 */
public class HistoryActivity extends BaseActivity<HistoryContract.IView, HistoryPresenter> implements HistoryContract.IView {

    @BindView(R.id.historyRv)
    RecyclerView mRv;
    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.historyNumTv)
    TextView mNumTv;
    @BindView(R.id.historyPriceTv)
    TextView mPriceTv;

    private HistoryOrderAdapter mAdapter;

    private Account mAccount;
    private LoadMoreWrapper mLoadMoreWrapper;

    @Override
    protected HistoryPresenter initPresenter() {
        return new HistoryPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.history_activity;
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAccount = AccountUtil.getAccount(this);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        getData();
    }

    @Override
    protected void getData() {
        mPresenter.getList();
    }

    @Override
    public void onRefreshCallback(String num, String price, boolean hasnext, List<Order> datas) {
        mRefreshLayout.setRefreshing(false);
        mNumTv.setText(num);
        if("0".equals(price)||null==price)
        {
            mPriceTv.setText("0");
        }
        else
        {
            mPriceTv.setText(price);
        }


        if (datas == null || datas.size() == 0) {
            showNoData();
            return;
        }
        if (mAdapter == null) {
            mAdapter = new HistoryOrderAdapter(mContext, R.layout.historyorder_recycler_item, datas);
            mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
            mLoadMoreWrapper.setLoadMoreView(new CustomLoadMoreView());
            mRv.setAdapter(mLoadMoreWrapper);

            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (mAccount.isOwner()) {
                        //startActivity(new Intent(mContext, OwnerOrderDetailActivity.class));
                        startActivity(new Intent(mContext, OwnerOrderActivity.class));
                   }
// else if (mAccount.isDriver()) {
////                        Intent intent = new Intent(mContext, DriverOrderDetailActivity.class);
////                        intent.putExtra("type", "cancel");
////                        startActivity(intent);
//                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    mPresenter.getMoreList();
                }
            });
        } else {
            mAdapter.setDatas(datas);
            mLoadMoreWrapper.notifyDataSetChanged();
        }

        mLoadMoreWrapper.setEnableLoadMore(hasnext);

        showContent();
    }

    @Override
    public void onLoadCallback(boolean hasnext, List<Order> datas) {
        mAdapter.setDatas(datas);

        if (!hasnext) {
            mLoadMoreWrapper.loadMoreEnd();
        } else {
            mLoadMoreWrapper.loadMoreComplete();
        }
    }

    @Override
    public void onRefreshError(Throwable e) {
        mRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            showError();
        }
    }

    @Override
    public void onError(BaseEntity entity) {
        mRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            showError();
        }
        handleError(entity);
    }

    @Override
    public void onLoadError(Throwable e) {
        mLoadMoreWrapper.loadMoreFail();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Order>> entity) {
    }
    @Override
    public void onLoadSuccess(BaseEntity<List<Order>> entity) {

    }
}
