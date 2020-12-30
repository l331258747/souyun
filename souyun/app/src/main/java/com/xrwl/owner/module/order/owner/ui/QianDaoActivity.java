package com.xrwl.owner.module.order.owner.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderContract;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderDetailPresenter;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.tab.activity.TabActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:26.
 */
public class QianDaoActivity extends BaseActivity<OwnerOrderContract.IDetailView, OwnerOrderDetailPresenter>
        implements OwnerOrderContract.IDetailView {
    @BindView(R.id.qiandao)
    ImageView mqiandao;
   @BindView(R.id.fanhuiqd)
   ImageView mfanhuiqd;
   @Override
    protected OwnerOrderDetailPresenter initPresenter() {
        return new OwnerOrderDetailPresenter(this);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.qiandao;
    }
    @Override
    protected void initViews() {

     }

    @OnClick({ R.id.qiandao, R.id.fanhuiqd})
    public void onClick(View v) {
        if (v.getId() == R.id.qiandao) {
           showToast("正在开发中....");
         }
        else if(v.getId() == R.id.fanhuiqd)
        {
         startActivity(new Intent(mContext, TabActivity.class));
        }
    }
    @Override
    public void resultsSuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onNavSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onNavError(Throwable e) {

    }

    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onCancelOrderError(Throwable e) {

    }

    @Override
    public void onCancelpayOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onCancelpayOrderError(Throwable e) {

    }

    @Override
    public void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onConfirmOrderError(Throwable e) {

    }

    @Override
    public void onConfirmtixingOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onConfirmtixingOrderError(Throwable e) {

    }

    @Override
    public void onConfirmOwnerkaishiyunshuSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onConfirmOwnerkaishiyunshuError(Throwable e) {

    }

    @Override
    public void onLocationSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onLocationError(Throwable e) {

    }

    @Override
    public void onPaySuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onPayError(Throwable e) {

    }

    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void wxonOnlinePayError(Throwable e) {

    }

    @Override
    public void wxonOnlinedaishouPaySuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void wxonOnlinedaishouPayError(Throwable e) {

    }

    @Override
    public void onCancelDriverkaishiyunshuSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onCancelDriverkaishiyunshuError(Throwable e) {

    }

    @Override
    public void onTotalPriceSuccess(String price) {

    }

    @Override
    public void onTixianSuccess() {

    }

    @Override
    public void onTixianError(BaseEntity entity) {

    }

    @Override
    public void onTixianException(Throwable e) {

    }

    @Override
    public void onTixiantuikuanSuccess() {

    }

    @Override
    public void onTixiantuikuanError(BaseEntity entity) {

    }

    @Override
    public void onTixiantuikuanException(Throwable e) {

    }

    @Override
    public void onPingJiaSuccess() {

    }

    @Override
    public void onPingJiaError(BaseEntity entity) {

    }

    @Override
    public void onPingJiaException(Throwable e) {

    }

    @Override
    public void ongetlistpingjiaSuccess(BaseEntity<String> entity) {

    }

    @Override
    public void ongetlistpingjiaError(BaseEntity entity) {

    }

    @Override
    public void ongetlistpingjiaException(Throwable e) {

    }

    @Override
    public void onetlistpingjiasSuccess(String duqu, String username, String dianhua, String chexing, String pingfen, String chehao) {

    }

    @Override
    public void onWx_refundSuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onWx_refundError(Throwable e) {

    }

 @Override
 public void updateOrderdunSuccess(BaseEntity entity) {

 }

 @Override
 public void updateOrderdunError(BaseEntity e) {

 }

 @Override
    public void onRefreshSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
