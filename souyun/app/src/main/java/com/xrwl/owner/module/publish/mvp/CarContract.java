package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.module.publish.bean.CarManageBean;
import com.xrwl.owner.module.publish.bean.CarManageSearchBean;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface CarContract {

    interface IView extends BaseMVP.IBaseView<List<CarManageBean>> {
        void addDataSuccess(BaseEntity entity);
        void addDataError(BaseEntity entity);

        void getDataSuccess(BaseEntity<List<CarManageBean>> list);
        void getDataError(BaseEntity entity);

        void searchDataSuccess(BaseEntity<List<CarManageSearchBean>> list);
        void searchDataError(BaseEntity entity);
    }

    abstract class APresenter extends MyPresenter<IView> {
        public APresenter(Context context) {
            super(context);
        }

        public abstract void getData();
        public abstract void addData(Map<String, String> params);
        public abstract void searchData(Map<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<List<CarManageBean>>> getData(Map<String, String> params);
        Observable<BaseEntity> addData(Map<String, String> params);
        Observable<BaseEntity<List<CarManageSearchBean>>> searchCar(Map<String, String> params);
    }

}
