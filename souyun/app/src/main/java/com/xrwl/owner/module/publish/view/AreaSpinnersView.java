package com.xrwl.owner.module.publish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ldw.library.adapter.listview.CommonAdapter;
import com.ldw.library.adapter.listview.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address;
import com.xrwl.owner.db.AreaDao;

import java.util.List;

public class AreaSpinnersView extends LinearLayout {

    private AreaDao mAreaDao;
    private OnAreaSpinnerSelectListener mListener;

    public AreaSpinnersView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AreaSpinnersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AreaSpinnersView(Context context) {
        super(context);
    }

    private Spinner spiProvince;
    private Spinner spiCity;
    private Spinner spiArea;

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

        if (isInEditMode()) {
            return;
        }

        setAdapter();
        setSpinnerListener();
    }

    private void setAdapter() {
        mAreaDao = new AreaDao(getContext());
        provinces = mAreaDao.getAllProvinces();
        spiProvince.setAdapter(new AreaSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, provinces));

        //设置第一个省份对应的城市
        setCitySpiAdapter(provinces.get(0).getId(), provinces.get(0).getName());
    }

    /** Spinner设置监听器 */
    private void setSpinnerListener() {
        spiProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setCitySpiAdapter(provinces.get(position).getId(), provinces.get(position).getName());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spiCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onCitySelect(citys.get(position));
                }
                setAreaSpiAdapter(citys.get(position).getId(), citys.get(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spiArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curAreaId = areas.get(position).getId();
                curAreaDes = areas.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCitySpiAdapter(int provinceId, String name) {
        curProvinceId = provinceId;
        curProvinceDes = name;

        citys = mAreaDao.getAllCityByProId(provinceId);
        spiCity.setAdapter(new AreaSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, citys));

        //设置第一个区域
        setAreaSpiAdapter(citys.get(0).getId(), citys.get(0).getName());
    }

    private void setAreaSpiAdapter(int cityId, String name) {
        curCityId = cityId;
        curCityDes = name;

        areas = mAreaDao.getAllZoneByCityId(cityId);
        spiArea.setAdapter(new AreaSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, areas));
    }

    private class AreaSpinnerAdapter extends CommonAdapter<Address> {

        public AreaSpinnerAdapter(Context context, int layoutId, List<Address> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, Address address, int position) {
            holder.setText(android.R.id.text1, address.getName());
        }
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
        void onCitySelect(Address city);
    }
}
