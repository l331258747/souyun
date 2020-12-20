package com.xrwl.owner.module.order.driver.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.event.DriverListRrefreshEvent;
import com.xrwl.owner.event.DriverOrderListRrefreshEvent;
import com.xrwl.owner.module.order.driver.mvp.DriverOrderContract;
import com.xrwl.owner.module.order.driver.mvp.DriverOrderDetailPresenter;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.Photo;
import com.xrwl.owner.utils.AMapUtil;
import com.xrwl.owner.utils.Constants;
import com.xrwl.owner.view.PhotoRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/5 下午9:52.
 */
public class DriverOrderDetailActivity extends BaseActivity<DriverOrderContract.IDetailView, DriverOrderDetailPresenter>
        implements DriverOrderContract.IDetailView {

    @BindView(R.id.detailStartPosTv)
    TextView mStartPosTv;
    @BindView(R.id.detailEndPosTv)
    TextView mEndPosTv;
    @BindView(R.id.detailProductNameTv)
    TextView mProductNameTv;
    @BindView(R.id.detailTruckTv)
    TextView mTruckTv;
    @BindView(R.id.detailAreaTv)
    TextView mAreaTv;
    @BindView(R.id.detailPriceTv)
    TextView mPriceTv;
    @BindView(R.id.detailWeightTv)
    TextView mWeightTv;
    @BindView(R.id.detailKiloTv)
    TextView mKiloTv;
    @BindView(R.id.detailNumTv)
    TextView mNumTv;

    @BindView(R.id.detailCancelBtn)
    Button mCancelBtn;
    @BindView(R.id.detailNavBtn)
    Button mNavBtn;
    @BindView(R.id.detailGrabBtn)
    Button mGrabBtn;
    @BindView(R.id.detailTransBtn)
    Button mTransBtn;
    @BindView(R.id.detailConfirmBtn)
    Button mConfirmBtn;
    @BindView(R.id.detailUploadBtn)
    Button mUploadBtn;

    @BindView(R.id.detailPhotoView)
    PhotoRecyclerView mPhotoView;

    @BindView(R.id.detailConsignorTv)
    TextView mConsignorTv;//发货电话
    @BindView(R.id.detailConsigneeTv)
    TextView mConsigneeTv;//收货电话

    @BindView(R.id.detailRemarkTv)
    TextView mRemarkTv;//备注

    @BindView(R.id.payLayout)
    LinearLayout mPayLayout;

    @BindView(R.id.detailOrderIdTv)
    TextView mOrderIdTv;//订单编号

    private ProgressDialog mLoadingDialog;
    private List<Photo> mImagePaths;
    private ProgressDialog mOperationDialog;
    private String mId;

    private String mConsignorPhone;
    private String mConsigneePhone;
    private OrderDetail mOrderDetail;
    private ProgressDialog mPayDialog;
    private IWXAPI mWXApi;
    private PayBroadcastReceiver mPayBroadcastReceiver;
    private boolean mConfirmClick;

    @Override
    protected DriverOrderDetailPresenter initPresenter() {
        return new DriverOrderDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {

        return R.layout.driverorderdetail_activity;
    }

    @Override
    protected void initViews() {

        //简单暴力，强制使用，代码修改简单（但是非常不推荐）
      if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPhotoView.setNestedScrollingEnabled(false);
        }

        mPhotoView.setOnPhotoRvControlListener(new PhotoRecyclerView.OnPhotoRvControlListener() {
            @Override
            public void onCamera() {
                PictureSelector.create(mContext)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .compress(true)
                        .circleDimmedLayer(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPayBroadcastReceiver, filter);

        mId = getIntent().getStringExtra("id");
        mPresenter.getOrderDetail(mId);
    }

    @Override
    public void showLoading() {
        mLoadingDialog = LoadingProgress.showProgress(this, "正在加载...");
    }

    @Override

    public void onWxPaySuccess(BaseEntity<PayResult> entity) {
        mPayDialog.dismiss();
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
//                  mWXApi.registerApp(pr.appid);
                mWXApi.sendReq(request);
                showToast("启动微信中...");
            }
        });
    }

    @Override
    public void onWxPayError(Throwable e) {
        mPayDialog.dismiss();
        showToast("网络异常，支付失败");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRefreshSuccess(BaseEntity<OrderDetail> entity) {
        OrderDetail od = entity.getData();
        mOrderDetail = od;
        mStartPosTv.setText(od.startPos);
        mEndPosTv.setText(od.endPos);
        mProductNameTv.setText("货名：" + od.productName);
        mTruckTv.setText("车型：" + od.truck);
        mAreaTv.setText("体积：" + od.area + "方");
        if(od.category.equals("0"))
        {
            float startPrice = Float.parseFloat(od.price);
            float abcd=0;
            abcd=(startPrice-(startPrice * 8)/100)+1;
            mPriceTv.setText("价格：" + abcd + "元");
        }
        else if(od.category.equals("5"))
        {
            float startPrice = Float.parseFloat(od.price);
            float abcd=0;
            abcd=(startPrice-(startPrice * 8)/100)+1;
            mPriceTv.setText("价格：" + abcd + "元");
        }
        else if(od.category.equals("1"))
        {
            float startPrice = Float.parseFloat(od.price);
            float abcd=0;
            abcd=startPrice-(startPrice * 6)/100+1;
            mPriceTv.setText("价格：" + abcd + "元");
        }
        else if(od.category.equals("2"))
        {
            float startPrice = Float.parseFloat(od.price);
            float abcd=0;
            abcd=startPrice-(startPrice * 4)/100+1;
            mPriceTv.setText("价格：" + abcd + "元");
        }
        else
        {
            mPriceTv.setText("价格：" + od.price + "元");
        }
        mWeightTv.setText("重量：" + od.weight + "吨");
        mKiloTv.setText("公里：" + od.kilo + "公里");
       // mNumTv.setText("数量：" + od.num + "件");
        mConsignorTv.setText("发货电话：" + (mConsignorPhone = od.consignorPhone));
        mConsigneeTv.setText("收货电话：" + (mConsigneePhone = od.consigneePhone));
        mRemarkTv.setText(od.remark);

        mImagePaths = od.getPics();

        mOrderIdTv.setText("订单编号：" + od.orderId);

        mPhotoView.setDatas(od.getPics());

        mLoadingDialog.dismiss();

        if (od.type.equals("0")) {
            mGrabBtn.setVisibility(View.VISIBLE);
            mCancelBtn.setVisibility(View.GONE);
            mNavBtn.setVisibility(View.GONE);
            mTransBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);

        } else if (od.type.equals("1")) {
            mCancelBtn.setVisibility(View.VISIBLE);
            mNavBtn.setVisibility(View.VISIBLE);
            mTransBtn.setVisibility(View.VISIBLE);
            mGrabBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
            mUploadBtn.setVisibility(View.VISIBLE);

        } else if (od.type.equals("2")) {
            mNavBtn.setVisibility(View.VISIBLE);
            mTransBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.VISIBLE);
            mGrabBtn.setVisibility(View.GONE);
            mCancelBtn.setVisibility(View.GONE);
            mUploadBtn.setVisibility(View.VISIBLE);

        } else if (od.type.equals("3")) {
            mNavBtn.setVisibility(View.GONE);
            mTransBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
            mGrabBtn.setVisibility(View.GONE);
            mCancelBtn.setVisibility(View.GONE);
            mPhotoView.setVisibility(View.VISIBLE);
            mUploadBtn.setVisibility(View.VISIBLE);
        } else if (od.type.equals("4")) {
            new AlertDialog.Builder(this)
                    .setMessage(entity.getMsg())
                    .setPositiveButton("确定！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            EventBus.getDefault().post(new DriverListRrefreshEvent());

                            finish();
                        }
                    }).show();
        }

