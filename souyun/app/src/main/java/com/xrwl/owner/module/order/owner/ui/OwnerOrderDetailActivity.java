package com.xrwl.owner.module.order.owner.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.blankj.utilcode.util.AppUtils;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.listener.OnResultListener;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.GridSpacingItemDecoration;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.previewlibrary.ZoomMediaLoader;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.owner.Frame.auxiliary.ImageLoader;
import com.xrwl.owner.Frame.auxiliary.RetrofitManager;
import com.xrwl.owner.Frame.retrofitapi.NetService;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.DriverpositioningBeen;
import com.xrwl.owner.bean.OrderDetail;
import com.xrwl.owner.event.OwnerOrderListRrefreshEvent;
import com.xrwl.owner.event.PublishClearCacheEvent;
import com.xrwl.owner.module.home.ui.CustomDialog;
import com.xrwl.owner.module.home.ui.HongbaolistActivity;
import com.xrwl.owner.module.home.ui.OnRedPacketDialogClickListener;
import com.xrwl.owner.module.home.ui.QinLocationActivity;
import com.xrwl.owner.module.home.ui.RedPacketEntity;
import com.xrwl.owner.module.home.ui.RedPacketViewHolder;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderContract;
import com.xrwl.owner.module.order.owner.mvp.OwnerOrderDetailPresenter;
import com.xrwl.owner.module.order.owner.ui.ui.route.WalkRouteOverlay;
import com.xrwl.owner.module.publish.bean.Config;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.Photo;
import com.xrwl.owner.module.publish.ui.ChongzhiActivity;
import com.xrwl.owner.module.publish.ui.PublishSuccessActivity;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.xrwl.owner.utils.AMapUtil;
import com.xrwl.owner.utils.Constants;
import com.xrwl.owner.view.PhotoRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 最后一级订单详情
 * Created by www.longdw.com on 2018/4/4 下午1:26.
 */
