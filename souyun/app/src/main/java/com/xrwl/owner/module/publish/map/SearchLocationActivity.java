package com.xrwl.owner.module.publish.map;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ldw.library.mvp.BaseMVP;
import com.ldw.library.utils.AppUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.module.publish.adapter.SearchLocationAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 搜索或定位位置
 * Created by www.longdw.com on 2018/4/2 下午2:32.
 */

public class SearchLocationActivity extends BaseActivity implements AMapLocationListener {
    private int GPS_REQUEST_CODE = 1;
    @BindView(R.id.slMapView)
    MapView mMapView;

    @BindView(R.id.slSearchTv)
    TextView mSearchTv;
    @BindView(R.id.slCityEt)
    EditText mCityEt;//城市
    @BindView(R.id.slKeyEt)
    EditText mKeyEt;//位置关键字

    @BindView(R.id.slListView)
    ListView mListView;
    @BindView(R.id.slResultLayout)
    View mResultLayout;
    private AMap mAmap;
    private SearchLocationAdapter mAdapter;
    private AMapLocationClient mLocationClient;
    private BitmapDescriptor mMarkerIcon;
    private Disposable mDisposable;

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.searchlocation_activity;
    }

    @Override
    protected void initViews() {

        openGPSSEtting();


        mMarkerIcon = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R
                .drawable.poi_marker_pressed));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem pi = mAdapter.getItem(position);
                LatLng ll = new LatLng(pi.getLatLonPoint().getLatitude(), pi.getLatLonPoint().getLongitude());
                //设置缩放级别
                mAmap.animateCamera(CameraUpdateFactory.zoomTo(mAmap.getMaxZoomLevel() - 3));
                //将地图移动到定位点
                mAmap.animateCamera(CameraUpdateFactory.changeLatLng(ll));

                mAmap.clear();

                mAmap.addMarker(new MarkerOptions().icon(mMarkerIcon).position(ll));
            }
        });
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);

        if (mAmap == null) {
            mAmap = mMapView.getMap();
        }

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).subscribe
                (new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            initMap();
                        } else {
                            new AlertDialog.Builder(mContext)
                                    .setMessage("本页面需要您授权位置权限，否则将无法使用该模块的功能，是否授权？")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setPositiveButton("授权定位", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            AppUtils.toSelfSetting(mContext);
                                        }
                                    }).show();
                        }
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
//         MyLocationStyle myLocationIcon(;//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，
        // 定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAmap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAmap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mAmap.getUiSettings().setCompassEnabled(true);   //设置指南针用于向 App 端用户展示地图方向，默认不显示
        mAmap.getUiSettings().setScaleControlsEnabled(true);    //设置比例尺控件。位于地图右下角，可控制其显示与隐藏
        mAmap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
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

    @OnClick(R.id.slSearchTv)
    public void startSearch() {
        String keyword = mKeyEt.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
            return;
        }
        String city = mCityEt.getText().toString();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "请输入当前城市", Toast.LENGTH_SHORT).show();
            return;
        }

        PoiSearch.Query query = new PoiSearch.Query(keyword, "", city);
        query.setPageSize(10);//设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查第一页
//        query.setCityLimit(true);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                List<PoiItem> datas = poiResult.getPois();
                Log.e("getPois", datas.toString());

                if (mAdapter == null) {
                    mAdapter = new SearchLocationAdapter(mContext, R.layout.searchlocation_listview_item, datas);
                    mAdapter.setOnPoiItemClickListener(new SearchLocationAdapter.OnPoiItemClickListener() {
                        @Override
                        public void poiItemClick(PoiItem pi) {
                            double lat = pi.getLatLonPoint().getLatitude();
                            double lon = pi.getLatLonPoint().getLongitude();

//                            LatLng ll = new LatLng(lat, lon);
                            Intent intent = new Intent();
                            intent.putExtra("title", pi.getTitle());
                            intent.putExtra("lon", lon);
                            intent.putExtra("lat", lat);
                            intent.putExtra("city", pi.getCityName());
                            intent.putExtra("pro", pi.getProvinceName());
                            setResult(RESULT_OK, intent);
                            finish();

//                            //设置缩放级别
//                            mAmap.animateCamera(CameraUpdateFactory.zoomTo(mAmap.getMaxZoomLevel() - 3));
//                            //将地图移动到定位点
//                            mAmap.animateCamera(CameraUpdateFactory.changeLatLng(ll));
                        }
                    });
                    mListView.setAdapter(mAdapter);
                    mResultLayout.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.setDatas(datas);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
//        if (mCurrentLocation != null) {
//            LatLonPoint llp = new LatLonPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//            poiSearch.setBound(new PoiSearch.SearchBound(llp, 3000, true));//3000米以内
//        }
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocationClient.stopLocation();
                mCityEt.setText(aMapLocation.getCity());
                BitmapDescriptor iconDes = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R
                        .drawable.poi_marker_pressed));
                mAmap.addMarker(new MarkerOptions().icon(iconDes).position(new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude())));
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mMarkerIcon != null) {
            mMarkerIcon.recycle();
        }
        if (mLocationClient != null) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
        }

        if (mDisposable != null) {
            mDisposable.dispose();
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


    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSSEtting() {
        if (checkGpsIsOpen()){
            //Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        }else {
            new android.app.AlertDialog.Builder(this).setTitle("请打开定位权限")
                    .setMessage("打开位置信息")
                    //  取消选项
                    .setNegativeButton("取消",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(SearchLocationActivity.this, "close", Toast.LENGTH_SHORT).show();
                            // 关闭dialog
                            dialogInterface.dismiss();
                        }
                    })
                    //  确认选项
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //跳转到手机原生设置页面
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent,GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }


}
