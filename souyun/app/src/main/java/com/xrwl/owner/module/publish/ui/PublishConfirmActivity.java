package com.xrwl.owner.module.publish.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.PostOrder;
import com.xrwl.owner.event.PublishClearCacheEvent;
import com.xrwl.owner.module.friend.bean.Friend;
import com.xrwl.owner.module.friend.ui.FriendActivity;
import com.xrwl.owner.module.publish.bean.Changtulingdan;
import com.xrwl.owner.module.publish.bean.Config;
import com.xrwl.owner.module.publish.bean.Insurance;
import com.xrwl.owner.module.publish.bean.PayResult;
import com.xrwl.owner.module.publish.bean.PublishBean;
import com.xrwl.owner.module.publish.bean.Truck;
import com.xrwl.owner.module.publish.dialog.CategoryDialog;
import com.xrwl.owner.module.publish.dialog.InsuranceDialog;
import com.xrwl.owner.module.publish.dialog.RemarkDialog;
import com.xrwl.owner.module.publish.mvp.PublishConfirmContract;
import com.xrwl.owner.module.publish.mvp.PublishConfirmPresenter;
import com.xrwl.owner.utils.Constants;
import com.xrwl.owner.utils.MyUtils;
import com.xrwl.owner.view.RegionNumberEditText;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/3/26 下午9:29.
 */

public class PublishConfirmActivity extends BaseActivity<PublishConfirmContract.IView, PublishConfirmPresenter> implements PublishConfirmContract.IView {
    protected String ghtoken;
    private IWXAPI mWXApi;
    private PostOrder mPostOrder;
    private PayBroadcastReceiver mPayBroadcastReceiver;
    public static final int RESULT_FRIEND_PAY = 111;//代付
    public static final int RESULT_FRIEND_GET = 222;//代收
    public static final int RESULT_FRIEND_GETs = 333;//获取中转点
    public static final int RESULT_FRIEND_GETss = 444;//获取中转点
    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_FAPIAO = 777;//发票
    public static int price = 0;
    public String renmibi = "0";
    private RemarkDialog mRemarkDialog;
    private DateFormat mDateFormat;
    private ProgressDialog mPostDialog;
    @BindView(R.id.yumoney)
    TextView myumoney;
    @BindView(R.id.pcSendBySelfCb)
    CheckBox mSendBySelfCb;
    @BindView(R.id.pcPickBySelfCb)
    CheckBox mPickBySelfCb;
    @BindView(R.id.pcBySelfLayout)
    View mBySelfLayout;
    @BindView(R.id.pcBottomLayout)
    View mBottomLayout;
    @BindView(R.id.pcRootLayout)
    View mRootLayout;
    @BindView(R.id.pcTotalPriceTv)
    TextView mTotalPriceTv;
    @BindView(R.id.daishoushouxufeiTv)
    TextView mdaishoushouxufeiTv;
    @BindView(R.id.pcPopLayout)
    View mPopView;
    @BindView(R.id.pcArrowIv)
    ImageView mArrowIv;
    @BindView(R.id.pcInsuranceTv)
    public TextView mInsuranceTv;//保险
    @BindView(R.id.pcReceiptCb)
    public CheckBox mReceiptCb;
    @BindView(R.id.pcReceiptLayout)
    public View mReceiptView;
    @BindView(R.id.pcHelpPayCb)
    public CheckBox mHelpPayCb;
    @BindView(R.id.pcHelpPayLayout)
    public View mHelpPayView;
    @BindView(R.id.pcHelpGetCb)
    public CheckBox mHelpGetCb;
    @BindView(R.id.pcHelpGetLayout)
    public View mHelpGetView;
    @BindView(R.id.pcHelpGetPriceEt)
    EditText mHelpGetPriceEt;//代收货款
    private PublishBean mPublishBean;
    @BindView(R.id.pcProductNameTv)
    TextView mProductNameTv;
    @BindView(R.id.pcStartPositionTv)
    TextView mStartPosTv;
    @BindView(R.id.pcEndPositionTv)
    TextView mEndPosTv;
    @BindView(R.id.pcTruckTv)
    TextView mTruckTv;//车型
    @BindView(R.id.pcAreaTv)
    TextView mAreaTv;
    @BindView(R.id.pcPriceTv)
    TextView mPriceTv;
    @BindView(R.id.pcWeightTv)
    TextView mWeightTv;
    @BindView(R.id.pcDistanceTv)
    TextView mDistanceTv;
    @BindView(R.id.pcNumTv)
    TextView mNumTv;
    @BindView(R.id.zidongjieshouTv)
    TextView mzidongjieshouTv;
    @BindView(R.id.pcRecommendLayout)
    View mRecommendView;//如果是同城 隐藏推荐
    @BindView(R.id.pcFreightEt)
    RegionNumberEditText mFreightEt;//运费
    @BindView(R.id.pcRecommendPriceTv)
    TextView mRecommendPriceTv;//推荐运费
    @BindView(R.id.pcPriceHintTv)
    TextView mPriceHintTv;//价格区间提示
    @BindView(R.id.pcTaxNumEt)
    EditText mTaxNumEt;//税号
    @BindView(R.id.pcUnitNameEt)
    EditText mUnitNameEt;//单位名称
    @BindView(R.id.pcEmailEt)
    EditText mEmailEt;//邮箱
    @BindView(R.id.pcCouponTv)
    public TextView mCouponTv;
    @BindView(R.id.pcHelpPayTv)
    TextView mHelpPayTv;//代付运费
    @BindView(R.id.tongchengyouhuijuan)
    LinearLayout myouhuijuan;
    @BindView(R.id.pcHelpGetTv)
    TextView mHelpCollectTv;//代收货款

    @BindView(R.id.jiahao)
    ImageView mjiahao;//加号
    @BindView(R.id.jianhao)
    ImageView mjianhao;//减号

    private ProgressDialog mPayDialog;
    private ProgressDialog mOnlinePayDialog;
    private ProgressDialog mXrwlwxpayDialog;
    private int mMinPrice = 0;
    private int mMaxPrice = 1000000000;
    private int mInsurancePrice;//保险价格
    private float mfuwufei;//服务费
    private int mFull, mReduce;//满 减
    private int mFreightPrice;//运费价格
    private double mMindon = 0;
    private double mMaxdon = 1000000000;

    private String jiajianprice = "0";

    private String xianshijiage = "0";
    /**
     * 弹出款
     **/
    @BindView(R.id.pcPopFreightTv)
    TextView mPopFreightTv;//运费
    @BindView(R.id.pcPopInsuranceTv)
    TextView mPopInsuranceTv;//保费
    @BindView(R.id.pcPopCouponTv)
    TextView mPopCouponTv;//优惠券
    @BindView(R.id.pcRemarkTv)
    TextView mRemarkTv;//备注
    @BindView(R.id.kcys)
    LinearLayout mkcys;
    @BindView(R.id.yunfeijiagetv)
    TextView myunfeijiagetv;//运费价格
    @BindView(R.id.tuijianyunfeijiagetv)
    TextView mtuijianyunfeijiagetv;//推荐运费总价
    @BindView(R.id.pcsimiCb)
    CheckBox mpcsimiCb;
    @BindView(R.id.xezsdTV)
    TextView mxezsdTV;
    @BindView(R.id.xeztdTV)
    TextView mxeztdTV;
    public Truck truck;


