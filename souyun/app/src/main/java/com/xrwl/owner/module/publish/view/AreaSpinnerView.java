package com.xrwl.owner.module.publish.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address;
import com.xrwl.owner.db.AreaDao;

import java.util.List;

public class AreaSpinnerView extends LinearLayout {

    private AreaDao mAreaDao;
    private OnAreaSpinnerSelectListener mListener;
    private FragmentManager mFm;

    public AreaSpinnerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AreaSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AreaSpinnerView(Context context) {
        super(context);
    }

    private CustomSpinner spiProvince;
    private CustomSpinner spiCity;
    private CustomSpinner spiArea;

    private List<Address> provinces;
    private List<Address> citys;
    private List<Address> areas;

    private int curProvinceId, curCityId, curAreaId;
    private String curProvinceDes, curCityDes, curAreaDes;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        spiProvince = findViewById(R.id.areaProvinceSpinner);
        spiCity = findViewById(R.id.areaCitySpinner);
        spiArea = findViewById(R.id.areaAreaSpinner);

    }

    public void init(FragmentManager fm) {
        mFm = fm;
        setAdapter();
        setSpinnerListener();
    }

    public void clear() {
        spiCity.setText("");
        spiArea.setText("");
        spiProvince.setText("");
    }

    private void setAdapter() {
        mAreaDao = new AreaDao(getContext());
        provinces = mAreaDao.getAllProvinces();
        spiProvince.setDatas(provinces, mFm);
    }

    /** Spinner设置监听器 */
    private void setSpinnerListener() {
        spiProvince.setOnCustomSpinnerItemClickListener(new CustomSpinner.OnCustomSpinnerItemClickListener() {
            @Override
            public void onItemClick(Address address) {
                spiCity.setText(getContext().getString(R.string.hint_please_select));
                spiArea.setText(getContext().getString(R.string.hint_please_select));
                if (mListener != null) {
                    mListener.onProSelect(address);
                    mListener.onCitySelect(null);
                }
                setCitySpiAdapter(address.getId(), address.getName());
            }
        });
        spiCity.setOnCustomSpinnerItemClickListener(new CustomSpinner.OnCustomSpinnerItemClickListener() {
            @Override
            public void onItemClick(Address address) {
                if (mListener != null) {
                    mListener.onCitySelect(address);
                }
                spiArea.setText(getContext().getString(R.string.hint_please_select));
                setAreaSpiAdapter(address.getId(), address.getName());
            }
        });
        spiArea.setOnCustomSpinnerItemClickListener(new CustomSpinner.OnCustomSpinnerItemClickListener() {
            @Override
            public void onItemClick(Address address) {
                curAreaId = address.getId();
                curAreaDes = address.getName();
            }
        });
    }

    private void setCitySpiAdapter(int provinceId, String name) {
        curProvinceId = provinceId;
        curProvinceDes = name;

        citys = mAreaDao.getAllCityByProId(provinceId);
        spiCity.setDatas(citys, mFm);
    }

    private void setAreaSpiAdapter(int cityId, String name) {
        curCityId = cityId;
        curCityDes = name;

        areas = mAreaDao.getAllZoneByCityId(cityId);
        spiArea.setDatas(areas, mFm);
    }

    public int getCurProvinceId() {
        return curProvinceId;
    }

    public int getCurCityId() {
        return curCityId;
    }

    public int getCurAreaId() {
        return curAreaId;
    }

    public String getCurProvinceDes() {
        return curProvinceDes;
    }

    public String getCurCityDes() {
        return curCityDes;
    }

    public String getCurAreaDes() {
        return curAreaDes;
    }

    public void setOnAreaSpinnerSelectListener(OnAreaSpinnerSelectListener l) {
        mListener = l;
    }

    public interface OnAreaSpinnerSelectListener {
        void onProSelect(Address pro);
        void onCitySelect(Address city);
    }
}
