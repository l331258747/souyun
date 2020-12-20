package com.xrwl.owner.module.find.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.LoadMoreWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.CustomLoadMoreView;
import com.ldw.library.view.TitleView;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseEventFragment;
import com.xrwl.owner.bean.Address;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.bean.ProductSearch;
import com.xrwl.owner.event.DriverListRrefreshEvent;
import com.xrwl.owner.module.find.adapter.FindAdapter;
import com.xrwl.owner.module.find.dialog.ChooseAddressDialog;
import com.xrwl.owner.module.find.dialog.ProductSearchDialog;
import com.xrwl.owner.module.find.mvp.FindContract;
import com.xrwl.owner.module.find.mvp.FindPresenter;
import com.xrwl.owner.module.order.driver.ui.DriverOrderDetailActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 司机找货
 * Created by www.longdw.com on 2018/4/5 上午9:17.
 */
public class FindFragment extends BaseEventFragment<FindContract.IView, FindPresenter> implements ChooseAddressDialog
        .OnSelectListener, FindContract.IView, ProductSearchDialog.OnProductSearchListener {

    @BindView(R.id.baseRv)
    RecyclerView mRv;

    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.baseTitleView)
    TitleView mTitleView;

    @BindView(R.id.findStartTv)
    TextView mStartTv;

    @BindView(R.id.findEndTv)
    TextView mEndTv;

    Map<String, String> params = new HashMap<>();

    private List<Address> mSelectedEndAddressList;
    private ChooseAddressDialog mStartAddressDialog;
    private ChooseAddressDialog mEndAddressDialog;
    private ProductSearchDialog mProductSearchDialog;
    private FindAdapter mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption = null;
    private ProductSearch mProductSearch;

    public static FindFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        FindFragment fragment = new FindFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FindPresenter initPresenter() {
        return new FindPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.find_fragment;
    }

    @Override
    protected void initView(View view) {
        initBaseRv(mRv);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        initLocation();
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(mContext);
        mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    String city = aMapLocation.getCity();
                    String chuangcity = aMapLocation.getCity();
                    mStartTv.setText(city);

                    params.put("start", city);
                    String sijicity=null;
                    try {
                        sijicity= URLEncoder.encode(chuangcity,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    params.put("current_city", sijicity);
                }
                else {
                    params.put("start", "全国");
                }

                mLocationClient.stopLocation();

                getData();

            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(DriverListRrefreshEvent event) {
        getData();
    }

    @Override
    protected void getData() {
        mPresenter.getData(params);
    }

    private void getMoreData() {
        Map<String, String> params = new HashMap<>();
        params.put("", "");
        mPresenter.getMoreData(params);
    }

    @OnClick({R.id.findStartLayout, R.id.findEndLayout, R.id.findSearchLayout})
    public void onClick(View v) {
        if (v.getId() == R.id.findStartLayout) {
            if (mStartAddressDialog != null) {
                mStartAddressDialog.dismiss();
                return;
            }
            mStartAddressDialog = ChooseAddressDialog.newInstance(mTitleView.getHeight() + v.getHeight(), true);
            mStartAddressDialog.setOnSelectListener(this);
            mStartAddressDialog.show(getChildFragmentManager(), "start");
        } else if (v.getId() == R.id.findEndLayout) {
            if (mEndAddressDialog != null) {
                mEndAddressDialog.dismiss();
                return;
            }
            mEndAddressDialog = ChooseAddressDialog.newInstance(mTitleView.getHeight() + v.getHeight(), false);
            mEndAddressDialog.setOnSelectListener(this);
            mEndAddressDialog.setCurrentZones(mSelectedEndAddressList);
            mEndAddressDialog.show(getChildFragmentManager(), "end");
        } else if(v.getId() == R.id.findSearchLayout) {//高级搜索
            if (mProductSearchDialog != null) {
                mProductSearchDialog.dismiss();
                return;
            }
            mProductSearchDialog = ProductSearchDialog.newInstance(mTitleView.getHeight() + v.getHeight(), mProductSearch);
            mProductSearchDialog.setOnDismissListener(this);
            mProductSearchDialog.show(getChildFragmentManager(), "search");
        }
    }

    /** 起点单选回调 */
    @Override
    public void onSingleSelect(Address address) {
        mStartTv.setText(address.getName());

        params.put("start", address.getName());

        getData();
    }

    /** 终点多选回调 */
    @Override
    public void onMultiSelect(List<Address> datas) {
        mSelectedEndAddressList = datas;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = datas.size();i < size;i++) {
            Address a = datas.get(i);
            sb.append(a.getName());
            if (i < size - 1) {
                sb.append(",");
            }
        }
        mEndTv.setText(sb.toString());

        params.put("end", sb.toString());

        getData();
    }

    /** 高级搜索回调 */
    @Override
    public void onProductSearch(ProductSearch ps) {

        String encodeParme=null;
        try {
            encodeParme= URLEncoder.encode(ps.productName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("product_name", encodeParme);

        params.put("short_truck", ps.shortTrucks);
        params.put("long_truck", ps.longTrucks);
        params.put("category", ps.category + "");
        params.put("start_date", ps.startDate);
        params.put("end_date", ps.endDate);

        mProductSearch = ps;

        getData();
    }

    @Override
    public void onDialogDismiss() {
        mStartAddressDialog = null;
        mEndAddressDialog = null;
        mProductSearchDialog = null;
    }

    @Override
    public void onRefreshSuccess(final BaseEntity<List<Order>> entity) {

        mRefreshLayout.setRefreshing(false);
        if (entity.getData().size() == 0) {
            showNoData();
            return;
        }

        if (mAdapter == null) {
            mAdapter = new FindAdapter(mContext, R.layout.find_recycler_item, entity.getData());
            mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
            mLoadMoreWrapper.setLoadMoreView(new CustomLoadMoreView());
            mRv.setAdapter(mLoadMoreWrapper);

            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(mContext, DriverOrderDetailActivity.class);
                    intent.putExtra("id", mAdapter.getDatas().get(position).id);
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
                    getMoreData();
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

    @Override
    public void onDestroyView() {

        if (mLocationClient != null) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
        }

        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }
}
