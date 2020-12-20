package com.xrwl.owner.module.friend.ui;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;

/**
 * Created by www.longdw.com on 2018/4/16 下午8:44.
 */
public class FriendActivity extends BaseActivity {
    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.friend_activity;
    }

    @Override
    protected void initViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.friendContainerLayout, FriendFragment.newInstance("好友", true))
                .commit();
    }
}
