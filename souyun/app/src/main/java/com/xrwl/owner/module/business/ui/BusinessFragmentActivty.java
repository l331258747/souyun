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
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.base.BaseEventFragment;
import com.xrwl.owner.bean.Business;
import com.xrwl.owner.event.BusinessListRefreshEvent;
import com.xrwl.owner.event.BusinessTabCountEvent;
import com.xrwl.owner.module.business.adapter.BusinessAdapter;
import com.xrwl.owner.module.business.mvp.BusinessContract;
import com.xrwl.owner.module.business.mvp.BusinessDetailContract;
import com.xrwl.owner.module.business.mvp.BusinessDetailPresenter;
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
public class BusinessFragmentActivty extends BaseActivity<BusinessContract.IView, BusinessPresenter> implements BusinessContract.IView {

    @BindView(R.id.businessRv)
    RecyclerView mRv;
    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private BusinessAdapter mAdapter;

    @BindView(R.id.xitonggengxinIv)
    ImageView mxitonggengxinIv;
    @BindView(R.id.dingdanIv)
    ImageView mdingdanIv;
    @BindView(R.id.qianbaoIv)
    ImageView mqianbaoIv;
   protected String canshu="0";
    public static BusinessFragmentActivty newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        BusinessFragmentActivty fragment = new BusinessFragmentActivty();
       // fragment.setArguments(args);
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
//            Intent intent = new Intent(mContext, BusinessFragmentActivty.class);
//            intent.putExtra("canshu", "3");

        }
        else if (id == R.id.dingdanIv)
        {
//            Intent intent = new Intent(mContext, BusinessFragmentActivty.class);
//            intent.putExtra("canshu", "1");
//
//            this.startActivity(intent);
        }
        else if (id == R.id.qianbaoIv)
        {
//            Intent intent = new Intent(mContext, BusinessFragmentActivty.class);
//            intent.putExtra("canshu", "2");
//            intent.setClass(mContext, BusinessFragmentActivty.class);//从哪里跳到哪里
//            this.startActivity(intent);
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
        Intent intent = getIntent();
        String tempData = intent.getStringExtra("canshunihao");
        mPresenter.requestData(tempData);
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
        getData();
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
