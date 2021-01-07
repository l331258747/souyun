package com.xrwl.owner.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.ldw.library.view.RetryView;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.utils.AccountUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by www.longdw.com on 2017/11/2 上午9:20.
 */

public abstract class BaseFragment<V extends BaseMVP.IBaseView, P extends BaseMVP.BasePresenter<V>> extends Fragment {
    public String TAG;
    protected Activity mContext;
    protected P mPresenter;
    protected RetryView mRetryView;
    private Unbinder mUnbinder;
    protected Account mAccount;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getName();

        mAccount = AccountUtil.getAccount(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();

        //初始化Presenter
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.attach((V) this);
        }

        View view = inflater.inflate(getLayoutId(), container, false);

        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    protected Account getAccount() {
        return AccountUtil.getAccount(mContext);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRetryView(view);
        initView(view);
        handleEvents();

        //设置标题
        if (getArguments() != null) {
            String title = getArguments().getString("title");
            if (!TextUtils.isEmpty(title)) {
                setTitleText(view, title);
            }
        }
    }

    public void setTitleText(View view, String text) {
        TextView titleTv = view.findViewById(R.id.baseTitleTv);
        if (titleTv != null) {
            titleTv.setText(text);
        }
    }

    private void initRetryView(View view) {
        mRetryView = view.findViewById(R.id.baseRetryView);
        if (mRetryView != null) {
            mRetryView.setOnRetryListener(new RetryView.OnRetryListener() {
                @Override
                public void onRetry() {
                    getData();
                }
            });
        }
    }

    protected void showContent() {
        if (mRetryView != null) {
            mRetryView.showContent();
        }
    }

    protected void showError() {
        if (mRetryView != null) {
            mRetryView.showError();
        }
    }

    protected void showNoData() {
        if (mRetryView != null) {
            mRetryView.showNoData();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    protected void showNetworkError() {
        showToast(getString(R.string.toast_network_exception));
    }

    public void handleError(BaseEntity entity) {
        if (entity == null) {
            showToast(getString(R.string.toast_network_exception));
            return;
        }
        if (entity.isTokenInvalid()) {//授权过期
//            logout();
//            EventBus.getDefault().post(new LogoutEvent());
        } else {
            if (TextUtils.isEmpty(entity.getMsg())) {
//                showToast(getString(R.string.toast_network_exception));
            } else {
                showToast(entity.getMsg());
            }
        }
    }

    protected void initBaseRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    protected void initBaseRv2(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    protected void finish() {
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detach();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    protected abstract P initPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected void handleEvents() {}

    protected void getData() {}

}
