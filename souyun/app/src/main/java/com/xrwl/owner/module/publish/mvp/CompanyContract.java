package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.module.publish.bean.CompanyManageBean;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public interface CompanyContract {

    interface IView extends BaseMVP.IBaseView<List<CompanyManageBean>> {
        void addDataSuccess(BaseEntity entity);
        void addDataError(BaseEntity entity);

        void getDataSuccess(BaseEntity<List<CompanyManageBean>> list);
        void getDataError(BaseEntity entity);
    }

    abstract class APresenter extends MyPresenter<IView> {
        public APresenter(Context context) {
            super(context);
        }

        public abstract void getData();
        public abstract void addData(Map<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<List<CompanyManageBean>>> getData(Map<String, String> params);
        Observable<BaseEntity> addData(Map<String, String> params);
    }

}
