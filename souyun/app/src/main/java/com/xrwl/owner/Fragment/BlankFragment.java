package com.xrwl.owner.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.ldw.library.utils.AppUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.CompanyFahuoBean;
import com.xrwl.owner.bean.CompanyShouhuoBean;
import com.xrwl.owner.bean.HomeChexingBean;
import com.xrwl.owner.bean.HomeHuowuBean;
import com.xrwl.owner.bean.MarkerBean;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.module.friend.ui.FriendActivity;
import com.xrwl.owner.module.home.adapter.HomeAdAdapter;
import com.xrwl.owner.module.home.adapter.HomesAdAdapter;
import com.xrwl.owner.module.home.ui.CustomDialog;
import com.xrwl.owner.module.home.ui.RedPacketViewHolder;
import com.xrwl.owner.module.publish.adapter.SearchLocationAdapter;
import com.xrwl.owner.module.publish.bean.Truck;
import com.xrwl.owner.module.publish.ui.AddressActivity;
import com.xrwl.owner.module.publish.ui.TruckActivity;
import com.xrwl.owner.module.publish.view.CompanyManageActivity;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.xrwl.owner.utils.MyTextWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment implements LocationSource, AMapLocationListener {

    public static final int RESULT_TRUCK = 111;//已选车型
    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位
    public static final int RESULT_FRIEND_START = 444;//发货人电话
    public static final int RESULT_FRIEND_END = 555;//收货人电话
    public static final int RESULT_CONPANY_START = 666;//发货单位
    public static final int RESULT_CONPANY_END = 777;//收货单位

    private Account mAccount;
    private Disposable mDisposable;
    private HomeAdAdapter mHomeAdAdapter;

    private HomesAdAdapter mHomesAdAdapter;

    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private CustomDialog mRedPacketDialog;


    private Double latitude;
    private Double longitude;
    private static final int REQUEST_CODE_INSTALL_PERMISSION = 107;
    private static final int NOT_NOTICE = 2;
    private static final int PERMISSION_GRANTED = 1;
    private AlertDialog alertDialog;
    private AlertDialog mDialog;
    private boolean shouldBack;
    private String mCurrentCity;
    private AMapLocation mCurrentLocation;
    private Context context;
    @BindView(R.id.nlMapView)
    MapView mMapView;

    //出发地
    @BindView(R.id.et_chufadi)
    EditText et_chufadi;
    @BindView(R.id.bt_chufadi_sousuo)
    TextView bt_chufadi_sousuo;
    @BindView(R.id.bt_chufadi_xuanze)
    TextView bt_chufadi_xuanze;
    @BindView(R.id.et_fahuoren)
    EditText et_fahuoren;
    @BindView(R.id.et_fahuotel)
    EditText et_fahuotel;
    @BindView(R.id.bt_fahuo_xuanze)
    TextView bt_fahuo_xuanze;
    //目的地
    @BindView(R.id.et_mudidi)
    EditText et_mudidi;
    @BindView(R.id.bt_mudidi_sousuo)
    TextView bt_mudidi_sousuo;
    @BindView(R.id.bt_mudidi_xuanze)
    TextView bt_mudidi_xuanze;
    @BindView(R.id.et_shouhuoren)
    EditText et_shouhuoren;
    @BindView(R.id.et_shouhuotel)
    EditText et_shouhuotel;
    @BindView(R.id.bt_shouhuo_xuanze)
    TextView bt_shouhuo_xuanze;
    //发货单位
    @BindView(R.id.et_fahuodanwei)
    EditText et_fahuodanwei;
    @BindView(R.id.bt_fahuodanwei_xuanze)
    TextView bt_fahuodanwei_xuanze;
    //收货单位
    @BindView(R.id.et_shouhuodanwei)
    EditText et_shouhuodanwei;
    @BindView(R.id.bt_shouhuodanwei_xuanze)
    TextView bt_shouhuodanwei_xuanze;
    //货物吨数
    @BindView(R.id.et_huowu_dun)
    EditText et_huowu_dun;
    @BindView(R.id.et_huowu_fang)
    EditText et_huowu_fang;
    @BindView(R.id.et_huowu_jian)
    EditText et_huowu_jian;
    //车型
    @BindView(R.id.sp_chexing)
    Spinner sp_chexing;
    @BindView(R.id.tv_chexing)
    TextView tv_chexing;
    @BindView(R.id.bt_chexing_xuanze)
    TextView bt_chexing_xuanze;
    //选择
    @BindView(R.id.slResultLayout)
    RelativeLayout mResultLayout;
    @BindView(R.id.slListView)
    ListView mListView;

    private SearchLocationAdapter mAdapter;
    private SearchLocationAdapter mAdapter2;
    String city = "";
    boolean locationFirst = true;
    MarkerBean locationBean = new MarkerBean();//出发地
    MarkerBean destinationBean = new MarkerBean();//目的地
    CompanyFahuoBean fahuodanweiBean = new CompanyFahuoBean();
    CompanyShouhuoBean shouhuodanweiBean = new CompanyShouhuoBean();
    HomeChexingBean chexingBean = new HomeChexingBean();
    HomeHuowuBean huowuBean = new HomeHuowuBean();
    int chexingType = 0;

    private AMap aMap;
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    //定位蓝点
    MyLocationStyle myLocationStyle;


    private final String[] mTitles = {
            "热门"
    };
    private SlidingTabLayout mSl;
    private ViewPager mViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyFragmentAdapter mFragmentAdapter;
    private String mKeyword;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //    @BindView(R.id.nlMapView)
//    MapView nlMapView;
//    @BindView(R.id.v_mask)
//    View vMask;
    Unbinder unbinder;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图


        if (getArguments() != null) {
            Context context;
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    private void initView() {
        sp_chexing.setAdapter(new ArrayAdapter<>(getContext(),
                R.layout.my_simple_spinner_dropdown_item, android.R.id.text1,
                new String[]{ "同城车型","长途车型"}));
        sp_chexing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chexingType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        et_huowu_dun.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString())){
                    huowuBean.setDun(s.toString());
                    ((TabActivity)getActivity()).setHuowu(huowuBean);
                }
            }
        });

        et_huowu_fang.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                huowuBean.setFang(s.toString());
                ((TabActivity)getActivity()).setHuowu(huowuBean);
            }
        });

        et_huowu_jian.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                huowuBean.setJian(s.toString());
                ((TabActivity)getActivity()).setHuowu(huowuBean);
            }
        });

        et_fahuoren.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                locationBean.setName(s.toString());
                ((TabActivity)getActivity()).setMyLocation(locationBean,true);
            }
        });
        et_fahuotel.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                locationBean.setTel(s.toString());
                ((TabActivity)getActivity()).setMyLocation(locationBean,true);
            }
        });

        et_shouhuoren.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                destinationBean.setName(s.toString());
                ((TabActivity)getActivity()).setDestination(destinationBean);
            }
        });
        et_shouhuotel.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                destinationBean.setTel(s.toString());
                ((TabActivity)getActivity()).setDestination(destinationBean);
            }
        });

        et_fahuodanwei.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                fahuodanweiBean.setName(s.toString());
                ((TabActivity)getActivity()).setFahuodanweiBean(fahuodanweiBean);
            }
        });
        et_shouhuodanwei.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                shouhuodanweiBean.setName(s.toString());
                ((TabActivity)getActivity()).setShouhuodanweiBean(shouhuodanweiBean);
            }
        });

        quanxian();
    }

    boolean goPermission;
    public void quanxian(){
        RxPermissions rxPermissions = new RxPermissions(getActivity());
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
                            new AlertDialog.Builder(getContext())
                                    .setMessage("本页面需要您授权位置权限，否则将无法使用该模块的功能，是否授权？")
                                    .setNegativeButton("取消", (dialog, which) -> {

                                    })
                                    .setPositiveButton("授权定位", (dialog, which) -> {
                                        AppUtils.toSelfSetting(getContext());
                                        goPermission = true;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_blank3, container, false);

        unbinder = ButterKnife.bind(this, inflate);

        mMapView = inflate.findViewById(R.id.nlMapView);
        mMapView.onCreate(savedInstanceState);
        //
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);

//        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
//                Log.e("Msg", "location：" + location.getExtras().toString());
//            }
//        });

        initView();

        return inflate;
    }

    private void initMap() {

        //设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        //蓝点初始化
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。

        myLocationStyle.showMyLocation(true);

    }

