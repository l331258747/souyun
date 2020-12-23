package com.xrwl.owner.module.home.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Auth;
import com.xrwl.owner.bean.GongAnAuth;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/29 下午8:43.
 */
public interface AuthContract {
    interface IView extends BaseMVP.IBaseView<Auth> {
        void onPostSuccess(BaseEntity entity);
        void onPostError(BaseEntity entity);
        void onPostError(Throwable e);
        void shenfenzhengSuccess(BaseEntity<GongAnAuth> entity);
        void shenfenzhengError(BaseEntity entity);
        void onGetCodeSuccess(BaseEntity<MsgCode> entity);
        void onGetCodeError(Throwable e);

    }

    abstract class APresenter extends MyPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void postData(Map<String, String> picMaps, Map<String, String> params);
        public abstract void postDatashenfenzheng(Map<String, String> picMaps, Map<String, String> params);
        public abstract void postputongData(Map<String, String> params);
        public abstract void getData();
        public abstract void shenfenzheng(String face_cardimg);
        public abstract void wxtel(String tel,String openid,String unionid,String nickname);
        public abstract void getCode(String phone);
    }

    interface IModel {
        Observable<BaseEntity<Auth>> getData(Map<String, String> params);
        Observable<BaseEntity> postData(Map<String, RequestBody> params);
        Observable<BaseEntity> postputongData(Map<String, String> params);
        Observable<BaseEntity> postDatashenfenzheng(Map<String, RequestBody> params);
        Observable<BaseEntity<GongAnAuth>> shenfenzheng(Map<String, String> params);
        Observable<BaseEntity> wxtel(Map<String, String> params);
        Observable<BaseEntity<MsgCode>> getCode(Map<String, String> params);
    }
}
