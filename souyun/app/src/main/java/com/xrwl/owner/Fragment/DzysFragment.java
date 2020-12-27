package com.xrwl.owner.Fragment;

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
import com.xrwl.owner.bean.MarkerBean;
import com.xrwl.owner.event.PublishClearCacheEvent;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.module.friend.ui.FriendActivity;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.dialog.CategoryDialog;
import com.xrwl.owner.module.publish.dialog.ProductDialog2;
import com.xrwl.owner.module.publish.map.SearchLocationActivity;
import com.xrwl.owner.module.publish.mvp.PublishContract;
import com.xrwl.owner.module.publish.mvp.PublishPresenter;
import com.xrwl.owner.module.publish.ui.AddressActivity;
import com.xrwl.owner.module.publish.ui.PublishConfirmActivity;
import com.xrwl.owner.module.publish.view.CompanyManageActivity;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.xrwl.owner.utils.AccountUtil;
import com.xrwl.owner.view.PhotoScrollView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DzysFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DzysFragment extends BaseEventFragment<PublishContract.IView, PublishPresenter> implements PublishContract.IView {

    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位
    public static final int RESULT_FRIEND_START = 444;//发货电话
    public static final int RESULT_FRIEND_END = 555;//收货人
    public static final int RESULT_CONPANY_START = 666;//发货单位
    public static final int RESULT_CONPANY_END = 777;//收货单位


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //发货定位
    @BindView(R.id.publishAddressDefaultStartLocationTv)
    TextView mpublishAddressDefaultStartLocationTv;
    @BindView(R.id.publishAddressDefaultStartLocationIv)
    ImageView mpublishAddressDefaultStartLocationIv;

    //发货单位，收货单位
    @BindView(R.id.publishCompanyTv)
    EditText mpublishCompanyTv;
    @BindView(R.id.publishCompanyshouhuoTv)
    EditText mpublishCompanyshouhuoTv;
    @BindView(R.id.publishCompanyIv)
    ImageView mpublishCompanyIv;
    @BindView(R.id.publishCompanyshouhuoIv)
    ImageView mpublishCompanyshouhuoIv;

    //货物吨数
    @BindView(R.id.ppDefaultWeightEt)
    EditText mppDefaultWeightEt;
    @BindView(R.id.ppDefaultAreaEt)
    EditText mppDefaultAreaEt;
    @BindView(R.id.jianDefaultWeightEt)
    EditText mjianDefaultWeightEt;

    //折损率,产品单价
    @BindView(R.id.yunxuzhesunlvEt)
    EditText myunxuzhesunlvEt;
    @BindView(R.id.chanpindanjiaEt)
    EditText mchanpindanjiaEt;

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

    public DzysFragment() {
        // Required empty public constructor
    }

    public static DzysFragment newInstance(String param1, String param2) {
        DzysFragment fragment = new DzysFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        isCreate = true;
    }

    @Override
    protected PublishPresenter initPresenter() {
        return new PublishPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dzys;
    }

    @Override
    protected void initView(View view) {
        mAccount = AccountUtil.getAccount(getActivity());
        mPublishBean = new PublishBean();
        mPublishBean.category = CategoryDialog.CategoryEnum.Type_Mineral.getValue();

        String usernamess = "";
        /**判断数据库都出来吨用户名*/
        if ("0".equals(mAccount.getName())) {
            usernamess = "";
        } else {
            if (mAccount.getName() == null) {
                usernamess = "";
            } else {
                try {
                    usernamess = (URLDecoder.decode(mAccount.getName(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }

        mpublishStartPhonepersonEt.setText(usernamess);
        mpublishStartPhoneEt.setText(mAccount.getPhone());

        mPhotoScrollView.setOnSelectListener(v -> PictureSelector.create(DzysFragment.this).openGallery(PictureMimeType.ofImage())
                .maxSelectNum(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .compress(true)
                .circleDimmedLayer(true)
                .forResult(PictureConfig.CHOOSE_REQUEST));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreate) {
            if (TextUtils.isEmpty(mpublishAddressDefaultStartLocationTv.getText().toString())) {
                MarkerBean bean = ((TabActivity) getActivity()).getMyLocation();
                if (bean != null) {
                    mpublishAddressDefaultStartLocationTv.setText(bean.getAddress());
                    mStartCity = bean.getCity();
                    mStartProvince = bean.getProvince();

                    mPublishBean.startDesc = bean.getAddress();
                    mPublishBean.startX = bean.getLon() + "";
                    mPublishBean.startY = bean.getLat() + "";

                    mPublishBean.defaultStartLon = bean.getLon();
                    mPublishBean.defaultStartLat = bean.getLat();

                    mPublishBean.longStartCityDes = mStartCity;
                    mPublishBean.longStartProvinceDes = mStartProvince;
                    mPublishBean.longStartAreaDes = bean.getAddress();

                    checkLongLocation();
                }
            }
            if (TextUtils.isEmpty(mpublishAddressDefaultEndLocationTv.getText().toString())) {
                MarkerBean bean = ((TabActivity) getActivity()).getDestination();
                if (bean != null) {
                    mpublishAddressDefaultEndLocationTv.setText(bean.getAddress());
                    mEndCity = bean.getCity();
                    mEndProvince = bean.getProvince();

                    mPublishBean.endDesc = bean.getAddress();
                    mPublishBean.endX = bean.getLon() + "";
                    mPublishBean.endY = bean.getLat() + "";

                    mPublishBean.defaultEndLon = bean.getLon();
                    mPublishBean.defaultEndLat = bean.getLat();

                    mPublishBean.longEndCityDes = mStartCity;
                    mPublishBean.longEndProvinceDes = mStartProvince;
                    mPublishBean.longEndAreaDes = bean.getAddress();

                    checkDefaultLocation();
                }
            }
        }
    }

    @OnClick({
            R.id.publishCompanyshouhuoIv, R.id.publishCompanyIv
    })
    public void CompanyClick(View v) {
        Intent intent = new Intent(getContext(), CompanyManageActivity.class);
        /**请选择发货单位*/
        if (v.getId() == R.id.publishCompanyshouhuoIv) {
            intent.putExtra("title", "请选择发货单位");
            startActivityForResult(intent, RESULT_CONPANY_START);
        }
        /**请选择收货单位*/
        if (v.getId() == R.id.publishCompanyIv) {
            intent.putExtra("title", "请选择收货单位");
            startActivityForResult(intent, RESULT_CONPANY_END);
        }
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
            R.id.publishProductTv
    })
    public void onProductClick(View v) {
        /**货物名称*/
        if (v.getId() == R.id.publishProductTv) {
            ProductDialog2 dialog = new ProductDialog2();
            dialog.setOnProductSelectListener(name -> {
                mpublishProductTv.setText(name);
            });
            dialog.show(Objects.requireNonNull(getFragmentManager()), "product");
        }
    }

    @OnClick(R.id.publishNextBtn)
    public void next() {
        mPublishBean.consignorName = mpublishStartPhonepersonEt.getText().toString();
        mPublishBean.consignorPhone = mpublishStartPhoneEt.getText().toString().replace("-", "").replace("+", "").replace(" ", "");
        mPublishBean.consigneeName = mpublishGetPersonEt.getText().toString();
        mPublishBean.consigneePhone = mpublishGetPhoneEt.getText().toString().replace("-", "").replace("+", "").replace(" ", "");
        mPublishBean.defaultNo = "0";

        mPublishBean.zhesunlv = myunxuzhesunlvEt.getText().toString();
        mPublishBean.danjia = mchanpindanjiaEt.getText().toString();
        mPublishBean.company = mpublishCompanyTv.getText().toString();
        mPublishBean.shouhuodanweiname = mpublishCompanyshouhuoTv.getText().toString();

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

            showToast("有选项没有填写");
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
            mPublishBean.longStartCityDes = mStartCity;
            mPublishBean.startDesc = title;
            requestCityLonLat();

            mDefaultStartLat = data.getDoubleExtra("lat", 0);
            mDefaultStartLon = data.getDoubleExtra("lon", 0);

            //设置发货定位
            mPublishBean.defaultStartLon = mDefaultStartLon;
            mPublishBean.defaultStartLat = mDefaultStartLat;

            mPublishBean.longStartProvinceDes = mStartProvince;
            mPublishBean.longStartAreaDes = title;

            checkLongLocation();

        }
        /**到货定位*/
        else if (requestCode == RESULT_POSITION_END) {
            String title = data.getStringExtra("title");
            mpublishAddressDefaultEndLocationTv.setText(title);

            mEndCity = data.getStringExtra("city");
            mEndProvince = data.getStringExtra("pro");
            mPublishBean.longEndCityDes = mEndCity;
            mPublishBean.endDesc = title;
            requestCityLonLat();

            mDefaultEndLat = data.getDoubleExtra("lat", 0);
            mDefaultEndLon = data.getDoubleExtra("lon", 0);
            mPublishBean.defaultEndLon = mDefaultEndLon;
            mPublishBean.defaultEndLat = mDefaultEndLat;
            mPublishBean.longEndProvinceDes = mEndProvince;
            mPublishBean.defaultEndPosDes = title;
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
        /**发货单位*/
        else if(requestCode == RESULT_CONPANY_START){
            String name = data.getStringExtra("name");
            mpublishCompanyTv.setText(name);
        }
        /**收货单位*/
        else if(requestCode == RESULT_CONPANY_END){
            String name = data.getStringExtra("name");
            mpublishCompanyshouhuoTv.setText(name);
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

    /**
     * 计算城市的经纬度
     */
    private void requestCityLonLat() {
        if (!TextUtils.isEmpty(mStartCity) && !TextUtils.isEmpty(mEndCity)) {
            mPresenter.requestCityLonLat(mStartCity, mEndCity);
        }
    }

    /**
     * 正在请求关键数据
     */
    private void checkLongLocation() {
        if (mStartCity != null && mEndCity != null) {
            mGetDistanceDialog = LoadingProgress.showProgress(mContext, "正在请求关键数据...");
            mPresenter.calculateDistanceWithCityName(mStartCity, mEndCity, mStartProvince, mEndProvince);
        }
    }

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
        Integer d =entity.getData();
        mPublishBean.nidaye  =d.toString();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<Distance> entity) {
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
        mGetDistanceDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        mGetDistanceDialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(PublishClearCacheEvent event) {
        mPublishBean = null;
        mPublishBean = new PublishBean();
        mDefaultStartLat = 0;
        mDefaultStartLon = 0;
        mDefaultEndLat = 0;
        mDefaultEndLon = 0;

        mpublishAddressDefaultStartLocationTv.setText("");
        mpublishCompanyTv.setText("");
        mpublishCompanyshouhuoTv.setText("");
        mppDefaultWeightEt.setText("");
        mppDefaultAreaEt.setText("");
        mjianDefaultWeightEt.setText("");
        myunxuzhesunlvEt.setText("");
        mchanpindanjiaEt.setText("");
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