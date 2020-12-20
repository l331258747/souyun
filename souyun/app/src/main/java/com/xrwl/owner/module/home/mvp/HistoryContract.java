package com.xrwl.owner.module.home.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.mvp.MyLoadPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/27 下午9:00.
 */
public interface HistoryContract {
    interface IView extends BaseMVP.IBaseLoadView<List<Order>> {
        void onRefreshCallback(String num, String price, boolean hasnext, List<Order> datas);
        void onLoadCallback(boolean hasnext, List<Order> datas);
    }

    abstract class APresenter extends MyLoadPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getList();
        public abstract void getMoreList();
    }

    interface IModel {
        Observable<BaseEntity<HistoryOrder>> getData(Map<String, String> params);
    }
}