public class OwnerOrderDetailActivity extends BaseActivity<OwnerOrderContract.IDetailView, OwnerOrderDetailPresenter>
        implements OwnerOrderContract.IDetailView, AMapLocationListener, AMap.OnMapClickListener,
        AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {
    private static final int GPS_REQUEST_CODE = 1;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private WalkRouteResult mWalkRouteResult;
    private static final int ROUTE_TYPE_WALK = 3;
    private String mKeyword;
    private boolean isInteger = false;
    private boolean isIntegers = false;
    private boolean isIntegerss = false;
    private final Handler mHandler = new Handler();
    private double myyue = 0;
    private ProgressDialog mLoadingDialog;
    private List<Photo> mImagePaths;
    private String mId;
    private String mDriverId;
    private ProgressDialog mPostDialog;
    private OrderDetail mOrderDetail;
    private IWXAPI mWXApi;
    private PayBroadcastReceiver mPayBroadcastReceiver;
    private float xzfjfs = 1;
    private String canshutijiao = "0";
    private String daishoulo = "1";
    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private CustomDialog mRedPacketDialog;
    private String overtotal_price = "0";
    private String daishoutype = "0";
    private String dianpinglo = "0";
    private String zhifuren = "0";//默认是发货方进行支付
    @BindView(R.id.yumoney)
    TextView myumoney;
    @BindView(R.id.yueCB)
    CheckBox myuecb;


    @BindView(R.id.detailStartPosTv)

    TextView mStartPosTv;
    @BindView(R.id.detailStartPosTvs)

    TextView mStartPosTvs;
    @BindView(R.id.detailEndPosTv)

    TextView mEndPosTv;

    @BindView(R.id.detailEndPosTvs)

    TextView mEndPosTvs;


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
    @BindView(R.id.detailBaozhuangTv)
    TextView mBaozhuangTv;
    @BindView(R.id.paytv)
    TextView ispaytv;
    @BindView(R.id.detailCancelBtn)
    Button mCancelBtn;
    @BindView(R.id.detailLocationBtn)
    Button mLocationBtn;
    @BindView(R.id.detailConfirmBtn)
    Button mConfirmBtn;
    @BindView(R.id.zhifuweikuanBtn)
    Button mzhifuweikuanBtn;
    @BindView(R.id.zhifuweikuandaishouBtn)
    Button mzhifuweikuandaishouBtn;
    @BindView(R.id.payhuokuantv)
    TextView mpayhuokuantv;
    @BindView(R.id.huozhuxianxiazhifu)
    Button mhuozhuxianxiazhifu;
    @BindView(R.id.fanhuishouye)
    Button mConfirmBtnfanhui;
    @BindView(R.id.querenzhifu)
    Button mConfirmBtnzhifu;
    @BindView(R.id.querenzhifuhuokuan)
    Button mConfirmBtnzhifuhuokuan;
    @BindView(R.id.money)
    Spinner mmoney;
    @BindView(R.id.payLayout)
    View mPayView;//支付宝和微信支付
    @BindView(R.id.detailPhotoView)
    PhotoRecyclerView mPhotoView;
    @BindView(R.id.detailOrderIdTv)
    TextView mOrderIdTv;//订单编号
    @BindView(R.id.rg_tab_group)
    RadioGroup rgTabGroup;
    @BindView(R.id.rb_owner)
    RadioButton rb_ownerqe;
    @BindView(R.id.rb_driver)
    RadioButton rb_driversy;
    @BindView(R.id.weixinCB)
    CheckBox mweixincb;
    @BindView(R.id.zhifubaoCB)
    CheckBox mzhifubaocb;
    @BindView(R.id.yinhangkaCB)
    CheckBox myinhangkacb;
    @BindView(R.id.xuanzeonlinexianxia)
    CheckBox mxuanzeonlinexianxia;
    @BindView(R.id.xuanzeonline)
    CheckBox mxuanzeonline;
    @BindView(R.id.detailConfirmfaBtn)
    Button mdetailConfirmfaBtn;//确认发货
    @BindView(R.id.yingcanglo)
    LinearLayout myingcanglo;
    @BindView(R.id.desLayout)
    LinearLayout mdesLayout;
    @BindView(R.id.tupianyincang)
    LinearLayout mtupianyincang;
    @BindView(R.id.yijieqing)
    LinearLayout myijieqing;
    @BindView(R.id.huozhuxiangqingbtn)
    LinearLayout mhuozhuxiangqingbtn;
    @BindView(R.id.addShowPhotosRv)
    RecyclerView maddShowPhotosRv;
    @BindView(R.id.homeIntroRv)
    RecyclerView mHomeIntroRv;
    @BindView(R.id.homeViewPager)
    ViewPager mViewPager;
    @BindView(R.id.homeServiceRv)
    RecyclerView mHomeServiceRv;
    @BindView(R.id.detailSendByselfBtn)
    Button mdetailSendByselfBtn;//开始自送
    @BindView(R.id.detailSendByselfquxiaoBtn)
    Button mdetailSendByselfquxiaoBtn;//取消自送
    @BindView(R.id.detailPickByselfBtn)
    Button mdetailPickByselfBtn;//开始自提
    @BindView(R.id.detailPickByselfquxiaoBtn)
    Button mdetailPickByselfquxiaoBtn;//取消自提

    /**
     * 评价系统
     */
    @BindView(R.id.dianpingbt)
    Button mdianpingbt;//在线点评
    @BindView(R.id.pingjiacontent)
    EditText mpingjiacontent;
    @BindView(R.id.pingjiaLayout)
    LinearLayout mpingjiaLayout;
    /**
     * 在完成的状态下显示在线点评
     */
    @BindView(R.id.dianpingLL)
    LinearLayout mdianpingLL;
    @BindView(R.id.detailDianPingBtn)
    Button mdetailDianPingBtn;
    @BindView(R.id.detailSelectBtn)
    Button mdetailSelectBtn;
    private ProgressDialog mXrwlwxpayDialog;

    private AMapLocationClient mLocationClient;
    private Object shippingNoteInfos;

    private String mCurrentCity;
    private AMapLocation mCurrentLocation;

    @BindView(R.id.nlMapView)
    MapView mMapView;
    private AMap mAmap;
    public Double nidayex;
    public Double nidayey;
    private OrderDetail od;
    public LatLonPoint mStartPoint;
    public LatLonPoint mEndPoint;
    private RouteSearch mRouteSearch;
    private RetrofitManager retrofitManager;


    @Override
    protected OwnerOrderDetailPresenter initPresenter() {
        return new OwnerOrderDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ownerorderdetail_activity;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        if (mAmap == null) {
            mAmap = mMapView.getMap();
        }
//        if (!PermissionUtils.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
//            new AlertDialog.Builder(this).setMessage("您需要打开定位权限才能使用此功能")
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            finish();
//                        }
//                    })
//                    .setPositiveButton("去打开", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            PermissionUtils.openAppSettings();
//                        }
//                    }).show();
//        } else {
//            initMap();
//            // initLocation();


    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 注册监听
     */
    private void registerListener() {
        mAmap.setOnMapClickListener((AMap.OnMapClickListener) OwnerOrderDetailActivity.this);
        mAmap.setOnMarkerClickListener((AMap.OnMarkerClickListener) OwnerOrderDetailActivity.this);
        mAmap.setOnInfoWindowClickListener((AMap.OnInfoWindowClickListener) OwnerOrderDetailActivity.this);
        mAmap.setInfoWindowAdapter((AMap.InfoWindowAdapter) OwnerOrderDetailActivity.this);

    }

    /**
     * 开始搜索路线规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            Toast.makeText(this, "起点未设置", Toast.LENGTH_LONG).show();
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(this, "终点未设置", Toast.LENGTH_LONG).show();
        }
        //     showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_WALK) {
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);
        }
    }

    /***
     * 根据订单类型展示不同地图(实施刷新地图可以写定时器 每秒去触发)、
     * 考虑服务器压力写成手动去刷新（去获取司机位置）
     */
    private void initwjdMap() {
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);

    }

    public void initjjdMap() {
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);

    }

    /**
     * 开始坐标
     * 给起点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     *
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getStartBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.amap_start);
    }

    /**
     * 结束坐标
     * 给终点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     *
     * @return 更换的Marker图片。
     * @since V2.1.0
     */
    protected BitmapDescriptor getEndBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.amap_car);
    }

    private void inityjdMap() {
        registerListener();

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mAmap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_pressed)));
        mAmap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);

    }

    /**
     * 2020年4月11日 10:39:46
     * other 张彬彬
     * 司机实时经纬度返回接口
     */

    private void Driverpositioning() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userid", mDriverId);
        retrofitManager = RetrofitManager.getInstance();
        retrofitManager.createReq(NetService.class)
                .Driverpositioning(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DriverpositioningBeen>() {

                    private String zbblat;
                    private String zbblon;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DriverpositioningBeen driverpositioningBeen) {

                        Double lon = Double.valueOf(driverpositioningBeen.getData().getLon());
                        Log.e("zbb", lon.toString());
                        Double lat = Double.valueOf(driverpositioningBeen.getData().getLat());
                        Log.e("zbb", lat.toString());
                        mEndPoint = new LatLonPoint(lat, lon);
                        Log.e("zbb", mEndPoint.toString());
                        getEndBitmapDescriptor();
                        getStartBitmapDescriptor();
                        initjjdMap();


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initMap() {
            MyLocationStyle myLocationStyle;
        // MyLocationStyle myLocationIcon(;//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，
        // 定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeWidth(1);//设置定位蓝点精度圈的边框宽度的方法


        mAmap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAmap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        mAmap.getUiSettings().setCompassEnabled(true);   //设置指南针用于向 App 端用户展示地图方向，默认不显示
        mAmap.getUiSettings().setScaleControlsEnabled(true);    //设置比例尺控件。位于地图右下角，可控制其显示与隐藏
        mAmap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        //在nextActivity活动中取出数据


//        LatLng latLng = new LatLng(37.861469, 112.57373);
//        doSearchQuery();
//        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
//        /**
//         * 展示坐标点
//         */
//        OrderDetail detail = new OrderDetail();
//
//        for (double[] coord : coords) {
//            MarkerOptions markerOption = new MarkerOptions();
//            latLng = new LatLng(coord[1], coord[0]);
//            markerOption.position(latLng);
//            markerOption.anchor(0.5f, 0.5f);
//            markerOption.title("标记点");
//
//            markerOption.snippet("default point");
//            markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_pressed
//            ));
//            markerOptionlst.add(markerOption);
//            LatLonPoint point = new LatLonPoint(latLng.latitude, latLng.longitude);
//            //getAddress(point);
//        }
//        mAmap.addMarkers(markerOptionlst, true);
//        final Marker marker = mAmap.addMarker(new MarkerOptions().position(latLng).title("").snippet("DefaultMarker"));
    }

//    private double[][] coords = {{111.475466, 36.01835},
//            {111.477869, 35.995368},
//            {111.483534, 36.030914},
//            {111.467055, 36.022862},
//    };

    /**
     * 对当前位置进行定位，获取当前位置的坐标信息
     */
    private void initLocation() {

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //设置定位参数
        mLocationClient.setLocationOption(getDefaultOption());
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 对输入出发地的地点进行位置查询
     */
    private void doSearchQuery() {

        PoiSearch.Query query = new PoiSearch.Query(mKeyword, "", "");
        query.setPageSize(1);//设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查第一页
//        query.setCityLimit(true);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                List<PoiItem> datas = poiResult.getPois();

                for (PoiItem pi : datas) {
                    double lat = pi.getLatLonPoint().getLatitude();
                    double lon = pi.getLatLonPoint().getLongitude();
                    mAmap.addMarker(new MarkerOptions().title(pi.getTitle()).position(new LatLng(lat, lon)));
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
        if (mCurrentLocation != null) {
            LatLonPoint llp = new LatLonPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            poiSearch.setBound(new PoiSearch.SearchBound(llp, 5000, true));//3000米以内
        }
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    /**
     * 对坐标点进行数据请求
     */
    private void dadate() {

        PoiSearch.Query query = new PoiSearch.Query(mKeyword, "", "");
        query.setPageSize(1);//设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查第一页
//        query.setCityLimit(true);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                List<PoiItem> datas = poiResult.getPois();

                for (PoiItem pi : datas) {
                    double lat = pi.getLatLonPoint().getLatitude();
                    double lon = pi.getLatLonPoint().getLongitude();
                    mAmap.addMarker(new MarkerOptions().title(pi.getTitle()).position(new LatLng(lat, lon)));
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
            }
        });
        if (mCurrentLocation != null) {
            LatLonPoint llp = new LatLonPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            poiSearch.setBound(new PoiSearch.SearchBound(llp, 3000, true));//3000米以内
        }
        poiSearch.searchPOIAsyn();// 异步搜索
    }


    @Override
    protected void initViews() {

        mPayView.setVisibility(View.GONE);//隐藏大的支付框框
        mhuozhuxianxiazhifu.setVisibility(View.GONE);
        /**隐藏自送和自提按钮事件*/
        mdetailSendByselfBtn.setVisibility(View.GONE);
        mdetailSendByselfquxiaoBtn.setVisibility(View.GONE);
        mdetailPickByselfBtn.setVisibility(View.GONE);
        mdetailPickByselfquxiaoBtn.setVisibility(View.GONE);
        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mWXApi.registerApp(Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        ZoomMediaLoader.getInstance().init(new ImageLoader());
        /**
         * 选中
         */
        mPhotoView.setOnPhotoRvControlListener(new PhotoRecyclerView.OnPhotoRvControlListener() {
            @Override
            public void onCamera() {
                PictureSelector.create(mContext)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(5) //最大选则数
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .compress(true)
                        .circleDimmedLayer(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
        mId = getIntent().getStringExtra("id");
        mDriverId = getIntent().getStringExtra("driverid");
        Log.e("=======", mDriverId.toString());
        mLoadingDialog = LoadingProgress.showProgress(this, "正在加载...");
        mPresenter.getOrderDetail(mId);
        myuecb.setChecked(true);
        mPresenter.getTotalPriceBalance();
        mPresenter.getlistpingjias(mId);
        ArrayList arrayList = new ArrayList();
        // showToast(mOrderDetail.category);

    }

    /**
     * 图片
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == GPS_REQUEST_CODE) {

        }

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


    @OnClick({R.id.detailCancelBtn, R.id.detailLocationBtn, R.id.detailConfirmBtn, R.id.wxPayLayout, R.id.aliPayLayout, R.id.fanhuishouye, R.id.querenzhifu, R.id.yueCB, R.id.weixinCB, R.id.zhifubaoCB, R.id.yinhangkaCB, R.id.detailConfirmfaBtn, R.id.xuanzeonline, R.id.xuanzeonlinexianxia, R.id.huozhuxianxiazhifu, R.id.zhifuweikuanBtn, R.id.querenzhifuhuokuan, R.id.zhifuweikuandaishouBtn, R.id.detailSendByselfBtn, R.id.detailSendByselfquxiaoBtn, R.id.detailPickByselfBtn, R.id.detailPickByselfquxiaoBtn, R.id.dianpingbt, R.id.detailDianPingBtn, R.id.detailSelectBtn})
    public void onClick(View v) {
        int id = v.getId();
        /**是否确定取消订单*/
        if (id == R.id.detailCancelBtn) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            final String straa = formatter.format(new Date());
            new AlertDialog.Builder(this).setMessage("确认取消订单吗？订单取消后，附近的司机将无法查看到您的货物信息，已支付金额将于1 到 2 个工作日内原路经退回至账户。")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.cancelOrder(mId);
                            //取消订单，这里讲直接退还到微信上面来

                            //0未支付  1支付宝  2 微信   3余额
                            if ("1".equals(mOrderDetail.PaymentMethod)) {
                            } else if ("2".equals(mOrderDetail.PaymentMethod)) {
                                Map<String, String> params = new HashMap<>();
                                float bbbbbbbbbb = Float.parseFloat(mOrderDetail.overtotal_price) * 100;
                                params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
                                params.put("WaterId", mOrderDetail.WaterId);
                                params.put("refundNo", straa);
                                params.put("money", String.valueOf(bbbbbbbbbb).substring(0, String.valueOf(bbbbbbbbbb).indexOf(".")));
                                params.put("re_money", String.valueOf(bbbbbbbbbb).substring(0, String.valueOf(bbbbbbbbbb).indexOf(".")));
                                mPresenter.wx_refund(params);
                            } else if ("3".equals(mOrderDetail.PaymentMethod)) {
                                mPresenter.yuepayrefund(mId);
                            }

                        }
                    }).show();
        }
        /**
         * 点击在线点评按钮*/
        else if (id == R.id.detailDianPingBtn) {

            if ("1".equals(dianpinglo)) {
                showToast("无法再次点评");
            } else {
                Intent mpIntent = new Intent(this, OwnerOrderDianpingActivity.class);
                mpIntent.putExtra("id", mId);
                mpIntent.putExtra("driverid", mDriverId);
                startActivity(mpIntent);
            }
        }

        /**
         * 点击查看线路*/
        else if (id == R.id.detailSelectBtn) {

        }

        /**是否确认发货*/
        else if (id == R.id.detailConfirmfaBtn) {
            new AlertDialog.Builder(this)
                    .setMessage("请确认货物已与司机交接，并确认发货")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.confirmOwnerkaishiyunshu(mId);//货主点击开始运输
                        }
                    }).show();
        }

        /**+*/
        else if (id == R.id.detailLocationBtn) {
//            mPresenter.location(mId, mDriverId);







            ShippingNoteInfo shippingNoteInfo = new ShippingNoteInfo();
            shippingNoteInfo.setShippingNoteNumber("123");
            shippingNoteInfo.setSerialNumber("123");
            shippingNoteInfo.setStartCountrySubdivisionCode("123");
            shippingNoteInfo.setEndCountrySubdivisionCode("123");

            shippingNoteInfo.setSendCount(Integer.parseInt("123"));
            shippingNoteInfo.setAlreadySendCount(Integer.parseInt("123"));
            ShippingNoteInfo[] shippingNoteInfos = new  ShippingNoteInfo[1];
            int s = shippingNoteInfos.length;
            shippingNoteInfos[0] = shippingNoteInfo;
            //停止服务。context 必须为 activity。
            LocationOpenApi.stop(this,shippingNoteInfos, new OnResultListener() {
                        @Override
                        public void onSuccess() {
                            //Toast.makeText(mContext, "成功了", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String s, String s1) {
                           // Toast.makeText(mContext, s1.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

           // Toast.makeText(mContext, "已刷新司机位置，请注意查收", Toast.LENGTH_SHORT).show();
            Driverpositioning();

        }
        /**请完成支付后，再确认收货*/
        else if (id == R.id.detailConfirmBtn) {


            //限定--必需判断是否已经全额支付了运费没有，才能操作
            if (mOrderDetail.pay.equals("1")) {
                showToast("请完成支付后，再确认收货");
                return;
            }
            if (mOrderDetail.renmibi.equals("1")) {
                showToast("请完成支付后，再确认收货");
                return;
            }
            if (mOrderDetail.total_prices == null) {
                if (mOrderDetail.sijidianji.equals("1")) {
                    new AlertDialog.Builder(this)
                            .setMessage("您确定要确认收货吗？感谢您对16飕云的信任，欢迎您下次再见")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                    mPresenter.confirmOrder(mId);
                                }
                            }).show();
                }
                if (mOrderDetail.huozhudianji.equals("1")) {
                    new AlertDialog.Builder(this)
                            .setMessage("您确定要确认收货吗？感谢您对16飕云的信任，欢迎您下次再见")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                    mPresenter.confirmOrder(mId);
                                    //  hongbao();
                                }
                            }).show();
                }


            } else  //这个是有代收货款的
            {
                if (mOrderDetail.daishoutype.equals("1")) {

                    new AlertDialog.Builder(this)
                            .setMessage("请您先支付代收货款，在支付剩余尾款")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
                } else {
                    if (mOrderDetail.pay.equals("1")) {
                        showToast("请完成支付后，再确认收货");
                        return;
                    }
                    if (mOrderDetail.sijidianji.equals("1")) {
                        new AlertDialog.Builder(this)
                                .setMessage("您确定要确认收货吗？感谢您对16飕云的信任，欢迎您下次再见")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                        mPresenter.confirmOrder(mId);
                                    }
                                }).show();
                    }
                    if (mOrderDetail.huozhudianji.equals("1")) {
                        new AlertDialog.Builder(this)
                                .setMessage("您确定要确认收货吗？感谢您对16飕云的信任，欢迎您下次再见")
                                .setNegativeButton("取消", null)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                        mPresenter.confirmOrder(mId);
                                    }
                                }).show();
                    }
                }
            }
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
        } else if (v.getId() == R.id.dianpingbt) {
            final TextView displayTva = (TextView) findViewById(R.id.display);
            final TextView displayTvaa = (TextView) findViewById(R.id.displays);
            final TextView displayTvaaa = (TextView) findViewById(R.id.displayss);
            mPresenter.dianping(mId, mDriverId, "", displayTva.getText().toString().replace("当前评分为：", ""), displayTvaa.getText().toString().replace("当前评分为：", ""), displayTvaaa.getText().toString().replace("当前评分为：", ""), "", "", mpingjiacontent.getText().toString());
        } else if (v.getId() == R.id.zhifuweikuandaishouBtn) {

            myijieqing.setVisibility(View.VISIBLE);
            myingcanglo.setVisibility(View.VISIBLE);

            mhuozhuxianxiazhifu.setVisibility(View.GONE);
            mzhifuweikuanBtn.setVisibility(View.GONE);

            if (mOrderDetail.daishoutype.equals("0")) {

                mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                mPayView.setVisibility(View.GONE);
                if (mOrderDetail.pay.equals("0")) {
                    mConfirmBtnzhifu.setVisibility(View.GONE);

                } else {
                    mConfirmBtnzhifu.setVisibility(View.VISIBLE);
                }
            } else {
                mConfirmBtnzhifu.setVisibility(View.VISIBLE);
                mPayView.setVisibility(View.VISIBLE);
                mConfirmBtnzhifuhuokuan.setVisibility(View.VISIBLE);
                mConfirmBtnzhifu.setVisibility(View.GONE);

            }

            mdesLayout.setVisibility(View.GONE);

        }
        /**支付运费尾款*/
        else if (v.getId() == R.id.zhifuweikuanBtn) {
            if (mAccount.getId().equals(mOrderDetail.userid)) {
                mConfirmBtnfanhui.setVisibility(View.VISIBLE);
                mConfirmBtnzhifu.setVisibility(View.VISIBLE);
                myijieqing.setVisibility(View.VISIBLE);
                myingcanglo.setVisibility(View.VISIBLE);
                mPayView.setVisibility(View.VISIBLE);
                mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                mhuozhuxianxiazhifu.setVisibility(View.GONE);
                mzhifuweikuanBtn.setVisibility(View.GONE);
                mzhifuweikuandaishouBtn.setVisibility(View.GONE);
            } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                if (mOrderDetail.daishoutype.equals("0")) {
                    mzhifuweikuanBtn.setText("支付运费");
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);

                    if (mOrderDetail.pay.equals("0")) {

                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mPayView.setVisibility(View.GONE);
                    } else {
                        mConfirmBtnzhifu.setVisibility(View.VISIBLE);
                        mPayView.setVisibility(View.VISIBLE);
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        mConfirmBtnfanhui.setVisibility(View.VISIBLE);
                    }
                } else {
                    mConfirmBtnzhifu.setVisibility(View.VISIBLE);
                    mPayView.setVisibility(View.VISIBLE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.VISIBLE);
                    mConfirmBtnfanhui.setVisibility(View.VISIBLE);
                }


                myijieqing.setVisibility(View.VISIBLE);
                myingcanglo.setVisibility(View.VISIBLE);


                mhuozhuxianxiazhifu.setVisibility(View.GONE);

                mzhifuweikuandaishouBtn.setVisibility(View.GONE);
            }
            mdesLayout.setVisibility(View.GONE);
        } else if (v.getId() == R.id.huozhuxianxiazhifu) {
            new AlertDialog.Builder(this).setMessage("是否确定要线下支付吗？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("提醒司机线下支付", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //这个是请求接口
                            mPresenter.confirmtixingOrder(mId);
                        }
                    }).show();
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
        } else if (v.getId() == R.id.querenzhifuhuokuan) {
            daishoutype = "4";
            //region 微信支付
            if (mweixincb.isChecked()) {
                mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                Map<String, String> params = new HashMap<>();
                params.put("appname", AppUtils.getAppName());
                params.put("product_name", mOrderDetail.productName);
                params.put("describe", mOrderDetail.weight + "吨" + mOrderDetail.area + "方");
                params.put("orderid", mId);
                params.put("type", "1");
                params.put("price", mOrderDetail.total_prices + "");
                params.put("daishou", "2");
                // mPresenter.wxonlinedaishouPay(params);
                mPresenter.wxPay(params);
                return;

            }

            //endregion
        } else if (v.getId() == R.id.querenzhifu) {
            rgTabGroup.setVisibility(View.GONE);
            //region 微信支付
            if (mweixincb.isChecked()) {
                if (mOrderDetail.type.equals("1") || mOrderDetail.type.equals("2") || mOrderDetail.type.equals("3")) {
                    new AlertDialog.Builder(this).setMessage("请您支付尾款：支付尾款后,有利于司机更好的为您服务")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                    Map<String, String> params = new HashMap<>();
                                    params.put("appname", AppUtils.getAppName());
                                    params.put("product_name", mOrderDetail.productName);
                                    params.put("describe", mOrderDetail.weight + "吨" + mOrderDetail.area + "方");
                                    params.put("orderid", mId);
                                    params.put("order_id", mId);
                                    params.put("pay_type", "APP");
                                    params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
                                    params.put("type", "1");
                                    params.put("daishou", "0");


                                    //判断是发货方支付还是收货方支付，如果是发货支付那就先支付10%保证金最大不超过300元，如果是收货方支付
                                    //1.判断是发货发付钱
                                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                                        zhifuren = "0";
                                        if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0")) {
                                            rgTabGroup.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                            if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                                                params.put("price", xzfjfs + "");
                                                params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.Final_payment) * 100));
                                            } else {
                                                params.put("price", xzfjfs + "");
                                                params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.Final_payment) * 100));
                                            }


                                        } else if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1")) {

                                            rb_ownerqe.setText("");
                                            rb_driversy.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                            if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                                                params.put("price", mOrderDetail.price + "");
                                                params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.Final_payment) * 100));
                                            } else {
                                                params.put("price", xzfjfs + "");
                                                params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.Final_payment) * 100));
                                            }

                                        } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0")) {

                                            rgTabGroup.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);

                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.Final_payment) * 100));
                                        } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1")) {
                                            switch (rgTabGroup.getCheckedRadioButtonId()) {
                                                case R.id.rb_owner:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                    canshutijiao = "1";
                                                    break;
                                                case R.id.rb_driver:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                    canshutijiao = "0";
                                                    break;
                                            }
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.Final_payment) * 100));
                                        } else {
                                            switch (rgTabGroup.getCheckedRadioButtonId()) {
                                                case R.id.rb_owner:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                    canshutijiao = "1";
                                                    break;
                                                case R.id.rb_driver:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                    canshutijiao = "0";
                                                    break;
                                            }
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.Final_payment) * 100));

                                        }
                                        // mPresenter.wxPay(params);
                                        //1先发起微信支付，2在进行微信退款
                                        mXrwlwxpayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
                                        mPresenter.xrwlwxpay(params);
                                    }
                                    //2.判断是收货方付钱
                                    else {


                                        zhifuren = "1";

                                        if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0")) {
                                            rgTabGroup.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                            if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                                                params.put("price", xzfjfs + "");
                                                params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.freight) * 100));
                                            } else {
                                                params.put("price", xzfjfs + "");
                                                params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.freight) * 100));
                                            }


                                        } else if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1")) {

                                            rb_ownerqe.setText("");
                                            rb_driversy.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                            if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                                                params.put("price", mOrderDetail.price + "");
                                                params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.freight) * 100));
                                            } else {
                                                params.put("price", xzfjfs + "");
                                                params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.freight) * 100));
                                            }

                                        } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0")) {

                                            rgTabGroup.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.freight);

                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.freight) * 100));
                                        } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1")) {
                                            switch (rgTabGroup.getCheckedRadioButtonId()) {
                                                case R.id.rb_owner:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                    canshutijiao = "1";
                                                    break;
                                                case R.id.rb_driver:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                    canshutijiao = "0";
                                                    break;
                                            }
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.freight) * 100));
                                        } else {
                                            switch (rgTabGroup.getCheckedRadioButtonId()) {
                                                case R.id.rb_owner:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                    canshutijiao = "1";
                                                    break;
                                                case R.id.rb_driver:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                    canshutijiao = "0";
                                                    break;
                                            }
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", String.valueOf(Float.parseFloat(mOrderDetail.freight) * 100));
                                        }
                                        // mPresenter.wxPay(params);
                                        //1先发起微信支付，2在进行微信退款
                                        mXrwlwxpayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
                                        mPresenter.xrwlwxpay(params);
                                        //这里讲直接退还到微信上面来  这个是通过后台获取到数值后，有总值减去累加后的值进行计算
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                                        final String straaa = formatter.format(new Date());
                                        Map<String, String> paramsa = new HashMap<>();

                                        float bbbbbbbbbb = -(Float.parseFloat(mOrderDetail.Final_payment) * 100);

                                        paramsa.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
                                        paramsa.put("WaterId", mOrderDetail.WaterId);
                                        paramsa.put("refundNo", straaa);
                                        paramsa.put("money", String.valueOf(bbbbbbbbbb).substring(0, String.valueOf(bbbbbbbbbb).indexOf(".")));
                                        paramsa.put("re_money", String.valueOf(bbbbbbbbbb).substring(0, String.valueOf(bbbbbbbbbb).indexOf(".")));
                                        mPresenter.wx_refund(paramsa);


                                    }

                                }
                            }).show();
                } else {


                    new AlertDialog.Builder(this).setMessage("全额支付：支付全款后形成发货订单,有利于司机更好的为您接单\n保  证  金：预先支付10%的运费后,形成发货订单,确认收货后支付剩余尾款")

                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                    Map<String, String> params = new HashMap<>();
                                    params.put("appname", AppUtils.getAppName());
                                    params.put("product_name", mOrderDetail.productName);
                                    params.put("describe", mOrderDetail.weight + "吨" + mOrderDetail.area + "方");
                                    params.put("orderid", mId);
                                    params.put("type", "1");
                                    params.put("daishou", "3");
                                    params.put("order_id", mId);
                                    params.put("pay_type", "APP");

                                    params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");


                                    //判断是发货发还是收货方
                                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                                        //这个是发货
                                        zhifuren = "0";
                                        if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0")) {
                                            rgTabGroup.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);

                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", xzfjfs * 100 + "");
                                        } else if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1")) {

                                            rb_ownerqe.setText("");
                                            rb_driversy.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                            if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                                                params.put("price", mOrderDetail.price + "");
                                                params.put("total_fee", Float.parseFloat(mOrderDetail.Final_payment) * 100 + "");
                                            } else {
                                                params.put("price", xzfjfs + "");
                                                params.put("total_fee", xzfjfs * 100 + "");
                                            }


                                        } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0")) {
                                            rgTabGroup.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", xzfjfs * 100 + "");
                                        } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1")) {
                                            switch (rgTabGroup.getCheckedRadioButtonId()) {
                                                case R.id.rb_owner:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                    canshutijiao = "1";
                                                    break;
                                                case R.id.rb_driver:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                    canshutijiao = "0";
                                                    break;
                                            }
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", xzfjfs * 100 + "");

                                        } else {
                                            switch (rgTabGroup.getCheckedRadioButtonId()) {
                                                case R.id.rb_owner:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                    canshutijiao = "1";
                                                    break;
                                                case R.id.rb_driver:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                    canshutijiao = "0";
                                                    break;
                                            }
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", xzfjfs * 100 + "");

                                        }
                                    } else {
                                        //这个是收货方--------收货方付全款后，进行发货发原来支付金的退回
                                        zhifuren = "1";
                                        if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0")) {
                                            rgTabGroup.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.freight);

                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", xzfjfs * 100 + "");
                                        } else if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1")) {

                                            rb_ownerqe.setText("");
                                            rb_driversy.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                            if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                                                params.put("price", mOrderDetail.price + "");
                                                params.put("total_fee", Float.parseFloat(mOrderDetail.freight) * 100 + "");
                                            } else {
                                                params.put("price", xzfjfs + "");
                                                params.put("total_fee", xzfjfs * 100 + "");
                                            }


                                        } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0")) {
                                            rgTabGroup.setVisibility(View.GONE);
                                            xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", xzfjfs * 100 + "");
                                        } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1")) {
                                            switch (rgTabGroup.getCheckedRadioButtonId()) {
                                                case R.id.rb_owner:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                    canshutijiao = "1";
                                                    break;
                                                case R.id.rb_driver:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                    canshutijiao = "0";
                                                    break;
                                            }
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", xzfjfs * 100 + "");

                                        } else {
                                            switch (rgTabGroup.getCheckedRadioButtonId()) {
                                                case R.id.rb_owner:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                    canshutijiao = "1";
                                                    break;
                                                case R.id.rb_driver:
                                                    xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                    canshutijiao = "0";
                                                    break;
                                            }
                                            params.put("price", xzfjfs + "");
                                            params.put("total_fee", xzfjfs * 100 + "");

                                        }
                                    }
                                    //  params.put("price", mOrderDetail.price + "");
                                    mXrwlwxpayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
                                    mPresenter.xrwlwxpay(params);
                                }
                            }).show();
                }
                rgTabGroup.setVisibility(View.GONE);
            }
            //endregion
            else if (myuecb.isChecked()) {
                Map<String, String> params = new HashMap<>();
                params.put("Payment_method", "3");
                params.put("orderid", mId);
                params.put("balance", "0");
                //首先获取余额是多少钱，才能进行支付  这个支付方式和
                switch (rgTabGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_owner:
                        if (mOrderDetail.userid.equals(mAccount.getId())) {
                            xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                            canshutijiao = "1";
                            params.put("requestrenminbi", mOrderDetail.freight);
                            params.put("zhifuren", "0");
                            break;
                        } else {
                            xzfjfs = Float.parseFloat(mOrderDetail.freight);
                            params.put("requestrenminbi", mOrderDetail.freight);
                            canshutijiao = "1";
                            params.put("zhifuren", "1");
                            break;
                        }

                    case R.id.rb_driver:
                        if (mOrderDetail.userid.equals(mAccount.getId())) {
                            xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                            canshutijiao = "0";
                            params.put("requestrenminbi", mOrderDetail.freight);
                            params.put("zhifuren", "0");
                            break;
                        } else {
                            xzfjfs = Float.parseFloat(mOrderDetail.freight);
                            params.put("requestrenminbi", mOrderDetail.freight);
                            canshutijiao = "0";
                            params.put("zhifuren", "1");
                            break;
                        }

                }
                if (myyue >= xzfjfs) {

                    mPresenter.yuepay(String.valueOf(xzfjfs), "6666666666666666", mId, canshutijiao);

                    mPresenter.results(params);//这个是更改数据库余额支付的变化情况
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

            //region 支付宝支付
            else if (mzhifubaocb.isChecked()) {
                new AlertDialog.Builder(this).setMessage("全额支付：支付全款后形成发货订单,有利于司机更好的为您接单\n保  证  金：预先支付10%的运费后,形成发货订单,确认收货后支付剩余尾款")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(mContext, PayDemosActivity.class);
                                intent.putExtra("etGoodsName", mOrderDetail.productName);
                                String dun = "";
                                String fang = "";
                                String jian = "";
                                if (!TextUtils.isEmpty(mOrderDetail.weight)) {
                                    dun = mOrderDetail.weight + "吨  ";
                                } else {
                                    dun = "";
                                }
                                if (!TextUtils.isEmpty(mOrderDetail.area)) {
                                    fang = mOrderDetail.area + "方  ";
                                } else {
                                    fang = "";
                                }
                                if (!TextUtils.isEmpty(mOrderDetail.num)) {
                                    jian = mOrderDetail.num + "件  ";
                                } else {
                                    jian = "";
                                }
                                intent.putExtra("describe", dun + fang + jian);
                                intent.putExtra("Payment_method", "2");
                                intent.putExtra("zfb", "0");


                                //判断是发货发还是收货方进行

                                if (mOrderDetail.userid.equals(mAccount.getId())) {
                                    intent.putExtra("zhifuren", "0");
                                    //发货发进行支付
                                    if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0")) {
                                        rgTabGroup.setVisibility(View.GONE);
                                        xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    } else if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1")) {
                                        rb_ownerqe.setText("");
                                        rb_driversy.setVisibility(View.GONE);
                                        xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0")) {
                                        rgTabGroup.setVisibility(View.GONE);
                                        xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1")) {
                                        switch (rgTabGroup.getCheckedRadioButtonId()) {
                                            case R.id.rb_owner:
                                                xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                canshutijiao = "1";
                                                break;
                                            case R.id.rb_driver:
                                                xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                canshutijiao = "0";
                                                break;
                                        }
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    } else {
                                        switch (rgTabGroup.getCheckedRadioButtonId()) {
                                            case R.id.rb_owner:
                                                xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                canshutijiao = "1";
                                                break;
                                            case R.id.rb_driver:
                                                xzfjfs = Float.parseFloat(mOrderDetail.Final_payment);
                                                canshutijiao = "0";
                                                break;
                                        }
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    }
                                } else {
                                    intent.putExtra("zhifuren", "1");
                                    //收货方进行支付
                                    if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0")) {
                                        rgTabGroup.setVisibility(View.GONE);
                                        xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    } else if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1")) {
                                        rb_ownerqe.setText("");
                                        rb_driversy.setVisibility(View.GONE);
                                        xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");

                                    } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0")) {
                                        rgTabGroup.setVisibility(View.GONE);
                                        xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1")) {
                                        switch (rgTabGroup.getCheckedRadioButtonId()) {
                                            case R.id.rb_owner:
                                                xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                canshutijiao = "1";
                                                break;
                                            case R.id.rb_driver:
                                                xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                canshutijiao = "0";
                                                break;
                                        }
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    } else {
                                        switch (rgTabGroup.getCheckedRadioButtonId()) {
                                            case R.id.rb_owner:
                                                xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                canshutijiao = "1";
                                                break;
                                            case R.id.rb_driver:
                                                xzfjfs = Float.parseFloat(mOrderDetail.freight);
                                                canshutijiao = "0";
                                                break;
                                        }
                                        intent.putExtra("price", xzfjfs + "");
                                        intent.putExtra("requestrenminbi", xzfjfs + "");
                                    }
                                }
                                intent.putExtra("orderid", mId);
                                intent.putExtra("id", mId);
                                intent.putExtra("daishou", "1");
                                intent.putExtra("pay_type", canshutijiao + "");
                                intent.putExtra("price", xzfjfs + "");

                                startActivity(intent);
                            }
                        }).show();


            }
            //endregion
            //region 银行卡支付
            else if (myinhangkacb.isChecked()) {
                showToast("暂未开放，敬请期待");
                return;
            }
            //endregion


        } else if (id == R.id.yueCB) {
            if (myuecb.isChecked()) {
                mzhifubaocb.setChecked(false);
                myinhangkacb.setChecked(false);
                mweixincb.setChecked(false);
            } else {
                myuecb.setChecked(false);
            }
        } else if (id == R.id.weixinCB) {
            //region 微信被选中

            if (mweixincb.isChecked()) {
                mzhifubaocb.setChecked(false);
                myinhangkacb.setChecked(false);
                myuecb.setChecked(false);
            } else {
                mweixincb.setChecked(false);
            }
            //endregion
        } else if (id == R.id.zhifubaoCB) {
            //region 支付宝被选中

            if (mzhifubaocb.isChecked()) {
                mweixincb.setChecked(false);
                myinhangkacb.setChecked(false);
                myuecb.setChecked(false);
            } else {
                mzhifubaocb.setChecked(false);
            }
            //endregion
        } else if (id == R.id.yinhangkaCB) {
            //region 银行卡被选中

            if (myinhangkacb.isChecked()) {
                mweixincb.setChecked(false);
                mzhifubaocb.setChecked(false);
                myuecb.setChecked(false);
            } else {
                myinhangkacb.setChecked(false);
            }
            //endregion
        } else if (id == R.id.detailSendByselfBtn)//开始自送
        {
            new AlertDialog.Builder(this)
                    .setMessage("您确定要发起线路导航吗？导航方便您能准确定位发货的位置在哪里，大大提升您找到货物位置的效率")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //mOperationDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                            mPresenter.nav(mId);
                        }
                    }).show();
        } else if (id == R.id.detailSendByselfquxiaoBtn)//取消自送
        {

        } else if (id == R.id.detailPickByselfBtn)//开始自提
        {

        } else if (id == R.id.detailPickByselfquxiaoBtn)//取消自提
        {

        }
