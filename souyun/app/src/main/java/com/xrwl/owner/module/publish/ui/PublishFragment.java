package com.xrwl.owner.module.publish.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.location.AMapLocationClient;
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
import com.xrwl.owner.bean.Address;
import com.xrwl.owner.bean.Distance;
import com.xrwl.owner.event.PublishClearCacheEvent;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.module.friend.ui.FriendActivity;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.bean.Truck;
import com.xrwl.owner.module.publish.dialog.CategoryDialog;
import com.xrwl.owner.module.publish.dialog.ProductDialog;
import com.xrwl.owner.module.publish.map.SearchLocationActivity;
import com.xrwl.owner.module.publish.mvp.PublishContract;
import com.xrwl.owner.module.publish.mvp.PublishPresenter;
import com.xrwl.owner.module.publish.view.AreaSpinnerView;
import com.xrwl.owner.module.publish.view.PublishProductLongView;
import com.xrwl.owner.view.PhotoScrollView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 发布货源
 * Created by www.longdw.com on 2018/3/25 下午11:43.
 */

public class PublishFragment extends BaseEventFragment<PublishContract.IView, PublishPresenter> implements PublishContract.IView {
    public static String fanhuijiagelo;
    protected static String chexingid;
    public static final int RESULT_TRUCK = 111;//已选车型
    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位
    public static final int RESULT_FRIEND_START_PHONE = 444;//发货电话
    public static final int RESULT_FRIEND_GET_PERSON = 555;//收货人
    public static final int RESULT_FRIEND_GET_PHONE = 666;//收货电话
    public String shijianlo;
    public String julilo;
    private CategoryDialog.CategoryEnum mCategory;
    private String name;
    private ArrayList<String> mImagePaths;
    private PublishBean mPublishBean;
    private ProgressDialog mGetDistanceDialog;
    private String mEndCity;
    private String mEndProvince;
    private String mStartCity;
    private String mStartProvince;
    private String mHuowuweizhi = "0";
    private String appjianshu = "0";
    private String appdun = "0";
    private String appfang = "0";
    private String gouzao = "0";
    private String carnametype;
    private String string;
    private double mDefaultStartLat, mDefaultStartLon;
    private double mDefaultEndLat, mDefaultEndLon;
    private Address mStartCityAddress, mEndCityAddress, mStartPro, mEndPro;
    private AMapLocationClient mLocationClient;


    public String all_price;
    @BindView(R.id.publishCategoryTv)
    TextView mCategoryTv;//配送类型
    @BindView(R.id.publishTruckTv)
    TextView mTruckTv;
    @BindView(R.id.publishTruckLayout)
    View mTruckLayout;
    @BindView(R.id.truckLine)
    View mTruckLineLayout;
    @BindView(R.id.buzhidaoly)
    View mbuzhidaolayout;
    @BindView(R.id.publishProductTv)
    TextView mProductTv;
    @BindView(R.id.publishTimeTv)
    TextView mTimeTv;
    @BindView(R.id.publishProductDefaultLayout)
    View mProductDefaultLayout;
    //长途状态下货物重量体积
    @BindView(R.id.publishProductLongTotalLayout)
    PublishProductLongView mProductLongTotalLayout;
    //    @BindView(R.id.publishAddressDefaultLayout)
//    View mAddressDefaultLayout;
    @BindView(R.id.publishAddressDefaultStartLocationTv)
    TextView mDefaultStartLocationTv;
    @BindView(R.id.publishAddressDefaultEndLocationTv)
    TextView mDefaultEndLocationTv;
    @BindView(R.id.dunfangCb)
    CheckBox mdunfangcb;
    @BindView(R.id.jianCb)
    CheckBox mjiancb;
    @BindView(R.id.NoCb)
    CheckBox mNocb;
    @BindView(R.id.jianDefaultWeightEt)
    EditText mjianEt;
    //允许这损率
    @BindView(R.id.yunxuzhesunlvEt)
    EditText myunxuzhesunlvEt;
    //允许这损率单价
    @BindView(R.id.chanpindanjiaEt)
    EditText mchanpindanjiaEt;
    /**
     * 同城配送状态下的 吨 方  件
     */
    @BindView(R.id.ppDefaultWeightEt)
    EditText mDefaultWeightEt;
    @BindView(R.id.ppDefaultAreaEt)
    EditText mDefaultAreaEt;
    @BindView(R.id.quxiaojianTv)
    TextView mkuangchajianquxiao;
    @BindView(R.id.huowudunshu)
    TextView mhuowudunshu;
    @BindView(R.id.publishStartPhoneEt)
    EditText mStartPhoneEt;//发货电话
    @BindView(R.id.publishGetPersonEt)
    EditText mGetPersonEt;//收货人
    @BindView(R.id.publishGetPhoneEt)
    EditText mGetPhoneEt;//收货电话
    @BindView(R.id.kchuowubaozhuang)
    LinearLayout mkchuowubaozhuang;
    /**
     * 长途状态下的发货和收货地址
     */
    @BindView(R.id.publishAddressLongTotalLayout)
    View mAddressLongTotalLayout;
    @BindView(R.id.paLongStartSpinnerLayout)
    AreaSpinnerView mStartSpinnerView;
    @BindView(R.id.paLongEndSpinnerLayout)
    AreaSpinnerView mEndSpinnerView;
    @BindView(R.id.photo_scrollview)
    PhotoScrollView mPhotoScrollView;
    //判断在同城专车的情况下吨方件隐藏
    @BindView(R.id.dunfangjianshuru)
    View mdunfangjianly;
    @BindView(R.id.yincangxian)
    View myincangxian;
    @BindView(R.id.yincangxianer)
    View myincangxianer;
    //货物折损率及单价及厂家名称
    @BindView(R.id.huowuzhesunlv)
    LinearLayout mhuowuzhesunlv;
    @BindView(R.id.chanpindanjia)
    LinearLayout mchanpindanjia;
    //厂家名称
    @BindView(R.id.changjia)
    LinearLayout mchangjia;
    @BindView(R.id.publishCompanyTv)
    EditText mpublishCompanyTv;
    //收货单位
    @BindView(R.id.shouhuodanwei)
    LinearLayout mshouhuodanwei;
    @BindView(R.id.publishCompanyshouhuoTv)
    EditText mpublishCompanyshouhuoTv;

