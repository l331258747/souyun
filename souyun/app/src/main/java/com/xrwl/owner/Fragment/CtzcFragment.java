package com.xrwl.owner.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.dialog.CategoryDialog;
import com.xrwl.owner.module.publish.ui.TruckActivity;
import com.xrwl.owner.module.publish.view.AreaSpinnerView;
import com.xrwl.owner.module.publish.view.PublishProductLongView;
import com.xrwl.owner.view.PhotoScrollView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CtzcFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CtzcFragment extends Fragment {
    public static  String fanhuijiagelo;
    protected static String chexingid;
    public static final int RESULT_TRUCK = 111;//已选车型
    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位
    public static final int RESULT_FRIEND_START_PHONE = 444;//发货电话
    public static final int RESULT_FRIEND_GET_PERSON = 555;//收货人
    public static final int RESULT_FRIEND_GET_PHONE = 666;//收货电话
    public   String shijianlo;
    public   String julilo;
    private CategoryDialog.CategoryEnum mCategory;
    private String name;
    private ArrayList<String> mImagePaths;
    private PublishBean mPublishBean;
    private ProgressDialog mGetDistanceDialog;
    private String mEndCity;
    private String mEndProvince;
    private String mStartCity;
    private String mStartProvince;
    private  String mHuowuweizhi="0";
    private String appjianshu="0";
    private String appdun="0";
    private String appfang="0";
    private String gouzao="0";
    private String carnametype;
    private String string;
    private double mDefaultStartLat, mDefaultStartLon;
    private double mDefaultEndLat, mDefaultEndLon;
    private Address mStartCityAddress, mEndCityAddress, mStartPro, mEndPro;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Unbinder unbinder;
    public CtzcFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CtzcFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CtzcFragment newInstance(String param1, String param2) {
        CtzcFragment fragment = new CtzcFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inflate = inflater.inflate(R.layout.fragment_ctzc, container, false);

        unbinder = ButterKnife.bind(this, inflate);



        return inflate;

    }

    @OnClick(R.id.publishTruckLayout)
    public void truckClick() {
//        if (mCategory == null) {
//            //showToast(getString(R.string.publish_category_hint));
//            return;
//        }
        Intent intent = new Intent(getContext(), TruckActivity.class);
        if (mCategory == CategoryDialog.CategoryEnum.TYPE_SHORT) {
            intent.putExtra("categoty", "0");
            intent.putExtra("title", getString(R.string.publish_category_short));
        } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL) {
            intent.putExtra("categoty", "1");
            intent.putExtra("title", getString(R.string.publish_category_longtotal));

        } else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO) {
            intent.putExtra("categoty", "2");
            intent.putExtra("title", getString(R.string.publish_category_longzero));
        }
        else if (mCategory == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche) {
            intent.putExtra("categoty", "5");
            intent.putExtra("title", getString(R.string.publish_category_zhuanche));
        }
        else if (mCategory == CategoryDialog.CategoryEnum.Type_Mineral) {
            intent.putExtra("categoty", "6");
            intent.putExtra("title", getString(R.string.publish_category_Mineral));
        }
        else if (mCategory == CategoryDialog.CategoryEnum.TYPE_paotui) {
            intent.putExtra("categoty", "7");
            intent.putExtra("title", getString(R.string.publish_category_paotui));
        }
        startActivityForResult(intent, RESULT_TRUCK);
    }
}