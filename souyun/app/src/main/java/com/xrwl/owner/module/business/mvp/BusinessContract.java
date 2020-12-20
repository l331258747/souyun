package com.xrwl.owner.module.business.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Business;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/8/16 下午6:47.
 */
public interface BusinessContract {
    interface IView extends BaseMVP.IBaseLoadView<List<Business>> {

    }

    abstract class APresenter extends BaseMVP.BaseLoadPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void requestData(String classify);
        public abstract void requestMoreData();
    }

    interface IModel {
        Observable<BaseEntity<List<Business>>> getData(Map<String, String> params);

    }
}
