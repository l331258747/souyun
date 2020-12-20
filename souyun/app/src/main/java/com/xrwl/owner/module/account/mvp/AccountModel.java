package com.xrwl.owner.module.account.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.Register;
import com.xrwl.owner.bean.WxCode;
import com.xrwl.owner.retrofit.OtherRetrofitFactory;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/8 下午8:28.
 */
public class AccountModel implements AccountContract.IModel {

    @Override
    public Observable<BaseEntity<Account>> login(Map<String, String> params) {
        return RetrofitFactory.getInstance().login(params).compose(RxSchedulers.<BaseEntity<Account>>compose());
    }

    @Override
    public Observable<BaseEntity<Account>> loginwx(Map<String, String> params) {
        return RetrofitFactory.getInstance().loginwx(params).compose(RxSchedulers.<BaseEntity<Account>>compose());
    }

    @Override
    public Observable<BaseEntity<Register>> register(Map<String, String> params) {
        return RetrofitFactory.getInstance().register(params).compose(RxSchedulers.<BaseEntity<Register>>compose());
    }

    @Override
    public Observable<BaseEntity> modify(Map<String, String> params) {
        return RetrofitFactory.getInstance().modify(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity> retrieve(Map<String, String> params) {
        return RetrofitFactory.getInstance().modify(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity<MsgCode>> getCode(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").getCode(params).compose(RxSchedulers
                .<BaseEntity<MsgCode>>compose());
    }

    @Override
    public Observable<BaseEntity<WxCode>> wxtoken(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("https://api.weixin.qq.com/").wxtoken(params).compose(RxSchedulers
                .<BaseEntity<WxCode>>compose());
    }





    @Override
    public Observable<BaseEntity<WxCode>> wxuserinfo(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("https://api.weixin.qq.com/").wxuserinfo(params).compose(RxSchedulers
                .<BaseEntity<WxCode>>compose());
    }


}
