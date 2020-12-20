package com.xrwl.owner.module.me.mvp;

import android.content.Context;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.bean.Aliyunbank;
import com.xrwl.owner.bean.HistoryOrder;
import com.xrwl.owner.module.me.bean.Bank;
import com.xrwl.owner.mvp.MyPresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by www.longdw.com on 2018/4/21 上午9:24.
 */
public interface BankContract {
    interface IAddView extends BaseMVP.IBaseView {

    }

    abstract class AAddPresenter extends MyPresenter<IAddView> {

        public AAddPresenter(Context context) {
            super(context);
        }
        public abstract void addBank(Map<String, String> params);
        public abstract void getbanksanyuansu(String accountNo, String idCard, String name);
     }

    interface IView extends BaseMVP.IBaseView<List<Bank>> {
        void onTotalPriceSuccess(String price);

        void onTixianSuccess();
        void onTixianError(BaseEntity entity);
        void onTixianException(Throwable e);

        void onBanksanyuansuSuccess();
        void onBanksanyuansuError(BaseEntity entity);
        void onBanksanyuansuException(Throwable e);
    }

    abstract class APresenter extends MyPresenter<IView> {

        public APresenter(Context context) {
            super(context);
        }

        public abstract void getBankList();


        public abstract void getTotalPrice();

        public abstract void tixian(String price);

       public abstract void getbanksanyuansu(String accountNo,String idCard,String name);
    }

    interface IModel {
        Observable<BaseEntity> addBank(Map<String, String> params);
        Observable<BaseEntity<List<Bank>>> getBankList(Map<String, String> params);
        Observable<BaseEntity<HistoryOrder>> getTotalPriceBalance(Map<String, String> params);
        Observable<BaseEntity<HistoryOrder>> getTotalPrice(Map<String, String> params);
        Observable<BaseEntity> tixian(Map<String, String> params);
        Observable<BaseEntity<Aliyunbank>> getbanksanyuansu(Map<String, String> params);

    }
}
