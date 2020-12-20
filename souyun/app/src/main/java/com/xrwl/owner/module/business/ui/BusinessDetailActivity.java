package com.xrwl.owner.module.business.ui;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.event.BusinessListRefreshEvent;
import com.xrwl.owner.module.business.mvp.BusinessDetailContract;
import com.xrwl.owner.module.business.mvp.BusinessDetailPresenter;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/8/16 下午8:30.
 */
public class BusinessDetailActivity extends BaseActivity<BusinessDetailContract.IView, BusinessDetailPresenter> implements BusinessDetailContract.IView {

    @BindView(R.id.fahuoren)
    TextView mfahuoren;

    @BindView(R.id.fahuoaddress)
    TextView mfahuoaddress;


    @BindView(R.id.detailTv)
    TextView mDetailTv;

    @BindView(R.id.shouhuoren)
    TextView mshouhuoren;

    @BindView(R.id.address)
    TextView maddress;

    @BindView(R.id.businessItemTimejiageTv)
    TextView mbusinessItemTimejiageTv;

    @BindView(R.id.mstitle)
    TextView mmmstitle;


    @BindView(R.id.ddbh)
    TextView mddbh;


    @BindView(R.id.diyi)
    LinearLayout mdiyi;

    @BindView(R.id.dier)
    LinearLayout mdier;

    @BindView(R.id.disan)
    LinearLayout mdisan;

    @BindView(R.id.disi)
    LinearLayout mdisi;


    @BindView(R.id.businessItemLayout)
    FrameLayout mmbusinessItemLayout;


    @BindView(R.id.xianshipictrue)
    ImageView mxianshipictrue;

    @BindView(R.id.ddsrc)
    ImageView mddsrc;

    @Override
    protected BusinessDetailPresenter initPresenter() {
        return new BusinessDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.business_detail_activity;
    }

    @Override
    protected void initViews() {
        String id = getIntent().getStringExtra("id");
        String canshu = getIntent().getStringExtra("canshu");
        String canshupic = getIntent().getStringExtra("pic");
        mPresenter.requestData(id);

        if("3".equals(canshu))
        {
            Glide.with(this).load(canshupic).into(mxianshipictrue);
            mdiyi.setVisibility(View.GONE);
            mdier.setVisibility(View.GONE);
            mdisan.setVisibility(View.GONE);
            mmbusinessItemLayout.setVisibility(View.GONE);
            mdisi.setVisibility(View.VISIBLE);
            mmmstitle.setVisibility(View.GONE);
            String product_name = "";

            if(TextUtils.isEmpty(getIntent().getStringExtra("mcontent")) || null==getIntent().getStringExtra("mcontent"))
            {
                mDetailTv.setText("");
            }
            else
            {
                try {
                    product_name = URLDecoder.decode(getIntent().getStringExtra("mcontent"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                mDetailTv.setText(product_name);
            }
       }
        else
        {
            Glide.with(this).load(canshupic).into(mddsrc);
            mdiyi.setVisibility(View.VISIBLE);
            mdier.setVisibility(View.VISIBLE);
            mdisan.setVisibility(View.VISIBLE);
            mmbusinessItemLayout.setVisibility(View.VISIBLE);
            mdisi.setVisibility(View.VISIBLE);

            String product_name = "";
            try {
                product_name = URLDecoder.decode(getIntent().getStringExtra("product_name"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            mDetailTv.setText("货名："+product_name);

            String mget_person = ""; String mget_phone = "";
            try {
                mget_person = URLDecoder.decode(getIntent().getStringExtra("mget_person"), "UTF-8");
                mget_phone = URLDecoder.decode(getIntent().getStringExtra("mget_phone"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            mshouhuoren.setText("收货人：  "+mget_person+"         "+mget_phone);


            String mmaddress = "";
            try {
                if (getIntent().getStringExtra("mend_province")!=null){
                    mmaddress = URLDecoder.decode(getIntent().getStringExtra("mend_province"), "UTF-8")+"  "+URLDecoder.decode(getIntent().getStringExtra("mend_city"), "UTF-8")+"  "+URLDecoder.decode(getIntent().getStringExtra("mend_area"), "UTF-8")+"  "+URLDecoder.decode(getIntent().getStringExtra("mend_desc"), "UTF-8")  ;
                }

            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            maddress.setText("收货地址：  "+mmaddress);
            String jiagegege = "";
            try {
                if(getIntent().getStringExtra("mtotal_price")!= null ){
                    jiagegege = URLDecoder.decode(getIntent().getStringExtra("mtotal_price"), "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            mbusinessItemTimejiageTv.setText("金额："+jiagegege+"元");


            String mtitle = "";
            try {
                mtitle = URLDecoder.decode(getIntent().getStringExtra("mtitle"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            mmmstitle.setText(mtitle);


            String mddbhs = "";
            try {
                mddbhs = URLDecoder.decode(getIntent().getStringExtra("id"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            mddbh.setText("订单编号：20190621"+mddbhs);
        }

    }

    @Override
    public void onRefreshSuccess(BaseEntity entity) {

        EventBus.getDefault().post(new BusinessListRefreshEvent());
    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
    }
}