    @BindView(R.id.publishgoodspacking)
    Spinner mGoodspackingEt;
    //发货人姓名
    @BindView(R.id.publishStartPhonepersonEt)
    EditText mpublishStartPhonepersonEt;
    //发货人姓名
    @BindView(R.id.zhengtidianji)
    ScrollView mzhengtidianji;
    private Distance d;

    public static PublishFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        PublishFragment fragment = new PublishFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PublishPresenter initPresenter() {
        return new PublishPresenter(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.publish_fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void initView(final View view) {
        fanhuijiagelo = "0";
        setName(name); //初始化全局变量
        setGouzao(gouzao);
        //吨的焦点
        mDefaultWeightEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    // 获得焦点
                    mDefaultWeightEt.setText("");


                } else {
                }
            }
        });
        //监听数字输入为0显示0.
        mDefaultWeightEt.addTextChangedListener(new TextWatcher() {   // 这是主要方法，下面为一些处理
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.toString().equals("0")) {
                    mDefaultWeightEt.setText("0.");
                    mDefaultWeightEt.setSelection(mDefaultWeightEt.getText().length());
                    mDefaultWeightEt.removeTextChangedListener(this);

                }


            }
        });
        //方的焦点
        mDefaultAreaEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    // 获得焦点
                    mDefaultAreaEt.setText("");


                } else {

                    // 失去焦点

                }

            }
        });
        //监听数字输入为0显示0.
        mDefaultAreaEt.addTextChangedListener(new TextWatcher() {   // 这是主要方法，下面为一些处理
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.toString().equals("0")) {
                    mDefaultAreaEt.setText("0.");
                    mDefaultAreaEt.setSelection(mDefaultAreaEt.getText().length());
                    mDefaultAreaEt.removeTextChangedListener(this);

                }


            }
        });
        //件的焦点
        mjianEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    // 获得焦点
                    mjianEt.setText("");


                } else {

                    // 失去焦点

                }

            }
        });
        //监听数字输入为0显示0.
        mjianEt.addTextChangedListener(new TextWatcher() {   // 这是主要方法，下面为一些处理
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.toString().equals("0")) {
                    mjianEt.setText("0.");
                    mjianEt.setSelection(mjianEt.getText().length());
                    mjianEt.removeTextChangedListener(this);

                }


            }
        });
        if ("选择配送类型".equals(mCategoryTv.getText().toString())) {
            categoryClick();
        }
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
        mPublishBean = new PublishBean();
        mPhotoScrollView.setOnSelectListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(PublishFragment.this).openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .previewImage(true)
                        .isCamera(true)
                        .compress(true)
                        .circleDimmedLayer(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });

        mStartSpinnerView.init(getFragmentManager());
        mStartSpinnerView.setOnAreaSpinnerSelectListener(new AreaSpinnerView.OnAreaSpinnerSelectListener() {
            @Override
            public void onProSelect(Address pro) {
                mStartPro = pro;
            }

            @Override
            public void onCitySelect(Address city) {
                //起点城市
                mStartCityAddress = city;
                checkLongLocation1();
            }
        });
        mEndSpinnerView.init(getFragmentManager());
        mEndSpinnerView.setOnAreaSpinnerSelectListener(new AreaSpinnerView.OnAreaSpinnerSelectListener() {
            @Override
            public void onProSelect(Address pro) {
                mEndPro = pro;
            }

            @Override
            public void onCitySelect(Address city) {
                //终点城市
                mEndCityAddress = city;
                checkLongLocation1();
            }
        });
        mpublishStartPhonepersonEt.setText(usernamess);
        mStartPhoneEt.setText(mAccount.getPhone());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        mTimeTv.setText(simpleDateFormat.format(date));
        mhuowuzhesunlv.setVisibility(View.GONE);
        mchanpindanjia.setVisibility(View.GONE);
        mchangjia.setVisibility(View.GONE);
        mshouhuodanwei.setVisibility(View.GONE);
        mhuowudunshu.setVisibility(View.GONE);
        mdunfangcb.setChecked(true);
        mDefaultWeightEt.setEnabled(true);
        mDefaultAreaEt.setEnabled(true);
        mDefaultWeightEt.setText("");
        mDefaultAreaEt.setText("");
        mjiancb.setChecked(false);
        mNocb.setChecked(false);
    }

    @OnClick(R.id.publishCategoryLayout)
    public void categoryClick() {
        CategoryDialog dialog = new CategoryDialog();
        dialog.mListener = new CategoryDialog.OnCategoryCallbackListener() {
            @Override
            public void onCategoryCallback(CategoryDialog.CategoryEnum category, String des) {
                mCategory = category;
                mCategoryTv.setText(des);
                mPublishBean.category = mCategory.getValue();
                /**跑腿---很简单的输入*/
                if (mCategory == CategoryDialog.CategoryEnum.TYPE_paotui) {
                    mchangjia.setVisibility(View.GONE);
                    mshouhuodanwei.setVisibility(View.GONE);
                    mdunfangcb.setVisibility(View.GONE);
                    mhuowudunshu.setVisibility(View.GONE);
                    mkchuowubaozhuang.setVisibility(View.GONE);
                    mTruckLayout.setVisibility(View.GONE);//隐藏
                    mTruckLineLayout.setVisibility(View.GONE);
                    //目前要开通长途零担和整车的吨方件
                    mProductDefaultLayout.setVisibility(View.GONE);
                    myincangxian.setVisibility(View.GONE);
                    myincangxianer.setVisibility(View.GONE);
                    mbuzhidaolayout.setVisibility(View.GONE);
                    mkuangchajianquxiao.setVisibility(View.GONE);
                    mDefaultAreaEt.setVisibility(View.GONE);
                    //mDefaultWeightEt.setVisibility(View.GONE);
                    mdunfangjianly.setVisibility(View.GONE);
                    mchanpindanjia.setVisibility(View.GONE);
                    mhuowuzhesunlv.setVisibility(View.GONE);
                    mStartCity = null;
                    mEndCity = null;
                    mDefaultStartLon = 0;
                    mDefaultEndLon = 0;
                    mDefaultStartLat = 0;
                    mDefaultEndLat = 0;
                    mDefaultStartLocationTv.setText("");
                    mDefaultEndLocationTv.setText("");
                    mTruckTv.setText("");
                }
                /**长途零担*/
                else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO) {
                    mchangjia.setVisibility(View.GONE);
                    mshouhuodanwei.setVisibility(View.GONE);
                    mdunfangcb.setVisibility(View.VISIBLE);
                    mhuowudunshu.setVisibility(View.GONE);
                    mkchuowubaozhuang.setVisibility(View.GONE);
                    mTruckLayout.setVisibility(View.GONE);//隐藏
                    mTruckLineLayout.setVisibility(View.VISIBLE);

                    //目前要开通长途零担和整车的吨方件
                    mProductDefaultLayout.setVisibility(View.VISIBLE);
                    myincangxian.setVisibility(View.VISIBLE);
                    myincangxianer.setVisibility(View.VISIBLE);
                    mbuzhidaolayout.setVisibility(View.GONE);
                    mkuangchajianquxiao.setVisibility(View.VISIBLE);
                    mDefaultAreaEt.setVisibility(View.VISIBLE);
                    mchanpindanjia.setVisibility(View.GONE);
                    mhuowuzhesunlv.setVisibility(View.GONE);
                    mStartCity = null;
                    mEndCity = null;
                    mDefaultStartLon = 0;
                    mDefaultEndLon = 0;
                    mDefaultStartLat = 0;
                    mDefaultEndLat = 0;
                    mDefaultStartLocationTv.setText("");
                    mDefaultEndLocationTv.setText("");
                    mTruckTv.setText("");
                }
                /**同城整车*/
                else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL) {

                    mchangjia.setVisibility(View.GONE);
                    mshouhuodanwei.setVisibility(View.GONE);
                    mdunfangcb.setVisibility(View.VISIBLE);
                    mhuowudunshu.setVisibility(View.GONE);
                    mkchuowubaozhuang.setVisibility(View.GONE);
                    mTruckLayout.setVisibility(View.VISIBLE);
                    mTruckLineLayout.setVisibility(View.VISIBLE);


                    mProductDefaultLayout.setVisibility(View.VISIBLE);

                    myincangxian.setVisibility(View.VISIBLE);
                    myincangxianer.setVisibility(View.VISIBLE);
                    mbuzhidaolayout.setVisibility(View.GONE);
                    mkuangchajianquxiao.setVisibility(View.VISIBLE);
                    mDefaultAreaEt.setVisibility(View.VISIBLE);
                    mchanpindanjia.setVisibility(View.GONE);
                    mhuowuzhesunlv.setVisibility(View.GONE);
                    mStartCity = null;
                    mEndCity = null;
                    mDefaultStartLon = 0;
                    mDefaultEndLon = 0;
                    mDefaultStartLat = 0;
                    mDefaultEndLat = 0;
                    mDefaultStartLocationTv.setText("");
                    mDefaultEndLocationTv.setText("");
                    mTruckTv.setText("");
                }
                /**长途整车*/
                else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche) {
                    mchangjia.setVisibility(View.GONE);
                    mshouhuodanwei.setVisibility(View.GONE);
                    mdunfangcb.setVisibility(View.VISIBLE);
                    mhuowudunshu.setVisibility(View.GONE);
                    mkchuowubaozhuang.setVisibility(View.GONE);
                    myincangxian.setVisibility(View.GONE);
                    myincangxianer.setVisibility(View.GONE);


                    mProductDefaultLayout.setVisibility(View.VISIBLE);


                    mTruckLayout.setVisibility(View.VISIBLE);
                    mTruckLineLayout.setVisibility(View.VISIBLE);
                    mbuzhidaolayout.setVisibility(View.GONE);
                    mkuangchajianquxiao.setVisibility(View.VISIBLE);
                    mchanpindanjia.setVisibility(View.GONE);
                    mhuowuzhesunlv.setVisibility(View.GONE);
                    mStartCity = null;
                    mEndCity = null;
                    mDefaultStartLon = 0;
                    mDefaultEndLon = 0;
                    mDefaultStartLat = 0;
                    mDefaultEndLat = 0;
                    mDefaultStartLocationTv.setText("");
                    mDefaultEndLocationTv.setText("");
                    mTruckTv.setText("");
                }
                /**同城零担*/
                else if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {
                    mchangjia.setVisibility(View.GONE);
                    mshouhuodanwei.setVisibility(View.GONE);
                    mdunfangcb.setVisibility(View.VISIBLE);
                    mhuowudunshu.setVisibility(View.GONE);
                    mkchuowubaozhuang.setVisibility(View.GONE);
                    mbuzhidaolayout.setVisibility(View.GONE);
                    myincangxian.setVisibility(View.VISIBLE);
                    myincangxianer.setVisibility(View.VISIBLE);
                    mProductDefaultLayout.setVisibility(View.VISIBLE);
                    mTruckLayout.setVisibility(View.GONE);
                    mTruckLineLayout.setVisibility(View.VISIBLE);
                    mkuangchajianquxiao.setVisibility(View.VISIBLE);
                    mDefaultAreaEt.setVisibility(View.VISIBLE);
                    mchanpindanjia.setVisibility(View.GONE);
                    mhuowuzhesunlv.setVisibility(View.GONE);
                    mStartCity = null;
                    mEndCity = null;
                    mDefaultStartLon = 0;
                    mDefaultEndLon = 0;
                    mDefaultStartLat = 0;
                    mDefaultEndLat = 0;
                    mDefaultStartLocationTv.setText("");
                    mDefaultEndLocationTv.setText("");
                    mTruckTv.setText("");
                }
                /**矿产运输*/
                else if (mCategory == CategoryDialog.CategoryEnum.Type_Mineral) {
                    mchangjia.setVisibility(View.VISIBLE);
                    mshouhuodanwei.setVisibility(View.VISIBLE);
                    mdunfangcb.setChecked(true);
                    mDefaultWeightEt.setText("");
                    mdunfangcb.setVisibility(View.GONE);
                    mhuowudunshu.setVisibility(View.VISIBLE);
                    mkchuowubaozhuang.setVisibility(View.GONE);
                    mbuzhidaolayout.setVisibility(View.GONE);
                    myincangxian.setVisibility(View.VISIBLE);
                    myincangxianer.setVisibility(View.VISIBLE);
                    mProductDefaultLayout.setVisibility(View.VISIBLE);
                    mTruckLayout.setVisibility(View.GONE);
                    mTruckLineLayout.setVisibility(View.VISIBLE);
                    mbuzhidaolayout.setVisibility(View.GONE);
                    mDefaultAreaEt.setVisibility(View.INVISIBLE);
                    mkuangchajianquxiao.setVisibility(View.INVISIBLE);
                    mchanpindanjia.setVisibility(View.VISIBLE);
                    mhuowuzhesunlv.setVisibility(View.VISIBLE);
                    mStartCity = null;
                    mEndCity = null;
                    mDefaultStartLon = 0;
                    mDefaultEndLon = 0;
                    mDefaultStartLat = 0;
                    mDefaultEndLat = 0;
                    mDefaultStartLocationTv.setText("");
                    mDefaultEndLocationTv.setText("");
                    mTruckTv.setText("");
                }
                /**其他类别---后面再补齐*/
                else {
                    mchangjia.setVisibility(View.GONE);
                    mshouhuodanwei.setVisibility(View.GONE);
                    mdunfangcb.setVisibility(View.VISIBLE);
                    mhuowudunshu.setVisibility(View.GONE);
                    mkchuowubaozhuang.setVisibility(View.GONE);
                    mbuzhidaolayout.setVisibility(View.GONE);
                    myincangxian.setVisibility(View.VISIBLE);
                    myincangxianer.setVisibility(View.VISIBLE);
                    mProductDefaultLayout.setVisibility(View.VISIBLE);
                    mTruckLayout.setVisibility(View.VISIBLE);
                    mTruckLineLayout.setVisibility(View.VISIBLE);
                    mkuangchajianquxiao.setVisibility(View.VISIBLE);
                    mDefaultAreaEt.setVisibility(View.VISIBLE);
                    mchanpindanjia.setVisibility(View.GONE);
                    mhuowuzhesunlv.setVisibility(View.GONE);
                    mStartCity = null;
                    mEndCity = null;
                    mDefaultStartLon = 0;
                    mDefaultEndLon = 0;
                    mDefaultStartLat = 0;
                    mDefaultEndLat = 0;
                    mDefaultStartLocationTv.setText("");
                    mDefaultEndLocationTv.setText("");
                    mTruckTv.setText("");
                }
            }
        };
        dialog.mCategory = mCategory;
        dialog.show(getFragmentManager(), "category");
    }

    @OnClick(R.id.publishTruckLayout)
    public void truckClick() {
        if (mCategory == null) {
            showToast(getString(R.string.publish_category_hint));
            return;
        }
        Intent intent = new Intent(mContext, TruckActivity.class);
        if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {
            intent.putExtra("categoty", "0");
            intent.putExtra("title", getString(R.string.publish_category_short));
        } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL) {
            intent.putExtra("categoty", "1");
            intent.putExtra("title", getString(R.string.publish_category_longtotal));

        } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO) {
            intent.putExtra("categoty", "2");
            intent.putExtra("title", getString(R.string.publish_category_longzero));
        } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche) {
            intent.putExtra("categoty", "5");
            intent.putExtra("title", getString(R.string.publish_category_zhuanche));
        } else if (mCategory == CategoryDialog.CategoryEnum.Type_Mineral) {
            intent.putExtra("categoty", "6");
            intent.putExtra("title", getString(R.string.publish_category_Mineral));
        } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_paotui) {
            intent.putExtra("categoty", "7");
            intent.putExtra("title", getString(R.string.publish_category_paotui));
        }
        startActivityForResult(intent, RESULT_TRUCK);
    }

    //然后尝试使用editText.setFocusable(false);和editText.setEnabled(false);设置不可编辑状态；editText.setFocusable(true);和 editText.setEnabled(true);设置可编辑状态。
    @OnClick({
            R.id.publishAddressDefaultStartLocationTv, R.id.publishAddressDefaultEndLocationTv,
            R.id.publishAddressDefaultStartLocationIv, R.id.publishAddressDefaultEndLocationIv
    })
    public void defaultLocationClick(View v) {
        Intent intent = new Intent(mContext, SearchLocationActivity.class);
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
            startActivityForResult(new Intent(mContext, AddressActivity.class), RESULT_POSITION_START);
        }
        /**请选择到货定位*/
        else if (v.getId() == R.id.publishAddressDefaultEndLocationIv) {
            startActivityForResult(new Intent(mContext, AddressActivity.class), RESULT_POSITION_END);
        }
    }

    @OnClick({
            R.id.publishProductTv, R.id.publishStartPhoneIv,
            R.id.publishGetPersonIv, R.id.publishGetPhoneIv,
            R.id.publishTimeTv, R.id.dunfangCb, R.id.jianCb, R.id.NoCb, R.id.publishCompanyTv, R.id.ppDefaultWeightEt
    })
    public void onClick(View v) {
        /**货物名称*/
        if (v.getId() == R.id.publishProductTv) {
            ProductDialog dialog = new ProductDialog();
            dialog.setOnProductSelectListener(new ProductDialog.OnProductSelectListener() {
                @Override
                public void onProductSelect(String name) {
                    mProductTv.setText(name);
                    mPublishBean.productName = name;
                }
            });
            dialog.show(Objects.requireNonNull(getFragmentManager()), "product");
        }
        /**货物吨数*/
        else if (v.getId() == R.id.ppDefaultWeightEt) {
            if (mDefaultWeightEt.getText().toString().substring(0).equals("0")) {
                mDefaultWeightEt.setText("0.0");
            }
        }
        /**发货单位*/
        else if (v.getId() == R.id.publishCompanyTv) {
            mPublishBean.company = mpublishCompanyTv.getText().toString();
        }
        /**发货人电话*/
        else if (v.getId() == R.id.publishStartPhoneIv) {
            Intent intent = new Intent(getContext(), FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_START_PHONE);
        }
        /**收货人电话*/
        else if (v.getId() == R.id.publishGetPersonIv) {
            Intent intent = new Intent(getContext(), FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_GET_PERSON);
        }
        /**收货人号码*/
        else if (v.getId() == R.id.publishGetPhoneIv) {
            Intent intent = new Intent(getContext(), FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_GET_PHONE);
        }
        /**发货时间*/
        else if (v.getId() == R.id.publishTimeTv) {
            //获取一个现在时间
            final Time now = new Time();
            now.setToNow();

            new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker arg0, final int year, final int month, int day) {
                    now.year = year;
                    now.month = month;
                    now.monthDay = day;

                    new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker arg0, int hour, int minute) {
                            now.hour = hour;
                            now.minute = minute;

                            String monthStr = String.valueOf(now.month + 1);
                            String dayStr = String.valueOf(now.monthDay);
                            String hourStr = String.valueOf(now.hour);
                            String minuteStr = String.valueOf(now.minute);

                            if (monthStr.length() == 1) {
                                monthStr = "0" + monthStr;
                            }
                            if (dayStr.length() == 1) {
                                dayStr = "0" + dayStr;
                            }
                            if (hourStr.length() == 1) {
                                hourStr = "0" + hourStr;
                            }
                            if (minuteStr.length() == 1) {
                                minuteStr = "0" + minuteStr;
                            }

                            String des = now.year + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr;

                            //比较两个日期
//                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d HH:MM");

//                            ParsePosition pos = new ParsePosition(0);

//                            if (Utils.compareDate("yyyy-M-d HH:MM", des, dateFormat.format(new Date()))) {
//                                showToast("时间不能小于当前时间");
//                                return;
//                            }

                            mTimeTv.setText(des);

                        }
                    }, now.hour, now.minute, true).show();
                }
            }, now.year, now.month, now.monthDay).show();

        }
    }

    @OnClick(R.id.publishNextBtn)
    public void next() {
        /**请先选择配送类型*/
        if (mCategory == null) {
            showToast("请先选择配送类型");
            return;
        }
        mPublishBean.consignorPhone = mStartPhoneEt.getText().toString().replace("-", "").replace("+", "").replace(" ", "");
        mPublishBean.consigneeName = mGetPersonEt.getText().toString();
        mPublishBean.consigneePhone = mGetPhoneEt.getText().toString().replace("-", "").replace("+", "").replace(" ", "");
        mPublishBean.packingType = mGoodspackingEt.getSelectedItem().toString();
        mPublishBean.defaultNo = mHuowuweizhi;
        /**长途整车*/
        if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche) {
            Map<String, String> params = new HashMap<>();
            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            params.put("type", "3");
            params.put("time", mPublishBean.duration);
            params.put("distance", mPublishBean.distance);
            // params.put("car_type",chexingid);
            if (TextUtils.isEmpty(mDefaultWeightEt.getText().toString())) {
                params.put("ton", "0");
            } else {
                params.put("ton", mDefaultWeightEt.getText().toString());
            }
            if (TextUtils.isEmpty(mDefaultAreaEt.getText().toString())) {
                params.put("square", "0");
            } else {
                params.put("square", mDefaultAreaEt.getText().toString());
            }
            if (TextUtils.isEmpty(mjianEt.getText().toString())) {
                params.put("piece", "0");
            } else {
                params.put("piece", mjianEt.getText().toString());
            }

            //    mPresenter.calculateDistancecount(params);
        }
        /**长途零担*/
        if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL) {
            Map<String, String> params = new HashMap<>();
            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            params.put("type", "4");
            params.put("time", mPublishBean.duration);
            params.put("distance", mPublishBean.distance);
            params.put("car_type", chexingid);
            if (TextUtils.isEmpty(mDefaultWeightEt.getText().toString())) {
                params.put("ton", "0");
            } else {
                params.put("ton", mDefaultWeightEt.getText().toString());
            }
            if (TextUtils.isEmpty(mDefaultAreaEt.getText().toString())) {
                params.put("square", "0");
            } else {
                params.put("square", mDefaultAreaEt.getText().toString());
            }
            if (TextUtils.isEmpty(mjianEt.getText().toString())) {
                params.put("piece", "0");
            } else {
                params.put("piece", mjianEt.getText().toString());
            }
            mPresenter.calculateDistancecount(params);
        }
        /**矿产运输*/
        if (mCategory == CategoryDialog.CategoryEnum.Type_Mineral) {
            mPublishBean.defaultWeight = mDefaultWeightEt.getText().toString();

            mPublishBean.defaultArea = "0";
            mPublishBean.defaultNum = "0";
            mPublishBean.zhesunlv = myunxuzhesunlvEt.getText().toString();
            mPublishBean.danjia = mchanpindanjiaEt.getText().toString();
            mPublishBean.company = mpublishCompanyTv.getText().toString();
            mPublishBean.shouhuodanweiname = mpublishCompanyshouhuoTv.getText().toString();
        }

        /**同城零担*/
        if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {
            if (TextUtils.isEmpty(mDefaultWeightEt.getText().toString()) || mDefaultWeightEt.getText().toString().equals("0")) {
                showToast("请输入货物吨数");
                return;
            }
            if (TextUtils.isEmpty(mDefaultAreaEt.getText().toString()) || mDefaultAreaEt.getText().toString().equals("0")) {
                showToast("请输入货物方数");
                return;
            }
            if (TextUtils.isEmpty(mjianEt.getText().toString()) || mjianEt.getText().toString().equals("0")) {
                showToast("请输入货物件数");
                return;
            }
            Map<String, String> params = new HashMap<>();
            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            params.put("type", "1");
            params.put("time", mPublishBean.duration);
            params.put("distance", mPublishBean.distance);
            params.put("car_type", "1");
            if (TextUtils.isEmpty(mDefaultWeightEt.getText().toString())) {
                params.put("ton", "0");
            } else {
                params.put("ton", mDefaultWeightEt.getText().toString());
            }
            if (TextUtils.isEmpty(mDefaultAreaEt.getText().toString())) {
                params.put("square", "0");
            } else {
                params.put("square", mDefaultAreaEt.getText().toString());
            }
            if (TextUtils.isEmpty(mjianEt.getText().toString())) {
                params.put("piece", "0");
            } else {
                params.put("piece", mjianEt.getText().toString());
            }
            mPresenter.calculateDistancecount(params);
        }
        /**同城整车*/
        if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO) {
            if (TextUtils.isEmpty(mDefaultWeightEt.getText().toString()) || mDefaultWeightEt.getText().toString().equals("0")) {
                showToast("请输入货物吨数");
                return;
            }
            if (TextUtils.isEmpty(mDefaultAreaEt.getText().toString()) || mDefaultAreaEt.getText().toString().equals("0")) {
                showToast("请输入货物方数");
                return;
            }
            if (TextUtils.isEmpty(mjianEt.getText().toString()) || mjianEt.getText().toString().equals("0")) {
                showToast("请输入货物件数");
                return;
            }

            Map<String, String> params = new HashMap<>();
            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            params.put("type", "2");
            params.put("time", mPublishBean.duration);
            params.put("distance", mPublishBean.distance);
            params.put("car_type", "2");
            if (TextUtils.isEmpty(mDefaultWeightEt.getText().toString())) {
                params.put("ton", "0");
            } else {
                params.put("ton", mDefaultWeightEt.getText().toString());
            }
            if (TextUtils.isEmpty(mDefaultAreaEt.getText().toString())) {
                params.put("square", "0");
            } else {
                params.put("square", mDefaultAreaEt.getText().toString());
            }
            if (TextUtils.isEmpty(mjianEt.getText().toString())) {
                params.put("piece", "0");
            } else {
                params.put("piece", mjianEt.getText().toString());
            }

            mPresenter.calculateDistancecount(params);
        }


        /**跑腿*/
        if (mCategory == CategoryDialog.CategoryEnum.TYPE_paotui) {
            mPublishBean.defaultWeight = "0";

            mPublishBean.defaultArea = "0";
            mPublishBean.defaultNum = "0";

        }

        mPublishBean.time = mTimeTv.getText().toString();
        mPublishBean.imagePaths = mImagePaths;
        mPublishBean.defaultNum = mjianEt.getText().toString();
        mPublishBean.defaultWeight = mDefaultWeightEt.getText().toString();
        mPublishBean.defaultArea = mDefaultAreaEt.getText().toString();

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
        /**已选车型*/
        else if (requestCode == RESULT_TRUCK) {
            Truck truck = (Truck) data.getSerializableExtra("data");
            chexingid = truck.getId();
            mTruckTv.setText(truck.getTitle());
            mPublishBean.truck = truck;
        }
        /**发货定位*/
        else if (requestCode == RESULT_POSITION_START) {
            String title = data.getStringExtra("title");
            mDefaultStartLocationTv.setText(title);
            mStartCity = data.getStringExtra("city");
            mStartProvince = data.getStringExtra("pro");
            mPublishBean.longStartCityDes = mStartCity;

            mPublishBean.startDesc = title;

            requestCityLonLat();

            if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {

                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城订单不能出现不同的城市，请重新选择城市");
                        mStartCity = null;
                        mDefaultStartLocationTv.setText("");
                        return;
                    }
                }

                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);

                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;
                mPublishBean.longStartProvinceDes = mStartProvince;

                mPublishBean.defaultStartPosDes = title;

                checkDefaultLocation();
            } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche)//同城专车
            {
                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城专车订单不能出现不同的城市，请重新选择城市");
                        mStartCity = null;
                        mDefaultStartLocationTv.setText("");
                        return;
                    }
                }

                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);

                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;
                mPublishBean.longStartProvinceDes = mStartProvince;
                mPublishBean.defaultStartPosDes = title;

                checkDefaultLocation();
            } else if (mCategory == CategoryDialog.CategoryEnum.Type_Mineral)//矿产
            {
                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);

                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;

                mPublishBean.longStartProvinceDes = mStartProvince;
                mPublishBean.longStartAreaDes = title;

                checkLongLocation();
            } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_paotui) {
                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城订单不能出现不同的城市，请重新选择城市");
                        mEndCity = null;
                        mDefaultEndLocationTv.setText("");
                        return;
                    }
                }
                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);

                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;

                mPublishBean.longStartProvinceDes = mStartProvince;
                mPublishBean.longStartAreaDes = title;

                checkLongLocation();


            } else {
                if (mStartCity != null && mEndCity != null) {
                    if (mStartCity.equals(mEndCity) || mStartCity.contains(mEndCity) || mEndCity.contains(mStartCity)) {
                        showLongToast("长途订单不能出现相同的城市，请重新选择城市");
                        mStartCity = null;
                        mDefaultStartLocationTv.setText("");
                        return;
                    }
                }

                mDefaultStartLat = data.getDoubleExtra("lat", 0);
                mDefaultStartLon = data.getDoubleExtra("lon", 0);

                //设置发货定位
                mPublishBean.defaultStartLon = mDefaultStartLon;
                mPublishBean.defaultStartLat = mDefaultStartLat;

                mPublishBean.longStartProvinceDes = mStartProvince;
                mPublishBean.longStartAreaDes = title;

                checkLongLocation();
            }

        }
        /**到货定位*/
        else if (requestCode == RESULT_POSITION_END) {
            String title = data.getStringExtra("title");
            mDefaultEndLocationTv.setText(title);
            mEndCity = data.getStringExtra("city");
            mEndProvince = data.getStringExtra("pro");
            mPublishBean.longEndCityDes = mEndCity;
            mPublishBean.endDesc = title;
            requestCityLonLat();
            if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {

                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城订单不能出现不同的城市，请重新选择城市");
                        mEndCity = null;
                        mDefaultEndLocationTv.setText("");
                        return;
                    }
                }
                mDefaultEndLat = data.getDoubleExtra("lat", 0);
                mDefaultEndLon = data.getDoubleExtra("lon", 0);
                //设置到货定位
                mPublishBean.defaultEndLon = mDefaultEndLon;
                mPublishBean.defaultEndLat = mDefaultEndLat;
                mPublishBean.longEndProvinceDes = mEndProvince;
                mPublishBean.defaultEndPosDes = title;
                checkDefaultLocation();
            } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche) {
                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城专车订单不能出现不同的城市，请重新选择城市");
                        mEndCity = null;
                        mDefaultEndLocationTv.setText("");
                        return;
                    }
                }
                mDefaultEndLat = data.getDoubleExtra("lat", 0);
                mDefaultEndLon = data.getDoubleExtra("lon", 0);
                //设置到货定位
                mPublishBean.defaultEndLon = mDefaultEndLon;
                mPublishBean.defaultEndLat = mDefaultEndLat;
                mPublishBean.longEndProvinceDes = mEndProvince;
                mPublishBean.defaultEndPosDes = title;
                checkDefaultLocation();
            } else if (mCategory == CategoryDialog.CategoryEnum.Type_Mineral) {
                mDefaultEndLat = data.getDoubleExtra("lat", 0);
                mDefaultEndLon = data.getDoubleExtra("lon", 0);
                mPublishBean.defaultEndLon = mDefaultEndLon;
                mPublishBean.defaultEndLat = mDefaultEndLat;
                mPublishBean.longEndProvinceDes = mEndProvince;
                mPublishBean.defaultEndPosDes = title;
                checkDefaultLocation();
            } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_paotui) {
                if (mStartCity != null && mEndCity != null) {
                    if (!mStartCity.contains(mEndCity) && !mEndCity.contains(mStartCity)) {
                        showLongToast("同城专车订单不能出现不同的城市，请重新选择城市");
                        mEndCity = null;
                        mDefaultEndLocationTv.setText("");
                        return;
                    }
                }


                mDefaultEndLat = data.getDoubleExtra("lat", 0);
                mDefaultEndLon = data.getDoubleExtra("lon", 0);
                mPublishBean.defaultEndLon = mDefaultEndLon;
                mPublishBean.defaultEndLat = mDefaultEndLat;
                mPublishBean.longEndProvinceDes = mEndProvince;
                mPublishBean.longEndAreaDes = title;
                checkDefaultLocation();
            } else {
                if (mStartCity != null && mEndCity != null) {
                    if (mStartCity.equals(mEndCity) || mStartCity.contains(mEndCity) || mEndCity.contains(mStartCity)) {
                        showLongToast("长途订单不能出现相同的城市，请重新选择城市");
                        mEndCity = null;
                        mDefaultEndLocationTv.setText("");
                        return;
                    }
                }

                mDefaultEndLat = data.getDoubleExtra("lat", 0);
                mDefaultEndLon = data.getDoubleExtra("lon", 0);
                mPublishBean.defaultEndLon = mDefaultEndLon;
                mPublishBean.defaultEndLat = mDefaultEndLat;
                mPublishBean.longEndProvinceDes = mEndProvince;
                mPublishBean.longEndAreaDes = title;
                checkLongLocation();
            }
        }
        /**发货电话*/
        else if (requestCode == RESULT_FRIEND_START_PHONE) {
            Friend friend = (Friend) data.getSerializableExtra("data");
            mStartPhoneEt.setText(friend.getPhone().replace("-", "").replace("+", "").replace(" ", ""));
            mPublishBean.consignorPhone = friend.getPhone();
        }
        /**收货人*/
        else if (requestCode == RESULT_FRIEND_GET_PERSON) {
            Friend friend = (Friend) data.getSerializableExtra("data");
            mGetPersonEt.setText(friend.getName());
            mGetPhoneEt.setText(friend.getPhone().replace("-", "").replace("+", "").replace(" ", ""));
            mPublishBean.consigneeName = friend.getName().replace("-", "").replace("+", "").replace(" ", "");
            mPublishBean.consigneePhone = friend.getPhone().replace("-", "").replace("+", "").replace(" ", "");
        }
        /**收货电话*/
        else if (requestCode == RESULT_FRIEND_GET_PHONE) {
            Friend friend = (Friend) data.getSerializableExtra("data");
            mGetPhoneEt.setText(friend.getPhone().replace("-", "").replace("+", "").replace(" ", ""));
            mPublishBean.consigneePhone = friend.getPhone().replace("-", "").replace("+", "").replace(" ", "");
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
     * 检查默认情况下（目前可特指同城配送）地理位置检查
     */
    private void checkDefaultLocation() {
        if (mDefaultStartLat != 0 && mDefaultStartLon != 0
                && mDefaultEndLat != 0 && mDefaultEndLon != 0) {
            mGetDistanceDialog = LoadingProgress.showProgress(mContext, "正在请求关键数据...");
            //发起请求以计算两点间最短距离
            mPresenter.calculateDistanceWithLonLat(mDefaultStartLon, mDefaultStartLat, mDefaultEndLon, mDefaultEndLat);
        }
    }

    public static String submitPostData(String strUrlPath, Map<String, String> params, String encode) {
        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {

            //String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
            URL url = null;
            try {
                url = new URL(strUrlPath);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
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
            for (Map.Entry<String, String> entry : params.entrySet()) {
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
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    /**
     * 目前该方法没啥用--正在请求关键数据
     */
    private void checkLongLocation1() {
        if (mEndCityAddress != null && mStartCityAddress != null) {
            mGetDistanceDialog = LoadingProgress.showProgress(mContext, "正在请求关键数据...");
            mPresenter.calculateDistanceWithCityName(mStartCityAddress.getName(), mEndCityAddress.getName(), mStartPro.getName
                    (), mEndPro.getName());
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
        mCategory = null;
        mDefaultStartLat = 0;
        mDefaultStartLon = 0;
        mDefaultEndLat = 0;
        mDefaultEndLon = 0;
        mStartPro = null;
        mStartCityAddress = null;
        mEndPro = null;
        mEndCityAddress = null;
        mTruckTv.setText("");
        mDefaultStartLocationTv.setText("");
        mDefaultEndLocationTv.setText("");
        mStartSpinnerView.clear();
        mEndSpinnerView.clear();
        mProductTv.setText("");
        mDefaultWeightEt.setText("");
        mDefaultAreaEt.setText("");
        mjianEt.setText("");
        mStartPhoneEt.setText("");
        mGetPersonEt.setText("");
        mGetPhoneEt.setText("");
        myunxuzhesunlvEt.setText("");
        mchanpindanjiaEt.setText("");
        mPhotoScrollView.setDatas(null, null);
        mImagePaths = null;
        mHuowuweizhi = "0";
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
        Integer d = entity.getData();
        mPublishBean.nidaye = d.toString();
//        Intent intent = new Intent(mContext, PublishConfirmActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("data", (Serializable) mPublishBean);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    public String getGouzao() {
        return gouzao;
    }

    public void setGouzao(String gouzao) {
        this.gouzao = gouzao;
    }


}
