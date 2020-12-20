package com.xrwl.owner.module.friend.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.retrofit.BaseObserver;
import com.xrwl.owner.retrofit.BaseSimpleObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by www.longdw.com on 2018/3/28 下午10:23.
 */

public class FriendPresenter extends FriendAddContract.APresenter {

    private FriendAddContract.IModel mModel;

    public FriendPresenter(Context context) {
        super(context);

        mModel = new FriendAddModel();
    }

    @Override
    public void requestRemoteData() {
        Map<String, String> params = new HashMap<>();
        params.put("userid", getAccount().getId());
        mModel.getData(params).subscribe(new BaseObserver<List<Friend>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity<List<Friend>> entity) {
                if (entity.isSuccess()) {
                    BaseEntity<List<EntityWrapper<Friend>>> finalData = new BaseEntity<>();
                    if (entity.getData().size() > 0) {
                        //组装数据
                        //转换List
                        List<EntityWrapper<Friend>> result = Friend.transform(entity.getData());
                        //先返回出去给搜索页面使用
                        mView.onSearchData((ArrayList<Friend>) entity.getData());

                        //添加头部
                        EntityWrapper<Friend> header = new EntityWrapper<>();
                        Friend headerFriend = new Friend();
                        header.setData(headerFriend);
                        header.setItemType(EntityWrapper.TYPE_HEADER);
                        header.setIndexTitle("↑");
                        result.add(0, header);

                        finalData.setData(result);
                    }

                    mView.onRefreshSuccess(finalData);
                } else {
                    mView.onError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onRefreshError(e);
            }
        });
    }

    @Override
    public void sendMsg(String mobiles) {
        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("mobile", mobiles);
        mModel.sendMsg(params).subscribe(new BaseSimpleObserver<BaseEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            protected void onHandleSuccess(BaseEntity entity) {
                if (entity.isSuccess()) {
                    mView.onSendMsgSuccess(entity);
                } else {
                    mView.onSendMsgError(entity);
                }
            }

            @Override
            protected void onHandleError(Throwable e) {
                mView.onSendMsgError(e);
            }
        });
    }
}
