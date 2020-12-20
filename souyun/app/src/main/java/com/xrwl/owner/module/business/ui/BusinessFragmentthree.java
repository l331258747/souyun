package com.xrwl.owner.module.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseEventFragment;
import com.xrwl.owner.bean.Business;
import com.xrwl.owner.event.BusinessListRefreshEvent;
import com.xrwl.owner.event.BusinessTabCountEvent;
import com.xrwl.owner.module.business.adapter.BusinessAdapter;
import com.xrwl.owner.module.business.adapter.BusinessqianbaoAdapter;
import com.xrwl.owner.module.business.adapter.BusinessxitongAdapter;
import com.xrwl.owner.module.business.mvp.BusinessContract;
import com.xrwl.owner.module.business.mvp.BusinessPresenter;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/3/25 下午11:33.
 */

public class BusinessFragmentthree extends BaseEventFragment<BusinessContract.IView, BusinessPresenter> implements BusinessContract.IView {

    @BindView(R.id.businessRv)
    RecyclerView mRv;
    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private BusinessAdapter mAdapter;
    private BusinessqianbaoAdapter mAdapterqianbao;
    private BusinessxitongAdapter mAdapterxitong;
    @BindView(R.id.xitonggengxinIv)
    ImageView mxitonggengxinIv;
    @BindView(R.id.dingdanIv)
    ImageView mdingdanIv;
    @BindView(R.id.qianbaoIv)
    ImageView mqianbaoIv;
    public static String canshu="3";
    public static BusinessFragmentthree newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        BusinessFragmentthree fragment = new BusinessFragmentthree();
        fragment.setArguments(args);
        return fragment;
    }
    @OnClick({R.id.xitonggengxinIv,R.id.dingdanIv,R.id.qianbaoIv})
    public void onClick(View v) {
        int id = v.getId();
        /**
         * classify=1 订单消息
         * classify=2 钱包消息
         * classify=3 系统更新*/
        if (id == R.id.xitonggengxinIv) {
            startActivity(new Intent(mContext, BusinessFragmentthree.class));
        }
        else if (id == R.id.dingdanIv)
        {
            startActivity(new Intent(mContext,BusinessFragment.class));

        }
        else if (id == R.id.qianbaoIv)
        {
            getActivity().getSupportFragmentManager().popBackStack();
            //startActivity(new Intent(mContext,BusinessFragmenttwo.class));
       }
    }
    @Override
    protected BusinessPresenter initPresenter() {
        return new BusinessPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.business_fragment;
    }

    @Override
    protected void initView(View view) {

        initBaseRv(mRv);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestData("3");
            }
        });
        mPresenter.requestData("3");

    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Business>> entity) {

        if (entity.getData() == null) {
            return;
        }

        EventBus.getDefault().post(new BusinessTabCountEvent(entity.getCount()));

        mRefreshLayout.setRefreshing(false);
        if (entity.getData().size() == 0) {
            showNoData();
            return;
        }


        if (mAdapterxitong == null) {
            mAdapterxitong = new BusinessxitongAdapter(mContext, R.layout.business_recycler_itemxitong, entity.getData());
            mRv.setAdapter(mAdapterxitong);
        } else {
            mAdapterxitong.setDatas(entity.getData());
            mAdapterxitong.notifyDataSetChanged();
        }
        mAdapterxitong.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Business business = mAdapterxitong.getDatas().get(position);
                Intent intent = new Intent(mContext, BusinessDetailActivity.class);
                intent.putExtra("mtitle", business.title);
                intent.putExtra("mcontent", business.content);
                intent.putExtra("id", business.id);
                intent.putExtra("canshu","3");
                startActivity(intent);
                // startActivity(new Intent(mContext, OwnerOrderActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        showContent();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BusinessListRefreshEvent event) {
        mPresenter.requestData("1");

    }

    @Override
    public void onRefreshError(Throwable e) {
        mRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onError(BaseEntity entity) {
        mRefreshLayout.setRefreshing(false);
        handleError(entity);
    }

    @Override
    public void onLoadSuccess(BaseEntity<List<Business>> entity) {

    }

    @Override
    public void onLoadError(Throwable e) {

    }
}
