package com.xrwl.owner.module.publish.mvp;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.module.publish.bean.CompanyManageBean;
import com.xrwl.owner.retrofit.RetrofitFactory;
import com.xrwl.owner.retrofit.RxSchedulers;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class CompanyModel implements CompanyContract.IModel{
    @Override
    public Observable<BaseEntity<List<CompanyManageBean>>> getData(Map<String, String> params) {
        return RetrofitFactory.getInstance().getCompany(params).compose(RxSchedulers.<BaseEntity<List<CompanyManageBean>>>compose());
    }

    @Override
    public Observable<BaseEntity> addData(Map<String, String> params) {
        return RetrofitFactory.getInstance().addCompany(params).compose(RxSchedulers.<BaseEntity>compose());
    }
}