//        else if(id==R.id.pingjiaone)
//        {
//            mpingjiaone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mpingjiaone.setImageResource(R.drawable.star_red);
//                }
//            });
//        }
//        else if(id==R.id.pingjiatwo)
//        {
//            mpingjiatwo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mpingjiatwo.setImageResource(R.drawable.star_red);
//                }
//            });
//        }
//        else if (id == R.id.wxPayLayout) {
//            //region 暂时不考虑
//
//            mPostDialog = LoadingProgress.showProgress(this, "正在提交...");
//            Map<String, String> params = new HashMap<>();
//            params.put("appname", AppUtils.getAppName());
//            params.put("product_name", mOrderDetail.productName);
//            params.put("describe", mOrderDetail.weight + "吨" + mOrderDetail.area + "方");
//            params.put("orderid", mId);
//            params.put("type", "1");
//            params.put("daishou", "0");
//            if(mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0"))
//            {
//                rgTabGroup.setVisibility(View.GONE);
//            }
//            else  if(mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1"))
//            {
//                rb_ownerqe.setText("");
//                rb_driversy.setVisibility(View.GONE);
//                xzfjfs = Float.parseFloat(mOrderDetail.price)*90/100;
//                params.put("price", xzfjfs + "");
//            }
//            else  if(mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0"))
//            {
//                rgTabGroup.setVisibility(View.GONE);
//            }
//            else if(mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1"))
//            {
//                switch (rgTabGroup.getCheckedRadioButtonId()) {
//                    case R.id.rb_owner:
//                        xzfjfs = Float.parseFloat(mOrderDetail.price);
//                        canshutijiao="1";
//                        break;
//                    case R.id.rb_driver:
//                        xzfjfs = Float.parseFloat(mOrderDetail.price)*10/100;
//                        canshutijiao="0";
//                        break;
//                }
//                params.put("price", xzfjfs + "");
//            }
//            else
//            {
//                switch (rgTabGroup.getCheckedRadioButtonId()) {
//                    case R.id.rb_owner:
//                        xzfjfs = Float.parseFloat(mOrderDetail.price);
//                        canshutijiao="1";
//                        break;
//                    case R.id.rb_driver:
//                        xzfjfs = Float.parseFloat(mOrderDetail.price)*10/100;
//                        canshutijiao="0";
//                        break;
//                }
//                params.put("price", xzfjfs + "");
//            }
//            //  params.put("price", mOrderDetail.price + "");
//
//            mPresenter.wxPay(params);
//            //endregion
//        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRefreshSuccess(BaseEntity<OrderDetail> entity) {


        mOrderDetail = entity.getData();
        mStartPoint = new LatLonPoint(Double.valueOf(mOrderDetail.startLat), Double.valueOf(mOrderDetail.startLon));
        //终点
        mEndPoint = new LatLonPoint(Double.valueOf(mOrderDetail.endLat), Double.valueOf(mOrderDetail.endLon));

        if ("7".equals(mOrderDetail.category)) {
            mAreaTv.setVisibility(View.INVISIBLE);
            mWeightTv.setVisibility(View.INVISIBLE);

            mNumTv.setVisibility(View.INVISIBLE);
        } else {
            mAreaTv.setVisibility(View.VISIBLE);
            mWeightTv.setVisibility(View.VISIBLE);

            mNumTv.setVisibility(View.VISIBLE);
        }


        if ("3".equals(mOrderDetail.type)) {
            if ("0".equals(mOrderDetail.renmibi)) {
                if (!mOrderDetail.hongbao.equals("1")) {
                    if ("1".equals(mOrderDetail.isquerenshouhuo)) {
                        hongbao();
                    }

                }
            }
            /**这个主要是判断点评的按钮是否显示*/
            mdianpingLL.setVisibility(View.VISIBLE);


            mpingjiaLayout.setVisibility(View.VISIBLE);
            final TextView displayTv = (TextView) findViewById(R.id.display);
            final StarBar starBar = (StarBar) findViewById(R.id.starBar);
            starBar.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
                @Override
                public void onStarChange(float mark) {
                    displayTv.setText("" + mark);

                }
            });
            findViewById(R.id.she3_6).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    starBar.setStarMark(3.6f);
                }
            });
            findViewById(R.id.getStar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayTv.setText("：" + starBar.getStarMark());
                }
            });
            findViewById(R.id.integer).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInteger = !isInteger;
                    Button button = (Button) v;

                    button.setText("整数评分: " + isInteger);
                }
            });
            final TextView displaysTv = (TextView) findViewById(R.id.displays);
            final StarBar starBars = (StarBar) findViewById(R.id.starBars);
            starBars.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
                @Override
                public void onStarChange(float marks) {
                    displaysTv.setText("" + marks);
                }
            });
            findViewById(R.id.she3_6s).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    starBars.setStarMark(3.6f);
                }
            });
            findViewById(R.id.getStars).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displaysTv.setText("" + starBars.getStarMark());
                }
            });
            findViewById(R.id.integers).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isIntegers = !isIntegers;
                    Button button = (Button) v;
                    starBars.setIntegerMark(isIntegers);
                    button.setText("整数评分: " + isIntegers);
                }
            });

            final TextView displayssTv = (TextView) findViewById(R.id.displayss);
            final StarBar starBarss = (StarBar) findViewById(R.id.starBarss);
            starBarss.setOnStarChangeListener(new StarBar.OnStarChangeListener() {
                @Override
                public void onStarChange(float markss) {
                    displayssTv.setText("" + markss);
                }
            });
            findViewById(R.id.she3_6ss).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    starBarss.setStarMark(3.6f);
                }
            });
            findViewById(R.id.getStarss).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayssTv.setText("" + starBarss.getStarMark());
                }
            });
            findViewById(R.id.integerss).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isIntegerss = !isIntegerss;
                    Button button = (Button) v;
                    starBarss.setIntegerMark(isIntegerss);
                    button.setText("整数评分: " + isIntegerss);
                }
            });

        } else {
            mpingjiaLayout.setVisibility(View.GONE);
            mpingjiaLayout.setVisibility(View.GONE);
        }


        if (mOrderDetail.total_prices == null) {
            mpayhuokuantv.setVisibility(View.GONE);
        } else {
            if (mOrderDetail.daishoutype.equals("0")) {
                mpayhuokuantv.setText("货款：全付");
                mpayhuokuantv.setTextColor(Color.BLUE);
            } else {
                mpayhuokuantv.setText("货款：未付");

            }
        }


        mStartPosTv.setText(mOrderDetail.startPos);
        mStartPosTvs.setText(mOrderDetail.start_desc);
        mEndPosTv.setText(mOrderDetail.endPos);
        mEndPosTvs.setText(mOrderDetail.end_desc);


        mProductNameTv.setText("货名：" + mOrderDetail.productName);

        if (TextUtils.isEmpty(mOrderDetail.truck)) {
            mTruckTv.setText("车型：无车型需求");
        } else {
            mTruckTv.setText("车型：" + mOrderDetail.truck);
        }


        if (mOrderDetail.type.equals("1") || mOrderDetail.type.equals("2")) {
            mAreaTv.setText("体积：" + "整车");
            mWeightTv.setText("重量：" + "整车");

            mNumTv.setText("数量：" + "整车");
        } else {
            if (!TextUtils.isEmpty(mOrderDetail.area)) {
                mAreaTv.setText("体积：" + mOrderDetail.area + "方");
            } else {
                mAreaTv.setText("体积：" + "0方");
            }


            String dunxianshi = "";
            if (!TextUtils.isEmpty(mOrderDetail.weight)) {
                dunxianshi = "重量：" + mOrderDetail.weight + "吨";
                // mWeightTv.setText("重量：" + mOrderDetail.weight + "吨");
            } else {
                dunxianshi = "重量：" + "0吨";
                // mWeightTv.setText("重量：" + "0吨");
            }
            mWeightTv.setText(dunxianshi);

            if (!TextUtils.isEmpty(mOrderDetail.num)) {
                mNumTv.setText("数量：" + mOrderDetail.num + "件");
            } else {
                mNumTv.setText("数量：" + "0件");
            }
        }


        mPriceTv.setText("价格：" + mOrderDetail.overtotal_price + "元");


        mKiloTv.setText("公里：" + mOrderDetail.kilo + "公里");

        mBaozhuangTv.setText("包装：" + mOrderDetail.packingtype);


        mOrderIdTv.setText("订单编号：" + mOrderDetail.ddbh);

        mImagePaths = mOrderDetail.getPics();

        mPhotoView.setDatas(mOrderDetail.getPics());
        if (mOrderDetail.type.equals("0")) {
            initwjdMap();

            mCancelBtn.setVisibility(View.VISIBLE);
            mLocationBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
            mdetailConfirmfaBtn.setVisibility(View.GONE);
            if (mOrderDetail.total_prices == null) {
                if (mAccount.getId().equals(mOrderDetail.userid)) {
                    if (mOrderDetail.pay.equals("0")) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                    }

                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                    if ("0".equals(mOrderDetail.pay)) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);


                    } else {

                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                    }

                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setText("支付运费");
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);

                } else {
                    mzhifuweikuanBtn.setVisibility(View.GONE);
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                }
            } else {
                if (mOrderDetail.daishoutype.equals("1")) {

                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.VISIBLE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    }

                } else {
                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                        mzhifuweikuanBtn.setText("支付运费");
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    }
                }
            }
        } else if (mOrderDetail.type.equals("1")) {
            /**
             * 已接单展示内容
             */

            Driverpositioning();


            /**判断是否选择了自送这个事件*/
            if ("1".equals(mOrderDetail.is_sendbyself)) {
                mdetailSendByselfBtn.setVisibility(View.VISIBLE);
                mdetailSendByselfquxiaoBtn.setVisibility(View.VISIBLE);
            }
            mCancelBtn.setVisibility(View.GONE);
            mLocationBtn.setVisibility(View.VISIBLE);
            mConfirmBtn.setVisibility(View.GONE);
            mdetailConfirmfaBtn.setVisibility(View.VISIBLE);
            if (mOrderDetail.total_prices == null) {
                if (mAccount.getId().equals(mOrderDetail.userid)) {
                    if (mOrderDetail.pay.equals("0")) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                    }


                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                    if (mOrderDetail.pay.equals("0")) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                    }

                    mzhifuweikuanBtn.setText("支付运费");
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                } else {
                    mzhifuweikuanBtn.setVisibility(View.GONE);
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                }
            } else {
                if (mOrderDetail.daishoutype.equals("1")) {
                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }
                        // mzhifuweikuanBtn .setVisibility(View.VISIBLE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {

                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.VISIBLE);
                        mzhifuweikuandaishouBtn.setText("支付货款");
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    }

                } else {
                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }

                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                        mzhifuweikuanBtn.setText("支付运费");
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    }
                }
            }

            if (mOrderDetail.huozhudianji.equals("2")) {
                if (mOrderDetail.category.equals("6")) {
                    new AlertDialog.Builder(this)
                            .setMessage("请发货方在电脑端操作，否则无法打印货运单")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
//                                mPresenter.confirmOrder(mId);//货主点击开始到达
                                }
                            }).show();
                } else {
                    //if(mOrderDetail.sijidianji.equals("1"))
                    new AlertDialog.Builder(this)
                            .setMessage("司机请求开始运输操作，请确认")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
                                    mPresenter.confirmOwnerkaishiyunshu(mId);//货主点击开始运输
                                }
                            }).show();

                }

            }
        } else if (mOrderDetail.type.equals("2")) {

            mCancelBtn.setVisibility(View.GONE);
            mLocationBtn.setVisibility(View.VISIBLE);
            mConfirmBtn.setVisibility(View.VISIBLE);
            mdetailConfirmfaBtn.setVisibility(View.GONE);
            if (mOrderDetail.total_prices == null) {
                if (mAccount.getId().equals(mOrderDetail.userid)) {
                    if (mOrderDetail.pay.equals("0")) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                    }


                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                    if (mOrderDetail.pay.equals("0")) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                    }

                    mzhifuweikuanBtn.setText("支付运费");
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                } else {
                    mzhifuweikuanBtn.setVisibility(View.GONE);
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                }
            } else {
                if (mOrderDetail.daishoutype.equals("1")) {
                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }
                        // mzhifuweikuanBtn .setVisibility(View.VISIBLE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {

                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.VISIBLE);
                        mzhifuweikuandaishouBtn.setText("支付货款");
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    }

                } else {
                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }

                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                        mzhifuweikuanBtn.setText("支付运费");
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    }
                }
            }

            if (mOrderDetail.category.equals("6")) {
                if (mOrderDetail.sijidianji.equals("1")) {
                    new AlertDialog.Builder(this)
                            .setMessage("请发货方在电脑端操作，否则无法打印货运单")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
//                                mPresenter.confirmOrder(mId);//货主点击开始到达
                                }
                            }).show();
                }
            } else {
                if (mOrderDetail.sijidianji.equals("1")) {


//                    new AlertDialog.Builder(this)
//                            .setMessage("司机请求到达目的地，请确认收货")
//                            .setNegativeButton("确定", null).show();


                    new AlertDialog.Builder(this)
                            .setMessage("司机请求到达目的地，请确认收货")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //mPostDialog = LoadingProgress.showProgress(mContext, "正在提交...");
//                                    mPresenter.confirmOrder(mId);//货主点击开始到达
                                    // mPresenter.confirmOrder(mId);
                                }
                            }).show();
                }

            }
        } else if (mOrderDetail.type.equals("3")) {
            mCancelBtn.setVisibility(View.GONE);
            mLocationBtn.setVisibility(View.GONE);
            mConfirmBtn.setVisibility(View.GONE);
            /**判断是否选择了自提这个事件*/
            if ("1".equals(mOrderDetail.is_pickbyself)) {
                mdetailPickByselfBtn.setVisibility(View.VISIBLE);
                mdetailPickByselfquxiaoBtn.setVisibility(View.VISIBLE);
            }
            mdetailConfirmfaBtn.setVisibility(View.GONE);
            if (mOrderDetail.total_prices == null) {
                if (mAccount.getId().equals(mOrderDetail.userid)) {
                    if (mOrderDetail.pay.equals("0")) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                    }
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                    if (mOrderDetail.pay.equals("0")) {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                    }

                    mzhifuweikuanBtn.setText("支付运费");
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                } else {
                    mzhifuweikuanBtn.setVisibility(View.GONE);
                    myijieqing.setVisibility(View.GONE);
                    mConfirmBtnfanhui.setVisibility(View.GONE);
                    mConfirmBtnzhifu.setVisibility(View.GONE);
                    mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                    mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                }
            } else {
                if (mOrderDetail.daishoutype.equals("1")) {
                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }
                        // mzhifuweikuanBtn .setVisibility(View.VISIBLE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                        mzhifuweikuandaishouBtn.setText("支付运费");
                        mzhifuweikuandaishouBtn.setVisibility(View.VISIBLE);

                        mPayView.setVisibility(View.GONE);
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);

                    } else {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    }

                } else {
                    if (mAccount.getId().equals(mOrderDetail.userid)) {
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }

                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else if (mAccount.getId().equals(mOrderDetail.shouhuorenid)) {
                        if (mOrderDetail.pay.equals("0")) {
                            mzhifuweikuanBtn.setVisibility(View.GONE);
                        } else {
                            mzhifuweikuanBtn.setVisibility(View.VISIBLE);
                        }

                        mzhifuweikuanBtn.setText("支付运费");
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    } else {
                        mzhifuweikuanBtn.setVisibility(View.GONE);
                        myijieqing.setVisibility(View.GONE);
                        mConfirmBtnfanhui.setVisibility(View.GONE);
                        mConfirmBtnzhifu.setVisibility(View.GONE);
                        mConfirmBtnzhifuhuokuan.setVisibility(View.GONE);
                        mzhifuweikuandaishouBtn.setVisibility(View.GONE);
                    }
                }
            }
        }

        if (!mOrderDetail.showPay()) {
            mPayView.setVisibility(View.GONE);
        }
        if (mOrderDetail.type.equals("3")) {
            if (mOrderDetail.pay.equals("0")) {
                ispaytv.setTextColor(Color.BLUE);
                ispaytv.setText("支付：" + "已结清");
                mzhifuweikuanBtn.setVisibility(View.GONE);
                mzhifuweikuandaishouBtn.setVisibility(View.GONE);

            }
            if (mOrderDetail.tixing.equals("2")) {
                ispaytv.setTextColor(Color.BLUE);
                ispaytv.setText("支付：" + "已结清");
            }


            if (mOrderDetail.tixing.equals("2")) {
                ispaytv.setTextColor(Color.BLUE);
                ispaytv.setText("支付：" + "已结清");

            } else {

                // mzhifuweikuanBtn.setVisibility(View.VISIBLE);


                if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0")) {
                    ispaytv.setText("支付：" + "已全额支付");
                    rgTabGroup.setVisibility(View.GONE);
                } else if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1")) {

                    ispaytv.setText("支付：" + "已付保证金");
                    //  rb_ownerqe.setVisibility(View.GONE);
                    rb_ownerqe.setText("");
                    rb_driversy.setVisibility(View.GONE);
                } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0")) {
                    ispaytv.setText("支付：" + "运费全付");
                    rgTabGroup.setVisibility(View.GONE);
                } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1")) {
                    ispaytv.setText("支付：" + "运费未付");
                }
            }

            mPayView.setVisibility(View.INVISIBLE);
        } else {
            if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("0")) {
                ispaytv.setText("支付：" + "已全额支付");
                rgTabGroup.setVisibility(View.GONE);
            } else if (mOrderDetail.isbzj_type.equals("0") && mOrderDetail.pay.equals("1")) {

                ispaytv.setText("支付：" + "已付保证金");
                rb_ownerqe.setText("");
                rb_driversy.setVisibility(View.GONE);
            } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("0")) {
                ispaytv.setText("支付：" + "运费全付");
                rgTabGroup.setVisibility(View.GONE);
            } else if (mOrderDetail.isbzj_type.equals("1") && mOrderDetail.pay.equals("1")) {
                ispaytv.setText("支付：" + "运费未付");
            }
        }


        mmoney.setVisibility(View.GONE);
        rgTabGroup.setVisibility(View.VISIBLE);

        mLoadingDialog.dismiss();
    }


    @Override
    public void onRefreshError(Throwable e) {
        mLoadingDialog.dismiss();
        if (mXrwlwxpayDialog != null) {
            mXrwlwxpayDialog.dismiss();
        }
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if (mPostDialog != null) {
            mPostDialog.dismiss();
        }
        if (mXrwlwxpayDialog != null) {
            mXrwlwxpayDialog.dismiss();
        }
        mLoadingDialog.dismiss();
        handleError(entity);
    }


    @Override
    public void resultsSuccess(BaseEntity<PayResult> entity) {
        mPostDialog.dismiss();
        showToast("支付成功");
        if (!mOrderDetail.userid.equals(mAccount.getId())) {
            Map<String, String> paramstui = new HashMap<>();
            float bbbbbbbbbb = Float.parseFloat(mOrderDetail.Final_payment) * 100;
            float tuiqian = -bbbbbbbbbb;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            final String straaaa = formatter.format(new Date());
            paramstui.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            paramstui.put("WaterId", mOrderDetail.WaterId);
            paramstui.put("refundNo", straaaa);
            paramstui.put("money", String.valueOf(tuiqian).substring(0, String.valueOf(tuiqian).indexOf(".")));
            paramstui.put("re_money", String.valueOf(tuiqian).substring(0, String.valueOf(tuiqian).indexOf(".")));
            mPresenter.wx_refund(paramstui);
        }
        handleError(entity);
        finish();
    }

    @Override
    public void onNavSuccess(BaseEntity<OrderDetail> entity) {
        OrderDetail od = entity.getData();
        Intent intent = new Intent(mContext, OwnerOrderDetailActivity.class);
        if ("1".equals(od.category)) {
            intent.putExtra("x", od.startLat);
            intent.putExtra("y", od.startLon);
            intent.putExtra("xxx", od.endLat);
            intent.putExtra("yyy", od.endLon);
        } else if ("2".equals(od.category)) {
            intent.putExtra("x", od.endLat);
            intent.putExtra("y", od.endLon);
            intent.putExtra("xxx", od.startLat);
            intent.putExtra("yyy", od.startLon);
        }
        startActivity(intent);
    }

    @Override
    public void onNavError(Throwable e) {
        showToast("导航失败");
    }

    @Override
    public void onCancelOrderSuccess(BaseEntity<OrderDetail> entity) {
        mPostDialog.dismiss();
        showToast("取消订单成功");
        EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());
        finish();
    }

    @Override
    public void onCancelOrderError(Throwable e) {
        mPostDialog.dismiss();
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

        mPostDialog.dismiss();
        showToast("收货成功");
        handleError(entity);
        finish();
//        EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());
//        Intent intent = new Intent(this, OwnerOrderActivity.class);
//        intent.putExtra("position", 3);
//        startActivity(intent);

    }

    @Override
    public void onConfirmOrderError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onConfirmtixingOrderSuccess(BaseEntity<OrderDetail> entity) {
        // mPostDialog.dismiss();
//        showToast("线下支付提醒司机成功");
//        startActivity(new Intent(this, OwnerOrderActivity.class));
        finish();
    }

    @Override
    public void onConfirmtixingOrderError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onConfirmOwnerkaishiyunshuSuccess(BaseEntity<OrderDetail> entity) {
        // mPostDialog.dismiss();
        //  handleError(entity);
        //  EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());
//        Intent intent = new Intent(this, OwnerOrderActivity.class);
//        intent.putExtra("position", 1);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onConfirmOwnerkaishiyunshuError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onLocationSuccess(BaseEntity<OrderDetail> entity) {
        //   mPostDialog.dismiss();
        OrderDetail od = entity.getData();
        String lat = String.valueOf(od.lat);
        String lon = String.valueOf(od.lon);
        Intent intent = new Intent(mContext, QinLocationActivity.class);
        intent.putExtra("mlat", lat);
        intent.putExtra("mlon", lon);
        startActivity(intent);


//        NaviPara naviPara = new NaviPara();
//        //设置终点位置
//        naviPara.setTargetPoint(new LatLng(lat, lon));
//        //设置导航策略，这里是避免拥堵
//        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);
//        try {
//            AMapUtils.openAMapNavi(naviPara, this);
//        } catch (AMapException e) {
//            e.printStackTrace();
//            //如果没安装会进入异常，调起下载页面
//            AMapUtils.getLatestAMapApp(this);
//        }


    }

    @Override
    public void onLocationError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onPaySuccess(BaseEntity<PayResult> entity) {

        mPostDialog.dismiss();

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
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {
        // showToast("提交成功");
        // startActivity(new Intent(this, OwnerOrderActivity.class));
        // finish();
        // mXrwlwxpayDialog.dismiss();
        /**
         * 调用微信支付php*/
        mXrwlwxpayDialog.dismiss();
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
        EventBus.getDefault().post(new PublishClearCacheEvent());


    }

    @Override
    public void wxonOnlinePayError(Throwable e) {

    }

    @Override
    public void wxonOnlinedaishouPaySuccess(BaseEntity<PayResult> entity) {
        mPostDialog.dismiss();

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
        Intent intent = new Intent(this, OwnerOrderActivity.class);
        intent.putExtra("position", 2);
        startActivity(intent);
        finish();

    }

    @Override
    public void onCancelDriverkaishiyunshuError(Throwable e) {

    }

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
        handleError(entity);
    }

    @Override
    public void onTixianException(Throwable e) {

    }

    @Override
    public void onTixiantuikuanSuccess() {
        showToast("您的余额已经退款成功");

    }

    @Override
    public void onTixiantuikuanError(BaseEntity entity) {
        showToast("退款失败");
        handleError(entity);
    }

    @Override
    public void onTixiantuikuanException(Throwable e) {

    }

    @Override
    public void onPingJiaSuccess() {
        showToast("评价成功");
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

    }

    @Override
    public void ongetlistpingjiaError(BaseEntity entity) {

    }

    @Override
    public void ongetlistpingjiaException(Throwable e) {

    }

    @Override
    public void onetlistpingjiasSuccess(String duqu, String username, String dianhua, String chexing, String pingfen, String chehao) {
        dianpinglo = duqu;
    }

    @Override
    public void onWx_refundSuccess(BaseEntity<PayResult> entity) {
        showToast("退款成功");
        finish();
    }

    @Override
    public void onWx_refundError(Throwable e) {

    }