    /***
     * 支付*/

//    private PayBroadcastReceiver mPayBroadcastReceiver;
    private double xzfjfs = 1;
    private String balance;
    //private String userid;
    private String canshutijiao = "0";
    @BindView(R.id.rg_tab_group)
    RadioGroup rgTabGroup;
    @BindView(R.id.yueCB)
    CheckBox myuecb;
    @BindView(R.id.weixinCB)
    CheckBox mweixincb;
    @BindView(R.id.zhifubaoCB)
    CheckBox mzhifubaocb;
    @BindView(R.id.yinhangkaCB)
    CheckBox myinhangkacb;
    @BindView(R.id.money)
    Spinner mmoney;
    @BindView(R.id.xuanzeonline)
    CheckBox mxuanzeonline;
    @BindView(R.id.xuanzeonlinexianxia)
    CheckBox mxuanzeonlinexianxia;
    private double myyue = 0;
    private String mCode;
    @BindView(R.id.rb_driver)
    RadioButton mrb_driver;
    private Throwable e;
//    private static Handler handler = new Handler();
//    private long TIME_LISTEN = 1000;//设置2秒的延迟

    //默认是全额支付，否则就是保证金
    private String paytype = "1";
    private Integer d;
    private TextView viewById;

    @Override
    protected PublishConfirmPresenter initPresenter() {
        return new PublishConfirmPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.publishconfirm_activity;
    }

    @Override
    protected void initViews() {
        mPopView.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#000000"));
        }

        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        /**
         * 2020年4月10日 13:57:33
         * 接受
         */

        mPublishBean = (PublishBean) getIntent().getSerializableExtra("data");




        /**跑腿
         * 重写点击事件点击事件
         */

        viewById = findViewById(R.id.pcOkBtn);
//        viewById.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mPublishBean.freight = mTotalPriceTv.getText().toString();
//
//                mPresenter.postOrder(mPublishBean.imagePaths, mPublishBean.getParams());
//
//
//                Intent intent = new Intent(PublishConfirmActivity.this, OrderConfirmActivity.class);
//                intent.putExtra("publishBean", mPublishBean);
//                intent.putExtra("paytype", paytype);
//
////                BaseEntity<PostOrder> entity = new BaseEntity();
////                intent.putExtra("postOrder", entity.getData());
////                intent.putExtra("postOrder", mPublishBean.freight);
//                startActivity(new Intent(PublishConfirmActivity.this, OrderConfirmActivity.class));
//                startActivity(intent);
//                //  startActivity(new Intent(PublishConfirmActivity.this, OrderConfirmActivity.class));
//            }
//        });


        //公共
        mAreaTv.setText("体积：" + mPublishBean.getArea() + "方");
        mWeightTv.setText("重量：" + mPublishBean.getWeight() + "吨");
        mNumTv.setText("数量：" + mPublishBean.getNum() + "件");

        mTruckTv.setText("车型：无车型需求");

        mProductNameTv.setText("货物：" + mPublishBean.productName);
        mStartPosTv.setText(mPublishBean.getStartPos());
        mEndPosTv.setText(mPublishBean.getEndPos());
        mDistanceTv.setText("公里：" + mPublishBean.getDistance() + "公里");


