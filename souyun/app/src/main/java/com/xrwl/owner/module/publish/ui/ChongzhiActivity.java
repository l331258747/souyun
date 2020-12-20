package com.xrwl.owner.module.publish.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.owner.Frame.auxiliary.RetrofitManager1;
import com.xrwl.owner.Frame.retrofitapi.NetService;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.PostOrder;
import com.xrwl.owner.bean.wxhdBeen;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.owner.module.order.owner.ui.PayDemosActivity;
import com.xrwl.owner.module.publish.bean.Config;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.mvp.OrderConfirmContract;
import com.xrwl.owner.module.publish.mvp.OrderConfirmPresenter;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.xrwl.owner.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Math.floor;

/**
 * 订单确认
 * Created by www.longdw.com on 2018/4/3 下午4:30.
 */

public class ChongzhiActivity extends BaseActivity<OrderConfirmContract.IView, OrderConfirmPresenter> implements OrderConfirmContract.IView {

//    @BindView(R.id.ocDateTv)
////    TextView mDateTv;
//    @BindView(R.id.ocTruckTv)
//    TextView mTruckTv;
//    @BindView(R.id.ocStartLocationTv)
//    TextView mStartLocationTv;
//    @BindView(R.id.ocEndLocationTv)
//    TextView mEndLocationTv;
//    @BindView(R.id.ocTruckLayout)
//    View mTruckLayout;

    //    @BindView(R.id.ocOnlinePayCb)
//    CheckBox mOnlinePayCb;
    @BindView(R.id.ocOfflinePayCb)
    CheckBox mOfflinePayCb;
    @BindView(R.id.ocPayLayout)
    View mPayLayout;
    @BindView(R.id.ocConfirmBtn)
    Button mConfirmBtn;

    @BindView(R.id.fanhuishouye)
    Button mConfirmBtnfanhui;


    @BindView(R.id.querenzhifu)
    Button mConfirmBtnzhifu;

    private IWXAPI mWXApi;
    private PayBroadcastReceiver mPayBroadcastReceiver;
    private PublishBean mPublishBean;
    private PostOrder mPostOrder;
    private ProgressDialog mPayDialog;
    private ProgressDialog mOnlinePayDialog;
    //付款方式
    private double xzfjfs = 1;
    private String canshutijiao = "0";
//    @BindView(R.id.rg_tab_group)
//    RadioGroup rgTabGroup;


    //判断支付的复选框
    @BindView(R.id.weixinCB)
    CheckBox mweixincb;

    @BindView(R.id.zhifubaoCB)
    CheckBox mzhifubaocb;

    @BindView(R.id.yinhangkaCB)
    CheckBox myinhangkacb;

//    @BindView(R.id.money)
//    Spinner mmoney;


    @BindView(R.id.xuanzeonline)
    CheckBox mxuanzeonline;

    @BindView(R.id.xuanzeonlinexianxia)
    CheckBox mxuanzeonlinexianxia;


    @BindView(R.id.chongzhijine)
    TextView mchongzhijine;
    private Double name;
    private String s;
    private RetrofitManager1 retrofitManager;


    @Override
    protected OrderConfirmPresenter initPresenter() {
        return new OrderConfirmPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.chongzhi_activity;
    }

    @Override
    protected void initViews() {


        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPayBroadcastReceiver, filter);
        mweixincb.setChecked(true);
        Intent intent = getIntent();