//    public void showRedPacketDialog(RedPacketEntity entity) {
//        if (mRedPacketDialogView == null) {
//            mRedPacketDialogView = View.inflate(getContext(), R.layout.dialog_red_packet, null);
//            mRedPacketViewHolder = new RedPacketViewHolder(getContext(), mRedPacketDialogView);
//            mRedPacketDialog = new CustomDialog(getContext(), mRedPacketDialogView, R.style.custom_dialog);
//            mRedPacketDialog.setCancelable(false);
//        }
//
//        mRedPacketViewHolder.setData(entity);
//        mRedPacketViewHolder.setOnRedPacketDialogClickListener(new OnRedPacketDialogClickListener() {
//            @Override
//            public void onCloseClick() {
//                mRedPacketDialog.dismiss();
//            }
//
//            @Override
//            public void onOpenClick() {
//                //领取红包,调用接口
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(getContext(), HongbaolistActivity.class);
//                        startActivity(intent);
//                        mRedPacketDialog.dismiss();
//                        // finish();
//                    }
//                }, 1000);
//
//            }
//        });
//
//        mRedPacketDialog.show();
//    }
//    private void initEvent() {
//        mViewPager.setAdapter(mFragmentAdapter);
//        mSl.setViewPager(mViewPager, mTitles);
//
//        //反射修改最少滑动距离
//        try {
//            Field mTouchSlop = ViewPager.class.getDeclaredField("mTouchSlop");
//            mTouchSlop.setAccessible(true);
//            mTouchSlop.setInt(mViewPager, dp2px(50));
//            Log.d("Msg", "往下滑动阻尼设置");
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//            Log.e("Msg", "这是错误信息");
//        }
//        mViewPager.setOffscreenPageLimit(mFragments.size());
//    }
//    public int dp2px(float dpVal) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                dpVal, getResources().getDisplayMetrics());
//    }
//
//    private void initData() {
//        mFragments.add(new SongFragment());
//
//        mFragmentAdapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager());
//
//
//    }


    private class MyFragmentAdapter extends FragmentPagerAdapter {

        MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();

        //返回键监听
        getView().setOnKeyListener((view, i, keyEvent) -> {
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                if(mResultLayout.getVisibility() == View.VISIBLE){
                    mResultLayout.setVisibility(View.GONE);
                    return true;
                }
            }
            return false;
        });

        if(goPermission){
            quanxian();
            goPermission = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //--定位监听---------

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(getActivity());
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);

            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    //定位回调  在回调方法中调用“mListener.onLocationChanged(amapLocation);”可以在地图上显示系统小蓝点。
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                locationBean.setCity(aMapLocation.getCity());
                locationBean.setProvince(aMapLocation.getProvince());
                locationBean.setAddress(aMapLocation.getAddress());
                locationBean.setLat(aMapLocation.getLatitude());
                locationBean.setLon(aMapLocation.getLongitude());

                ((TabActivity)getActivity()).setMyLocation(locationBean);

                if(TextUtils.isEmpty(et_chufadi.getText().toString()) && !locationFirst){
                    et_chufadi.setText(aMapLocation.getAddress());
                    city = aMapLocation.getCity();
                    locationFirst = false;
                }

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("定位AmapErr", errText);
            }
        }
    }

    @OnClick({R.id.bt_chufadi_sousuo,R.id.bt_mudidi_sousuo,
            R.id.bt_chufadi_xuanze,R.id.bt_mudidi_xuanze,
            R.id.bt_chexing_xuanze,
            R.id.bt_fahuodanwei_xuanze,R.id.bt_shouhuodanwei_xuanze,
            R.id.bt_fahuo_xuanze,R.id.bt_shouhuo_xuanze})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_chufadi_sousuo:
                String chufadi = et_chufadi.getText().toString();
                if (TextUtils.isEmpty(chufadi)) {
                    ToastUtils.showShort("请输入出发地");
                    return;
                }

                PoiSearch.Query query = new PoiSearch.Query(chufadi, "", city);
                query.setPageSize(10);//设置每页最多返回多少条poiitem
                query.setPageNum(0);//设置查第一页
                PoiSearch poiSearch = new PoiSearch(getContext(), query);
                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    @Override
                    public void onPoiSearched(PoiResult poiResult, int i) {
                        List<PoiItem> datas = poiResult.getPois();

                        if (mAdapter == null) {
                            mAdapter = new SearchLocationAdapter(getContext(), R.layout.searchlocation_listview_item, datas);
                            mAdapter.setOnPoiItemClickListener(pi -> {
                                double lat = pi.getLatLonPoint().getLatitude();
                                double lon = pi.getLatLonPoint().getLongitude();

                                locationBean.setCity(pi.getCityName());
                                locationBean.setProvince(pi.getProvinceName());
                                locationBean.setAddress(pi.getTitle());
                                locationBean.setLat(pi.getLatLonPoint().getLatitude());
                                locationBean.setLon(pi.getLatLonPoint().getLongitude());

                                ((TabActivity)getActivity()).setMyLocation(locationBean,true);

                                et_chufadi.setText(pi.getTitle());

                                aMap.clear();
                                aMap.addMarker(new MarkerOptions().title(pi.getTitle()).position(new LatLng(lat, lon)));
                                aMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));

                                mResultLayout.setVisibility(View.GONE);

                            });
                        }

                        mListView.setAdapter(mAdapter);
                        if(datas !=null && datas.size() > 0){

                            //返回键监听
                            getView().setFocusableInTouchMode(true);
                            getView().requestFocus();

                            mResultLayout.setVisibility(View.VISIBLE);
                            mAdapter.setDatas(datas);
                        }else{
                            ToastUtils.showShort("未搜索出目的地");
                        }
                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {

                    }
                });
                poiSearch.searchPOIAsyn();// 异步搜索

                break;
            case R.id.bt_mudidi_sousuo:
                String mudidi = et_mudidi.getText().toString();
                if (TextUtils.isEmpty(mudidi)) {
                    ToastUtils.showShort("请输入目的地");
                    return;
                }

                PoiSearch.Query query2 = new PoiSearch.Query(mudidi, "", city);
                query2.setPageSize(10);//设置每页最多返回多少条poiitem
                query2.setPageNum(0);//设置查第一页
                PoiSearch poiSearch2 = new PoiSearch(getContext(), query2);
                poiSearch2.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    @Override
                    public void onPoiSearched(PoiResult poiResult, int i) {
                        List<PoiItem> datas = poiResult.getPois();

                        if (mAdapter2 == null) {
                            mAdapter2 = new SearchLocationAdapter(getContext(), R.layout.searchlocation_listview_item, datas);
                            mAdapter2.setOnPoiItemClickListener(pi -> {
                                double lat = pi.getLatLonPoint().getLatitude();
                                double lon = pi.getLatLonPoint().getLongitude();

                                destinationBean.setCity(pi.getCityName());
                                destinationBean.setProvince(pi.getProvinceName());
                                destinationBean.setAddress(pi.getTitle());
                                destinationBean.setLat(pi.getLatLonPoint().getLatitude());
                                destinationBean.setLon(pi.getLatLonPoint().getLongitude());

                                ((TabActivity)getActivity()).setDestination(destinationBean);

                                et_mudidi.setText(pi.getTitle());

                                aMap.clear();
                                aMap.addMarker(new MarkerOptions().title(pi.getTitle()).position(new LatLng(lat, lon)));
                                aMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));

                                mResultLayout.setVisibility(View.GONE);

                            });
                        }

                        mListView.setAdapter(mAdapter2);
                        if(datas !=null && datas.size() > 0){

                            //返回键监听
                            getView().setFocusableInTouchMode(true);
                            getView().requestFocus();

                            mResultLayout.setVisibility(View.VISIBLE);
                            mAdapter2.setDatas(datas);
                        }else{
                            ToastUtils.showShort("未搜索出目的地");
                        }
                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {

                    }
                });
                poiSearch2.searchPOIAsyn();// 异步搜索
                break;
            case R.id.bt_chufadi_xuanze:
                startActivityForResult(new Intent(getContext(), AddressActivity.class), RESULT_POSITION_START);
                break;
            case R.id.bt_mudidi_xuanze:
                startActivityForResult(new Intent(getContext(), AddressActivity.class), RESULT_POSITION_END);
                break;
            case R.id.bt_chexing_xuanze:
                Intent intent = new Intent(getContext(), TruckActivity.class);
                if(chexingType == 0){//同城车型
                    intent.putExtra("categoty", "5");
                }else if(chexingType == 1){//长途车型
                    intent.putExtra("categoty", "1");
                }
                intent.putExtra("title", chexingType == 0?"同城车型":"长途车型");
                startActivityForResult(intent, RESULT_TRUCK);
                break;
            case R.id.bt_fahuodanwei_xuanze:
                Intent intentfahuo = new Intent(getContext(), CompanyManageActivity.class);
                intentfahuo.putExtra("isItemClick", true);
                /**请选择发货单位*/
                intentfahuo.putExtra("title", "请选择发货单位");
                startActivityForResult(intentfahuo, RESULT_CONPANY_START);
                break;
            case R.id.bt_shouhuodanwei_xuanze:
                /**请选择收货单位*/
                Intent intentshouhuo = new Intent(getContext(), CompanyManageActivity.class);
                intentshouhuo.putExtra("isItemClick", true);
                intentshouhuo.putExtra("title", "请选择收货单位");
                startActivityForResult(intentshouhuo, RESULT_CONPANY_END);
                break;
            case R.id.bt_fahuo_xuanze:
                Intent intentfahuotel = new Intent(getContext(), FriendActivity.class);
                startActivityForResult(intentfahuotel, RESULT_FRIEND_START);
                break;
            case R.id.bt_shouhuo_xuanze:
                Intent intentshouhuotel = new Intent(getContext(), FriendActivity.class);
                startActivityForResult(intentshouhuotel, RESULT_FRIEND_END);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == RESULT_POSITION_START) {

            String title = data.getStringExtra("title");
            String city = data.getStringExtra("city");
            String province = data.getStringExtra("pro");
            double lat = data.getDoubleExtra("lat", 0);
            double lon = data.getDoubleExtra("lon", 0);
            String userName = data.getStringExtra("userName");
            String tel = data.getStringExtra("tel");

            locationBean.setCity(city);
            locationBean.setProvince(province);
            locationBean.setAddress(title);
            locationBean.setLat(lat);
            locationBean.setLon(lon);

            ((TabActivity)getActivity()).setMyLocation(locationBean,true);

            et_chufadi.setText(title);
            if(!TextUtils.isEmpty(userName)){
                locationBean.setName(userName);
                et_fahuoren.setText(userName);
            }
            if(!TextUtils.isEmpty(tel)){
                locationBean.setTel(tel);
                et_fahuotel.setText(tel);
            }

        } else if (requestCode == RESULT_POSITION_END) {

            String title = data.getStringExtra("title");
            String city = data.getStringExtra("city");
            String province = data.getStringExtra("pro");
            double lat = data.getDoubleExtra("lat", 0);
            double lon = data.getDoubleExtra("lon", 0);
            String userName = data.getStringExtra("userName");
            String tel = data.getStringExtra("tel");

            destinationBean.setCity(city);
            destinationBean.setProvince(province);
            destinationBean.setAddress(title);
            destinationBean.setLat(lat);
            destinationBean.setLon(lon);

            ((TabActivity)getActivity()).setDestination(destinationBean);

            et_mudidi.setText(title);
            if(!TextUtils.isEmpty(userName)){
                destinationBean.setName(userName);
                et_shouhuoren.setText(userName);
            }
            if(!TextUtils.isEmpty(tel)){
                destinationBean.setTel(tel);
                et_shouhuotel.setText(tel);
            }

        }
        /**已选车型*/
        else if (requestCode == RESULT_TRUCK) {
            Truck truck = (Truck) data.getSerializableExtra("data");

            chexingBean.setChexing(truck.getTitle());
            chexingBean.setTruck(truck);
            chexingBean.setChexingType(chexingType);

            ((TabActivity)getActivity()).setChexing(chexingBean);

            tv_chexing.setText(truck.getTitle());

        }
        /**发货单位*/
        else if(requestCode == RESULT_CONPANY_START){
            String name = data.getStringExtra("name");
            et_fahuodanwei.setText(name);

            fahuodanweiBean.setName(name);

            ((TabActivity)getActivity()).setFahuodanweiBean(fahuodanweiBean);
        }
        /**收货单位*/
        else if(requestCode == RESULT_CONPANY_END){
            String name = data.getStringExtra("name");
            et_shouhuodanwei.setText(name);

            shouhuodanweiBean.setName(name);

            ((TabActivity)getActivity()).setShouhuodanweiBean(shouhuodanweiBean);
        }
        /**发货电话*/
        else if (requestCode == RESULT_FRIEND_START) {
            Friend friend = (Friend) data.getSerializableExtra("data");

            String name = friend.getName();
            String tel = friend.getPhone().replace("-", "").replace("+", "").replace(" ", "");

            et_fahuoren.setText(name);
            et_fahuotel.setText(tel);

            locationBean.setName(name);
            locationBean.setTel(name);

            ((TabActivity)getActivity()).setMyLocation(locationBean,true);

        }
        /**收货人*/
        else if (requestCode == RESULT_FRIEND_END) {
            Friend friend = (Friend) data.getSerializableExtra("data");
            String name = friend.getName();
            String tel = friend.getPhone().replace("-", "").replace("+", "").replace(" ", "");

            et_shouhuoren.setText(name);
            et_shouhuotel.setText(tel);

            destinationBean.setName(name);
            destinationBean.setTel(name);

            ((TabActivity)getActivity()).setDestination(destinationBean);

        }
    }
}