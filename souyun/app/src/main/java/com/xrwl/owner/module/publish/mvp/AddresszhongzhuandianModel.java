package com.xrwl.owner.module.publish.mvp;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Address2zhongzhuandian;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:24.
 */
public class AddresszhongzhuandianModel implements AddresszhongzhuandianContract.IModel {
    @Override
    public Observable<BaseEntity<List<Address2zhongzhuandian>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getAddresszhongzhuandianList(params).compose(RxSchedulers.<BaseEntity<List<Address2zhongzhuandian>>>compose());
    }

    @Override
    public Observable<BaseEntity> postData(Map<String, String> params) {
        return RetrofitFactory.getInstance().addAddress(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
