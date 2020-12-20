package com.xrwl.owner.module.friend.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.module.friend.bean.EntityWrapper;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/3/28 下午8:54.
 */

public interface FriendAddContract {
    interface IView extends BaseMVP.IBaseView<List<EntityWrapper<Friend>>> {
        /** 返回数据给搜索界面使用 */
        void onSearchData(ArrayList<Friend> datas);
        void onSendMsgSuccess(BaseEntity entity);
        void onSendMsgError(BaseEntity entity);
        void onSendMsgError(Throwable e);
    }

    interface IAddView extends BaseMVP.IBaseView<List<EntityWrapper<Friend>>> {
        void onPostSuccess(BaseEntity entity, Friend friend);
        void onPostError(BaseEntity entity);
        void onPostError(Throwable e);
    }

    abstract class APresenter extends MyPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void requestRemoteData();
        public abstract void sendMsg(String mobiles);
    }

    abstract class AAddPresenter extends MyPresenter<IAddView> {

        public AAddPresenter(Context context) {
            super(context);
        }

        public abstract void requestAccounts();

        public abstract void postAdd(Friend friend);
    }

    interface IModel {
        Observable<BaseEntity<List<Friend>>> getData(Map<String, String> params);
        Observable<BaseEntity> sendMsg(Map<String, String> params);
    }

    interface IAddModel {
        Observable<BaseEntity> postAdd(Map<String, String> params);
    }
}
