package com.xrwl.owner.module.publish.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.module.publish.adapter.TruckAdapter;
import com.xrwl.owner.module.publish.bean.Truck;
import com.xrwl.owner.module.publish.mvp.TruckContract;
import com.xrwl.owner.module.publish.mvp.TruckPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 选车型
 * Created by www.longdw.com on 2018/3/31 下午11:20.
 */

public class TruckActivity extends BaseActivity<TruckContract.IView, TruckPresenter> implements TruckContract.IView {

    @BindView(R.id.baseRv)
    RecyclerView mRv;
    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private TruckAdapter mAdapter;

    @Override
    protected TruckPresenter initPresenter() {
        return new TruckPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.publish_truck_activity;
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

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
        String type = getIntent().getStringExtra("categoty");
        Map<String, String> params = new HashMap<>();
        if ("0".equals(type)) {
            params.put("type", "1");
        }
        else if("5".equals(type))
        {
            params.put("type","2");
        }
        else if("6".equals(type))
        {
            params.put("type","4");
        }
        else if("1".equals(type))
        {
            params.put("type","1");
        }
        else {
            params.put("type", "2");
        }
       // mPresenter.getList(params);


        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");

        mPresenter.getListTrucks(params);
    }

    @Override
    public void onRefreshSuccess(final BaseEntity<List<Truck>> entity) {

        mRefreshLayout.setRefreshing(false);

        if (entity.getData() == null) {
            showNoData();
            return;
        }
        if (mAdapter == null) {
            mAdapter = new TruckAdapter(this, R.layout.publish_truck_recycler_item, entity.getData());
            mRv.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", entity.getData().get(position));
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } else {
            mAdapter.setDatas(entity.getData());
        }

        showContent();
    }

    @Override
    public void onRefreshError(Throwable e) {
        mRefreshLayout.setRefreshing(false);
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        mRefreshLayout.setRefreshing(false);
        handleError(entity);
    }
}
