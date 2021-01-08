package com.xrwl.owner.module.order.owner.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.event.OwnerOrderListRrefreshEvent;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderContract;
import com.xrwl.owner.module.order.owner.mvp.PayDemoPresenter;
import com.xrwl.owner.payfor.PayResult;
import com.xrwl.owner.payfor.SignUtils;
import com.xrwl.owner.payfor.ZhifuAlipay;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * 支付宝支付界面
 */
public class PayDemosActivity extends BaseActivity<OwnerOrderContract.IDetailView, PayDemoPresenter>
        implements OwnerOrderContract.IDetailView {
    public static final String PARTNER = "2088811209459119";
    public static final String SELLER = "xingrongshangcheng@163.com";
    public static final String RSA_PRIVATE = "MIIBPAIBAAJBALPNgy60++qycjC3T9wKdw9URn/mxBCZVvBRwZQNPtplfwh+dNEft8Q45jMrwfQnE92t/u54YwJKAPc2Icjp4GcCAwEAAQJBAK8looP9COi2q0WJS+Gs0A2+qm4s/RCuTmILQeZWgMN8NyhJPVP+JifQpoCk0NJcu14ua4oFlbksYsL2ZIa6bAECIQDsisqWX7lSQmYAiOaWVL8GiU8QtDfvB1qnBUgK2MB/FwIhAMKX4OMS2H9Gk5Pa/JlfUe0DXHQYsP3NPoqlOL4F+vsxAiB6TBgKP1u1qBbM1/tAZniNjJiQbl0s/IRmLljdIswD6QIhAIqDhcbdEVJ3sHDcvlWGLlDhxZu2P7ZJtK0IMr3AkAjhAiEA64IfA0cTb31IwXFPqXI+52WA85YKCkdXalyvdC0G/Xc=";
    public static final String RSA_PUBLIC = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALPNgy60++qycjC3T9wKdw9URn/mxBCZVvBRwZQNPtplfwh+dNEft8Q45jMrwfQnE92t/u54YwJKAPc2Icjp4GcCAwEAAQ==";
    private static final int SDK_PAY_FLAG = 1;
    private TextView product_subject;
    private TextView shangpin_miaoshu;
    private TextView product_price;

    //private ChangeBean changeBean;

    private String etGoodsName;
    private String etConsignee;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Intent intent = getIntent();
                        String s = intent.getStringExtra("price");//获得价格
                        String sid = intent.getStringExtra("id");//获得id
                        String pay_type=intent.getStringExtra("pay_type");
                        String Payment_method="2";//这个是支付宝支付
                        String zfb=intent.getStringExtra("zfb");
                        String zhifuren=intent.getStringExtra("zhifuren");
                        String requestrenminbi=intent.getStringExtra("requestrenminbi");
                        String yigeren=intent.getStringExtra("yigeren");
                        Map<String, String> params = new HashMap<>();
                        params.put("orderid", sid);
                        params.put("Payment_method","2");
                        if("0".equals(zfb))  //这个是全额支付
                        {
                            params.put("zhifuren",zhifuren);
                            params.put("requestrenminbi",requestrenminbi);
                            params.put("zfb","0");
                        }
                        else //支付保证金
                        {
                            params.put("zfb","1");
                            params.put("requestrenminbi",requestrenminbi);
                        }
                        mPresenter.results(params);

                        Toast.makeText(PayDemosActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());
                        //支付跳转到个人订单界面

                        startActivity(new Intent(mContext, OwnerOrderActivity.class));


                        finish();

                        } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayDemosActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayDemosActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
        product_subject = (TextView) findViewById(R.id.product_subject);
        shangpin_miaoshu = (TextView) findViewById(R.id.shangpin_miaoshu);
        product_price = (TextView) findViewById(R.id.product_price);

        String s = "16飕云";
        Intent intent = getIntent();
        String na = intent.getStringExtra("etGoodsName");
        String describe=intent.getStringExtra("describe");
        product_subject.setText(na + "");
        shangpin_miaoshu.setText(describe+"");
        String price = intent.getStringExtra("price");
        product_price.setText(price);
    }

    @Override
    protected PayDemoPresenter initPresenter() {
        return new PayDemoPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pay_main;
    }

    @Override
    protected void initViews() {

    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        Intent intent = getIntent();




        String price = intent.getStringExtra("price");

        etGoodsName = intent.getExtras().getString("etGoodsName");
        etConsignee = intent.getExtras().getString("etGoodsName");
//        if (etGoodsName == null) {
//            etGoodsName = "该商品";
//        } else if (etConsignee == null) {
//            etConsignee = "该商品";
//        }

        String orderInfo = getOrderInfo(etGoodsName, "从起点到终点", price);
        Log.e("orderInfo", orderInfo + "," + etGoodsName + "," + etConsignee);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String signnnn = sign(orderInfo);

        Log.e("sign", signnnn);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            signnnn = URLEncoder.encode(signnnn, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//
//        /**
//         * 完整的符合支付宝参数规范的订单信息
//         */
        final String payInfo = orderInfo + "&sign=\"" + signnnn + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayDemosActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
//            Intent intent = new Intent(this, H5PayDemoActivity.class);
//            Bundle extras = new Bundle();
//            /**
//             * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//             * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//             * 商户可以根据自己的需求来实现
//             */
//            String url = "http://m.taobao.com";
//            // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
//            extras.putString("url", url);
//            intent.putExtras(extras);
//            startActivity(intent);
        Intent intent = new Intent();
        intent.setClass(getBaseContext(), ZhifuAlipay.class);
        startActivity(intent);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    @Override
    public void resultsSuccess(BaseEntity<com.xrwl.owner.module.publish.bean.PayResult> entity) {

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
    public void onPaySuccess(BaseEntity<com.xrwl.owner.module.publish.bean.PayResult> entity) {

    }

    @Override
    public void onPayError(Throwable e) {

    }

    @Override
    public void wxonOnlinePaySuccess(BaseEntity<com.xrwl.owner.module.publish.bean.PayResult> entity) {

    }

    @Override
    public void wxonOnlinePayError(Throwable e) {

    }

    @Override
    public void wxonOnlinedaishouPaySuccess(BaseEntity<com.xrwl.owner.module.publish.bean.PayResult> entity) {

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
    public void onWx_refundSuccess(BaseEntity<com.xrwl.owner.module.publish.bean.PayResult> entity) {

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
    public void updateOrderdundaodaSuccess(BaseEntity entity) {

    }

    @Override
    public void updateOrderdundaodaError(BaseEntity e) {

    }

    @Override
    public void onUploadImagesSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onUploadImagesError(BaseEntity e) {

    }

    @Override
    public void onUploadImagesError(Throwable e) {

    }

//    @Override
//    public void onetlistpingjiasSuccess(String price) {
//
//    }

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
