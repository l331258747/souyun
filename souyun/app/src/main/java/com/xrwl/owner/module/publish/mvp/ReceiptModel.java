package com.xrwl.owner.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.bean.Address2;
import com.xrwl.owner.bean.Receipt;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/7/11 下午9:24.
 */
public class ReceiptModel implements ReceiptContract.IModel {

    @Override
    public Observable<BaseEntity<List<Receipt>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getReceiptList(params).compose(RxSchedulers.<BaseEntity<List<Receipt>>>compose());
    }
    @Override
    public Observable<BaseEntity> postData(Map<String, String> params) {
        return RetrofitFactory.getInstance().addReceipt(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
