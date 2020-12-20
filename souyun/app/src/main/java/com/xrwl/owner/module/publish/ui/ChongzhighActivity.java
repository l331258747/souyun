package com.xrwl.owner.module.publish.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.owner.module.publish.bean.Config;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.mvp.OrderConfirmContract;
import com.xrwl.owner.module.publish.mvp.OrderConfirmPresenter;
import com.xrwl.owner.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单确认
 * Created by www.longdw.com on 2018/4/3 下午4:30.
 */

public class ChongzhighActivity extends BaseActivity<OrderConfirmContract.IView, OrderConfirmPresenter> implements OrderConfirmContract.IView {



    private IWXAPI mWXApi;
    private PayBroadcastReceiver mPayBroadcastReceiver;
    private PublishBean mPublishBean;
    private PostOrder mPostOrder;
    private ProgressDialog mPayDialog;
    private ProgressDialog mXrwlwxpayDialog;
    //付款方式
    private double xzfjfs=1;
    private String canshutijiao="0";
//    @BindView(R.id.rg_tab_group)
//    RadioGroup rgTabGroup;





//    @BindView(R.id.money)
//    Spinner mmoney;




    public String goods_id=null;
    public String order_number=null;
    public String present_price=null;
    public String num=null;
    public String price=null;
    public static String token=null;
    @Override
    protected OrderConfirmPresenter initPresenter() {
        return new OrderConfirmPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.chongzhigh_activity;
    }

    @Override
    protected void initViews() {

        Intent intent = getIntent();

        goods_id=intent.getStringExtra("goods_id");
        order_number=intent.getStringExtra("order_number");
        present_price=intent.getStringExtra("goods_id");
        num=intent.getStringExtra("num");
        price=intent.getStringExtra("price");
        token=intent.getStringExtra("token");



        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPayBroadcastReceiver, filter);
        Map<String, String> params = new HashMap<>();
        params.put("appname", AppUtils.getAppName());
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("product_name", "11");
        params.put("describe", "2");

        params.put("type", "1");//1代表微信支付
        params.put("pay_type","APP");
        params.put("price", price);
        params.put("daishou", "0");
        params.put("total_fee", price);
        params.put("order_id",goods_id);
        mXrwlwxpayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
        mPresenter.xrwlwxpay(params);

    }


    @Override
    public void onRefreshSuccess(BaseEntity<PayResult> entity) {

        mPayDialog.dismiss();
        /**
         * 调用微信支付php*/
        final PayResult pr = entity.getData();
        final Config result = pr.getConfig();
        //  Log.d(TAG, "result.bean = " + result.toString());

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
        if (mXrwlwxpayDialog != null) {
            mXrwlwxpayDialog.dismiss();
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
        mXrwlwxpayDialog.dismiss();
        showToast("提交成功");

        startActivity(new Intent(this, OwnerOrderActivity.class));
        finish();
    }

    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {
        mXrwlwxpayDialog.dismiss();
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

            Log.d("qinyanwei",token);
            if (arg1.getIntExtra("type", 0) == 0) {

                Map<String,String> paramsa = new HashMap<String,String>();
                paramsa.put("goods_id",goods_id);
                paramsa.put("order_number", order_number);
                paramsa.put("present_price", present_price);
                paramsa.put("num", num);
                paramsa.put("price",price);
                //paramsa.put("token", "/mobile/index.php?m=user");
                //服务器请求路径
                String strUrlPath = "https://gh.jishanhengrui.com/api/goods/order/create_order";
                String strResult=submitPostData(strUrlPath,paramsa, "utf-8");
                showToast(strResult);

                // 模拟http请求，提交数据到服务器
                String path = "https://gh.jishanhengrui.com/api/goods/order/notify?order_number="+order_number+"";
                try {
                    URL url = new URL(path);
                    // 2.建立一个http连接
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    // 3.设置一些请求方式
                    conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
                    conn.setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");

                    int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
                    // // 503服务器内部错误
                    if (code == 1) {
                        InputStream is = conn.getInputStream();
                        // 把is的内容转换为字符串
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = is.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                        }
                        String result = new String(bos.toByteArray());
                        is.close();
                        //Toast.makeText(this, result, 0).show();
                        // showToast("注册工会成功");

                    } else {
                        showToast("请求失败");
                        //Toast.makeText(this, "请求失败，失败原因: " + code, ).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(this, "请求失败，请检查logcat日志控制台", 0).show();
                    showToast("请求失败。请检查网络");
                }



                finish();
            } else {
                showToast("付款失败");
            }
        }

    }

    /*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     */
    public static String submitPostData(String strUrlPath, Map<String, String> params, String encode) {

        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {

//String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
            URL url = new URL(strUrlPath);


            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            httpURLConnection.setRequestProperty("Device-Type", "android");
            httpURLConnection.setRequestProperty("token",token);

//设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
//获得输出流，向服务器写入数据




            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
//e.printStackTrace();
            return "err: " + e.getMessage().toString();
        }
        return "-1";
    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

}