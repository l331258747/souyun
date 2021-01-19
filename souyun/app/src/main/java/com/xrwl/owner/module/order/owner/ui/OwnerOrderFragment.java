package com.xrwl.owner.module.order.owner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.LoadMoreWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.CustomLoadMoreView;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseEventFragment;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.event.OwnerOrderListRrefreshEvent;
import com.xrwl.owner.module.order.owner.adapter.OwnerOrderAdapter;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderContract;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderPresenter;
import com.xrwl.owner.utils.MyUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/4/4 上午9:45.
 */
public class OwnerOrderFragment extends BaseEventFragment<OwnerOrderContract.IView, OwnerOrderPresenter> implements
        OwnerOrderContract.IView {
    private int GPS_REQUEST_CODE = 1;
    @BindView(R.id.baseRv)
    RecyclerView mRv;
    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private OwnerOrderAdapter mAdapter;
    private String mType;
    private LoadMoreWrapper mLoadMoreWrapper;

    public static OwnerOrderFragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString("type", type);
        OwnerOrderFragment fragment = new OwnerOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected OwnerOrderPresenter initPresenter() {
        return new OwnerOrderPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ownerorder_fragment;
    }

    @Override
    protected void initView(View view) {
        initBaseRv2(mRv);

        mType = getArguments().getString("type");

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(OwnerOrderListRrefreshEvent event) {
        getData();
    }

    @Override
    protected void getData() {
        mPresenter.getOrderList(mType);
    }

    @Override
    public void onRefreshSuccess(final BaseEntity<List<Order>> entity) {
        mRefreshLayout.setRefreshing(false);
        if (entity.getData() == null || entity.getData().size() == 0) {
            showNoData();
            return;
        }
        if (mAdapter == null) {
            mAdapter = new OwnerOrderAdapter(mContext, R.layout.ownerorder_recycler_item, entity.getData());
            mAdapter.setType(mType);
            mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
            mLoadMoreWrapper.setLoadMoreView(new CustomLoadMoreView());
            mRv.setAdapter(mLoadMoreWrapper);

            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if(!MyUtils.isFastClick())
                        return;
                    Intent intent = new Intent(mContext, OwnerOrderDetailActivity.class);
                    intent.putExtra("id", mAdapter.getDatas().get(position).id);
                    intent.putExtra("driverid", mAdapter.getDatas().get(position).getDriverId());
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    mPresenter.getMoreOrderList(mType);
                }
            });
        } else {
            mAdapter.setDatas(entity.getData());
            mLoadMoreWrapper.notifyDataSetChanged();
        }

        mLoadMoreWrapper.setEnableLoadMore(entity.hasNext());

        showContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mAdapter = null;
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
    public void onLoadSuccess(BaseEntity<List<Order>> entity) {
        mAdapter.setDatas(entity.getData());

        if (!entity.hasNext()) {
            mLoadMoreWrapper.loadMoreEnd();
        } else {
            mLoadMoreWrapper.loadMoreComplete();
        }
    }

    @Override
    public void onLoadError(Throwable e) {
        mLoadMoreWrapper.loadMoreFail();
    }






}
