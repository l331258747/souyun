package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.PostOrder;
import com.xrwl.owner.module.publish.bean.CargoBean;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface CargoContract {

    interface IView extends BaseMVP.IBaseView<PostOrder> {
        void getListSuccessa(BaseEntity<List<CargoBean>> entity);
        void getListError(BaseEntity entity);
        void getListException(Throwable e);

    }

    abstract class APresenter extends MyPresenter<CargoContract.IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getList(Map<String, String> params);

    }

    interface IModel {
        Observable<BaseEntity<List<CargoBean>>> getList(Map<String, String> params);
    }
}
