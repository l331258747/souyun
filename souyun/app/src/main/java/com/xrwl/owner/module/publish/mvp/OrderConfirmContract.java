package com.xrwl.owner.module.publish.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.module.publish.bean.PayResult;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/23 下午1:22.
 */
public interface OrderConfirmContract {
    interface IView extends BaseMVP.IBaseView<PayResult> {

        void resultsSuccess(BaseEntity<PayResult> entity);




        void onOnlinePaySuccess(BaseEntity<PayResult> entity);
        void wxonOnlinePaySuccess(BaseEntity<PayResult> entity);


        void onTotalPriceSuccess(String price);

        void onTixianSuccess();
        void onTixianError(BaseEntity entity);
        void onTixianException(Throwable e);

        void onHongbaoSuccess();
        void onHongbaoError(BaseEntity entity);
        void onHongbaoException(Throwable e);


        /**这个针对的是短信验证码货主和司机交互*/
        void onGetCodeSuccess(BaseEntity<MsgCode> entity);
        void onGetCodeError(Throwable e);
    }

    abstract class APresenter extends BaseMVP.BasePresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }
        public abstract void wxPay(Map<String, String> params);
        public abstract void czwxPay(Map<String, String> params);
        public abstract void wxonlinePay(Map<String, String> params);
        public abstract void onlinePay(Map<String, String> params);
        public abstract void getTotalPriceBalance();
        public abstract void tixian(String price,String yinhangka,String orderid,String zftype);
        public abstract void yuepay(String price,String yinhangka,String orderid,String zftype);
        public abstract void hongbao(String price,String userid);
        public abstract void getCodeButton(String mobile,String type,String start_city,String end_city,String order_sn);
        public abstract void getCodeButtontwo(String mobile,String type,String start_city,String end_city,String name,String phone);
        //public abstract void xrwlwxpay(String token,String total_fee,String order_id,String pay_type);
        public abstract void xrwlwxpay(Map<String, String> params);
        public abstract void results(Map<String, String> params);
    }

    interface IModel {
        Observable<BaseEntity<PayResult>> pay(Map<String, String> params);
        Observable<BaseEntity<PayResult>> czpay(Map<String, String> params);
        Observable<BaseEntity<PayResult>> wxonlinePay(Map<String, String> params);
        Observable<BaseEntity<PayResult>> onlinePay(Map<String, String> params);
        Observable<BaseEntity<HistoryOrder>> getTotalPriceBalance(Map<String, String> params);
        Observable<BaseEntity> tixian(Map<String, String> params);
        Observable<BaseEntity> yuepay(Map<String, String> params);
        Observable<BaseEntity> hongbao(Map<String, String> params);
        Observable<BaseEntity<MsgCode>> getCodeButton(Map<String, String> params);
        Observable<BaseEntity<MsgCode>> getCodeButtontwo(Map<String, String> params);

        Observable<BaseEntity<PayResult>> xrwlwxpay(Map<String, String> params);



        Observable<BaseEntity<PayResult>> results(Map<String, String> params);


    }
}
