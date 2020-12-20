package com.xrwl.owner.module.tab.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.listener.OnResultListener;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.ldw.library.utils.Utils;
import com.ldw.library.view.LBadgeView;
import com.ldw.library.view.tab.BottomTab;
import com.ldw.library.view.tab.BottomTabItem;
import com.xrwl.owner.Frame.auxiliary.RetrofitManager1;
import com.xrwl.owner.Frame.retrofitapi.NetService;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseEventActivity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.Cljbxx;
import com.xrwl.owner.bean.Tab;
import com.xrwl.owner.event.BusinessTabCountEvent;
import com.xrwl.owner.event.TabCheckEvent;
import com.xrwl.owner.module.account.activity.LoginActivity;
import com.xrwl.owner.module.tab.mvp.TabPresenter;
import com.xrwl.owner.utils.AccountUtil;
import com.xrwl.owner.utils.BadgeUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by www.longdw.com on 2018/3/25 下午10:39.
 */

public class TabActivity extends BaseEventActivity<BaseMVP.IBaseView, TabPresenter> implements AMapLocationListener, BaseMVP.IBaseView {
    private int GPS_REQUEST_CODE = 1;
    @BindView(R.id.tabBottomTab)
    BottomTab mMainBottomTab;
    private List<Tab> mDatas;
    private List<Fragment> mFragmentList = new ArrayList<>();

    private AMapLocationClient mLocationClient;
    private Object shippingNoteInfos;
    private RetrofitManager1 retrofitManager;


    @Override
    protected TabPresenter initPresenter() {
        return new TabPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tab_activity;
    }

    @Override
    protected void initViews() {


        //在启动页或 app 首页中，初始化 sdk 服务。context 必须为 activity。
        String appId = "com.xrwl.owner";
        String appSecurity = "978943b74c8f42328567d50479940344fa439712ea1d4949b664de25d3ca773a";
        String enterpriseSenderCode = "14101810";
       String environment = "debug";
        String ShippingNoteInfo[];
        LocationOpenApi.init(TabActivity.this, appId, appSecurity,enterpriseSenderCode,environment , new OnResultListener() {
            @Override
            public void onSuccess() {

                //Toast.makeText(TabActivity.this, "Sdk初始化成功", Toast.LENGTH_SHORT).show();
                Driverpositioning();
            }

            @Override
            public void onFailure(String s, String s1) {

                //Toast.makeText(TabActivity.this, s1.toString(), Toast.LENGTH_SHORT).show();

            }
        });







        openGPSSEtting();
        Account account = AccountUtil.getAccount(this);
        if (account.phone.length() < 1) {
            startActivity(new Intent(this, LoginActivity.class));
        }


        try {
            if (account.isOwner()) {
                //根据json中的描述初始化TAB的内容
                JSONObject json = new JSONObject(Utils.getAssetsString(mContext, "tab.json"));
                mDatas = Tab.parseJson(json);
            } else if (account.isDriver()) {
                //根据json中的描述初始化TAB的内容
                JSONObject json = new JSONObject(Utils.getAssetsString(mContext, "driver_tab.json"));
                mDatas = Tab.parseJson(json);
            }

            initNavBar();
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        initLocation();

        getBadgeCount();









        //启用服务。context 必须为 activity。
        LocationOpenApi.start(this, (com.hdgq.locationlib.entity.ShippingNoteInfo[]) shippingNoteInfos, new OnResultListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(String s, String s1) {

                    }
                });





//
//        //停止服务。context 必须为 activity。
//        LocationOpenApi.stop(this,
//                (com.hdgq.locationlib.entity.ShippingNoteInfo[]) shippingNoteInfos, new OnResultListener() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onFailure(String s, String s1) {
//
//                    }
//                });

    }



