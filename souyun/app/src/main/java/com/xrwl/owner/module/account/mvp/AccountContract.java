package com.xrwl.owner.module.account.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.Register;
import com.xrwl.owner.bean.WxCode;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:26.
 */
public interface AccountContract {
    interface ILoginView extends BaseMVP.IBaseView<Account> {

        void onGetCodeSuccess(BaseEntity<MsgCode> entity);
        void onGetCodeError(Throwable e);

        void onwxGetCodeSuccess(BaseEntity<WxCode> entity);
        void onwxGetCodeError(Throwable e);

        void onwxinfoGetCodeSuccess(BaseEntity<WxCode> entity);
        void onwxinfoGetCodeError(Throwable e);


    }

    abstract class ALoginPresenter extends MyPresenter<ILoginView> {

        public ALoginPresenter(Context context) {
            super(context);
        }

        public abstract void login(Map<String, String> params);

        public abstract void loginwx(Map<String, String> params);

        public abstract void getCode(String mobile);

        public abstract void wxtoken(String code);

        public abstract void wxuserinfo(String access_token,String openid);

    }

    interface IRegisterView extends BaseMVP.IBaseView<Register> {
        void onGetCodeSuccess(BaseEntity<MsgCode> entity);
        void onGetCodeError(Throwable e);

        void onwxGetCodeSuccess(BaseEntity<WxCode> entity);
        void onwxGetCodeError(Throwable e);

        void onwxinfoGetCodeSuccess(BaseEntity<WxCode> entity);
        void onwxinfoGetCodeError(Throwable e);


    }

    abstract class ARegisterPresenter extends MyPresenter<IRegisterView> {

        public ARegisterPresenter(Context context) {
            super(context);
        }

        public abstract void register(Map<String, String> params);

        public abstract void getCode(String mobile);

        public abstract void wxtoken(String code);

        public abstract void wxuserinfo(String access_token,String openid);
    }

    interface IModifyView extends BaseMVP.IBaseView {
        void onGetCodeSuccess(BaseEntity<MsgCode> entity);
        void onGetCodeError(Throwable e);

        void onwxGetCodeSuccess(BaseEntity<WxCode> entity);
        void onwxGetCodeError(Throwable e);

        void onwxinfoGetCodeSuccess(BaseEntity<WxCode> entity);
        void onwxinfoGetCodeError(Throwable e);


    }

    abstract class AModifyPresenter extends MyPresenter<IModifyView> {

        public AModifyPresenter(Context context) {
            super(context);
        }

        public abstract void modify(Map<String, String> params);

        public abstract void retrieve(Map<String, String> params);

        public abstract void getCode(String phone);

        public abstract void wxtoken(String code);

        public abstract void wxuserinfo(String access_token,String openid);
    }

    interface IModel {
        Observable<BaseEntity<Account>> login(Map<String, String> params);

        Observable<BaseEntity<Account>> loginwx(Map<String, String> params);

        Observable<BaseEntity<Register>> register(Map<String, String> params);

        Observable<BaseEntity> modify(Map<String, String> params);

        Observable<BaseEntity> retrieve(Map<String, String> params);

        Observable<BaseEntity<MsgCode>> getCode(Map<String, String> params);

        Observable<BaseEntity<WxCode>> wxtoken(Map<String, String> params);

        Observable<BaseEntity<WxCode>> wxuserinfo(Map<String, String> params);

    }
}