        s = intent.getStringExtra("order_number");
        name = Double.valueOf(String.valueOf(floor(Double.parseDouble(intent.getStringExtra("num").replace("¥","")))));
//       String name1 = name + ".";
//        String[] split = name1.split(".");
//
        Log.e("wwww", String.valueOf(name));

//        mchongzhijine.setText(name);



    }



    @OnClick({R.id.ocOfflinePayCb, R.id.ocConfirmBtn, R.id.fanhuishouye, R.id.querenzhifu, R.id.weixinCB, R.id.zhifubaoCB, R.id.yinhangkaCB, R.id.xuanzeonline, R.id.xuanzeonlinexianxia
    })
    public void onClick(View v) {
        if (v.getId() == R.id.ocWeixinPayLayout) {
            Map<String, String> params = new HashMap<>();
            params.put("appname", AppUtils.getAppName());
            params.put("product_name", mPublishBean.productName);
            params.put("describe", mPublishBean.defaultWeight + "吨" + mPublishBean.defaultArea + "方");
            params.put("orderid", mPostOrder.orderId);
            params.put("type", "1");//1代表微信支付
            params.put("daishou", "0");
//            switch (rgTabGroup.getCheckedRadioButtonId()) {
//                case R.id.rb_owner:
//                    xzfjfs = mPublishBean.getPrice();
//                    canshutijiao="1";
//                    break;
//                case R.id.rb_driver:
//                    xzfjfs = mPublishBean.getPrice()*10/100;
//                    canshutijiao="0";
//                    break;
//            }

            params.put("price", xzfjfs + "");
            mPayDialog = LoadingProgress.showProgress(this, "正在发起支付...");
            mPresenter.wxPay(params);
        } else if (v.getId() == R.id.xuanzeonline) {
            if (mxuanzeonline.isChecked()) {
                mweixincb.setChecked(true);
                mxuanzeonline.setChecked(true);
                mxuanzeonlinexianxia.setChecked(false);
            } else {
                mweixincb.setChecked(false);
                mxuanzeonlinexianxia.setChecked(true);
                mxuanzeonline.setChecked(false);
            }
        } else if (v.getId() == R.id.xuanzeonlinexianxia) {
            if (mxuanzeonlinexianxia.isChecked()) {
                mxuanzeonline.setChecked(false);
                mxuanzeonlinexianxia.setChecked(true);
                mweixincb.setChecked(false);
                myinhangkacb.setChecked(false);
                mzhifubaocb.setChecked(false);

            } else {
                mweixincb.setChecked(true);
                myinhangkacb.setChecked(false);
                mzhifubaocb.setChecked(false);
                mxuanzeonlinexianxia.setChecked(false);
                mxuanzeonline.setChecked(true);
            }
        } else if (v.getId() == R.id.querenzhifu) {
            if (mxuanzeonlinexianxia.isChecked()) {
                Intent intent = new Intent(mContext, TabActivity.class);
                startActivity(intent);
            }

            if (mweixincb.isChecked()) {
//                if ("0".equals(mchongzhijine.getText().toString()) || TextUtils.isEmpty(mchongzhijine.getText().toString())) {
//                    new AlertDialog.Builder(this).setMessage("充值金额必须填写正确的金额")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            }).show();
//                } else {
                    new AlertDialog.Builder(this).setMessage("充值金额：有利于更好的使用余额进行支付，形成发货单，方便司机更容易进行接单")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("appname", AppUtils.getAppName());
                                    params.put("product_name", "充值");
                                    params.put("describe", "充值");
                                    params.put("orderid", "1111");
                                    params.put("type", "1");//1代表微信支付
                                    params.put("daishou", "0");
                                    params.put("userid", mAccount.getId());
                                    String renmibi = mchongzhijine.getText().toString();
                                    params.put("price", name + "");
                                    params.put("canshu", "59");
                                    params.put("pay_type", "APP");
                                    params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
                                    params.put("total_fee", name * 100 + "");
                                    params.put("order_id", "1824");

                                    mPayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
                                    mPresenter.xrwlwxpay(params);


                                    //  mPayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
                                    // mPresenter.czwxPay(params);
                                }
                            }).show();
