package com.xrwl.owner.module.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/3/25 下午11:33.
 */

public class BusinessFragmenttwo extends BaseEventFragment<BusinessContract.IView, BusinessPresenter> implements BusinessContract.IView {

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
    public static String canshu = "0";

    public static BusinessFragmenttwo newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        BusinessFragmenttwo fragment = new BusinessFragmenttwo();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.xitonggengxinIv, R.id.dingdanIv, R.id.qianbaoIv})
    public void onClick(View v) {
        int id = v.getId();
        /**
         * classify=1 订单消息
         * classify=2 钱包消息
         * classify=3 系统消息*/
        if (id == R.id.xitonggengxinIv) {
            canshu = "3";
            mPresenter.requestData("3");

        } else if (id == R.id.dingdanIv) {
            canshu = "1";
            mPresenter.requestData("1");

        } else if (id == R.id.qianbaoIv) {
            canshu = "2";
            mPresenter.requestData("2");

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

        if ("0".equals(canshu)) {
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPresenter.requestData("1");
                }
            });
        } else {
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPresenter.requestData(canshu);
                }
            });

        }
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

        if (canshu.equals("1")) {
            if (mAdapter == null) {
                mAdapter = new BusinessAdapter(mContext, R.layout.business_recycler_item, entity.getData());
                mRv.setAdapter(mAdapter);
            } else {
                mAdapter.setDatas(entity.getData());
                mAdapter.notifyDataSetChanged();
            }
            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Business business = mAdapter.getDatas().get(position);
                    Intent intent = new Intent(mContext, BusinessDetailActivity.class);
                    intent.putExtra("product_name", business.product_name);
                    intent.putExtra("mget_person", business.get_person);
                    intent.putExtra("mget_phone", business.get_phone);
                    intent.putExtra("mstart_area", business.start_area);
                    intent.putExtra("mend_area", business.end_area);
                    intent.putExtra("mstart_desc", business.start_desc);
                    intent.putExtra("mend_desc", business.end_desc);
                    intent.putExtra("mend_province", business.end_province);
                    intent.putExtra("mend_city", business.end_city);
                    intent.putExtra("mtotal_price", business.total_price);
                    intent.putExtra("mtitle", business.title);
                    intent.putExtra("id", business.id);
                    intent.putExtra("canshu", "1");
                    startActivity(intent);
                    // startActivity(new Intent(mContext, OwnerOrderActivity.class));
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            showContent();

        } else if (canshu.equals("0")) {
            if (mAdapter == null) {
                mAdapter = new BusinessAdapter(mContext, R.layout.business_recycler_item, entity.getData());
                mRv.setAdapter(mAdapter);
            } else {
                mAdapter.setDatas(entity.getData());
                mAdapter.notifyDataSetChanged();
            }
            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Business business = mAdapter.getDatas().get(position);
                    Intent intent = new Intent(mContext, BusinessDetailActivity.class);
                    intent.putExtra("product_name", business.product_name);
                    intent.putExtra("mget_person", business.get_person);
                    intent.putExtra("mget_phone", business.get_phone);
                    intent.putExtra("mstart_area", business.start_area);
                    intent.putExtra("mend_area", business.end_area);
                    intent.putExtra("mstart_desc", business.start_desc);
                    intent.putExtra("mend_desc", business.end_desc);
                    intent.putExtra("mend_province", business.end_province);
                    intent.putExtra("mend_city", business.end_city);
                    intent.putExtra("mtotal_price", business.total_price);
                    intent.putExtra("mtitle", business.title);
                    intent.putExtra("id", business.id);
                    intent.putExtra("canshu", "1");
                    startActivity(intent);
                    // startActivity(new Intent(mContext, OwnerOrderActivity.class));
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            showContent();

        } else if (canshu.equals("2")) {
            if (mAdapterqianbao == null) {
                mAdapterqianbao = new BusinessqianbaoAdapter(mContext, R.layout.business_recycler_itemqianbao, entity.getData());
                mRv.setAdapter(mAdapterqianbao);
            } else {
                mAdapterqianbao.setDatas(entity.getData());
                mAdapterqianbao.notifyDataSetChanged();
            }
            mAdapterqianbao.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Business business = mAdapterqianbao.getDatas().get(position);
                    Intent intent = new Intent(mContext, BusinessDetailActivity.class);
                    intent.putExtra("product_name", business.product_name);
                    intent.putExtra("mget_person", business.get_person);
                    intent.putExtra("mget_phone", business.get_phone);
                    intent.putExtra("mstart_area", business.start_area);
                    intent.putExtra("mend_area", business.end_area);
                    intent.putExtra("mstart_desc", business.start_desc);
                    intent.putExtra("mend_desc", business.end_desc);
                    intent.putExtra("mend_province", business.end_province);
                    intent.putExtra("mend_city", business.end_city);
                    intent.putExtra("mtotal_price", business.total_price);
                    intent.putExtra("mtitle", business.title);
                    intent.putExtra("id", business.id);
                    intent.putExtra("canshu", "2");
                    startActivity(intent);
                    // startActivity(new Intent(mContext, OwnerOrderActivity.class));
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            showContent();

        } else if (canshu.equals("3")) {
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
                    intent.putExtra("canshu", "3");
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BusinessListRefreshEvent event) {
        if (canshu == null) {
            mPresenter.requestData("1");
        } else if ("0".equals(canshu)) {
            mPresenter.requestData("1");
        } else {
            mPresenter.requestData(canshu);
        }
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
