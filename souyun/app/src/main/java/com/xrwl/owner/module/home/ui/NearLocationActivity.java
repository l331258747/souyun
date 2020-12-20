package com.xrwl.owner.module.home.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2017/11/29 下午9:46.
 */

public class NearLocationActivity extends BaseActivity implements AMapLocationListener {
    private static final int REQUEST_CODE_INSTALL_PERMISSION = 107;
    private static final int NOT_NOTICE = 2;
    private static final int PERMISSION_GRANTED =1 ;
    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private boolean shouldBack;
    private AMapLocationClient mLocationClient;
    private String mCurrentCity;
    private AMapLocation mCurrentLocation;

    @BindView(R.id.nlMapView)
    MapView mMapView;
    private AMap mAmap;

    private String mKeyword;
    private Double latitude;
    private Double longitude;

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.nearlocation_activity;
    }

    @Override
    protected void initViews() {
        mKeyword = getIntent().getStringExtra("title");
    }
    private LocationManager locationManager;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);// 此方法必须重写

        if (mAmap == null) {
            mAmap = mMapView.getMap();
        }
        if (ContextCompat.checkSelfPermission(NearLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(NearLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {

            initMap();
            initLocation();

        }
    }

    private void initMap() {
        
        MyLocationStyle myLocationStyle;
        // MyLocationStyle myLocationIcon(;//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，

        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        mAmap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mAmap.getUiSettings().setCompassEnabled(true);   //设置指南针用于向 App 端用户展示地图方向，默认不显示
        mAmap.getUiSettings().setScaleControlsEnabled(true);    //设置比例尺控件。位于地图右下角，可控制其显示与隐藏
        mAmap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。



    }

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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                Log.e("msg","第二个比例尺");
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
                mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16));


                mCurrentLocation = aMapLocation;
                LatLng latlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                mCurrentCity = aMapLocation.getCity();

                mAmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, mAmap.getMaxZoomLevel() - 3));

                doSearchQuery();

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
     * 对输入出发地的地点进行位置查询
     */
    private void doSearchQuery() {

        PoiSearch.Query query = new PoiSearch.Query(mKeyword, "", mCurrentCity);
        query.setPageSize(10);//设置每页最多返回多少条poiitem
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
    protected void onDestroy() {
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




}
