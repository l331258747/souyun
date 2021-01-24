package com.xrwl.owner.module.publish.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.PostOrder;
import com.xrwl.owner.event.PublishClearCacheEvent;
import com.xrwl.owner.event.TabCheckEvent;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.owner.module.order.owner.ui.PayDemosActivity;
import com.xrwl.owner.module.publish.bean.Config;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.mvp.OrderConfirmContract;
import com.xrwl.owner.module.publish.mvp.OrderConfirmPresenter;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.xrwl.owner.utils.ActivityCollect;
import com.xrwl.owner.utils.Constants;
import com.xrwl.owner.utils.MyUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单确认
 * Created by www.longdw.com on 2018/4/3 下午4:30.
 */

public class OrderConfirmActivity extends BaseActivity<OrderConfirmContract.IView, OrderConfirmPresenter> implements OrderConfirmContract.IView {

//

    @BindView(R.id.ocEndLocationTvs)
    TextView mocEndLocationTvs;

    @BindView(R.id.ocStartLocationTvs)
    TextView mocStartLocationTvs;


    @BindView(R.id.paotuijiage)
    TextView mpaotuijiage;
    @BindView(R.id.yumoney)
    TextView myumoney;
    @BindView(R.id.ocDateTv)
    TextView mDateTv;
    @BindView(R.id.ocTruckTv)
    TextView mTruckTv;
    @BindView(R.id.ocStartLocationTv)
    TextView mStartLocationTv;
    @BindView(R.id.ocEndLocationTv)
    TextView mEndLocationTv;
    @BindView(R.id.ocTruckLayout)
    View mTruckLayout;
    @BindView(R.id.ocOnlinePayCb)
    CheckBox mOnlinePayCb;
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
    @BindView(R.id.onlinezhifu)
    RelativeLayout onlinezhifu;
    @BindView(R.id.wxyeIv)
    ImageView wxyeIv;
    @BindView(R.id.wxyeTv)
    TextView wxyeTv;
    @BindView(R.id.ocYuePayLayout)
    LinearLayout ocYuePayLayout;
    @BindView(R.id.wxIv)
    ImageView wxIv;
    @BindView(R.id.wxTv)
    TextView wxTv;
    @BindView(R.id.ocWeixinPayLayout)
    LinearLayout ocWeixinPayLayout;
    @BindView(R.id.aliPayIv)
    ImageView aliPayIv;
    @BindView(R.id.aliPayTv)
    TextView aliPayTv;
    @BindView(R.id.ocAliPayLayout)
    LinearLayout ocAliPayLayout;
    @BindView(R.id.aliPayIvs)
    ImageView aliPayIvs;
    @BindView(R.id.aliPayTvs)
    TextView aliPayTvs;
    @BindView(R.id.yinhangkazhifu)
    LinearLayout yinhangkazhifu;
    @BindView(R.id.xianxiazhifu)
    RelativeLayout xianxiazhifu;
    @BindView(R.id.fkje)
    TextView fkje;
    @BindView(R.id.rb_owner)
    RadioButton rbOwner;
    @BindView(R.id.zbb)
    RadioButton zbb;
    @BindView(R.id.zbbb)
    RadioButton zbbb;

    private IWXAPI mWXApi;
    private PayBroadcastReceiver mPayBroadcastReceiver;
    private PublishBean mPublishBean;
    private PostOrder mPostOrder;
    private ProgressDialog mPayDialog;
    private ProgressDialog mOnlinePayDialog;
    private ProgressDialog mXrwlwxpayDialog;
    private double xzfjfs = 1;
    private String balance;
    //private String userid;
    private String canshutijiao = "0";
    @BindView(R.id.rg_tab_group)
    RadioGroup rgTabGroup;
    @BindView(R.id.yueCB)
    ImageView myuecb;
    @BindView(R.id.weixinCB)
    ImageView mweixincb;
    @BindView(R.id.zhifubaoCB)
    ImageView mzhifubaocb;
    @BindView(R.id.yinhangkaCB)
    ImageView myinhangkacb;
    @BindView(R.id.money)
    Spinner mmoney;
    @BindView(R.id.xuanzeonline)
    CheckBox mxuanzeonline;
    @BindView(R.id.xuanzeonlinexianxia)
    CheckBox mxuanzeonlinexianxia;
    private double myyue = 0;
    private String mCode;

