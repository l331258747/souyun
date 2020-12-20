package com.xrwl.owner.module.home.mvp;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Auth;
import com.xrwl.owner.bean.GongAnAuth;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.retrofit.OtherRetrofitFactory;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * Created by www.longdw.com on 2018/4/29 下午9:11.
 */
public class AuthModel implements AuthContract.IModel {
    @Override
    public Observable<BaseEntity<Auth>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getAuthInfo(params).compose(RxSchedulers.<BaseEntity<Auth>>compose());
    }

    @Override
    public Observable<BaseEntity> postData(Map<String, RequestBody> params) {
        return RetrofitFactory.getInstance().postAuthInfo(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity> postputongData(Map<String, String> params) {
        return RetrofitFactory.getInstance().postputongAuthInfo(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    public Observable<BaseEntity> postDatashenfenzheng(Map<String, RequestBody> params) {
         return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").postAuthInfoshenfenzheng(params).compose(RxSchedulers.<BaseEntity>compose());
    }

    @Override
    //public/admin/idcar/cardids
    public Observable<BaseEntity<GongAnAuth>> shenfenzheng(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").shenfenzheng(params).compose(RxSchedulers
                .<BaseEntity<GongAnAuth>>compose());
    }
    @Override
    public Observable<BaseEntity> wxtel(Map<String, String> params) {
        return RetrofitFactory.getInstance().wxtel(params).compose(RxSchedulers.<BaseEntity>compose());
    }
    @Override
    public Observable<BaseEntity<MsgCode>> getCode(Map<String, String> params) {
        return OtherRetrofitFactory.getInstance("http://distance.16souyun.com/").getCode(params).compose(RxSchedulers
                .<BaseEntity<MsgCode>>compose());
    }
}
