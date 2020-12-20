package com.xrwl.owner.module.publish.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.xrwl.owner.module.home.ui.HongbaolistActivity;
import com.xrwl.owner.module.me.ui.BankyueActivity;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.owner.module.order.owner.ui.PayDemosActivity;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.mvp.OrderConfirmContract;
import com.xrwl.owner.module.publish.mvp.OrderConfirmPresenter;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.xrwl.owner.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单确认
 * Created by www.longdw.com on 2018/4/3 下午4:30.
 */

public class HongbaoActivity extends BaseActivity<OrderConfirmContract.IView, OrderConfirmPresenter> implements OrderConfirmContract.IView {


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



   //付款方式
    private double xzfjfs=1;
    private String canshutijiao="0";
//    @BindView(R.id.rg_tab_group)
//    RadioGroup rgTabGroup;


    @BindView(R.id.jieguo)
    TextView mjieguo;

    @BindView(R.id.fanhuijian)
    ImageView mfanhuijian;


    @BindView(R.id.money)
    TextView mmoney;


    @Override
    protected OrderConfirmPresenter initPresenter() {
        return new OrderConfirmPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_hongbao);


        Random r = new Random(100);
        //随机产生[0.8;1.6;1.8;3.6;3.8;16.16],0.8出现的概率为30%，1.6出现的概率为30%，1.8出现的概率为30%，3.6出现的概率为5%;3.8出现的概率为4%，16.16出现的概率为1%，
        double a = r.nextDouble();//随机产生[0,100)的整数，每个数字出现的概率为1%
        double b; //需要的结果数字
        if(a< 10){ //前20个数字的区间，代表30%的几率
            b = 0.8;
        }else if(a< 50){//[20,80)，60个数字的区间，代表60%的几率
            b =1.6;
        }
        else if(a< 30){//[20,80)，60个数字的区间，代表60%的几率
            b =1.8;
        }
        else if(a< 5){//[20,80)，60个数字的区间，代表60%的几率
            b =3.6;
        }
        else if(a< 4){//[20,80)，60个数字的区间，代表60%的几率
            b =1.6;
        }else{//[80,100)，20个数字区间，代表20%的几率
            b =16.16;
        }

        TextView tv = (TextView) findViewById(R.id.money);
        tv.setText(String.valueOf(b));

//        mjieguo = (TextView) findViewById(R.id.jieguo);
//        mfanhuijian = (ImageView) findViewById(R.id.fanhuijian);
//
//        //1.匿名内部类
//        mjieguo.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
////                mPresenter.hongbao(mmoney.getText().toString(),mAccount.getId());
//                Intent intent = new Intent(mContext, BankyueActivity.class);
//                intent.putExtra("title", "金        额");
//                startActivity(intent);
//
//            }
//        });
//        //1.匿名内部类
//        mfanhuijian.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, TabActivity.class);
//                startActivity(intent);
//
//            }
//        });

    }


    @Override
    public void resultsSuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onOnlinePaySuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {

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


    @Override
    public void onRefreshSuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }
}