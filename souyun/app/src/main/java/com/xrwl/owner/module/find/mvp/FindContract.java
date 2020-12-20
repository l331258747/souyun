package com.xrwl.owner.module.find.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Order;
import com.xrwl.owner.mvp.MyLoadPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/5/3 下午10:10.
 */
public interface FindContract {
    interface IView extends BaseMVP.IBaseLoadView<List<Order>> {
        void showLoading();
    }

    abstract class APresenter extends MyLoadPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getData(Map<String, String> params);

        public abstract void getMoreData(Map<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<List<Order>>> getData(Map<String, String> params);
    }
}
