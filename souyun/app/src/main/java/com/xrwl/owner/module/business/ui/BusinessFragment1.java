package com.xrwl.owner.module.business.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.mvp.BaseMVP;
import com.ldw.library.utils.Utils;
import com.ldw.library.view.GridSpacingItemDecoration;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseFragment;
import com.xrwl.owner.bean.HomeItem;
import com.xrwl.owner.module.account.activity.WebActivity;
import com.xrwl.owner.module.home.adapter.HomeAdAdapter;
import com.xrwl.owner.module.home.adapter.HomeRecyclerAdapter;
import com.xrwl.owner.module.home.ui.DriverAuthActivity;
import com.xrwl.owner.module.home.ui.OwnerAuthActivity;
import com.xrwl.owner.retrofit.BaseSimpleObserver;
import com.xrwl.owner.retrofit.RxSchedulers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/3/25 下午11:33.
 */

public class BusinessFragment1 extends BaseFragment {

    @BindView(R.id.homeIntroRv)
    RecyclerView mHomeIntroRv;
    @BindView(R.id.homeViewPager)
    ViewPager mViewPager;
    @BindView(R.id.homeServiceRv)
    RecyclerView mHomeServiceRv;
    private HomeAdAdapter mHomeAdAdapter;
    private Disposable mDisposable;

    public static BusinessFragment1 newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);
        BusinessFragment1 fragment = new BusinessFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.business_fragment;
    }

    @Override
    protected void initView(View view) {
        mHomeIntroRv.setNestedScrollingEnabled(false);
        mHomeServiceRv.setNestedScrollingEnabled(false);

        mHomeIntroRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeIntroRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));

        mHomeServiceRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeServiceRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));


        try {
            JSONObject jsonObject = new JSONObject(Utils.getAssetsString(mContext, "home.json"));
            final HomeItem homeItem = HomeItem.parseJson(jsonObject);

            HomeRecyclerAdapter introAdapter = new HomeRecyclerAdapter(mContext, R.layout.home_recycler_item, homeItem.getData1());
            mHomeIntroRv.setAdapter(introAdapter);
            introAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    //仓库服务 等
                    HomeItem hi = homeItem.getData1().get(position);
                    Intent intent = new Intent();
                    intent.putExtra("title", hi.getTitle());
                    intent.putExtra("url", hi.getUrl());
                    intent.setClass(mContext, WebActivity.class);
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

            mHomeAdAdapter = new HomeAdAdapter(mContext, mViewPager, homeItem.getAd(), new HomeAdAdapter.OnHomeAdListener() {
                @Override
                public void onHomeAddClick(int position) {
                    if (position == 0) {//实名认证
                        if (mAccount.isOwner()) {
                            startActivity(new Intent(mContext, OwnerAuthActivity.class));
                        } else {
                            startActivity(new Intent(mContext, DriverAuthActivity.class));
                        }
                    } else if (position == 1) {

                    } else if (position == 2) {
                        Uri uri = Uri.parse("https://mp.weixin.qq.com/s?__biz=MzA3ODExNzcyNw==&mid=2649690837&idx=1&sn=e43f89827fef1c4a213e6036b27d5c5f&chksm=875c1ce5b02b95f3907221a18d2bdbdc05e8696491a7a653baee45bd442c153765455a2fc3bb&mpshare=1&scene=1&srcid=0916aH7HI8CkyWgpDEmljYyU&pass_ticket=7UOXlojkwlQb%2Foh2Ujuxo%2BBnvd39xzAOvN8hSwAtkqn9ezATmXWL2Xg9RxA3Vts3#rd");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
            });
            mViewPager.setAdapter(mHomeAdAdapter);
            //开启定时器
            loopAd();

//            HomeRecyclerAdapter serviceAdapter = new HomeRecyclerAdapter(mContext, R.layout.home_recycler_item, homeItem.getData2());
//            mHomeServiceRv.setAdapter(serviceAdapter);
//            serviceAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                    //公里数 等
//
//                    HomeItem hi = homeItem.getData2().get(position);
//                    //公里数 等
//                    Intent intent = new Intent(mContext, NearLocationActivity.class);
//                    intent.putExtra("title", hi.getTitle());
//                    startActivity(intent);
//                }
//
//                @Override
//                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
//                    return false;
//                }
//            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环播放广告
     */
    private void loopAd() {
        final int totalSize = mHomeAdAdapter.getCount();
        Observable.interval(5000, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.<Long>compose())
                .subscribe(new BaseSimpleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    protected void onHandleSuccess(Long entity) {
                        if (mViewPager.getCurrentItem() == totalSize - 1) {
                            mViewPager.setCurrentItem(0);
                        } else {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                        }
                    }

                    @Override
                    protected void onHandleError(Throwable e) {
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroyView();
    }
}
