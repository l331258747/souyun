package com.xrwl.owner.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.listener.OnResultListener;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseEventFragment;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.Distance;
import com.xrwl.owner.bean.HomeChexingBean;
import com.xrwl.owner.bean.HomeHuowuBean;
import com.xrwl.owner.bean.MarkerBean;
import com.xrwl.owner.event.PublishClearCacheEvent;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.module.friend.ui.FriendActivity;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.dialog.CategoryDialog;
import com.xrwl.owner.module.publish.map.SearchLocationActivity;
import com.xrwl.owner.module.publish.mvp.PublishContract;
import com.xrwl.owner.module.publish.mvp.PublishPresenter;
import com.xrwl.owner.module.publish.ui.AddressActivity;
import com.xrwl.owner.module.publish.ui.CargoActivity;
import com.xrwl.owner.module.publish.ui.PublishConfirmActivity;
import com.xrwl.owner.module.publish.ui.TruckActivity;
import com.xrwl.owner.utils.AccountUtil;
import com.xrwl.owner.view.PhotoScrollView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CtldFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CtldFragment extends BaseEventFragment<PublishContract.IView, PublishPresenter> implements PublishContract.IView {

    public static final int RESULT_TRUCK = 111;//已选车型
    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位
    public static final int RESULT_FRIEND_START = 444;//发货电话
    public static final int RESULT_FRIEND_END = 555;//收货人
    public static final int RESULT_CARGO = 888;//货物名称


//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;

//    //已选车型
//    @BindView(R.id.publishTruckTv)
//    TextView mpublishTruckTv;

    //发货定位
    @BindView(R.id.publishAddressDefaultStartLocationTv)
    TextView mpublishAddressDefaultStartLocationTv;
    @BindView(R.id.publishAddressDefaultStartLocationIv)
    ImageView mpublishAddressDefaultStartLocationIv;

    //货物吨数
    @BindView(R.id.ppDefaultWeightEt)
    EditText mppDefaultWeightEt;
    @BindView(R.id.ppDefaultAreaEt)
    EditText mppDefaultAreaEt;
    @BindView(R.id.jianDefaultWeightEt)
    EditText mjianDefaultWeightEt;

    //发货姓名，电话
    @BindView(R.id.publishStartPhonepersonEt)
    EditText mpublishStartPhonepersonEt;
    @BindView(R.id.publishStartPhonepersonIv)
    ImageView mpublishStartPhonepersonIv;
    @BindView(R.id.publishStartPhoneEt)
    EditText mpublishStartPhoneEt;
    @BindView(R.id.publishStartPhoneIv)
    ImageView mpublishStartPhoneIv;

    //到货定位
    @BindView(R.id.publishAddressDefaultEndLocationTv)
    TextView mpublishAddressDefaultEndLocationTv;
    @BindView(R.id.publishAddressDefaultEndLocationIv)
    ImageView mpublishAddressDefaultEndLocationIv;

    //收货人姓名，电话
    @BindView(R.id.publishGetPersonEt)
    EditText mpublishGetPersonEt;
    @BindView(R.id.publishGetPersonIv)
    ImageView mpublishGetPersonIv;
    @BindView(R.id.publishGetPhoneEt)
    EditText mpublishGetPhoneEt;
    @BindView(R.id.publishGetPhoneIv)
    ImageView mpublishGetPhoneIv;

    //货物名称
    @BindView(R.id.publishProductTv)
    TextView mpublishProductTv;

    //备注
    @BindView(R.id.add_content)
    EditText madd_content;

    @BindView(R.id.photo_scrollview)
    PhotoScrollView mPhotoScrollView;

    protected Account mAccount;
    boolean isCreate;
    private ArrayList<String> mImagePaths;

    private PublishBean mPublishBean;
    private String mEndCity;
    private String mEndProvince;
    private String mStartCity;
    private String mStartProvince;
    private double mDefaultStartLat, mDefaultStartLon;
    private double mDefaultEndLat, mDefaultEndLon;

    private ProgressDialog mGetDistanceDialog;

    private Distance d;
    public String shijianlo;
    public String julilo;

    public MarkerBean myLocation;//出发地
    public MarkerBean destination;//目的地
    public HomeChexingBean chexing;
    public HomeHuowuBean huowu;

    public CtldFragment() {
        // Required empty public constructor
    }

//    public static CtldFragment newInstance(String param1, String param2) {
//        CtldFragment fragment = new CtldFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static CtldFragment newInstance(MarkerBean myLocation,MarkerBean destination,HomeChexingBean chexing,HomeHuowuBean huowu) {
        CtldFragment fragment = new CtldFragment();
        Bundle args = new Bundle();
        args.putSerializable("myLocation",myLocation);
        args.putSerializable("destination",destination);
        args.putSerializable("chexing",chexing);
        args.putSerializable("huowu",huowu);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        isCreate = true;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myLocation = (MarkerBean) getArguments().getSerializable("myLocation");
            destination = (MarkerBean) getArguments().getSerializable("destination");
            chexing = (HomeChexingBean) getArguments().getSerializable("chexing");
            huowu = (HomeHuowuBean) getArguments().getSerializable("huowu");
        }
        isCreate = true;
    }

    @Override
    protected PublishPresenter initPresenter() {
        return new PublishPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ctld;
    }

    @Override
    protected void initView(View view) {
        mAccount = AccountUtil.getAccount(getActivity());
        mPublishBean = new PublishBean();
        mPublishBean.category = CategoryDialog.CategoryEnum.TYPE_LONG_ZERO.getValue();

        mpublishStartPhonepersonEt.setText(mAccount.getNameDecode());
        mpublishStartPhoneEt.setText(mAccount.getPhone());

        mPhotoScrollView.setOnSelectListener(v -> PictureSelector.create(CtldFragment.this).openGallery(PictureMimeType.ofImage())
                .maxSelectNum(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .compress(true)
                .circleDimmedLayer(true)
                .forResult(PictureConfig.CHOOSE_REQUEST));

        setDefaultData();
    }

    public void setDefaultData() {
            //发货定位
            {
                MarkerBean bean = myLocation;
                if (bean != null) {
                    if(!TextUtils.isEmpty(bean.getName())){
                        mpublishStartPhonepersonEt.setText(bean.getName());
                    }
                    if(!TextUtils.isEmpty(bean.getTel())){
                        mpublishStartPhoneEt.setText(bean.getTel());
                    }

                    if(!TextUtils.isEmpty(bean.getAddress())){
                        mpublishAddressDefaultStartLocationTv.setText(bean.getAddress());

                        mStartCity = bean.getCity();
                        mStartProvince = bean.getProvince();

                        mDefaultStartLat = bean.getLat();
                        mDefaultStartLon = bean.getLon();

                        mPublishBean.startDesc = bean.getAddress();
                        mPublishBean.startX = mDefaultStartLon + "";
                        mPublishBean.startY = mDefaultStartLat + "";

                        mPublishBean.defaultStartLon = mDefaultStartLon;
                        mPublishBean.defaultStartLat = mDefaultStartLat;
                        mPublishBean.defaultStartPosDes = bean.getAddress();

                        mPublishBean.longStartCityDes = mStartCity;
                        mPublishBean.longStartProvinceDes = mStartProvince;
                        mPublishBean.longStartAreaDes = bean.getAddress();
                    }
                }
            }
            //到货定位
            {
                MarkerBean bean = destination;
                if (bean != null) {

                    if(!TextUtils.isEmpty(bean.getName())){
                        mpublishGetPersonEt.setText(bean.getName());
                    }
                    if(!TextUtils.isEmpty(bean.getTel())){
                        mpublishGetPhoneEt.setText(bean.getTel());
                    }

                    if(!TextUtils.isEmpty(bean.getAddress())){
                        mpublishAddressDefaultEndLocationTv.setText(bean.getAddress());

                        mEndCity = bean.getCity();
                        mEndProvince = bean.getProvince();

                        mDefaultEndLat = bean.getLat();
                        mDefaultEndLon = bean.getLon();

                        mPublishBean.endDesc = bean.getAddress();
                        mPublishBean.endX = mDefaultEndLon + "";
                        mPublishBean.endY = mDefaultEndLat + "";

                        mPublishBean.defaultEndLon = mDefaultEndLon;
                        mPublishBean.defaultEndLat = mDefaultEndLat;
                        mPublishBean.defaultEndPosDes = bean.getAddress();

                        mPublishBean.longEndCityDes = mEndCity;
                        mPublishBean.longEndProvinceDes = mEndProvince;
                        mPublishBean.longEndAreaDes = bean.getAddress();
                    }
                }
            }
            //吨
            {
                HomeHuowuBean bean = huowu;
                if (bean != null) {
                    if(!TextUtils.isEmpty(bean.getDun())){
                        mppDefaultWeightEt.setText(bean.getDun());
                    }
                }
            }
            //方
            {
                HomeHuowuBean bean = huowu;
                if (bean != null) {
                    if(!TextUtils.isEmpty(bean.getFang())){
                        mppDefaultAreaEt.setText(bean.getFang());
                    }
                }
            }
            //件
            {
                HomeHuowuBean bean = huowu;
                if (bean != null) {
                    if(!TextUtils.isEmpty(bean.getJian())){
                        mjianDefaultWeightEt.setText(bean.getJian());
                    }
                }
            }
            checkDefaultLocation();
        }

    @OnClick(R.id.publishTruckLayout)
    public void truckClick() {
        Intent intent = new Intent(mContext, TruckActivity.class);
        intent.putExtra("categoty", "2");
        intent.putExtra("title", getString(R.string.publish_category_longzero));
        startActivityForResult(intent, RESULT_TRUCK);
    }

    @OnClick({
            R.id.publishAddressDefaultStartLocationTv, R.id.publishAddressDefaultEndLocationTv,
            R.id.publishAddressDefaultStartLocationIv, R.id.publishAddressDefaultEndLocationIv
    })
    public void defaultLocationClick(View v) {
        Intent intent = new Intent(getContext(), SearchLocationActivity.class);
        /**请选择发货位置*/
        if (v.getId() == R.id.publishAddressDefaultStartLocationTv) {
            intent.putExtra("title", "请选择发货位置");
            startActivityForResult(intent, RESULT_POSITION_START);
        }
        /**请选择到货位置*/
        else if (v.getId() == R.id.publishAddressDefaultEndLocationTv) {
            intent.putExtra("title", "请选择到货位置");
            startActivityForResult(intent, RESULT_POSITION_END);
        }
        /**请选择发货定位*/
        else if (v.getId() == R.id.publishAddressDefaultStartLocationIv) {
            startActivityForResult(new Intent(getContext(), AddressActivity.class), RESULT_POSITION_START);
        }
        /**请选择到货定位*/
        else if (v.getId() == R.id.publishAddressDefaultEndLocationIv) {
            startActivityForResult(new Intent(getContext(), AddressActivity.class), RESULT_POSITION_END);
        }
    }

    @OnClick({
            R.id.publishStartPhonepersonIv, R.id.publishStartPhoneIv,
            R.id.publishGetPersonIv, R.id.publishGetPhoneIv
    })
    public void onClick(View v) {
        /**发货人*/
        if (v.getId() == R.id.publishStartPhonepersonIv || v.getId() == R.id.publishStartPhoneIv) {
            Intent intent = new Intent(getContext(), FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_START);
        }
        /**收货人*/
        else if (v.getId() == R.id.publishGetPersonIv || v.getId() == R.id.publishGetPhoneIv) {
            Intent intent = new Intent(getContext(), FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_END);
        }
    }

    @OnClick({
            R.id.publishProductTv,R.id.publishProductIv
    })
    public void onProductClick(View v) {
        /**货物名称*/
        if (v.getId() == R.id.publishProductTv) {
            Intent intent = new Intent(getContext(), CargoActivity.class);
            startActivityForResult(intent, RESULT_CARGO);
        }
    }

    public boolean dfjCheck(){
        if (!TextUtils.isEmpty(mppDefaultWeightEt.getText().toString()) && !mppDefaultWeightEt.getText().toString().equals("0")) {
            return true;
        }
        if (!TextUtils.isEmpty(mppDefaultAreaEt.getText().toString()) && !mppDefaultAreaEt.getText().toString().equals("0")) {
            return true;
        }
        if (!TextUtils.isEmpty(mjianDefaultWeightEt.getText().toString()) && !mjianDefaultWeightEt.getText().toString().equals("0")) {
            return true;
        }
        return false;
    }

    @OnClick(R.id.publishNextBtn)
    public void next() {
        mPublishBean.consignorName = mpublishStartPhonepersonEt.getText().toString();
        mPublishBean.consignorPhone = mpublishStartPhoneEt.getText().toString().replace("-", "").replace("+", "").replace(" ", "");
        mPublishBean.consigneeName = mpublishGetPersonEt.getText().toString();
        mPublishBean.consigneePhone = mpublishGetPhoneEt.getText().toString().replace("-", "").replace("+", "").replace(" ", "");
        mPublishBean.defaultNo = "0";

        if(!dfjCheck()){
            new AlertDialog.Builder(getActivity()).setMessage("请输入货物吨方件至少一项")
                    .setPositiveButton("确定", null).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
        params.put("type", "4");
        params.put("time", mPublishBean.duration);
        params.put("distance", mPublishBean.distance);
        if (TextUtils.isEmpty(mppDefaultWeightEt.getText().toString())) {
            params.put("ton", "0");
        } else {
            params.put("ton", mppDefaultWeightEt.getText().toString());
        }
        if (TextUtils.isEmpty(mppDefaultAreaEt.getText().toString())) {
            params.put("square", "0");
        } else {
            params.put("square", mppDefaultAreaEt.getText().toString());
        }
        if (TextUtils.isEmpty(mjianDefaultWeightEt.getText().toString())) {
            params.put("piece", "0");
        } else {
            params.put("piece", mjianDefaultWeightEt.getText().toString());
        }
//        mPresenter.calculateDistancecount(params);

        mPublishBean.imagePaths = mImagePaths;
        mPublishBean.defaultNum = mjianDefaultWeightEt.getText().toString();
        mPublishBean.defaultWeight = mppDefaultWeightEt.getText().toString();
        mPublishBean.defaultArea = mppDefaultAreaEt.getText().toString();
        mPublishBean.remark = madd_content.getText().toString();
        mPublishBean.productName = mpublishProductTv.getText().toString();

        if (mPublishBean.check()) {

            ShippingNoteInfo shippingNoteInfo = new ShippingNoteInfo();
            shippingNoteInfo.setShippingNoteNumber("123");
            shippingNoteInfo.setSerialNumber("123");
            shippingNoteInfo.setStartCountrySubdivisionCode("123");
            shippingNoteInfo.setEndCountrySubdivisionCode("123");

            shippingNoteInfo.setSendCount(Integer.parseInt("123"));
            shippingNoteInfo.setAlreadySendCount(Integer.parseInt("123"));
            ShippingNoteInfo[] shippingNoteInfos = new ShippingNoteInfo[1];
            int s = shippingNoteInfos.length;
            shippingNoteInfos[0] = shippingNoteInfo;
            //启用服务。context 必须为 activity。

            LocationOpenApi.start(getContext(), shippingNoteInfos, new OnResultListener() {
                @Override
                public void onSuccess() {
                    // Toast.makeText(mContext, "成功了", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String s, String s1) {
                    // Toast.makeText(mContext, s1.toString(), Toast.LENGTH_SHORT).show();
                }
            });


            Intent intent = new Intent(mContext, PublishConfirmActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) mPublishBean);


            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            new AlertDialog.Builder(getActivity()).setMessage("有选项没有填写")
                    .setPositiveButton("确定", null).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        /**本地图片上传*/
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            mImagePaths = new ArrayList<>();
            for (LocalMedia lm : selectList) {
                if (lm.isCompressed()) {
                    mImagePaths.add(lm.getCompressPath());
                } else {
                    mImagePaths.add(lm.getPath());
                }
            }
            mPhotoScrollView.setActivity(getActivity());
            mPhotoScrollView.setDatas(mImagePaths, selectList);
        }
        /**发货定位*/
        else if (requestCode == RESULT_POSITION_START) {
            String title = data.getStringExtra("title");
            mpublishAddressDefaultStartLocationTv.setText(title);

            mStartCity = data.getStringExtra("city");
            mStartProvince = data.getStringExtra("pro");

            if(!TextUtils.isEmpty(data.getStringExtra("userName"))){
                mpublishStartPhonepersonEt.setText(data.getStringExtra("userName"));
            }

            if(!TextUtils.isEmpty(data.getStringExtra("tel"))){
                mpublishStartPhoneEt.setText(data.getStringExtra("tel"));
            }

            mPublishBean.startDesc = title;

            mDefaultStartLat = data.getDoubleExtra("lat", 0);
            mDefaultStartLon = data.getDoubleExtra("lon", 0);

            mPublishBean.startX = mDefaultStartLon + "";
            mPublishBean.startY = mDefaultStartLat + "";

            //设置发货定位
            mPublishBean.defaultStartLon = mDefaultStartLon;
            mPublishBean.defaultStartLat = mDefaultStartLat;
            mPublishBean.defaultStartPosDes = title;

            mPublishBean.longStartCityDes = mStartCity;
            mPublishBean.longStartProvinceDes = mStartProvince;
            mPublishBean.longStartAreaDes = title;

            checkDefaultLocation();

        }
        /**到货定位*/
        else if (requestCode == RESULT_POSITION_END) {
            String title = data.getStringExtra("title");
            mpublishAddressDefaultEndLocationTv.setText(title);

            mEndCity = data.getStringExtra("city");
            mEndProvince = data.getStringExtra("pro");

            if(!TextUtils.isEmpty(data.getStringExtra("userName"))){
                mpublishGetPersonEt.setText(data.getStringExtra("userName"));
            }

            if(!TextUtils.isEmpty(data.getStringExtra("tel"))){
                mpublishGetPhoneEt.setText(data.getStringExtra("tel"));
            }

            mPublishBean.endDesc = title;

            mDefaultEndLat = data.getDoubleExtra("lat", 0);
            mDefaultEndLon = data.getDoubleExtra("lon", 0);

            mPublishBean.endX = mDefaultEndLon + "";
            mPublishBean.endY = mDefaultEndLat + "";

            mPublishBean.defaultEndLon = mDefaultEndLon;
            mPublishBean.defaultEndLat = mDefaultEndLat;
            mPublishBean.defaultEndPosDes = title;

            mPublishBean.longEndProvinceDes = mEndProvince;
            mPublishBean.longEndCityDes = mEndCity;
            mPublishBean.longEndAreaDes = title;

            checkDefaultLocation();
        }
        /**发货电话*/
        else if (requestCode == RESULT_FRIEND_START) {
            Friend friend = (Friend) data.getSerializableExtra("data");
            mpublishStartPhonepersonEt.setText(friend.getName());
            mpublishStartPhoneEt.setText(friend.getPhone().replace("-", "").replace("+", "").replace(" ", ""));
        }
        /**收货人*/
        else if (requestCode == RESULT_FRIEND_END) {
            Friend friend = (Friend) data.getSerializableExtra("data");
            mpublishGetPersonEt.setText(friend.getName());
            mpublishGetPhoneEt.setText(friend.getPhone().replace("-", "").replace("+", "").replace(" ", ""));
        }
        /**货物名称*/
        else if(requestCode == RESULT_CARGO){
            String name = data.getStringExtra("name");
            mpublishProductTv.setText(name);
        }
    }

    private void checkDefaultLocation() {
        if (mDefaultStartLat != 0 && mDefaultStartLon != 0
                && mDefaultEndLat != 0 && mDefaultEndLon != 0) {
            mGetDistanceDialog = LoadingProgress.showProgress(mContext, "正在请求关键数据...");
            //发起请求以计算两点间最短距离
            mPresenter.calculateDistanceWithLonLat(mDefaultStartLon, mDefaultStartLat, mDefaultEndLon, mDefaultEndLat);
        }
    }

//    /**
//     * 计算城市的经纬度
//     */
//    private void requestCityLonLat() {
//        if (!TextUtils.isEmpty(mStartCity) && !TextUtils.isEmpty(mEndCity)) {
//            mPresenter.requestCityLonLat(mStartCity, mEndCity);
//        }
//    }

//    /**
//     * 正在请求关键数据
//     */
//    private void checkLongLocation() {
//        if (mStartCity != null && mEndCity != null) {
//            mGetDistanceDialog = LoadingProgress.showProgress(mContext, "正在请求关键数据...");
//            mPresenter.calculateDistanceWithCityName(mStartCity, mEndCity, mStartProvince, mEndProvince);
//        }
//    }

    @Override
    public void onRequestCityLatLonSuccess(BaseEntity<Distance> entity) {
        Distance d = entity.getData();
        mPublishBean.startX = d.startX;
        mPublishBean.startY = d.startY;
        mPublishBean.endX = d.endX;
        mPublishBean.endY = d.endY;
    }

    @Override
    public void onRequestSuccessa(BaseEntity<Integer> entity) {
        if(entity.getData() == null)
            entity.setData(0);
        Integer d =entity.getData();
        mPublishBean.nidaye  =d.toString();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<Distance> entity) {
        if(mGetDistanceDialog != null && mGetDistanceDialog.isShowing())
            mGetDistanceDialog.dismiss();
        d = entity.getData();

        mPublishBean.distance = d.distance;
        mPublishBean.duration = d.duration;
        mPublishBean.end = d.end;
        mPublishBean.start = d.start;
        mPublishBean.singleTonPrice = d.ton;
        mPublishBean.singleAreaPrice = d.square;
        julilo = d.distance;
        shijianlo = d.duration.replace("小时", "");
    }

    @Override
    public void onRefreshError(Throwable e) {
        if(mGetDistanceDialog != null && mGetDistanceDialog.isShowing())
            mGetDistanceDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if(mGetDistanceDialog != null && mGetDistanceDialog.isShowing())
            mGetDistanceDialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(PublishClearCacheEvent event) {
        mPublishBean = null;
        mPublishBean = new PublishBean();
        mPublishBean.category = CategoryDialog.CategoryEnum.TYPE_LONG_ZERO.getValue();
        mDefaultStartLat = 0;
        mDefaultStartLon = 0;
        mDefaultEndLat = 0;
        mDefaultEndLon = 0;

        mpublishAddressDefaultStartLocationTv.setText("");
        mppDefaultWeightEt.setText("");
        mppDefaultAreaEt.setText("");
        mjianDefaultWeightEt.setText("");
        mpublishStartPhonepersonEt.setText("");
        mpublishStartPhoneEt.setText("");
        mpublishAddressDefaultEndLocationTv.setText("");
        mpublishGetPersonEt.setText("");
        mpublishGetPhoneEt.setText("");
        mpublishProductTv.setText("");
        madd_content.setText("");

        mPhotoScrollView.setDatas(null, null);
        mImagePaths = null;
    }
}