    @BindView(R.id.rb_driver)
    RadioButton mrb_driver;
    @BindView(R.id.paotuidispaly)
    LinearLayout mpaotuidispaly;


    private String paytype;
    private PayResult pr;
    private String s;

    @Override
    protected OrderConfirmPresenter initPresenter() {

        return new OrderConfirmPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ordercomfirm_activity;
    }

    @Override
    protected void initViews() {

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /**
         * 接受传过来的
         */
        startHideService();
        mPublishBean = (PublishBean) getIntent().getSerializableExtra("publishBean");
//        mPostOrder = (PostOrder) getIntent().getSerializableExtra("postOrder");
        paytype = getIntent().getStringExtra("paytype");
         s = getIntent().getStringExtra("postOrder");

//        mPublishBean.freight = (String) getIntent().getSerializableExtra("postOrder");



        /**
         * 支付金额的逻辑
         */
        zbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fkje.setText((CharSequence) mPublishBean.freight);
            }
        });
        zbbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String w = String.valueOf(getzbbb300());
                fkje.setText(w);
            }
        });

         /**
         * 这是那个支付 的 金额
         */
        final TextView fkje = findViewById(R.id.fkje);
        View viewById = findViewById(R.id.rb_driver);
        fkje.setText(String.valueOf(getzbbb300()));

         if ((mPublishBean.category) == 7) {
            mpaotuidispaly.setVisibility(View.GONE);
            mpaotuijiage.setVisibility(View.GONE);
            mpaotuijiage.setText(mPublishBean.freight + "元");

        } else {
            mpaotuidispaly.setVisibility(View.GONE);
            mpaotuijiage.setVisibility(View.GONE);
        }
        // mAccount.auth  判断认证1
        if (mAccount.auth.equals("1")) {
            //认证过为1就是全部可以支付
            mrb_driver.setVisibility(View.GONE);
        } else {
            //否则就是只能全额支付
            mrb_driver.setVisibility(View.GONE);
        }


        /**矿产运输 这块暂时先不考虑*/
        if (mPublishBean.category == 6) {
            mmoney.setVisibility(View.GONE);
            rgTabGroup.setVisibility(View.GONE);
        } else {
            mxuanzeonline.setChecked(true);
            mmoney.setVisibility(View.GONE);
            rgTabGroup.setVisibility(View.GONE);
        }
        /**这个主要是判断车型是否为空*/
        if (mPublishBean.truck != null) {
            if (TextUtils.isEmpty(mPublishBean.truck.title)) {
                mTruckTv.setText("无车型需求");
            } else {
                mTruckTv.setText(mPublishBean.truck.title);
            }
        } else {
            mTruckLayout.setVisibility(View.GONE);
        }


        mStartLocationTv.setText(mPublishBean.getStartPosshengshi());
        mocStartLocationTvs.setText(mPublishBean.getStartPosdezhi());

        mEndLocationTv.setText(mPublishBean.getEndPosshengshi());
        mocEndLocationTvs.setText(mPublishBean.getEndPosdezhi());

        mDateTv.setText(mPublishBean.date);
        setCheck(0);

        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPayBroadcastReceiver, filter);
        mPresenter.getTotalPriceBalance();
    }

    int isCheck = 0;
    public void setCheck(int type){
        myuecb.setImageResource(R.drawable.ic_radio_un);
        mweixincb.setImageResource(R.drawable.ic_radio_un);
        mzhifubaocb.setImageResource(R.drawable.ic_radio_un);
        myinhangkacb.setImageResource(R.drawable.ic_radio_un);
        isCheck = type;
        if(type == 0){
            myuecb.setImageResource(R.drawable.ic_radio_yes);
        }else if(type == 1){
            mweixincb.setImageResource(R.drawable.ic_radio_yes);
        }else if(type == 2){
            mzhifubaocb.setImageResource(R.drawable.ic_radio_yes);
        }else if(type == 3){
            myinhangkacb.setImageResource(R.drawable.ic_radio_yes);
        }
    }

    public double getzbbb300(){
        double zbbb300 = Double.valueOf(String.valueOf(mPublishBean.freight)) * 10 / 100;
        if(zbbb300 > 300){
            zbbb300 = 300d;
        }
        return zbbb300;
    }

    @OnClick({
            R.id.ocWeixinPayLayout, R.id.ocAliPayLayout, R.id.ocOnlinePayCb,
            R.id.ocOfflinePayCb, R.id.ocConfirmBtn, R.id.fanhuishouye, R.id.querenzhifu, R.id.yueCB,
            R.id.weixinCB, R.id.zhifubaoCB, R.id.yinhangkaCB, R.id.xuanzeonline, R.id.xuanzeonlinexianxia
    })

    public void onClick(View v) {
        if(!MyUtils.isFastClick())
            return;

        /**线上支付*/
        if (v.getId() == R.id.xuanzeonline) {
            if (mxuanzeonline.isChecked()) {
                setCheck(1);
                mxuanzeonline.setChecked(true);
                mxuanzeonlinexianxia.setChecked(false);
            } else {
                setCheck(-1);
                mxuanzeonlinexianxia.setChecked(true);
                mxuanzeonline.setChecked(false);
            }
        }
        /**线下支付*/
        else if (v.getId() == R.id.xuanzeonlinexianxia) {
            if (mxuanzeonlinexianxia.isChecked()) {
                mxuanzeonline.setChecked(false);
                mxuanzeonlinexianxia.setChecked(true);
                setCheck(-1);
            } else {
                setCheck(0);
                mxuanzeonlinexianxia.setChecked(false);
                mxuanzeonline.setChecked(true);
            }
        }
        /**确认支付*/
        else if (v.getId() == R.id.querenzhifu) {
            /**线下支付被选中*/
            if (mxuanzeonlinexianxia.isChecked()) {
                ActivityCollect.getAppCollect().finishAllNotHome();
            }
            /**微信支付被选中*/
            if (isCheck == 1) {
                mPublishBean.freight = fkje.getText().toString();
                new AlertDialog.Builder(this).setMessage("全额支付：支付全款后形成发货订单,有利于司机更好的为您接单\n保  证  金：预先支付10%的运费后,形成发货订单,确认收货后支付剩余尾款")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, String> params = new HashMap<>();
                                params.put("appname", AppUtils.getAppName());
                                params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
                                params.put("product_name", mPublishBean.productName);
                                params.put("describe", mPublishBean.defaultWeight + "吨" + mPublishBean.defaultArea + "方");
                                params.put("orderid", s);
                                params.put("type", "1");//1代表微信支付
                                params.put("pay_type", "APP");
                                String kuangchanyunfei = mmoney.getSelectedItem().toString();
                                switch (rgTabGroup.getCheckedRadioButtonId()) {
                                    case R.id.rb_owner:

                                        xzfjfs = Double.valueOf(String.valueOf(mPublishBean.freight));
                                        canshutijiao = "1";
                                        break;
                                    case R.id.rb_driver:
                                        if (Double.valueOf(String.valueOf(mPublishBean.freight)) * 10 / 100 >= 300) {
                                            xzfjfs = 300;
                                        } else {
                                            xzfjfs = Double.valueOf(String.valueOf(mPublishBean.freight)) * 10 / 100;
                                        }
                                        canshutijiao = "0";
                                        break;
                                }
                                params.put("price", xzfjfs + "");
                                params.put("daishou", "0");

                                params.put("total_fee", xzfjfs * 100 + "");

                                params.put("order_id", "19860509");

                                mXrwlwxpayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
                                mPresenter.xrwlwxpay(params);

//                               mPayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
//                               mPresenter.wxPay(params);
                            }
                        }).show();
            }
            /**余额支付被选中*/
            else if (isCheck == 0) {
                mPublishBean.freight = fkje.getText().toString();
                Map<String, String> params = new HashMap<>();
                params.put("Payment_method", "3");
//
                params.put("orderid", s);
                switch (rgTabGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_owner:
                        xzfjfs = Double.valueOf(String.valueOf(mPublishBean.freight));
                        canshutijiao = "1";
                        params.put("balance", "0");
                        params.put("requestrenminbi", String.valueOf(mPublishBean.freight));
                        params.put("zhifuren", "0");
                        break;
                    case R.id.rb_driver:
                        if (Double.valueOf(String.valueOf(mPublishBean.freight)) * 10 / 100 >= 300) {
                            xzfjfs = 300;
                            params.put("balance", "1");
                            params.put("requestrenminbi", "300");
                        } else {
                            xzfjfs = Double.valueOf(String.valueOf(mPublishBean.freight)) * 10 / 100;
                            params.put("balance", "1");
                            params.put("requestrenminbi", xzfjfs + "");
                        }
                        canshutijiao = "0";
                        break;
                }
                if (myyue >= xzfjfs) {
                    mPresenter.results(params);//这个是更改数据库余额支付的变化情况
                    //余额支付9.8折暂时去掉，恢复即可使用  mPresenter.yuepay(String.valueOf((xzfjfs-(xzfjfs *98 /100))),"6666666666666666",mPostOrder.orderId,canshutijiao);
                    mPresenter.yuepay(String.valueOf(xzfjfs), "6666666666666666", "19860509", canshutijiao);
                    /**发送短信*/
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date curDate = new Date(System.currentTimeMillis());
                    String str = formatter.format(curDate);
                    try {
                        mPresenter.getCodeButton(mPublishBean.consignorPhone, "1", URLDecoder.decode(mPublishBean.longStartCityDes, "UTF-8"), URLDecoder.decode(mPublishBean.longEndCityDes, "UTF-8"), str);
                        mPresenter.getCodeButtontwo(mPublishBean.consignorPhone, "2", URLDecoder.decode(mPublishBean.longStartCityDes, "UTF-8"), URLDecoder.decode(mPublishBean.longEndCityDes, "UTF-8"), URLDecoder.decode(mPublishBean.consigneeName, "UTF-8"), mPublishBean.consigneePhone);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    new AlertDialog.Builder(this).setMessage("余额不足，请前往充值")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(mContext, ChongzhiActivity.class));
                                }
                            }).show();
                }
            }
            /**支付宝支付被选中*/
            else if (isCheck == 2) {
                mPublishBean.freight = fkje.getText().toString();
                new AlertDialog.Builder(this).setMessage("全额支付：支付全款后形成发货订单,有利于司机更好的为您接单\n保  证  金：预先支付10%的运费后,形成发货订单,确认收货后支付剩余尾款")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, PayDemosActivity.class);
                                intent.putExtra("etGoodsName", mPublishBean.productName);
                                String dun = "";
                                String fang = "";
                                String jian = "";
                                if (!TextUtils.isEmpty(mPublishBean.defaultWeight)) {
                                    dun = mPublishBean.defaultWeight + "吨  ";
                                } else {
                                    dun = "";
                                }
                                if (!TextUtils.isEmpty(mPublishBean.defaultArea)) {
                                    fang = mPublishBean.defaultArea + "方  ";
                                } else {
                                    fang = "";
                                }
                                if (!TextUtils.isEmpty(mPublishBean.defaultNum)) {
                                    jian = mPublishBean.defaultNum + "件  ";
                                } else {
                                    jian = "";
                                }
                                String kuangchanyunfei = mmoney.getSelectedItem().toString();
                                switch (rgTabGroup.getCheckedRadioButtonId()) {
                                    case R.id.rb_owner:
                                        xzfjfs = Double.valueOf(String.valueOf(mPublishBean.freight));
                                        intent.putExtra("zfb", "0");
                                        intent.putExtra("zhifuren", "0");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                        canshutijiao = "1";
                                        break;
                                    case R.id.rb_driver:
                                        if (Double.valueOf(String.valueOf(mPublishBean.freight)) * 10 / 100 >= 300) {
                                            xzfjfs = 300;
                                            intent.putExtra("requestrenminbi", xzfjfs + "");
                                            intent.putExtra("zfb", "1");
                                        } else {
                                            xzfjfs = Double.valueOf(String.valueOf(mPublishBean.freight)) * 10 / 100;
                                            intent.putExtra("requestrenminbi", xzfjfs + "");
                                            intent.putExtra("zfb", "1");
                                        }
                                        canshutijiao = "0";
                                        break;
                                }
                                intent.putExtra("describe", dun + fang + jian);
                                intent.putExtra("id", "19860509");
                                intent.putExtra("pay_type", canshutijiao + "");
                                intent.putExtra("price", xzfjfs + "");
                                startActivity(intent);
                            }
                        }).show();
            }
            /**银行卡选中*/
            else if (isCheck == 3) {
                showToast("暂未开放，敬请期待");
                return;
            }else{
                showToast("请选择支付方式");
            }
        }
        /**余额支付*/
        else if (v.getId() == R.id.yueCB) {
            setCheck(0);
        }
        /**微信支付*/
        else if (v.getId() == R.id.weixinCB) {
            setCheck(1);
        }
        /**支付宝支付*/
        else if (v.getId() == R.id.zhifubaoCB) {
            setCheck(2);
        }
        /**银行卡支付*/
        else if (v.getId() == R.id.yinhangkaCB) {
            setCheck(3);
        }
        /**线上支付*/
        else if (v.getId() == R.id.ocOnlinePayCb) {
            if (mOnlinePayCb.isChecked()) {
                mOfflinePayCb.setChecked(false);
                mConfirmBtn.setVisibility(View.GONE);
                mPayLayout.setVisibility(View.GONE);
            } else {
                mPayLayout.setVisibility(View.GONE);
            }
        }
        /**货到现金线上支付*/
        else if (v.getId() == R.id.ocOfflinePayCb) {
            if (mOfflinePayCb.isChecked()) {
                mOnlinePayCb.setChecked(false);
                mPayLayout.setVisibility(View.GONE);
                mConfirmBtn.setVisibility(View.GONE);
            } else {
                mConfirmBtn.setVisibility(View.GONE);
            }
        }
        /**确定提交*/
        else if (v.getId() == R.id.ocConfirmBtn) {
            //这里提交
            mOnlinePayDialog = LoadingProgress.showProgress(this, "正在提交...");
            Map<String, String> params = new HashMap<>();
            params.put("id", "19860509");
            mPresenter.onlinePay(params);
        }

        /**返回首页*/
        else if (v.getId() == R.id.fanhuishouye) {
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
        pr = entity.getData();
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
//                request.appId = pr.appid;
//                request.partnerId = pr.partnerid;
//                request.prepayId = pr.prepayid;
//                request.packageValue = pr.packagestr;
//                request.nonceStr = pr.noncestr;
//                request.timeStamp = pr.timestamp;
//                request.sign = pr.sign;
//                mWXApi.registerApp(pr.appid);
                mWXApi.sendReq(request);
                showToast("启动微信中...");
            }
        });
        EventBus.getDefault().post(new PublishClearCacheEvent());

    }

    @Override
    public void onRefreshError(Throwable e) {
        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }
        if (mOnlinePayDialog != null) {
            mOnlinePayDialog.dismiss();
        }
        if (mXrwlwxpayDialog != null) {
            mXrwlwxpayDialog.dismiss();
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
        if (mXrwlwxpayDialog != null) {
            mXrwlwxpayDialog.dismiss();
        }
        handleError(entity);
    }

    /**
     * 开始预加载进程
     */
    private void startHideService() {
        Intent intent = new Intent(this, HideService.class);
        this.startService(intent);
    }

    private void stopHideService() {
        Intent intent = new Intent(this, HideService.class);
        this.stopService(intent);
    }

    @Override
    protected void onDestroy() {
        if (mPayBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPayBroadcastReceiver);
        }
        stopHideService();
        super.onDestroy();
    }


    @Override
    public void onOnlinePaySuccess(BaseEntity<PayResult> entity) {
        mOnlinePayDialog.dismiss();
        showToast("提交成功");
        EventBus.getDefault().post(new PublishClearCacheEvent());
        startActivity(new Intent(this, OwnerOrderActivity.class));
        finish();
    }

    @Override
    public void resultsSuccess(BaseEntity<PayResult> entity) {
        mXrwlwxpayDialog.dismiss();
        showToast("操作成功");
        handleError(entity);
        finish();
    }


    //现在这个方法暂时闲置
    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {

        mXrwlwxpayDialog.dismiss();
        /**
         * 调用微信支付php*/
        final PayResult pr = entity.getData();
        final Config result = pr.getConfig();
        Log.e(TAG, "result.bean = " + result.toString());

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
        EventBus.getDefault().post(new PublishClearCacheEvent());

    }

    /**
     * 判断剩余余额
     */
    @Override
    public void onTotalPriceSuccess(String price) {
        if (TextUtils.isEmpty(price)) {
            myumoney.setText("剩余余额：0元");
        } else {
            if (Double.valueOf(price) <= 0) {
                myumoney.setText("剩余余额：0元");
            } else {
                myumoney.setText("剩余余额：" + price + "元");
            }
        }

        myyue = Double.parseDouble(price);
    }

    @Override
    public void onTixianSuccess() {
        new AlertDialog.Builder(this)
                .setMessage("付款成功，请关注您的账户余额")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mContext, PublishSuccessActivity.class));
                        finish();
                    }
                }).show();
    }

    @Override
    public void onTixianError(BaseEntity entity) {
        showToast("付款失败");
        //startActivity(new Intent(mContext, PublishConfirmActivity.class));
        //handleError(entity);
        EventBus.getDefault().post(new TabCheckEvent());
    }

    @Override
    public void onTixianException(Throwable e) {
        showNetworkError();
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


    /**
     * 获取验证码支付成功和失败返回的信息
     */
    @Override
    public void onGetCodeSuccess(BaseEntity<MsgCode> entity) {
        MsgCode mc = entity.getData();
        showToast("短信已发送");
//        if (!TextUtils.isEmpty(mc.status) && mc.status.equals("0")) {
//            new AlertDialog.Builder(this)
//                    .setMessage(entity.getMsg())
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).show();
//        } else {
//
//            showToast("短信已发送");
//        }
    }

    @Override
    public void onGetCodeError(Throwable e) {
//        showToast("发送失败，请重试");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private class PayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getIntExtra("type", 0) == 0) {
                Map<String, String> params = new HashMap<>();

                params.put("orderid", s);
                params.put("Payment_method", "1");
                //params.put("userid", userid);
                double aa = mPublishBean.getNidaye();
                double bb = mPublishBean.getNidaye() * 10 / 100;
                if (canshutijiao.equals("1"))//1全额付款,0是付10%的订金
                {
                    params.put("wx", "0");
                    params.put("zhifuren", "0");  //这个肯定是发货人进行支付
                    //params.put("quankuan", "0");
                    params.put("requestrenminbi", String.valueOf(xzfjfs));
                } else {
                    params.put("wx", "1");
                    //params.put("quankuan", "1");
                    params.put("requestrenminbi", String.valueOf(xzfjfs));

                }
                // mPresenter.wxonlinePay(params);
                mPresenter.results(params);


                showToast("付款成功");
                /**发送短信*/
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                try {
                    mPresenter.getCodeButton(mPublishBean.consignorPhone, "1", URLDecoder.decode(mPublishBean.longStartCityDes, "UTF-8"), URLDecoder.decode(mPublishBean.longEndCityDes, "UTF-8"), str);
                    mPresenter.getCodeButton(mPublishBean.consigneePhone, "1", URLDecoder.decode(mPublishBean.longStartCityDes, "UTF-8"), URLDecoder.decode(mPublishBean.longEndCityDes, "UTF-8"), str);
                    mPresenter.getCodeButtontwo(mPublishBean.consignorPhone, "2", URLDecoder.decode(mPublishBean.longStartCityDes, "UTF-8"), URLDecoder.decode(mPublishBean.longEndCityDes, "UTF-8"), URLDecoder.decode(mPublishBean.consigneeName, "UTF-8"), mPublishBean.consigneePhone);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(mContext, PublishSuccessActivity.class));
                finish();
            } else {
                showToast("付款失败");
                EventBus.getDefault().post(new TabCheckEvent());

            }
        }

    }
}