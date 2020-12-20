package com.xrwl.owner.module.friend.ui;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.PermissionUtils;
import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.TitleView;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.event.FriendRefreshEvent;
import com.xrwl.owner.module.friend.adapter.FriendAdapter;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.module.friend.mvp.FriendAddContract;
import com.xrwl.owner.module.friend.mvp.FriendAddPresenter;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/3/28 下午9:14.
 */

public class FriendAddActivity extends BaseActivity<FriendAddContract.IAddView, FriendAddPresenter> implements FriendAddContract
        .IAddView {

    @BindView(R.id.friendAddaRv)
    RecyclerView mRv;

    @BindView(R.id.baseTitleView)
    TitleView mTitleView;
    private List<EntityWrapper<Friend>> mDatas;
    private FriendAdapter mAdapter;
    private ArrayList<Friend> mSelectedFriendList;
    private ProgressDialog mPostDialog;


    @Override
    protected FriendAddPresenter initPresenter() {
        return new FriendAddPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.friendadd_activity;
    }

        @Override
    protected void initViews() {
        initBaseRv(mRv);

            //判断用户是否已经授权给我们了 如果没有，调用下面方法向用户申请授权，之后系统就会弹出一个权限申请的对话框
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,new String[]{Manifest.permission.READ_CONTACTS},1);
            }








         if (!PermissionUtils.isGranted(Manifest.permission.GET_ACCOUNTS)) {

            PermissionUtils utils = PermissionUtils.permission(Manifest.permission.GET_ACCOUNTS);

            utils.request();
            utils.callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    mPresenter.requestAccounts();
                }

                @Override
                public void onDenied() {
                    finish();
                }
            });
        } else {
            mPresenter.requestAccounts();
        }

        mTitleView.setOnTitleViewListener(new TitleView.TitleViewListener() {
            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onRight() {
                if (mDatas == null) {
                    showToast(getString(R.string.hint_please_select));
                    return;
                }

                mSelectedFriendList = new ArrayList<>();
                for (EntityWrapper<Friend> f : mDatas) {
                    if (f.isContent() && f.getData().isSelected()) {
                        //这里组合选择的人
                        mSelectedFriendList.add(f.getData());
                    }
                }

                mPostDialog = LoadingProgress.showProgress(mContext, "正在添加好友...");
                //循环提交
                for (Friend f : mSelectedFriendList) {
                    mPresenter.postAdd(f);
                }
            }
        });
    }







    //回调方法，无论哪种结果，最终都会回调该方法，之后在判断用户是否授权，
    // 用户同意则调用readContacts（）方法，失败则会弹窗提示失败
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //readContacts();
                } else {
                    Toast.makeText(this, "获取联系人权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }





    @Override
    public void onRefreshSuccess(BaseEntity<List<EntityWrapper<Friend>>> entity) {
        mDatas = entity.getData();
        mAdapter = new FriendAdapter(this, mDatas, true, null, null);
        mRv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                EntityWrapper<Friend> item = mDatas.get(position);
                item.getData().setSelected(!item.getData().isSelected());
                if (item.isHeader()) {//全选
                    for (EntityWrapper<Friend> f : mDatas) {
                        if (f.isContent()) {
                            f.getData().setSelected(!f.getData().isSelected());
                        }
                    }
                } else {
                    boolean isSelectAll = true;
                    for (EntityWrapper<Friend> f : mDatas) {
                        if (f.isContent() && !f.getData().isSelected()) {
                            isSelectAll = false;
                            break;
                        }
                    }

                    //控制全选
                    mDatas.get(0).getData().setSelected(isSelectAll);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }

    @Override
    public void onPostSuccess(BaseEntity entity, Friend friend) {
        friend.setSelected(false);
        mAdapter.notifyDataSetChanged();
        checkPostStatus();
    }

    @Override
    public void onPostError(BaseEntity entity) {
        checkPostStatus();
        handleError(entity);
    }

    @Override
    public void onPostError(Throwable e) {
        checkPostStatus();
    }

    int count;
    private void checkPostStatus() {
        count++;
        if (count == mSelectedFriendList.size()) {
            mPostDialog.dismiss();
            //刷新好友列表
            EventBus.getDefault().post(new FriendRefreshEvent());
            finish();
        }
    }
}