    private void initNavBar() {
        for (int i = 0, size = mDatas.size(); i < size; i++) {
            Tab tab = mDatas.get(i);
            int normalRes = getResources().getIdentifier(tab.getIcon() + "_selected", "drawable", getPackageName());
            int selectedRes = getResources().getIdentifier(tab.getIcon() + "_normal", "drawable", getPackageName());

            BottomTabItem item = new BottomTabItem(this, selectedRes, normalRes, tab.getTitle());
            mMainBottomTab.addItem(item);

            String title = mDatas.get(i).getTitle();
            try {
                Class cls = Class.forName(getPackageName() + tab.getPage());
                Method method = cls.getMethod("newInstance", new Class[]{String.class});
                mFragmentList.add((Fragment) method.invoke(null, new Object[]{title}));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }

        mMainBottomTab.setDatas(getSupportFragmentManager(), R.id.tabContainerLayout, mFragmentList);
        mMainBottomTab.selectTab(0);
//        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), mFragmentList));
//        mViewPager.addOnPageChangeListener(mPageChangeListener);

        //  new LBadgeView(mContext).setGravityOffset(0, true).bindTarget(mMainBottomTab.getItems().get(1).getBadgeTargetView()).setBadgeNumber(20);

    }

    private void getBadgeCount() {
        mPresenter.getBadgeCount();
    }

    //更新"业务"右上角角标
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(BusinessTabCountEvent event) {
        String count = event.getCount();
        new LBadgeView(mContext).setGravityOffset(0, true).bindTarget(mMainBottomTab.getItems().get(1).getBadgeTargetView())
                .setBadgeText(count);

        BadgeUtil.setBadge(this, count);
    }

    /**
     * 点击HomeFragment页面的找货或者发货 动态选中Tab
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkTab(TabCheckEvent event) {
        mMainBottomTab.selectTab(2);
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        option.setInterval(5 * 60 * 1000);
        mLocationClient.setLocationOption(option);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            double lon = aMapLocation.getLongitude();
            double lat = aMapLocation.getLatitude();

            Log.e(TAG, "lon=" + lon + " lat=" + lat);

            mPresenter.postLonLat(lon, lat);
        }
    }

    @Override
    protected void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRefreshSuccess(BaseEntity entity) {
        String count = entity.getCount();
        BadgeUtil.setBadge(this, count);
    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }

    long time = 0;

    //@Override
//    public void onBackPressed() {
//        if (System.currentTimeMillis() - time < 1500) {
////            super.onBackPressed();
//
//            //退出到登录界面
////            AccountUtil.clear(this);
//
////            Account account = new Account();
//            Account account = AccountUtil.getAccount(this);
//           // account.setFirst(false);
//           // account.setLogin(false);
//            AccountUtil.saveAccount(this, account);
//
//            Intent intent = new Intent(this, LoginsActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//
//        } else {
//            time = System.currentTimeMillis();
//            showToast("再按一次退出");
//
//        }
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - time > 1500) {//如果两次按键时间间隔大于1500毫秒，则不退出
                Toast.makeText(TabActivity.this, "再按一次退出程序...",
                        Toast.LENGTH_SHORT).show();
                time = secondTime;//更新firstTime
                return true;
            } else {
                //System.exit(0);//否则退出程序
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setTitle("温馨提示");
                builder.setMessage("您正在退出16飕云系统，确定要继续吗？");
                builder.setCancelable(true);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                         *  在这里实现你自己的逻辑
                         */
                           finish();
                    ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE);
                     am.restartPackage(getPackageName());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                         *  在这里实现你自己的逻辑
                         */
                        Intent intent = new Intent(TabActivity.this, TabActivity.class);
                        startActivity(intent);
                    }
                });
                builder.create().show();

            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSSEtting() {
        if (checkGpsIsOpen()) {
            // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this).setTitle("请打开定位权限")
                    .setMessage("打开位置信息")
                    //  取消选项
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(TabActivity.this, "close", Toast.LENGTH_SHORT).show();
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
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private void Driverpositioning() {

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("VehicleNumber", "5454545");
        hashMap.put("VehiclePlateColorCode", "5454545");
        hashMap.put("VehicleType", "5454545");
        hashMap.put("Owner", "5454545");
        hashMap.put("UseCharacter", "5454545");
        hashMap.put("VIN", "5454545");
        hashMap.put("IssuingOrganizations", "5454545");
        hashMap.put("RegisterDate", "5454545");
        hashMap.put("IssueDate", "5454545");
        hashMap.put("VehicleEnergyType", "5454545");
        hashMap.put("VehicleTonnage", "5454545");
        hashMap.put("GrossMass", "5454545");
        hashMap.put("RoadTransportCertificateNumber", "5454545");
        hashMap.put("TrailerVehiclePlateNumber", "5454545");
        hashMap.put("Remark", "5454545");
   RetrofitManager1 retrofitManager = RetrofitManager1.getInstance();
        retrofitManager.createReq(NetService.class)
                .Cljbxx(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Cljbxx>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Cljbxx cljbxx) {

                        Toast.makeText(TabActivity.this, "ok", Toast.LENGTH_SHORT).show();

                    }




                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(TabActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
