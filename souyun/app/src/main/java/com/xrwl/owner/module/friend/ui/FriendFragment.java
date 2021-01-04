package com.xrwl.owner.module.friend.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.TitleView;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseEventFragment;
import com.xrwl.owner.event.FriendRefreshEvent;
import com.xrwl.owner.module.friend.adapter.FriendAdapter;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.module.friend.mvp.FriendAddContract;
import com.xrwl.owner.module.friend.mvp.FriendPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 好友
 * Created by www.longdw.com on 2018/3/28 下午8:51.
 */

public class FriendFragment extends BaseEventFragment<FriendAddContract.IView, FriendPresenter> implements FriendAddContract.IView {

    @BindView(R.id.baseTitleView)
    TitleView mTitleView;

    @BindView(R.id.baseRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.friendRv)
    RecyclerView mRv;
    private List<EntityWrapper<Friend>> mDatas;
    private ArrayList<Friend> mSearchDatas;

    private boolean mCanSelect;
    private ProgressDialog mSendMsgDialog;
    private boolean mCheckRegister;

    public static FriendFragment newInstance(String title) {


        return newInstance(title, false);
    }

    //该方法暂时用不上
    public static FriendFragment newInstance(String title, boolean canSelect, boolean checkRegister) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("canSelect", canSelect);
        args.putBoolean("checkRegister", checkRegister);

        FriendFragment fragment = new FriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param title
     * @param canSelect 是否是其他页面跳转过来选取 并有返回选中结果
     * @return
     */
    public static FriendFragment newInstance(String title, boolean canSelect) {

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("canSelect", canSelect);

        FriendFragment fragment = new FriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FriendPresenter initPresenter() {
        return new FriendPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.friend_fragment;
    }

    @Override
    protected void initView(View view) {
        mCanSelect = getArguments().getBoolean("canSelect");
        mCheckRegister = getArguments().getBoolean("checkRegister");

        initBaseRv(mRv);
        mTitleView.setOnTitleViewListener(new TitleView.TitleViewListener() {
            @Override
            public void onRight() {
                startActivity(new Intent(mContext, FriendAddActivity.class));
            }

            @Override
            public void onBack() {
                finish();
            }
        });

        if (mCanSelect) {
            mTitleView.setBackVisible();
        }

        mRefreshLayout.setOnRefreshListener(() -> getData());

        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(FriendRefreshEvent event) {
        getData();
    }

    @Override
    protected void getData() {
        mPresenter.requestRemoteData();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<EntityWrapper<Friend>>> entity) {
        mRefreshLayout.setRefreshing(false);
        mDatas = entity.getData();
        if (mDatas == null) {
            showNoData();
            return;
        }
        final FriendAdapter mAdapter = new FriendAdapter(mContext, mDatas, false, () -> {
            //搜索
            Intent intent = new Intent(mContext, FriendSearchActivity.class);
            intent.putExtra("data", mSearchDatas);
            startActivity(intent);
            mContext.overridePendingTransition(0, 0);
        }, f -> {
            if (!TextUtils.isEmpty(f.getPhone())) {
                mSendMsgDialog = LoadingProgress.showProgress(mContext, "正在发送邀请");
                mPresenter.sendMsg(f.getPhone());
            } else {
                showToast("该好友没有手机号码，没法发送邀请");
            }
        });
        mRv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mCanSelect) {
                    Friend friend = mDatas.get(position).getData();
//                    if (mCheckRegister) {
//                        if (!friend.isRegister()) {
//                            showToast("");
//                            return;
//                        }
//                    }
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", friend);
                    intent.putExtras(bundle);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    //跳转到好友详情页
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        showContent();
    }

    @Override
    public void onRefreshError(Throwable e) {
        showError();
    }

    @Override
    public void onError(BaseEntity entity) {
        showError();
        handleError(entity);
    }

    @Override
    public void onSearchData(ArrayList<Friend> datas) {
        mSearchDatas = datas;
    }

    @Override
    public void onSendMsgSuccess(BaseEntity entity) {
        mSendMsgDialog.dismiss();
        showToast("邀请发送成功");
    }

    @Override
    public void onSendMsgError(BaseEntity entity) {
        mSendMsgDialog.dismiss();
        handleError(entity);
    }

    @Override
    public void onSendMsgError(Throwable e) {
        mSendMsgDialog.dismiss();
        showNetworkError();
    }
}
