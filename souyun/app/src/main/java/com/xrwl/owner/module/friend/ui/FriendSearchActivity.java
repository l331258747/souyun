package com.xrwl.owner.module.friend.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.module.friend.adapter.FriendSearchAdapter;
import com.xrwl.owner.module.friend.bean.Friend;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/3/31 下午2:03.
 */

public class FriendSearchActivity extends BaseActivity {

    @BindView(R.id.friendSearchRv)
    RecyclerView mRecyclerView;

    @BindView(R.id.friendSearchView)
    SearchView mSearchView;

    @BindView(R.id.friendSearchNoResult)
    View mNoResultView;
    private ArrayList<Friend> mDatas;

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.friend_search_activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initViews() {
        initBaseRv(mRecyclerView);
        mDatas = (ArrayList<Friend>) getIntent().getSerializableExtra("data");

        final FriendSearchAdapter adapter = new FriendSearchAdapter(this, R.layout
                .friend_recycler_item, mDatas);
        adapter.setNoResultView(mNoResultView);
        mRecyclerView.setAdapter(adapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    adapter.getFilter().filter(newText.toLowerCase());
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    adapter.setDatas(mDatas);
                }
                return false;
            }
        });

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected void handleEvents() {
        findViewById(R.id.baseBackIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
