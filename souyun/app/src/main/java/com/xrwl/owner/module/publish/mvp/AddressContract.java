package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Address2;
import com.xrwl.owner.bean.New;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:07.
 */
public interface AddressContract {
    interface IView extends BaseMVP.IBaseView<List<Address2>> {
        void onPostSuccess(BaseEntity entity);
        void onPostError(BaseEntity entity);
        void onPostError(Throwable e);
    }

    abstract class APresenter extends MyPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getData();
        public abstract void getDatanew();
        public abstract void getDatanewshow(String id);
        public abstract void postData(HashMap<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<List<Address2>>> getData(Map<String, String> params);

        Observable<BaseEntity<List<New>>> getDatanew(Map<String, String> params);
        Observable<BaseEntity<List<New>>> getDatanewshow(Map<String, String> params);
        Observable<BaseEntity> postData(Map<String, String> params);
    }
}