//                }
            } else if (mzhifubaocb.isChecked()) {
                new AlertDialog.Builder(this).setMessage("充值金额：有利于更好的使用余额进行支付，形成发货单，方便司机更容易进行接单")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, PayDemosActivity.class);
                                String renmibi = mchongzhijine.getText().toString();

                                intent.putExtra("price", renmibi + "");
                                startActivity(intent);
                            }
                        }).show();
            } else if (myinhangkacb.isChecked()) {
                showToast("暂未开放，敬请期待");
                return;
            }
        } else if (v.getId() == R.id.weixinCB) {
            if (mweixincb.isChecked()) {
                mzhifubaocb.setChecked(false);
                myinhangkacb.setChecked(false);
            } else {
                mweixincb.setChecked(false);
            }
        } else if (v.getId() == R.id.zhifubaoCB) {
            if (mzhifubaocb.isChecked()) {
                mweixincb.setChecked(false);
                myinhangkacb.setChecked(false);
            } else {
                mzhifubaocb.setChecked(false);
            }
        } else if (v.getId() == R.id.yinhangkaCB) {
            if (myinhangkacb.isChecked()) {
                mweixincb.setChecked(false);
                mzhifubaocb.setChecked(false);
            } else {
                myinhangkacb.setChecked(false);
            }
        } else if (v.getId() == R.id.ocOfflinePayCb) {
            if (mOfflinePayCb.isChecked()) {
                //  mOnlinePayCb.setChecked(false);
                mPayLayout.setVisibility(View.GONE);
                mConfirmBtn.setVisibility(View.VISIBLE);
            } else {
                mConfirmBtn.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.ocConfirmBtn) {
            //这里提交
            mOnlinePayDialog = LoadingProgress.showProgress(this, "正在提交...");
            Map<String, String> params = new HashMap<>();
            params.put("id", mPostOrder.orderId);
            mPresenter.onlinePay(params);
        } else if (v.getId() == R.id.fanhuishouye) {
            new AlertDialog.Builder(this).setMessage("是否确定返回首页？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext, TabActivity.class);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    @Override
    public void onRefreshSuccess(BaseEntity<PayResult> entity) {

        mPayDialog.dismiss();
        /**
         * 调用微信支付php*/
        final PayResult pr = entity.getData();
        final Config result = pr.getConfig();
        Log.d(TAG, "result.bean = " + result.toString());

        //发起微信或支付宝支付
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!mWXApi.isWXAppInstalled()) {
                    Toast.makeText(mContext, "没有安装微信哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mWXApi.isWXAppSupportAPI()) {
                    Toast.makeText(mContext, "当前版本不支持支付功能", Toast.LENGTH_SHORT).show();
                    return;
                }
                PayReq request = new PayReq();
                request.appId = result.appid;
                request.partnerId = result.partnerid;
                request.prepayId = result.prepayid;
                request.packageValue = result.packagestr;
                request.nonceStr = result.noncestr;
                request.timeStamp = result.timestamp;
                request.sign = result.sign;
                mWXApi.registerApp(result.appid);
                mWXApi.sendReq(request);
                showToast("启动微信中...");
            }
        });

    }

    @Override
    public void onRefreshError(Throwable e) {
        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }
        if (mOnlinePayDialog != null) {
            mOnlinePayDialog.dismiss();
        }
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }
        if (mOnlinePayDialog != null) {
            mOnlinePayDialog.dismiss();
        }
        handleError(entity);
    }

    @Override
    protected void onDestroy() {

        if (mPayBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPayBroadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void resultsSuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onOnlinePaySuccess(BaseEntity<PayResult> entity) {
        mOnlinePayDialog.dismiss();
        showToast("提交成功");

        startActivity(new Intent(this, OwnerOrderActivity.class));
        finish();
    }

    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {
//        mOnlinePayDialog.dismiss();
//        new AlertDialog.Builder(this).setMessage("发货成功，请前往我的订单中查看订单")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                          startActivity(new Intent(mContext, OwnerOrderActivity.class));
//                    }
//                }).show();

        mPayDialog.dismiss();
        /**
         * 调用微信支付php*/
        final PayResult pr = entity.getData();
        final Config result = pr.getConfig();
        Log.d(TAG, "result.bean = " + result.toString());

        //发起微信或支付宝支付
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!mWXApi.isWXAppInstalled()) {
                    Toast.makeText(mContext, "没有安装微信哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mWXApi.isWXAppSupportAPI()) {
                    Toast.makeText(mContext, "当前版本不支持支付功能", Toast.LENGTH_SHORT).show();
                    return;
                }
                PayReq request = new PayReq();
                request.appId = result.appid;
                request.partnerId = result.partnerid;
                request.prepayId = result.prepayid;
                request.packageValue = result.packagestr;
                request.nonceStr = result.noncestr;
                request.timeStamp = result.timestamp;
                request.sign = result.sign;
                mWXApi.registerApp(result.appid);
                mWXApi.sendReq(request);
                showToast("启动微信中...");
            }
        });

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
    public void onHongbaoSuccess() {

    }

    @Override
    public void onHongbaoError(BaseEntity entity) {

    }

    @Override
    public void onHongbaoException(Throwable e) {

    }

    @Override
    public void onGetCodeSuccess(BaseEntity<MsgCode> entity) {

    }

    @Override
    public void onGetCodeError(Throwable e) {

    }

    private class PayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            if (arg1.getIntExtra("type", 0) == 0) {
                Map<String, String> params = new HashMap<>();
                params.put("canshu", "0509");
                params.put("userid", mAccount.getId());
                String renmibi = mchongzhijine.getText().toString();
                params.put("price", renmibi + "");
                mPresenter.czwxPay(params);
                showToast("付款成功");
                Driverpositioning();
                startActivity(new Intent(mContext, PublishSuccessActivity.class));
                finish();
            } else {
                showToast("付款失败");
            }
        }

    }
    private void Driverpositioning() {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", "200");
        params.put("order_sn", s);


        retrofitManager = RetrofitManager1.getInstance();
        retrofitManager.createReq(NetService.class)
                .wxhd(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<wxhdBeen>() {

                    private String zbblat;
                    private String zbblon;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(wxhdBeen wxhdBeen) {


                    }



                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}