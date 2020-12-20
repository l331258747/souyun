package com.xrwl.owner.module.business.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/8/16 下午8:31.
 */
public interface BusinessDetailContract {
    interface IView extends BaseMVP.IBaseView {

    }

    abstract class APresenter extends BaseMVP.BasePresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void requestData(String id);
    }

    interface IModel {
        Observable<BaseEntity> requestData(Map<String, String> params);
    }
}
