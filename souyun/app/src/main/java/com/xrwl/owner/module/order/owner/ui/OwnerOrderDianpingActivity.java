package com.xrwl.owner.module.order.owner.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ldw.library.bean.BaseEntity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.event.OwnerOrderListRrefreshEvent;
import com.xrwl.owner.module.home.ui.CustomDialog;
import com.xrwl.owner.module.home.ui.RedPacketViewHolder;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderContract;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderDetailPresenter;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.Photo;
import com.xrwl.owner.utils.AccountUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by www.longdw.com on 2018/4/4 下午1:26.
 */
public class OwnerOrderDianpingActivity extends BaseActivity<OwnerOrderContract.IDetailView, OwnerOrderDetailPresenter>
        implements OwnerOrderContract.IDetailView {
    private boolean isInteger = false;
    private boolean isIntegers = false;
    private boolean isIntegerss = false;
    private final Handler mHandler = new Handler();
    private double myyue=0;
    private ProgressDialog mLoadingDialog;
    private List<Photo> mImagePaths;
    private String mId;
    private String mDriverId;
    private ProgressDialog mPostDialog;
    private OrderDetail mOrderDetail;
    private IWXAPI mWXApi;

    private float xzfjfs=1;
    private String canshutijiao="0";
    private String daishoulo="1";
    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private CustomDialog mRedPacketDialog;
    private String overtotal_price="0";
    private String daishoutype="0";

    private Account mAccount;
   /**评价系统*/
   @BindView(R.id.dianpingbt)
   Button mdianpingbt;//在线点评
    @BindView(R.id.pingjiacontent)
    EditText mpingjiacontent;
    @BindView(R.id.pingjiaLayout)
    LinearLayout mpingjiaLayout;
    @BindView(R.id.nameTV)
    TextView mnameTV;
    @BindView(R.id.telTV)
    TextView mtelTV;


    @BindView(R.id.chehaoTv)
    TextView mchehaoTv;
    @BindView(R.id.pingfenTv)
    TextView mpingfenTv;
    @BindView(R.id.chexingTv)
    TextView mchexingTv;


    @Override
    protected OwnerOrderDetailPresenter initPresenter() {
        return new OwnerOrderDetailPresenter(this);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.base_pay_layoutpingjia;
    }
    @Override
    protected void initViews() {
        mAccount = AccountUtil.getAccount(mContext);
        mId = getIntent().getStringExtra("id");
        mDriverId = getIntent().getStringExtra("driverid");
        mPresenter.getOrderDetail(mId);
        mPresenter.getlistpingjias(mId);

    }
    @OnClick({R.id.dianpingbt})
    public void onClick(View v) {
        int id = v.getId();
       if(id == R.id.dianpingbt)
        {
            final TextView displayTva = (TextView) findViewById(R.id.display);
            final TextView displayTvaa = (TextView) findViewById(R.id.displays);
            final TextView displayTvaaa = (TextView) findViewById(R.id.displayss);
            mPresenter.dianping(mId,mDriverId,"",displayTva.getText().toString().replace("当前评分为：",""),displayTvaa.getText().toString().replace("当前评分为：",""),displayTvaaa.getText().toString().replace("当前评分为：",""),"","",mpingjiacontent.getText().toString());
        }

    //mLoadingDialog.dismiss();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {
      //  mLoadingDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if (mPostDialog != null) {
           // mPostDialog.dismiss();
        }
       // mLoadingDialog.dismiss();
        handleError(entity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

    }

    @Override
    public void resultsSuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onNavSuccess(BaseEntity<OrderDetail> entity) {
        OrderDetail od = entity.getData();


    }

    @Override
    public void onNavError(Throwable e) {
        showToast("导航失败");
    }

    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onCancelOrderError(Throwable e) {
        //mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onCancelpayOrderSuccess(BaseEntity<OrderDetail> entity) {
       // startActivity(new Intent(this, OwnerOrderActivity.class));
        finish();
    }

    @Override
    public void onCancelpayOrderError(Throwable e) {

    }

    @Override
    public void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity) {



        finish();
    }

    @Override
    public void onConfirmOrderError(Throwable e) {
       // mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onConfirmtixingOrderSuccess(BaseEntity<OrderDetail> entity) {

        finish();
    }

    @Override
    public void onConfirmtixingOrderError(Throwable e) {
       // mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onConfirmOwnerkaishiyunshuSuccess(BaseEntity<OrderDetail> entity) {

        finish();
    }

    @Override
    public void onConfirmOwnerkaishiyunshuError(Throwable e) {
        //mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onLocationSuccess(BaseEntity<OrderDetail> entity) {

    }

    @Override
    public void onLocationError(Throwable e) {
        //mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onPaySuccess(BaseEntity<PayResult> entity) {

         //mPostDialog.dismiss();

        final PayResult pr = entity.getData();

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
    }

    @Override
    public void onPayError(Throwable e) {
       // mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {
       // showToast("提交成功");
       // startActivity(new Intent(this, OwnerOrderActivity.class));
       // finish();
    }

    @Override
    public void wxonOnlinePayError(Throwable e) {

    }

    @Override
    public void wxonOnlinedaishouPaySuccess(BaseEntity<PayResult> entity) {
         //mPostDialog.dismiss();

        final PayResult pr = entity.getData();

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
    }

    @Override
    public void wxonOnlinedaishouPayError(Throwable e) {

    }

    @Override
    public void onCancelDriverkaishiyunshuSuccess(BaseEntity<OrderDetail> entity) {
        EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());

        finish();

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

        handleError(entity);
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
        showToast("评价成功");
        Intent intent = new Intent(mContext, OwnerOrderDetailActivity.class);
        intent.putExtra("id", mId);
        intent.putExtra("driverid", mDriverId);
        startActivity(intent);
    }

    @Override
    public void onPingJiaError(BaseEntity entity) {
        showToast("评价失败");
        handleError(entity);
    }

    @Override
    public void onPingJiaException(Throwable e) {

    }

    @Override
    public void ongetlistpingjiaSuccess(BaseEntity<String> entity) {
        //String d =entity.getData();

    }

    /**
     * 这个是点评返回的数据*/
    @Override
    public void onetlistpingjiasSuccess(String duqu,String username,String dianhua,String chexing,String pingfen,String chehao) {
        if(TextUtils.isEmpty(username) || null==username)
        {
            mnameTV.setText("");
        }
        else
        {
                   try {
                        mnameTV.setText("姓名："+URLDecoder.decode(username,"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
       }
        if(TextUtils.isEmpty(chexing) || null==chexing)
        {
            mchexingTv.setText("");

        }
        else
        {
            try {
                mchexingTv.setText("车型："+URLDecoder.decode(chexing,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(TextUtils.isEmpty(chehao) || null==chehao)
        {
            mchehaoTv.setText("");
        }
        else
        {
            try {
                mchehaoTv.setText(URLDecoder.decode(chehao,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

            mtelTV.setText("电话："+dianhua);

        if("0".equals(duqu)){
            mdianpingbt.setVisibility(View.VISIBLE);
        }
        else
        {
            mdianpingbt.setVisibility(View.GONE);
        }

    }

    @Override
    public void onWx_refundSuccess(BaseEntity<PayResult> entity) {

    }

    @Override
    public void onWx_refundError(Throwable e) {

    }

    @Override
    public void ongetlistpingjiaError(BaseEntity entity) {

    }

    @Override
    public void ongetlistpingjiaException(Throwable e) {

    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }


}