        /**
         * 2公里以内5公斤以下 8元
         * 2-5公里每增加一公里加收1元
         * 5-10公里每增加一公里加收2元
         * */
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_paotui.getValue()) {
            //起步两公里5元 多一公里增加1元
//            Map<String, String> params = new HashMap<>();
//            String sss=mPublishBean.getTruckIds();
//            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
//            params.put("type","7");
//            params.put("time",mPublishBean.duration);
//            params.put("distance",mPublishBean.distance);
//            params.put("ton",mPublishBean.defaultWeight);
//            params.put("square",mPublishBean.defaultArea);
//            params.put("piece",mPublishBean.defaultNum);
//            params.put("car_type",sss);
//            mPresenter.calculateDistancecount(params);

            BigDecimal b = new BigDecimal(mPublishBean.distance).setScale(0, BigDecimal.ROUND_HALF_UP);
            int paotuijiage = 0;
            //showToast(String.valueOf(b));

            double youhuipaotui = 0;

            if (Integer.parseInt(String.valueOf(b)) <= 2) {
                youhuipaotui = 7.6;
            } else if (2 < Integer.parseInt(String.valueOf(b)) && Integer.parseInt(String.valueOf(b)) <= 5) {
                youhuipaotui = (7.6 + (Integer.parseInt(String.valueOf(b)) - 2));
            } else if (5 < Integer.parseInt(String.valueOf(b)) && Integer.parseInt(String.valueOf(b)) <= 10) {
                youhuipaotui = (7.6 + (Integer.parseInt(String.valueOf(b)) - 2)) + (Integer.parseInt(String.valueOf(b)) - 5) * 2;
            } else if (10 < Integer.parseInt(String.valueOf(b))) {
                youhuipaotui = (7.6 + (Integer.parseInt(String.valueOf(b)) - 2)) + (Integer.parseInt(String.valueOf(b)) - 5) * 2 + (Integer.parseInt(String.valueOf(b)) - 10) * 3;
            }
//             else
//             {
//                // paotuijiage=(5 + (Integer.parseInt(String.valueOf(b))-2));
//             }

            BigDecimal bb = new BigDecimal(youhuipaotui).setScale(0, BigDecimal.ROUND_HALF_UP);
            paotuijiage = Integer.parseInt(String.valueOf(bb));
            mFreightEt.setFocusable(false);//不可编辑
            mAreaTv.setVisibility(View.VISIBLE);
            mWeightTv.setVisibility(View.VISIBLE);
            mNumTv.setVisibility(View.VISIBLE);

            mBySelfLayout.setVisibility(View.GONE);
            mTruckTv.setText("车型：跑腿");
            mRecommendView.setVisibility(View.VISIBLE);
            mFreightPrice = paotuijiage;
            int pricepaotui = paotuijiage;
            mFreightEt.setText(String.valueOf(pricepaotui));
            mFreightEt.setEnabled(true);
            String yan = "";
            yan = String.valueOf(pricepaotui);

            mkcys.setVisibility(View.VISIBLE);
            mRecommendPriceTv.setVisibility(View.VISIBLE);

            mzidongjieshouTv.setVisibility(View.GONE);

            mpcsimiCb.setVisibility(View.GONE);


            // price = (int) mPublishBean.getNidaye();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            mPriceTv.setText(simpleDateFormat.format(date));
            mRecommendPriceTv.setText(String.valueOf(pricepaotui));
            mFreightEt.setText(String.valueOf(pricepaotui));
            mMinPrice = (int) (pricepaotui * 0.8);
            mMaxPrice = (int) (pricepaotui * 1.2);
            mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");

            //把输入框的值赋值到这个总价上面来pcFreightEt
            mTotalPriceTv.setText(String.valueOf(mFreightPrice));


            mAreaTv.setText("体积：" + "跑腿");
            mWeightTv.setText("重量：" + "跑腿");
            mNumTv.setText("数量：" + "跑腿");

        }
        /**同城专车*/
        else if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche.getValue()) {
            Map<String, String> params = new HashMap<>();
            String sss = mPublishBean.getTruckIds();
            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            params.put("type", "3");
            params.put("time", mPublishBean.duration);
            params.put("distance", mPublishBean.distance);
            params.put("ton", mPublishBean.defaultWeight);
            params.put("square", mPublishBean.defaultArea);
            params.put("piece", mPublishBean.defaultNum);
            params.put("car_type", sss);
            mPresenter.calculateDistancecount(params);
        }
        /**长途专车*/
        else if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL.getValue()) {
            Map<String, String> params = new HashMap<>();
            String sss = mPublishBean.getTruckIds();
            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            params.put("type", "4");
            params.put("time", mPublishBean.duration);
            params.put("distance", mPublishBean.distance);
            params.put("ton", mPublishBean.defaultWeight);
            params.put("square", mPublishBean.defaultArea);
            params.put("piece", mPublishBean.defaultNum);
            params.put("car_type", sss);
            mPresenter.calculateDistancecount(params);
        }
        /**同城零担*/
        else if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_SHORT.getValue()) {
            Map<String, String> params = new HashMap<>();
            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            params.put("type", "1");
            params.put("time", mPublishBean.duration);
            params.put("distance", mPublishBean.distance);
            params.put("ton", mPublishBean.getWeight1());
            params.put("square", mPublishBean.getArea1());
            params.put("piece", mPublishBean.getNum1());
            mPresenter.calculateDistancecount(params);
        }
        /**长途零担*/
        else if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO.getValue()) {
            Map<String, String> params = new HashMap<>();
            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
            params.put("type", "2");
            params.put("time", mPublishBean.duration);
            params.put("distance", mPublishBean.distance);
            params.put("ton", mPublishBean.getWeight1());
            params.put("square", mPublishBean.getArea1());
            params.put("piece", mPublishBean.getNum1());
            mPresenter.calculateDistancecountlingdan(params);
        }
        /**矿产运输*/
        else if (mPublishBean.category == CategoryDialog.CategoryEnum.Type_Mineral.getValue()) {

        }
        initData();

        myuecb.setChecked(true);
        mWXApi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_KEY);
        mPayBroadcastReceiver = new PayBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.WX_P_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPayBroadcastReceiver, filter);
        mPresenter.getTotalPriceBalance();
    }


    @Override
    protected void onDestroy() {
        if (mPayBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPayBroadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    protected void handleEvents() {
        mFreightEt.setRegion(mMaxPrice, mMinPrice);
        mFreightEt.setTextWatcher(value -> {
            mFreightPrice = value;
            if (mFreightPrice < mFull) {
                mFull = 0;
                mReduce = 0;
                mCouponTv.setText("");
            }

        });
    }


    private Handler handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = () -> {
        //在这里调用服务器的接口，获取数据
        //  getSearchResult(editString, "all", 1, "true");
    };


    @SuppressLint("SetTextI18n")
    private void initData() {

        if (mFreightEt.length() > 0) {
        } else {
            mFreightEt.setText("0");
        }

        myouhuijuan.setVisibility(View.GONE);

        /**矿产运输*/
        if (mPublishBean.category == CategoryDialog.CategoryEnum.Type_Mineral.getValue()) {

            if(mPublishBean.truck != null)
                mTruckTv.setText("车型：" + mPublishBean.truck.getTitle());

            myunfeijiagetv.setText("运费单价");
            mtuijianyunfeijiagetv.setText("运费总价");
            mkcys.setVisibility(View.GONE);
            mRecommendPriceTv.setVisibility(View.GONE);
            mzidongjieshouTv.setVisibility(View.VISIBLE);
            mpcsimiCb.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(mFreightEt.getText().toString()) || mFreightEt.getText().toString().length() == 0) {
                price = 0;
            } else {
                price = Integer.parseInt(mFreightEt.getText().toString());
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            mPriceTv.setText(simpleDateFormat.format(date));
            mRecommendPriceTv.setText(String.valueOf(price));
            mMinPrice = (int) (1);
            mMaxPrice = (int) (10000000);
            mPriceHintTv.setText("请在" + 1 + "-" + 10000000 + "之间选择输入");
            mFreightEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String fangfang = "0";
                    String xiaofang = "0";
                    if (TextUtils.isEmpty(mWeightTv.getText().toString().replace("重量：", "").replace("吨", "")) || mWeightTv.getText().toString().replace("重量：", "").replace("吨", "").length() == 0) {
                        fangfang = "1";
                    } else {
                        fangfang = mWeightTv.getText().toString().replace("重量：", "").replace("吨", "");
                    }
                    if (s.toString().length() == 0 && TextUtils.isEmpty(s.toString())) {
                        xiaofang = "0";
                    } else {
                        xiaofang = s.toString();
                    }
                    String abcd = String.valueOf(Float.parseFloat(xiaofang) * Float.parseFloat(fangfang));

                    mzidongjieshouTv.setText(abcd);
                    mTotalPriceTv.setText(abcd);

                    calculateTotalPrice();
                }
            });

            mFreightPrice = Integer.parseInt(mzidongjieshouTv.getText().toString());

        }else{
            mFreightEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (delayRun != null) {
                        //每次editText有变化的时候，则移除上次发出的延迟线程
                        handler.removeCallbacks(delayRun);
                    }

                    //editString = s.toString();
                    String[] s1 = mFreightEt.getText().toString().split("\\.");
                    if (s1[0] != null && !"".equals(s1[0])) {
                        String[] s2 = String.valueOf(price * 0.8).split("\\.");
                        String[] s3 = String.valueOf(price * 1.2).split("\\.");
                        if (Integer.parseInt(s1[0]) < Integer.parseInt(s2[0])) {
                            xianshijiage = String.valueOf(s2[0]);
                            //showToast("您输入的价格超出了最低价格，按照最低价格算");
                            //mFreightEt.setText(xianshijiage);
                        } else if (Integer.parseInt(s1[0]) > Integer.parseInt(s3[0])) {
                            xianshijiage = String.valueOf(s3[0]);
                            // showToast("您输入的价格超出了最高价格，按照最低高格算");
                            //mFreightEt.setText(xianshijiage);
                        } else if (Integer.parseInt(s1[0]) > Integer.parseInt(s2[0]) && Integer.parseInt(s1[0]) < Integer.parseInt(s3[0])) {
                            xianshijiage = s.toString();
                        }
                    } else {
                        xianshijiage = "0";
                    }

                    mFreightPrice = Integer.parseInt(xianshijiage);
                    if (mReceiptCb.isChecked()) {
                        mfuwufei = mFreightPrice * 0.099f;
                    }

                    calculateTotalPrice();

                    handler.postDelayed(delayRun, 5000);
                }
            });

            mFreightPrice = Integer.parseInt(xianshijiage);


            myunfeijiagetv.setText("运费价格");
            mtuijianyunfeijiagetv.setText("推荐运费价格");
        }

        mHelpCollectTv.setText(mPublishBean.consigneeName);
    }

    @OnClick({R.id.pcPriceDetailLayout, R.id.pcPopLayout,
            R.id.pcInsuranceTv, R.id.pcReceiptCb, R.id.pcHelpPayCb,
            R.id.pcHelpGetCb, R.id.pcCouponTv, R.id.pcHelpGetLayout,
            R.id.pcHelpPayLayout, R.id.pcRemarkTv, R.id.pcSendBySelfCb, R.id.pcPickBySelfCb, R.id.xezsdTV, R.id.xeztdTV, R.id.jiahao, R.id.jianhao, R.id.yueCB, R.id.weixinCB, R.id.zhifubaoCB, R.id.yinhangkaCB
    })
    public void onClick(View v) {
        if(!MyUtils.isFastClick())
            return;

        /**合计外围的FrameLayout*/
        if (v.getId() == R.id.pcPriceDetailLayout) {
            if (mPopView.isShown()) {
                mPopView.setVisibility(View.GONE);
                mArrowIv.setImageResource(R.drawable.publish_ic_arrow_up2);
            } else {
                mPopView.setVisibility(View.VISIBLE);
                mPopFreightTv.setText(String.valueOf(mFreightPrice));
                mPopInsuranceTv.setText(String.valueOf(mInsurancePrice));
                mPopCouponTv.setText(String.format("%.1f", mfuwufei));
                mArrowIv.setImageResource(R.drawable.publish_ic_arrow_down2);
            }
        }
        /**选择自送地*/
        /**余额支付*/
        else if (v.getId() == R.id.yueCB) {
            if (myuecb.isChecked()) {
                mweixincb.setChecked(false);
                mzhifubaocb.setChecked(false);
                myinhangkacb.setChecked(false);
            } else {
                myuecb.setChecked(false);
            }
        }
        /**微信支付*/
        else if (v.getId() == R.id.weixinCB) {
            if (mweixincb.isChecked()) {
                myuecb.setChecked(false);
                mzhifubaocb.setChecked(false);
                myinhangkacb.setChecked(false);
            } else {
                mweixincb.setChecked(false);
            }
        }
        /**支付宝支付*/
        else if (v.getId() == R.id.zhifubaoCB) {
            if (mzhifubaocb.isChecked()) {
                myuecb.setChecked(false);
                mweixincb.setChecked(false);
                myinhangkacb.setChecked(false);
            } else {
                mzhifubaocb.setChecked(false);
            }
        }
        /**银行卡支付*/
        else if (v.getId() == R.id.yinhangkaCB) {
            if (myinhangkacb.isChecked()) {
                myuecb.setChecked(false);
                mweixincb.setChecked(false);
                mzhifubaocb.setChecked(false);
            } else {
                myinhangkacb.setChecked(false);
            }
        } else if (v.getId() == R.id.xezsdTV) {
            Intent intent = new Intent(mContext, Address_zhongzhuandianActivity.class);
            intent.putExtra("title", "请选择中转点");
            startActivityForResult(intent, RESULT_FRIEND_GETs);
        }
        /**选择自提地*/
        else if (v.getId() == R.id.xeztdTV) {
            Intent intent = new Intent(mContext, Address_zhongzhuandianActivity.class);
            intent.putExtra("title", "请选择中转点");
            startActivityForResult(intent, RESULT_FRIEND_GETss);
        }
        /**加号*/
        else if (v.getId() == R.id.jiahao) {
            //String aa=mFreightEt.getText().toString();
            //int qujian=Integer.parseInt(aa);
            int tltalprice = Integer.parseInt(jiajianprice);
            int jiajiage = tltalprice + (tltalprice * 5) / 100;
            int zuidajia = price + (price * 20) / 100;
            /**判断一下，他这个区间范围，最大能增加20%*/

            if (jiajiage <= zuidajia) {
                mTotalPriceTv.setText(String.valueOf(jiajiage));
                mFreightEt.setText(String.valueOf(jiajiage));
            } else {
                mTotalPriceTv.setText(String.valueOf(zuidajia));
                mFreightEt.setText(String.valueOf(zuidajia));
                showToast("已达到价格上线，无法继续上调");
                return;
            }


        }
        /**减号*/
        else if (v.getId() == R.id.jianhao) {
            //String aa=mFreightEt.getText().toString();
            ///int qujian=Integer.parseInt(aa);
            int tltalprice = Integer.parseInt(jiajianprice);
            int jiajiage = tltalprice - (tltalprice * 5) / 100;
            int zuidajian = price - (price * 20) / 100;
            if (jiajiage >= zuidajian) {
                mTotalPriceTv.setText(String.valueOf(jiajiage));
                mFreightEt.setText(String.valueOf(jiajiage));
            } else {
                mTotalPriceTv.setText(String.valueOf(zuidajian));
                mFreightEt.setText(String.valueOf(zuidajian));
                showToast("已达到价格下线，无法继续下调");
                return;
            }
        }
        /**自送CheckBox被选中*/
        else if (v.getId() == R.id.pcSendBySelfCb) {
            //mxezsdTV.setVisibility(View.VISIBLE);
            mxezsdTV.setVisibility(View.GONE);
            if (mSendBySelfCb.isChecked()) {
                if (mPickBySelfCb.isChecked()) {
                    int xiaokuangkuang = price;
                    int jianshao = xiaokuangkuang * 20 / 100;
                    int zongshu = xiaokuangkuang - jianshao;
                    String adbd = String.valueOf(zongshu);
                    mTotalPriceTv.setText(adbd);
                    // mFreightEt.setText(adbd);
                    mMinPrice = (int) (zongshu * 0.8);
                    mMaxPrice = (int) (zongshu * 1.2);
                    mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
                } else {
                    int xiaokuangkuang = price;
                    int jianshao = xiaokuangkuang * 10 / 100;
                    int zongshu = xiaokuangkuang - jianshao;
                    String adbd = String.valueOf(zongshu);
                    mTotalPriceTv.setText(adbd);
                    //mFreightEt.setText(adbd);
                    mMinPrice = (int) (zongshu * 0.8);
                    mMaxPrice = (int) (zongshu * 1.2);
                    mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
                }
            } else {
                if (mPickBySelfCb.isChecked()) {
                    int xiaokuangkuang = price;
                    int jianshao = xiaokuangkuang * 10 / 100;
                    int zongshu = xiaokuangkuang - jianshao;
                    String adbd = String.valueOf(zongshu);
                    mTotalPriceTv.setText(adbd);
                    //mFreightEt.setText(adbd);
                    mMinPrice = (int) (zongshu * 0.8);
                    mMaxPrice = (int) (zongshu * 1.2);
                    mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
                } else {
                    int xiaokuangkuang = price;
                    String adbd = String.valueOf(xiaokuangkuang);
                    mTotalPriceTv.setText(adbd);
                    // mFreightEt.setText(adbd);
                    mMinPrice = (int) (xiaokuangkuang * 0.8);
                    mMaxPrice = (int) (xiaokuangkuang * 1.2);
                    mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
                }
            }

        }
        /**自提CheckBox被选中*/
        else if (v.getId() == R.id.pcPickBySelfCb) {
            //mxeztdTV.setVisibility(View.VISIBLE);
            mxeztdTV.setVisibility(View.GONE);
            if (mPickBySelfCb.isChecked()) {
                if (mSendBySelfCb.isChecked()) {
                    int xiaokuangkuang = price;
                    int jianshao = xiaokuangkuang * 20 / 100;
                    int zongshu = xiaokuangkuang - jianshao;
                    String adbd = String.valueOf(zongshu);
                    mTotalPriceTv.setText(adbd);
                    // mFreightEt.setText(adbd);
                    mMinPrice = (int) (zongshu * 0.8);
                    mMaxPrice = (int) (zongshu * 1.2);
                    mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
                } else {
                    int xiaokuangkuang = price;
                    int jianshao = xiaokuangkuang * 10 / 100;
                    int zongshu = xiaokuangkuang - jianshao;
                    String adbd = String.valueOf(zongshu);
                    mTotalPriceTv.setText(adbd);
                    //mFreightEt.setText(adbd);
                    mMinPrice = (int) (zongshu * 0.8);
                    mMaxPrice = (int) (zongshu * 1.2);
                    mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
                }

            } else {
                if (mSendBySelfCb.isChecked()) {
                    int xiaokuangkuang = price;
                    int jianshao = xiaokuangkuang * 10 / 100;
                    int zongshu = xiaokuangkuang - jianshao;
                    String adbd = String.valueOf(zongshu);
                    mTotalPriceTv.setText(adbd);
                    //mFreightEt.setText(adbd);
                    mMinPrice = (int) (zongshu * 0.8);
                    mMaxPrice = (int) (zongshu * 1.2);
                    mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
                } else {
                    int xiaokuangkuang = price;
                    String adbd = String.valueOf(xiaokuangkuang);
                    mTotalPriceTv.setText(adbd);
                    //mFreightEt.setText(adbd);
                    mMinPrice = (int) (xiaokuangkuang * 0.8);
                    mMaxPrice = (int) (xiaokuangkuang * 1.2);
                    mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
                }
            }

        }
        /**弹出框--详细内容运费、优惠券、保险的FrameLayout*/
        else if (v.getId() == R.id.pcPopLayout) {
            mPopView.setVisibility(View.GONE);
            mArrowIv.setImageResource(R.drawable.publish_ic_arrow_down2);
        }
        /**确认提交跳转到下一个提交的activty*/
//        else if (v.getId() == R.id.pcOkBtn) {
//            //点击这里弹出要给文本框选择价格的方式
////            Toast.makeText(this, "点击事被执行", Toast.LENGTH_SHORT).show();
////            startActivity(new Intent(this, OrderConfirmActivity.class));
//
//
//        }
        /**保险服务*/
        else if (v.getId() == R.id.pcInsuranceTv) {
            InsuranceDialog dialog = new InsuranceDialog();
            dialog.setOnInsuranceItemClickListener(new InsuranceDialog.OnInsuranceItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(Insurance insurance, int position) {
                    mInsuranceTv.setText(insurance.title + "(" + insurance.price + "元)");
                    mInsurancePrice = Integer.parseInt(insurance.price);
                    if (mInsurancePrice == 0) {
                        mPublishBean.insurance = "0";
                    } else {
                        mPublishBean.insurance = String.valueOf(position);
                    }
                    calculateTotalPrice();
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
        }
        /**发票服务*/
        else if (v.getId() == R.id.pcReceiptCb) {
            if (mReceiptCb.isChecked()) {

                new AlertDialog.Builder(this)
                        .setMessage("您尚无发票信息,请前往更新发票信息？")
                        .setNegativeButton("取消", (dialog, which) -> mReceiptCb.setChecked(false))
                        .setPositiveButton("前往", (dialog, which) -> {
                            mReceiptCb.setChecked(false);
                            startActivityForResult(new Intent(mContext, ReceiptActivity.class), RESULT_FAPIAO);
                        }).show();
            } else {
                mReceiptView.setVisibility(View.GONE);

                mfuwufei = 0;
                calculateTotalPrice();
            }
        }
        /**代付服务*/
        else if (v.getId() == R.id.pcHelpPayCb) {
            if (mHelpPayCb.isChecked()) {
                mHelpPayView.setVisibility(View.VISIBLE);
            } else {
                mHelpPayView.setVisibility(View.GONE);
            }
        }
        /**代取服务*/
        else if (v.getId() == R.id.pcHelpGetCb) {
            if (mHelpGetCb.isChecked()) {
                mHelpGetView.setVisibility(View.VISIBLE);
            } else {
                mHelpGetView.setVisibility(View.GONE);
            }
        }
        /**优惠券*/
        else if (v.getId() == R.id.pcCouponTv) {
            if (mFreightPrice == 0) {
                showToast("请先输入运费价格");
                return;
            }
        }
        /**代收*/
        else if (v.getId() == R.id.pcHelpGetLayout) {
            Intent intent = new Intent(this, FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_GET);
        }
        /**代付*/
        else if (v.getId() == R.id.pcHelpPayLayout) {
            Intent intent = new Intent(this, FriendActivity.class);
            startActivityForResult(intent, RESULT_FRIEND_PAY);
        }
        /**备注*/
        else if (v.getId() == R.id.pcRemarkTv) {
            if (mRemarkDialog == null) {
                mRemarkDialog = new RemarkDialog();
                mRemarkDialog.setOnRemarkConfirmListener(new RemarkDialog.OnRemarkConfirmListener() {
                    @Override
                    public void onRemarkConfirm(String content) {
                        mRemarkTv.setText(content);

                        //设置备注
                        mPublishBean.remark = content;
                    }
                });
            }
            mRemarkDialog.show(Objects.requireNonNull(getSupportFragmentManager()), "remark");
        }
    }

    //
    @OnClick(R.id.pcOkBtn)
    public void postOrder() {
        if(!MyUtils.isFastClick())
            return;

        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_paotui.getValue()) {

        } else {
            if (mFreightPrice == 0) {
                showToast("请先输入运费价格");
                return;
            }
        }


        /**是否要发票*/
        if (mReceiptCb.isChecked()) {
            String taxNum = mTaxNumEt.getText().toString();
//            if (TextUtils.isEmpty(taxNum)) {
//                showToast("请输入税号");
//                return;
//            }
            String unitName = mUnitNameEt.getText().toString();
//            if (TextUtils.isEmpty(unitName)) {
//                showToast("请输入单位名称");
//                return;
//            }
            String email = mEmailEt.getText().toString();
            mPublishBean.taxNum = taxNum;
            mPublishBean.unitName = unitName;
            mPublishBean.email = email;
        }
        /**是否要代收货款*/
        if (mHelpGetCb.isChecked()) {
            String getPrice = mHelpGetPriceEt.getText().toString();
            if (TextUtils.isEmpty(getPrice)) {
                showToast("请输入代收货款");
                return;
            }
            mPublishBean.getPrice = getPrice;
        }
        /**请选择代付人*/
        if (mHelpPayCb.isChecked() && TextUtils.isEmpty(mPublishBean.helpPayId)) {
            showToast("请选择代付人");
            return;
        }
        /**请选择代收人*/
        if (mHelpGetCb.isChecked() && TextUtils.isEmpty(mPublishBean.helpGetId)) {
            showToast("请选择代收人");
            return;
        }
        /**长途零担判断自送和自提*/
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO.getValue()) {
            mPublishBean.isSendBySelf = mSendBySelfCb.isChecked() ? "1" : "0";
            mPublishBean.isPickBySelf = mPickBySelfCb.isChecked() ? "1" : "0";
        }
        mPublishBean.display = mpcsimiCb.isChecked() ? "1" : "0";


        mPublishBean.freight = mTotalPriceTv.getText().toString();

        mPublishBean.isReceipt = mReceiptCb.isChecked() ? "1" : "0";
        mPublishBean.isHelpPay = mHelpPayCb.isChecked() ? "1" : "0";
        mPublishBean.isHelpGet = mHelpGetCb.isChecked() ? "1" : "0";
        mPublishBean.finalPrice = mTotalPriceTv.getText().toString();
        mPublishBean.date = mDateFormat.format(new Date());


//        /**微信支付被选中*/
//        if(mweixincb.isChecked())
//        {
//            switch (rgTabGroup.getCheckedRadioButtonId()) {
//                case R.id.rb_owner:
//                   paytype="1";
//                    break;
//                case R.id.rb_driver:
//                    paytype="2";
//                    break;
//            }
//
////            new AlertDialog.Builder(this).setMessage("全额支付：支付全款后形成发货订单,有利于司机更好的为您接单\n保  证  金：预先支付10%的运费后,形成发货订单,确认收货后支付剩余尾款")
////                    .setNegativeButton("取消", null)
////                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            SimpleDateFormat s_format = new SimpleDateFormat("hhmmss");
////
////                            Map<String, String> params = new HashMap<>();
////                            params.put("appname", AppUtils.getAppName());
////                            params.put("token", "acd890f765efd007cbb5701fd1ac7ae0");
////                            params.put("product_name", mPublishBean.productName);
////                            params.put("describe", mPublishBean.defaultWeight + "吨" + mPublishBean.defaultArea + "方");
////                            params.put("orderid","3569");
////                            params.put("type", "1");//1代表微信支付
////                            params.put("pay_type","APP");
////                            String kuangchanyunfei = mmoney.getSelectedItem().toString();
////                            switch (rgTabGroup.getCheckedRadioButtonId()) {
////                                case R.id.rb_owner:
////
////                                    xzfjfs = Double.valueOf(mPublishBean.freight);
////                                    canshutijiao="1";
////                                    break;
////                                case R.id.rb_driver:
////                                    if(Double.valueOf(mPublishBean.freight)*10/100>=300)
////                                    {
////                                        xzfjfs = 300;
////                                    }
////                                    else
////                                    {
////                                        xzfjfs = Double.valueOf(mPublishBean.freight)*10/100;
////                                    }
////                                    canshutijiao="0";
////                                    break;
////                            }
////                            params.put("price", xzfjfs + "");
////                            params.put("daishou", "0");
////
////                            params.put("total_fee", xzfjfs * 100 + "");
////
////                           // params.put("orderid", "19860509");
////
////                          //  mXrwlwxpayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
////                            mPresenter.xrwlwxpay(params);
////
////
//////                               mPayDialog = LoadingProgress.showProgress(mContext, "正在发起支付...");
////  //                            mPresenter.wxPay(params);
////                        }
////                    }).show();
//        }
//        /**余额支付被选中*/
//        else if(myuecb.isChecked())
//        {
//            Map<String, String> params = new HashMap<>();
//            params.put("Payment_method","3");
//            params.put("orderid", "19860509");
//            switch (rgTabGroup.getCheckedRadioButtonId()) {
//                case R.id.rb_owner:
//                    xzfjfs = Double.valueOf(mPublishBean.freight);
//                    canshutijiao="1";
//                    params.put("balance","0");
//                    params.put("requestrenminbi",mPublishBean.freight);
//                    params.put("zhifuren","0");
//                    break;
//                case R.id.rb_driver:
//                    if(Double.valueOf(mPublishBean.freight)*10/100>=300)
//                    {
//                        xzfjfs = 300;
//                        params.put("balance","1");
//                        params.put("requestrenminbi","300");
//                    }
//                    else
//                    {
//                        xzfjfs = Double.valueOf(mPublishBean.freight)*10/100;
//                        params.put("balance","1");
//                        params.put("requestrenminbi",xzfjfs+"");
//                    }
//                    canshutijiao="0";
//                    break;
//            }
//            if(myyue>=xzfjfs)
//            {
//                mPresenter.results(params);//这个是更改数据库余额支付的变化情况
//                //余额支付9.8折暂时去掉，恢复即可使用  mPresenter.yuepay(String.valueOf((xzfjfs-(xzfjfs *98 /100))),"6666666666666666",mPostOrder.orderId,canshutijiao);
//                mPresenter.yuepay(String.valueOf(xzfjfs),"6666666666666666","19860509",canshutijiao);
//                /**发送短信*/
//                SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");
//                Date curDate =  new Date(System.currentTimeMillis());
//                String   str   =   formatter.format(curDate);
//                try {
//                    mPresenter.getCodeButton(mPublishBean.consignorPhone,"1", URLDecoder.decode(mPublishBean.longStartCityDes,"UTF-8"),URLDecoder.decode(mPublishBean.longEndCityDes,"UTF-8"),str);
//                    mPresenter.getCodeButtontwo(mPublishBean.consignorPhone,"2",URLDecoder.decode(mPublishBean.longStartCityDes,"UTF-8"),URLDecoder.decode(mPublishBean.longEndCityDes,"UTF-8"),URLDecoder.decode(mPublishBean.consigneeName,"UTF-8"),mPublishBean.consigneePhone);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//            else
//            {
//                new AlertDialog.Builder(this).setMessage("余额不足，请前往充值")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                startActivity(new Intent(mContext, ChongzhiActivity.class));
//                            }
//                        }).show();
//            }
//        }
//        /**支付宝支付被选中*/
//        else if(mzhifubaocb.isChecked())
//        {
//
//            new AlertDialog.Builder(this).setMessage("全额支付：支付全款后形成发货订单,有利于司机更好的为您接单\n保  证  金：预先支付10%的运费后,形成发货订单,确认收货后支付剩余尾款")
//                    .setNegativeButton("取消", null)
//                    .setPositiveButton("确定", new  DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(mContext, PayDemosActivity.class);
//                            intent.putExtra("etGoodsName", mPublishBean.productName);
//                            String dun="";String fang="";String jian="";
//                            if(!TextUtils.isEmpty(mPublishBean.defaultWeight)){dun=mPublishBean.defaultWeight+"吨  ";} else {dun="";}
//                            if(!TextUtils.isEmpty(mPublishBean.defaultArea)){fang=mPublishBean.defaultArea+"方  ";}else { fang="";}
//                            if(!TextUtils.isEmpty(mPublishBean.defaultNum)){jian=mPublishBean.defaultNum+"件  ";}else{ jian="";}
//                            String kuangchanyunfei = mmoney.getSelectedItem().toString();
//                            switch (rgTabGroup.getCheckedRadioButtonId()) {
//                                case R.id.rb_owner:
//                                    xzfjfs = Double.valueOf(mPublishBean.freight);
//                                    intent.putExtra("zfb", "0");
//                                    intent.putExtra("zhifuren", "0");
//                                    intent.putExtra("requestrenminbi", xzfjfs+"");
//                                    canshutijiao="1";
//                                    break;
//                                case R.id.rb_driver:
//                                    if(Double.valueOf(mPublishBean.freight)*10/100>=300)
//                                    {
//                                        xzfjfs = 300;
//                                        intent.putExtra("requestrenminbi", xzfjfs+"");
//                                        intent.putExtra("zfb", "1");
//                                    }
//                                    else
//                                    {
//                                        xzfjfs = Double.valueOf(mPublishBean.freight)*10/100;
//                                        intent.putExtra("requestrenminbi", xzfjfs+"");
//                                        intent.putExtra("zfb", "1");
//                                    }
//                                    canshutijiao="0";
//                                    break;
//                            }
//                            intent.putExtra("describe", dun + fang +jian);
//                            intent.putExtra("id", "19860509");
//
//                            intent.putExtra("pay_type", canshutijiao + "");
//                            intent.putExtra("price", xzfjfs + "");
//                            startActivity(intent);
//                        }
//                    }).show();
//        }
//        /**银行卡选中*/
//        else if(myinhangkacb.isChecked())
//        {
//            showToast("暂未开放，敬请期待");
//            return;
//        }


        /**这块是提交服务器，提交完成后返回当前id，在执行支付功能
         *
         */

        mPostDialog = LoadingProgress.showProgress(this, "正在提交...");
        mPresenter.postOrder(mPublishBean.imagePaths, mPublishBean.getParams());
        if (mHelpPayCb.isChecked()) {
            mPresenter.postOrder1(mPublishBean.imagePaths, mPublishBean.getParams());
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculateTotalPrice() {
        mPopFreightTv.setText(String.valueOf(mFreightPrice));
        mPopCouponTv.setText(String.format("%.1f", mfuwufei));
        float totalPrice = mFreightPrice + mInsurancePrice + mfuwufei;
        mTotalPriceTv.setText(String.format("%.1f", totalPrice));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == RESULT_FAPIAO) {
            mReceiptCb.setChecked(true);
            mfuwufei = mFreightPrice * 0.099f;
            calculateTotalPrice();
        }
        /**代付人*/
        else if (requestCode == RESULT_FRIEND_PAY) {
            Friend friend = (Friend) data.getSerializableExtra("data");
            if (!friend.isRegister()) {
                showToast("好友未注册，暂无法提供代付服务");
                return;
            }
            mHelpPayTv.setText(friend.getName());
            mPublishBean.helpPayId = friend.getId();
        }
        /**长途零担中获取就近中转点自送*/
        else if (requestCode == RESULT_FRIEND_GETs) {
            String nameqin = data.getStringExtra("nameqin");
            String district = data.getStringExtra("district");
            String xaxis = data.getStringExtra("Xaxis");
            String yaxis = data.getStringExtra("Yaxis");
            mPublishBean.sendend_lat = xaxis;
            mPublishBean.sendend_lon = yaxis;
            mxezsdTV.setText(nameqin + district);
        }
        /**长途零担中获取就近中转点自提*/
        else if (requestCode == RESULT_FRIEND_GETss) {
            String district = data.getStringExtra("district");
            String nameqin = data.getStringExtra("nameqin");
            String xaxis = data.getStringExtra("Xaxis");
            String yaxis = data.getStringExtra("Yaxis");
            mPublishBean.pickstart_lat = xaxis;
            mPublishBean.pickstart_lon = yaxis;
            mxeztdTV.setText(nameqin + district);
        } else if (requestCode == RESULT_FRIEND_GET) {//代收
            Friend friend = (Friend) data.getSerializableExtra("data");
            mHelpCollectTv.setText(friend.getName());
            mPublishBean.helpGetId = friend.getId();
        }
    }

    @Override
    public void onRequestSuccessa(BaseEntity<Integer> entity) {
        if(entity.getData() == null)
            entity.setData(0);
        d = entity.getData();
        mPublishBean.nidaye = d.toString();
        price = d;
        price = d;

        jiajianprice = d.toString();


        /**同城零担*/
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_SHORT.getValue()) {
            mFreightEt.setFocusable(true);
            mAreaTv.setVisibility(View.VISIBLE);
            mWeightTv.setVisibility(View.VISIBLE);
            mNumTv.setVisibility(View.VISIBLE);
            mkcys.setVisibility(View.VISIBLE);
            mHelpPayView.setVisibility(View.GONE);
            mRecommendPriceTv.setVisibility(View.VISIBLE);
            mzidongjieshouTv.setVisibility(View.GONE);
            mpcsimiCb.setVisibility(View.GONE);

            mRecommendView.setVisibility(View.VISIBLE);
            // mFreightPrice = (int) mPublishBean.getNidaye();
            mFreightPrice = price;
            mFreightEt.setText(String.valueOf(price));
            mFreightEt.setEnabled(true);
            String qin = "";
            qin = String.valueOf(price);
            mTotalPriceTv.setText(qin);
            mBySelfLayout.setVisibility(View.GONE);
            if (mPublishBean.defaultNo.equals("1")) {
                price = 5;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                mPriceTv.setText(simpleDateFormat.format(date));
                mRecommendPriceTv.setText(String.valueOf(price));
                mFreightEt.setText(String.valueOf(price));
                mMinPrice = (int) (price * 0.8);
                mMaxPrice = (int) (price * 1.2);
                mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
            } else {
                //  price = (int) mPublishBean.getNidaye();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                mPriceTv.setText(simpleDateFormat.format(date));
                mRecommendPriceTv.setText(String.valueOf(price));
                mFreightEt.setText(String.valueOf(price));
                if (!mSendBySelfCb.isChecked() && !mPickBySelfCb.isChecked()) {
                    mTotalPriceTv.setText(String.valueOf(price));
                }
                mMinPrice = (int) (price * 0.8);
                mMaxPrice = (int) (price * 1.2);
                mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
            }
        }
        mFreightEt.setFocusable(true);
        /**同城整车*/
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_zhuanche.getValue()) {
            //同城专车
            mAreaTv.setVisibility(View.VISIBLE);
            mWeightTv.setVisibility(View.VISIBLE);
            mNumTv.setVisibility(View.VISIBLE);
            mBySelfLayout.setVisibility(View.GONE);

            if(mPublishBean.truck != null)
                mTruckTv.setText("车型：" + mPublishBean.truck.getTitle());

            mRecommendView.setVisibility(View.VISIBLE);
            mFreightPrice = price;
            mFreightEt.setText(String.valueOf(price));
            mFreightEt.setEnabled(true);
            String yan = "";
            yan = String.valueOf(price);
            mTotalPriceTv.setText(yan);
            mkcys.setVisibility(View.VISIBLE);
            mRecommendPriceTv.setVisibility(View.VISIBLE);
            mzidongjieshouTv.setVisibility(View.GONE);
            mpcsimiCb.setVisibility(View.GONE);
            if (mPublishBean.defaultNo.equals("1")) {
                price = 5;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                mPriceTv.setText(simpleDateFormat.format(date));
                mRecommendPriceTv.setText(String.valueOf(price));
                mMinPrice = (int) (price * 0.8);
                mMaxPrice = (int) (price * 1.2);
                mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
            } else {
                // price = (int) mPublishBean.getNidaye();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
                Date date = new Date(System.currentTimeMillis());
                mPriceTv.setText(simpleDateFormat.format(date));
                if (!mSendBySelfCb.isChecked() && !mPickBySelfCb.isChecked()) {
                    mTotalPriceTv.setText(String.valueOf(price));
                }
                mRecommendPriceTv.setText(String.valueOf(price));
                mFreightEt.setText(String.valueOf(price));
                mMinPrice = (int) (price * 0.8);
                mMaxPrice = (int) (price * 1.2);
                mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
            }

        }
        /**长途零担*/
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_TOTAL.getValue()) {
            mFreightEt.setFocusable(true);
            mAreaTv.setVisibility(View.VISIBLE);
            mWeightTv.setVisibility(View.VISIBLE);
            mNumTv.setVisibility(View.VISIBLE);
            mFreightPrice = price;
            mFreightEt.setText(String.valueOf(price));

            String yan = "";
            yan = String.valueOf(price);
            mTotalPriceTv.setText(yan);
            mpcsimiCb.setVisibility(View.GONE);
            mkcys.setVisibility(View.VISIBLE);
            mRecommendPriceTv.setVisibility(View.VISIBLE);
            mzidongjieshouTv.setVisibility(View.GONE);
            mBySelfLayout.setVisibility(View.GONE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            mPriceTv.setText(simpleDateFormat.format(date));
            if (!mSendBySelfCb.isChecked() && !mPickBySelfCb.isChecked()) {
                mTotalPriceTv.setText(String.valueOf(price));
            }
            mRecommendPriceTv.setText(String.valueOf(price));
            mFreightEt.setText(String.valueOf(price));
            mMinPrice = (int) (price * 0.8);
            mMaxPrice = (int) (price * 1.2);
            mPriceHintTv.setText("请在" + mMinPrice + "-" + mMaxPrice + "之间选择输入");
        }

    }

    @Override
    public void onRequestSuccessalingdan(BaseEntity<Changtulingdan> entity) {
        Changtulingdan dd = entity.getData();
        String max = dd.max;
        String min = dd.min;
        String self_deliver = dd.self_deliver;
        String self_mention = dd.self_mention;
        mPublishBean.nidaye = String.valueOf(dd.price);
        price = dd.price;
        jiajianprice = String.valueOf(dd.price);
        /**长途整车mTotalPriceTv*/
        if (mPublishBean.category == CategoryDialog.CategoryEnum.TYPE_LONG_ZERO.getValue()) {
            mFreightEt.setFocusable(true);
            mAreaTv.setVisibility(View.VISIBLE);
            mWeightTv.setVisibility(View.VISIBLE);
            mNumTv.setVisibility(View.VISIBLE);
            mFreightPrice = price;
            mFreightEt.setText(String.valueOf(price));
            mBySelfLayout.setVisibility(View.VISIBLE);
            //mBySelfLayout.setVisibility(View.GONE);

            if(mPublishBean.truck != null)
                mTruckTv.setText("车型：" + mPublishBean.truck.getTitle());

            String yan = "";
            yan = String.valueOf(price);
            mTotalPriceTv.setText(yan);
            mpcsimiCb.setVisibility(View.GONE);
            mkcys.setVisibility(View.VISIBLE);
            mRecommendPriceTv.setVisibility(View.VISIBLE);
            mzidongjieshouTv.setVisibility(View.GONE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            mPriceTv.setText(simpleDateFormat.format(date));
            if (!mSendBySelfCb.isChecked() && !mPickBySelfCb.isChecked()) {
                mTotalPriceTv.setText(String.valueOf(price));
            }
            mRecommendPriceTv.setText(String.valueOf(price));


            //这个是传递价格的参数，主要是货主做zhi'f
            mFreightEt.setText(yan);


            mMinPrice = (int) (price * 0.8);
            mMaxPrice = (int) (price * 1.2);

        }
    }

    @Override
    public void onRefreshSuccess(BaseEntity<PostOrder> entity) {

        Intent intent = new Intent(this, OrderConfirmActivity.class);
        intent.putExtra("publishBean", mPublishBean);
        intent.putExtra("paytype", paytype);
        intent.putExtra("postOrder", entity.getData().orderId);
        startActivity(intent);
        finish();


//        mPublishBean.freight = mTotalPriceTv.getText().toString();
//
//        mPresenter.postOrder(mPublishBean.imagePaths, mPublishBean.getParams());
//        Intent intent = new Intent(PublishConfirmActivity.this, OrderConfirmActivity.class);
//        intent.putExtra("publishBean", mPublishBean);
//        intent.putExtra("paytype", paytype);
//        intent.putExtra("postOrder", entity.getData().orderId);
//        startActivity(intent);

    }

    @Override
    public void onRefreshError(Throwable e) {
        this.e = e;
        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }
        if (mOnlinePayDialog != null) {
            mOnlinePayDialog.dismiss();
        }
        if (mXrwlwxpayDialog != null) {
            mXrwlwxpayDialog.dismiss();
        }
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }
        if (mOnlinePayDialog != null) {
            mOnlinePayDialog.dismiss();
        }
        if (mXrwlwxpayDialog != null) {
            mXrwlwxpayDialog.dismiss();
        }
        handleError(entity);
    }

    @Override
    public void onOnlinePaySuccess(BaseEntity<PayResult> entity) {
        mOnlinePayDialog.dismiss();
        showToast("提交成功");
//        EventBus.getDefault().post(new PublishClearCacheEvent());
//        startActivity(new Intent(this, OwnerOrderActivity.class));
        finish();
    }

    @Override
    public void resultsSuccess(BaseEntity<PayResult> entity) {
        mXrwlwxpayDialog.dismiss();
        showToast("操作成功");
        handleError(entity);
        finish();
    }


    //现在这个方法暂时闲置
    @Override
    public void wxonOnlinePaySuccess(BaseEntity<PayResult> entity) {

        mXrwlwxpayDialog.dismiss();
        /**
         * 调用微信支付php*/
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

    /**
     * 判断剩余余额
     */
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
        showNetworkError();
    }

    @Override
    public void onHongbaoSuccess() {
    }

    @Override
    public void onHongbaoError(BaseEntity entity) {
    }

    @Override
    public void onHongbaoException(Throwable e) {
    }


    /**
     * 获取验证码支付成功和失败返回的信息
     */
    @Override
    public void onGetCodeSuccess(BaseEntity<MsgCode> entity) {
        MsgCode mc = entity.getData();
        showToast("短信已发送");
//        if (!TextUtils.isEmpty(mc.status) && mc.status.equals("0")) {
//            new AlertDialog.Builder(this)
//                    .setMessage(entity.getMsg())
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).show();
//        } else {
//
//            showToast("短信已发送");
//        }
    }

    @Override
    public void onGetCodeError(Throwable e) {
//        showToast("发送失败，请重试");
    }

    private class PayBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getIntExtra("type", 0) == 0) {
                showToast("付款成功");
//                Map<String, String> params = new HashMap<>();
//
//                SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");
//                Date curDate =  new Date(System.currentTimeMillis());
//                String   str   =   formatter.format(curDate);
//                params.put(" ", "3569");
//                params.put("Payment_method","1");
//                //params.put("userid", userid);
//                double  aa = mPublishBean.getNidaye();
//                double bb = mPublishBean.getNidaye()*10/100;
//                if(canshutijiao.equals("1"))//1全额付款,0是付10%的订金
//                {
//                    params.put("wx", "0");
//                    params.put("zhifuren","0");  //这个肯定是发货人进行支付
//                    //params.put("quankuan", "0");
//                    params.put("requestrenminbi",String.valueOf(xzfjfs));
//                }
//                else
//                {
//                    params.put("wx", "1");
//                    //params.put("quankuan", "1");
//                    params.put("requestrenminbi",String.valueOf(xzfjfs));
//
//                }
//                // mPresenter.wxonlinePay(params);
//                mPresenter.results(params);
//
//
//
//
//                showToast("付款成功");
//                /**发送短信*/
//
//                try {
//                    mPresenter.getCodeButton(mPublishBean.consignorPhone,"1", URLDecoder.decode(mPublishBean.longStartCityDes,"UTF-8"),URLDecoder.decode(mPublishBean.longEndCityDes,"UTF-8"),str);
//                    mPresenter.getCodeButton(mPublishBean.consigneePhone,"1",URLDecoder.decode(mPublishBean.longStartCityDes,"UTF-8"),URLDecoder.decode(mPublishBean.longEndCityDes,"UTF-8"),str);
//                    mPresenter.getCodeButtontwo(mPublishBean.consignorPhone,"2",URLDecoder.decode(mPublishBean.longStartCityDes,"UTF-8"),URLDecoder.decode(mPublishBean.longEndCityDes,"UTF-8"),URLDecoder.decode(mPublishBean.consigneeName,"UTF-8"),mPublishBean.consigneePhone);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                startActivity(new Intent(mContext, PublishSuccessActivity.class));
//                finish();
//            } else {
//                showToast("付款失败");
//
            }
        }

    }
}
