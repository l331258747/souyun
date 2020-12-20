package com.xrwl.owner.module.friend.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.retrofit.OtherRetrofitFactory;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/18 下午2:40.
 */
public class FriendAddModel implements FriendAddContract.IModel, FriendAddContract.IAddModel {
    @Override
    public Observable<BaseEntity<List<Friend>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getFriendList(params).compose(RxSchedulers.<BaseEntity<List<Friend>>>compose());
    }

    @Override
    public Observable<BaseEntity> sendMsg(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").sendMsg(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity> postAdd(Map<String, String> params) {
        return RetrofitFactory.getInstance().addFriends(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
