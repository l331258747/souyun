package com.xrwl.owner.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.PostOrder;
import com.xrwl.owner.module.publish.bean.Changtulingdan;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.retrofit.OtherRetrofitFactory;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/19 下午1:33.
 */
public class PublishConfirmModel implements PublishConfirmContract.IModel {
    @Override
    public Observable<BaseEntity<PostOrder>> postOrder(Map<String, RequestBody> params) {
        return RetrofitFactory.getInstance().postOrder(params).compose(RxSchedulers.<BaseEntity<PostOrder>>compose());
    }
    @Override
    public Observable<BaseEntity<Integer>> calculateDistancecount(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://jiekou.16souyun.com/").calculateDistancecount(params).compose(RxSchedulers
                .<BaseEntity<Integer>>compose());
    }

    @Override
    public Observable<BaseEntity<Changtulingdan>> calculateDistancecountlingdan(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://jiekou.16souyun.com/").calculateDistancecountlingdan(params).compose(RxSchedulers .<BaseEntity<Changtulingdan>>compose());
    }
    @Override
    public Observable<BaseEntity<PayResult>> pay(Map<String, String> params) {
        return RetrofitFactory.getInstance().pay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }
    @Override
    public Observable<BaseEntity<PayResult>> czpay(Map<String, String> params) {
        return RetrofitFactory.getInstance().czpay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }
    @Override
    public Observable<BaseEntity<PayResult>> wxonlinePay(Map<String, String> params) {
        return RetrofitFactory.getInstance().wxonlinePay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }


    @Override
    public Observable<BaseEntity<PayResult>> results(Map<String, String> params) {
        return RetrofitFactory.getInstance().results(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }


    @Override
    public Observable<BaseEntity<PayResult>> xrwlwxpay(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://jiekou.16souyun.com/").xrwlwxpay(params).compose(RxSchedulers
                .<BaseEntity<PayResult>>compose());
    }

    @Override
    public Observable<BaseEntity<PayResult>> onlinePay(Map<String, String> params) {
        return RetrofitFactory.getInstance().onlinePay(params).compose(RxSchedulers.<BaseEntity<PayResult>>compose());
    }
    @Override
    public Observable<BaseEntity<HistoryOrder>> getTotalPriceBalance(Map<String, String> params) {
        return RetrofitFactory.getInstance().getHistoryBalanceList(params).compose(RxSchedulers.<BaseEntity<HistoryOrder>>compose());
    }
    @Override
    public Observable<BaseEntity> tixian(Map<String, String> params) {
        return RetrofitFactory.getInstance().tixian(params).compose(RxSchedulers.<BaseEntity>compose());
    }
    @Override
    public Observable<BaseEntity> yuepay(Map<String, String> params) {
        return RetrofitFactory.getInstance().yuepay(params).compose(RxSchedulers.<BaseEntity>compose());
    }
    @Override
    public Observable<BaseEntity> hongbao(Map<String, String> params) {
        return RetrofitFactory.getInstance().hongbao(params).compose(RxSchedulers.<BaseEntity>compose());
    }
    @Override
    public Observable<BaseEntity<MsgCode>> getCodeButton(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://jiekou.16souyun.com/").getCodeButton(params).compose(RxSchedulers
                .<BaseEntity<MsgCode>>compose());
    }
    @Override
    public Observable<BaseEntity<MsgCode>> getCodeButtontwo(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://jiekou.16souyun.com/").getCodeButton(params).compose(RxSchedulers
                .<BaseEntity<MsgCode>>compose());
    }
}