//    @Override
//    public void onetlistpingjiasSuccess(String price) {
//
//    }

    @Override
    protected void onDestroy() {

        if (mPayBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPayBroadcastReceiver);
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (mLocationClient != null) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        super.onDestroy();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    public void showRedPacketDialog(RedPacketEntity entity) {
        if (mRedPacketDialogView == null) {
            mRedPacketDialogView = View.inflate(this, R.layout.dialog_red_packet, null);
            mRedPacketViewHolder = new RedPacketViewHolder(this, mRedPacketDialogView);
            mRedPacketDialog = new CustomDialog(this, mRedPacketDialogView, R.style.custom_dialog);
            mRedPacketDialog.setCancelable(false);
        }

        mRedPacketViewHolder.setData(entity);
        mRedPacketViewHolder.setOnRedPacketDialogClickListener(new OnRedPacketDialogClickListener() {
            @Override
            public void onCloseClick() {
                mRedPacketDialog.dismiss();
            }

            @Override
            public void onOpenClick() {
                //领取红包,调用接口
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(mContext, HongbaolistActivity.class);
                        intent.putExtra("orderid", mId);
                        intent.putExtra("nums", mOrderDetail.freight);
                        startActivity(intent);
                        mRedPacketDialog.dismiss();

                        // finish();
                    }
                }, 1000);

            }
        });
        mRedPacketDialog.show();
    }

    public void hongbao() {
        RedPacketEntity entity = new RedPacketEntity("16飕云", "http://www.16sbj.com/xcx/hongbao.png", "订单完成，红包返现");
        showRedPacketDialog(entity);
        mHomeIntroRv.setFocusableInTouchMode(false);
        mHomeIntroRv.setHasFixedSize(true);
        mHomeIntroRv.setNestedScrollingEnabled(false);
        mHomeServiceRv.setFocusableInTouchMode(false);
        mHomeServiceRv.setHasFixedSize(true);
        mHomeServiceRv.setNestedScrollingEnabled(false);
        mHomeIntroRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeIntroRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
        mHomeServiceRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeServiceRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                mCurrentLocation = aMapLocation;
                // LatLng latlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                LatLng latLng = new LatLng(35.95555, 111.356214);
                final Marker marker = mAmap.addMarker(new MarkerOptions().position(latLng).title("").snippet("DefaultMarker"));

                //  mCurrentCity = aMapLocation.getCity();

                //mAmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, mAmap.getMaxZoomLevel() - 3));

                // doSearchQuery();

                mLocationClient.stopLocation();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 这是必须重写的方法
     *
     * @param arg0
     * @return
     */
    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
    }


    @Override
    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        dissmissProgressDialog();

        if (mOrderDetail.type.equals("0")) {
            mAmap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mWalkRouteResult = result;
                        final WalkPath walkPath = mWalkRouteResult.getPaths()
                                .get(0);
                        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                this, mAmap, walkPath,
                                mWalkRouteResult.getStartPos(),
                                mWalkRouteResult.getTargetPos());

                        walkRouteOverlay.getWalkColor();//轨迹颜色修改
                        walkRouteOverlay.removeFromMap();
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
                        walkRouteOverlay.setNodeIconVisibility(false);//关闭行走图标轨迹
                        int dis = (int) walkPath.getDistance();
                        int dur = (int) walkPath.getDuration();
                        String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(this, "对不起 搜不到相关数据", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "对不起，搜不到相关数据", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, errorCode, Toast.LENGTH_LONG).show();
            }
        } else if (mOrderDetail.type.equals("1")) {
            mAmap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mWalkRouteResult = result;
                        final WalkPath walkPath = mWalkRouteResult.getPaths()
                                .get(0);
                        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                this, mAmap, walkPath,
                                mWalkRouteResult.getStartPos(),
                                mWalkRouteResult.getTargetPos());

                        walkRouteOverlay.getWalkColor();//轨迹颜色修改
                        walkRouteOverlay.removeFromMap();
                        walkRouteOverlay.addToMap2();
                        walkRouteOverlay.zoomToSpan();
                        walkRouteOverlay.setNodeIconVisibility(false);//关闭行走图标轨迹
                        int dis = (int) walkPath.getDistance();
                        int dur = (int) walkPath.getDuration();
                        String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(this, "对不起 搜不到相关数据", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "对不起，搜不到相关数据", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, errorCode, Toast.LENGTH_LONG).show();
            }

        } else if (mOrderDetail.type.equals("2")) {
            mAmap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mWalkRouteResult = result;
                        final WalkPath walkPath = mWalkRouteResult.getPaths()
                                .get(0);
                        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                this, mAmap, walkPath,
                                mWalkRouteResult.getStartPos(),
                                mWalkRouteResult.getTargetPos());

                        walkRouteOverlay.getWalkColor();//轨迹颜色修改
                        walkRouteOverlay.removeFromMap();
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
                        walkRouteOverlay.setNodeIconVisibility(false);//关闭行走图标轨迹
                        int dis = (int) walkPath.getDistance();
                        int dur = (int) walkPath.getDuration();
                        String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(this, "对不起 搜不到相关数据", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "对不起，搜不到相关数据", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, errorCode, Toast.LENGTH_LONG).show();
            }
        } else if (mOrderDetail.type.equals("3")) {
            mAmap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mWalkRouteResult = result;
                        final WalkPath walkPath = mWalkRouteResult.getPaths()
                                .get(0);
                        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                this, mAmap, walkPath,
                                mWalkRouteResult.getStartPos(),
                                mWalkRouteResult.getTargetPos());

                        walkRouteOverlay.getWalkColor();//轨迹颜色修改
                        walkRouteOverlay.removeFromMap();
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
                        walkRouteOverlay.setNodeIconVisibility(false);//关闭行走图标轨迹
                        int dis = (int) walkPath.getDistance();
                        int dur = (int) walkPath.getDuration();
                        String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(this, "对不起 搜不到相关数据", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "对不起，搜不到相关数据", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, errorCode, Toast.LENGTH_LONG).show();
            }
        }

    }


    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            //progDialog.dismiss();
        }
    }

    private class PayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getIntExtra("type", 0) == 0) {
                Map<String, String> params = new HashMap<>();
                if (daishoutype.equals("4")) {
                    params.put("orderid", mId);
                    params.put("daishou", "4");

                    mPresenter.wxonlinedaishouPay(params);
                    showToast("付款成功");
                    EventBus.getDefault().post(new OwnerOrderListRrefreshEvent());
                    startActivity(new Intent(mContext, OwnerOrderActivity.class));
                    finish();

                } else {

                    Float yuanshi = Float.parseFloat(mOrderDetail.overtotal_price);
                    Float xianzai = Float.parseFloat(mOrderDetail.Final_payment);
                    Float jieguo = xianzai;
                    params.put("orderid", mId);
                    params.put("Payment_method", "1");
                    params.put("wx", "0");
                    //通过变量知道是方支付
                    if (zhifuren.equals("0")) {
                        //发货发支付
                        params.put("requestrenminbi", String.valueOf(xzfjfs));
                        params.put("zhifuren", "0");
                    } else {
                        //收货方支付
                        params.put("requestrenminbi", String.valueOf(Float.parseFloat(mOrderDetail.freight)));
                        params.put("zhifuren", "1");  //这个肯定是全额支付
                        //收货方支付完成后，进行原来的第一次回退到既保证金的回退

                    }

                    params.put("bzj", "1");
                    params.put("quankuan", "0");
                    params.put("overtotal_price", String.valueOf(jieguo));
                    //mPresenter.wxonlinePay(params);
                    mPresenter.results(params);

                    if ("1".equals(mOrderDetail.isquerenshouhuo)) {
                        RedPacketEntity entity = new RedPacketEntity("16飕云", "http://www.16sbj.com/xcx/hongbao.png", "订单完成，红包返现");
                        showRedPacketDialog(entity);
                        mHomeIntroRv.setFocusableInTouchMode(false);
                        mHomeIntroRv.setHasFixedSize(true);
                        mHomeIntroRv.setNestedScrollingEnabled(false);
                        mHomeServiceRv.setFocusableInTouchMode(false);
                        mHomeServiceRv.setHasFixedSize(true);
                        mHomeServiceRv.setNestedScrollingEnabled(false);
                        mHomeIntroRv.setLayoutManager(new GridLayoutManager(mContext, 4));
                        mHomeIntroRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
                        mHomeServiceRv.setLayoutManager(new GridLayoutManager(mContext, 4));
                        mHomeServiceRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));
                        // finish();
                    }

                }
            } else {
                showToast("付款失败");
            }
        }

    }


}