//        if (od.canUpload()) {
//            mUploadBtn.setVisibility(View.VISIBLE);
//            mPhotoView.hideCamera();
//        }

        if (!od.showOnPay()) {
            mPayLayout.setVisibility(View.GONE);
        } else {
            if (mConfirmClick) {
                wxPay();
                mConfirmClick = false;
            }
        }
    }

    @Override
    public void onRefreshError(Throwable e) {
        mLoadingDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if (mOperationDialog != null) {
            mOperationDialog.dismiss();
        }
        mLoadingDialog.dismiss();
        handleError(entity);
    }

    @OnClick({R.id.detailConsignorTv, R.id.detailConsigneeTv})
    public void call(View v) {
        int id = v.getId();
        if (id == R.id.detailConsignorTv) {
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
                    || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (!TextUtils.isEmpty(mConsignorPhone)) {
                    Intent intent2 = new Intent();
                    intent2.setData(Uri.parse("tel:" + mConsignorPhone));
                    intent2.setAction(Intent.ACTION_CALL);
                    startActivity(intent2);
                }
            } else {
                new AlertDialog.Builder(this).setMessage("请先打开电话权限")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.openAppSettings();
                            }
                        }).show();
            }
        } else if (id == R.id.detailConsigneeTv) {
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
                    || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (!TextUtils.isEmpty(mConsigneePhone)) {

                    new AlertDialog.Builder(this).setMessage("是否拨打电话？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent2 = new Intent();
                                    intent2.setData(Uri.parse("tel:" + mConsigneePhone));
                                    intent2.setAction(Intent.ACTION_CALL);
                                    startActivity(intent2);
                                }
                            }).show();
                }
            } else {
                new AlertDialog.Builder(this).setMessage("请先打开电话权限")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.openAppSettings();
                            }
                        }).show();
            }
        }
    }

    @OnClick({R.id.detailCancelBtn, R.id.detailNavBtn, R.id.detailTransBtn, R.id.detailConfirmBtn, R.id.detailGrabBtn,
            R.id.detailUploadBtn})
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.detailCancelBtn) {//取消订单
            new AlertDialog.Builder(this).setMessage("是否确定取消订单？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.cancelOrder(mId);
                        }
                    }).show();
            return;
        } else if (id == R.id.detailNavBtn) {//导航
            new AlertDialog.Builder(this)
                    .setMessage("您确定要线路导航吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.nav(mId);
                        }
                    }).show();
        } else if (id == R.id.detailTransBtn) {//开始运输
            new AlertDialog.Builder(this)
                    .setMessage("您确定要开始运输吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.trans(mId);

                        }
                    }).show();
        } else if (id == R.id.detailConfirmBtn) {//确认
            new AlertDialog.Builder(this)
                    .setMessage("您确定要确认收货吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                            public void onClick(DialogInterface dialog, int which) {
                            wxPay();
                            mConfirmClick = true;
                            mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.confirmOrder(mId);
                        }
                    }).show();
        } else if (id == R.id.detailGrabBtn) {//抢单
            //判断司机的车载和未接单的重量是否一致






            new AlertDialog.Builder(this)
                    .setMessage("您确定要抢单吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.grapOrder(mId);
                        }
                    }).show();
        } else if (id == R.id.detailUploadBtn) {//上传图片
            boolean canUpload = false;
            for (Photo photo : mImagePaths) {
                if (photo.isCanDelete()) {
                    canUpload = true;
                    break;
                }
            }
            if (canUpload) {
                mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                mPresenter.uploadImages(mId, mImagePaths);
            } else {
                showToast("请先选择图片后再上传");
            }
        }
    }

    @OnClick({R.id.wxPayLayout, R.id.aliPayLayout})
    public void pay(View v) {
        if (v.getId() == R.id.wxPayLayout) {
            wxPay();
        } else if (v.getId() == R.id.aliPayLayout) {

        }
    }

    private void wxPay() {

        Map<String, String> params = new HashMap<>();
        params.put("appname", AppUtils.getAppName());
        params.put("product_name", mOrderDetail.productName);
        params.put("describe", mOrderDetail.weight + "吨" + mOrderDetail.area + "方");
        params.put("orderid", mId);
        params.put("type", "1");
        params.put("price", mOrderDetail.price + "");

        mPayDialog = LoadingProgress.showProgress(this, "正在发起支付...");
        mPresenter.pay(params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            for (LocalMedia lm : selectList) {
                if (lm.isCompressed()) {
                    Photo photo = new Photo();
                    photo.setCanDelete(true);
                    photo.setPath(lm.getCompressPath());
                    mImagePaths.add(photo);
                } else {
                    Photo photo = new Photo();
                    photo.setCanDelete(true);
                    photo.setPath(lm.getPath());
                    mImagePaths.add(photo);
                }
            }

            mPhotoView.setDatas(mImagePaths);
        }
    }

    @Override
    public void onNavSuccess(BaseEntity<OrderDetail> entity) {
        mOperationDialog.dismiss();
        OrderDetail od = entity.getData();
        if (!AMapUtil.hasGaodeApp(this)) {
            //如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(this);
            showToast("请先安装高德地图");
        } else {
            AMapUtil.openGaodeRouteMap(this, od.startLon, od.startLat, "", od.endLon, od.endLat, "", AppUtils.getAppName());
            showToast("正在打开高德地图");
        }
    }

    @Override
    public void onNavError(Throwable e) {
        mOperationDialog.dismiss();
        showToast("导航失败");
    }

    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {
        mOperationDialog.dismiss();
        showToast("取消订单成功");
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        finish();
    }

    @Override
    public void onCancelOrderError(Throwable e) {
        mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onConfirmOrderSuccess(BaseEntity<OrderDetail> entity) {
        mOperationDialog.dismiss();
        showToast("收货成功");
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());

        mPresenter.getOrderDetail(mId);

//        finish();
    }

    @Override
    public void onConfirmOrderError(Throwable e) {
        mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onLocationDriverSuccess(BaseEntity<OrderDetail> entity) {
        mOperationDialog.dismiss();
        showToast(entity.getMsg());
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        finish();
    }

    @Override
    public void onLocationDriverError(Throwable e) {
        mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onTransSuccess(BaseEntity<OrderDetail> entity) {
        mOperationDialog.dismiss();
        handleError(entity);
        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());

        Intent intent = new Intent(this, DriverOrderActivity.class);
        intent.putExtra("position", 1);
        startActivity(intent);

        finish();
    }

    @Override
    public void onTransError(Throwable e) {
        mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onGrapOrderSuccess(BaseEntity<OrderDetail> entity) {
        mOperationDialog.dismiss();

        OrderDetail od = entity.getData();
        if (!TextUtils.isEmpty(od.orderStatus) && od.orderStatus.equals("0")) {
            new AlertDialog.Builder(this)
                    .setMessage(entity.getMsg())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        } else {
            mPresenter.getOrderDetail(mId);
        }

        EventBus.getDefault().post(new DriverOrderListRrefreshEvent());
        EventBus.getDefault().post(new DriverListRrefreshEvent());

//        startActivity(new Intent(this, GrapSuccessActivity.class));
//        finish();
    }

    @Override
    public void onGrapOrderError(Throwable e) {
        mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onUploadImagesSuccess(BaseEntity<OrderDetail> entity) {
        mOperationDialog.dismiss();

        mPresenter.getOrderDetail(mId);
    }

    @Override
    public void onUploadImagesError(Throwable e) {
        mOperationDialog.dismiss();
        showNetworkError();
    }

    @Override
    protected void onDestroy() {

        if (mPayBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPayBroadcastReceiver);
        }

        super.onDestroy();
    }

    private class PayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getIntExtra("type", 0) == 0) {
                showToast("付款成功");

                mPresenter.getOrderDetail(mId);

//                finish();
            } else {
                showToast("付款失败");
            }
        }

    }
}
