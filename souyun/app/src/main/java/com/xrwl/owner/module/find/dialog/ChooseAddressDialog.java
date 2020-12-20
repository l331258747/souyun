package com.xrwl.owner.module.find.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.view.GridSpacingItemDecoration;
import com.ldw.library.view.SpaceItemDecoration;
import com.xrwl.owner.MyApplication;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Address;
import com.xrwl.owner.bean.AddressDao;
import com.xrwl.owner.bean.DaoSession;
import com.xrwl.owner.db.AreaDao;
import com.xrwl.owner.module.find.adapter.AddressAdapter;
import com.xrwl.owner.module.find.adapter.HistoryAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择起始地址
 * Created by www.longdw.com on 2018/4/9 上午10:34.
 */
public class ChooseAddressDialog extends BaseFindDialog {

    @BindView(R.id.caHistoryRv)
    RecyclerView mHistoryRv;

    @BindView(R.id.caProvinceRb)
    RadioButton mProvinceRb;

    @BindView(R.id.caCityRb)
    RadioButton mCityRb;

    @BindView(R.id.caAreaRb)
    RadioButton mAreaRb;

    @BindView(R.id.caViewFlipper)
    ViewFlipper mViewFlipper;

    @BindView(R.id.caProvinceRv)
    RecyclerView mProvinceRv;
    @BindView(R.id.caCityRv)
    RecyclerView mCityRv;
    @BindView(R.id.caZoneRv)
    RecyclerView mZoneRv;


    @BindView(R.id.caSelectedLayout)
    View mSelectedLayout;
//    @BindView(R.id.caSelectedRv)
//    RecyclerView mSelectedRv;

    @BindView(R.id.caFlowLayout)
    TagFlowLayout mTagFlowLayout;

    @BindView(R.id.caConfirmBtn)
    Button mConfirmBtn;

    private Address mCurrentProvince;
    private Address mCurrentCity;
    private Address mCurrentZone;

    private List<Address> mCurrentZones = new ArrayList<>();

    private AreaDao mAreaDao;

    private boolean isSingle;
    private AddressAdapter mProvinceAdapter;
    private AddressAdapter mZoneAdapter;
    private OnSelectListener mListener;

    private AddressDao mAddressHistoryDao;
    private HistoryAdapter mHistoryAdapter;

    public static ChooseAddressDialog newInstance(int y, boolean isSingle) {

        Bundle args = new Bundle();
        args.putInt("y", y);
        args.putBoolean("single", isSingle);
        ChooseAddressDialog fragment = new ChooseAddressDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.chooseaddress_dialog_layout;
    }

    @Override
    protected void init() {
        mAreaDao = new AreaDao(getContext());

        DaoSession daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();

        mAddressHistoryDao = daoSession.getAddressDao();

        initView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateSelected();

        updateHistory();
    }

    private void updateHistory() {
        mHistoryRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHistoryRv.addItemDecoration(new SpaceItemDecoration(10));
        final List<Address> results;
        if (isSingle) {
            results = mAddressHistoryDao.queryBuilder().where(AddressDao.Properties.Sm.eq(1)).orderDesc
                    (AddressDao.Properties.Uid).list();
        } else {
            results = mAddressHistoryDao.queryBuilder().where(AddressDao.Properties.Sm.eq(2)).orderDesc
                    (AddressDao.Properties.Uid).list();
        }

        //根据已选地区来决定状态
        if (mCurrentZones.size() > 0) {
            for (Address sa : mCurrentZones) {
                for (Address a : results) {
                    if (sa.getId() == a.getId()) {
                        a.setSelected(true);
                        break;
                    }
                }
            }
        }

        mHistoryAdapter = new HistoryAdapter(getContext(), R.layout.chooseaddress_history_recycler_item, results);
        mHistoryRv.setAdapter(mHistoryAdapter);

        mHistoryAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Address address = results.get(position);
                if (isSingle) {
                    mCurrentZone = address;

                    //选择好后退出
                    if (mListener != null) {
                        mListener.onSingleSelect(mCurrentZone);
                    }

                    dismiss();
                } else {

                    for (Address a : mCurrentZones) {
                        if (a.getName().equals("全国")) {
                            a.setSelected(false);
                            mCurrentZones.remove(a);
                            mProvinceAdapter.notifyDataSetChanged();
                            break;
                        }
                    }

                    if (!address.isSelected()) {
                        if (mCurrentZones.size() == 5) {
                            showToast("最多只能选择5个地区");
                            return;
                        }

                        mCurrentZones.add(address);
                    } else {

                        for (Address a : mCurrentZones) {
                            if (a.getId() == address.getId()) {
                                mCurrentZones.remove(a);
                                break;
                            }
                        }
                    }

                    address.setSelected(!address.isSelected());
                    mHistoryAdapter.notifyDataSetChanged();

                    updateSelected();

                    //更新区域状态
                    if (mZoneAdapter != null) {
                        List<Address> datas = mZoneAdapter.getDatas();
                        for (Address a : datas) {
                            if (a.getId() == address.getId()) {
                                a.setSelected(address.getIsSelected());
                                break;
                            }
                        }
                        mZoneAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initView() {

        isSingle = getArguments().getBoolean("single", true);

        if (!isSingle) {
            mConfirmBtn.setVisibility(View.VISIBLE);
            mSelectedLayout.setVisibility(View.VISIBLE);

//            mSelectedRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
//            mSelectedRv.addItemDecoration(new GridSpacingItemDecoration(2, 4, false));
        }

        mViewFlipper.setFlipInterval(0);
        mViewFlipper.setInAnimation(null);
        mViewFlipper.setOutAnimation(null);

        initProvince();
    }

    private Address createDefault() {
        Address address = new Address();
        address.setName("全国");
        return address;
    }

    private void initProvince() {
        mProvinceRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mProvinceRv.addItemDecoration(new GridSpacingItemDecoration(4, 4, false));

        final List<Address> datas = mAreaDao.getAllProvinces();

        if (!isSingle) {//多选的话 第一个是全国
            Address address = createDefault();
            if (mCurrentZones.size() == 0) {
                address.setSelected(true);
                mCurrentZones.add(address);
            } else if (mCurrentZones.size() == 1 && "全国".equals(mCurrentZones.get(0).getName())) {
                address.setSelected(true);
            }
            datas.add(0, address);


            //更新已选
            updateSelected();
        }

        mProvinceAdapter = new AddressAdapter(getContext(), R.layout.chooseaddress_recycler_item, datas);
        mProvinceRv.setAdapter(mProvinceAdapter);
        mProvinceAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (!isSingle && position == 0) {
                    Address address = datas.get(0);
                    address.setSelected(!address.isSelected());

                    if (address.isSelected()) {

                        mCurrentZones.clear();

                        mCurrentZones.add(address);
                    } else {
                        mCurrentZones.remove(address);
                    }

                    mProvinceAdapter.notifyDataSetChanged();

                    //更新已选
                    updateSelected();
                    return;
                }
                mCurrentProvince = datas.get(position);

                mProvinceRb.setText(mCurrentProvince.getName());
                mProvinceRb.setChecked(false);
                mCityRb.setChecked(true);

                mCityRb.setText("市");
                mCurrentCity = null;

                mViewFlipper.setDisplayedChild(1);

                initCity(mCurrentProvince.getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 更新已选
     */
    private void updateSelected() {
//        SelectedAdapter adapter = new SelectedAdapter(getContext(), R.layout.chooseaddress_selected_recycler_item, mCurrentZones);
//        mSelectedRv.setAdapter(adapter);

        final LayoutInflater inflater = LayoutInflater.from(getContext());

        final TagAdapter adapter = new TagAdapter<Address>(mCurrentZones) {
            @Override
            public View getView(FlowLayout parent, int position, Address address) {

                View view = inflater.inflate(R.layout.chooseaddress_selected_recycler_item, null);

                TextView tv = view.findViewById(R.id.caSelectedItemTv);
                tv.setText(address.getName());

                return view;
            }
        };

        mTagFlowLayout.setAdapter(adapter);

        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                if ("全国".equals(mCurrentZones.get(position).getName())) {

                    List<Address> datas = mProvinceAdapter.getDatas();
                    for (Address a : datas) {
                        if (a.getName().equals("全国")) {
                            a.setSelected(false);
                            break;
                        }
                    }

                    mProvinceAdapter.notifyDataSetChanged();
                }

                mCurrentZones.get(position).setSelected(false);

                if (mZoneAdapter != null) {

                    List<Address> datas = mZoneAdapter.getDatas();
                    for (Address a : datas) {
                        if (a.getId() == mCurrentZones.get(position).getId()) {
                            a.setSelected(false);
                            break;
                        }
                    }

                    mZoneAdapter.notifyDataSetChanged();
                }

                if (mHistoryAdapter != null) {
                    List<Address> datas = mHistoryAdapter.getDatas();
                    for (Address a : datas) {
                        if (a.getId() == mCurrentZones.get(position).getId()) {
                            a.setSelected(false);
                            break;
                        }
                    }

                    mHistoryAdapter.notifyDataSetChanged();
                }

                mCurrentZones.remove(position);
                adapter.notifyDataChanged();

                return true;
            }
        });
    }

    private void initCity(int provinceId) {
        mCityRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mCityRv.addItemDecoration(new GridSpacingItemDecoration(4, 4, false));

        final List<Address> datas = mAreaDao.getAllCityByProId(provinceId);

        AddressAdapter addressAdapter = new AddressAdapter(getContext(), R.layout.chooseaddress_recycler_item, datas);
        mCityRv.setAdapter(addressAdapter);
        addressAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mCurrentCity = datas.get(position);

                mCityRb.setText(mCurrentCity.getName());
                mCityRb.setChecked(false);
                mAreaRb.setChecked(true);

                mViewFlipper.setDisplayedChild(2);

                initZone(mCurrentCity.getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initZone(int zoneId) {
        mZoneRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mZoneRv.addItemDecoration(new GridSpacingItemDecoration(4, 4, false));

        final List<Address> datas = mAreaDao.getAllZoneByCityId(zoneId);

        for (Address a : mCurrentZones) {

            for (Address aa : datas) {
                if (a.getId() == aa.getId()) {
                    aa.setSelected(true);
                    break;
                }
            }
        }

        mZoneAdapter = new AddressAdapter(getContext(), R.layout.chooseaddress_recycler_item, datas);
        mZoneRv.setAdapter(mZoneAdapter);
        mZoneAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Address address = datas.get(position);
                if (isSingle) {
                    mCurrentZone = address;
                    address.setSm(1);

                    //保存到数据库
                    //1.判断数据库中记录的个数
                    List<Address> results = mAddressHistoryDao.queryBuilder().where(AddressDao.Properties.Sm.eq(1)).orderAsc
                            (AddressDao.Properties.Uid).list();

                    if (results.size() < 3) {

                        mAddressHistoryDao.insert(address);
                    } else {
                        mAddressHistoryDao.delete(results.get(0));
                        mAddressHistoryDao.insert(address);
                    }

                    //选择好后退出
                    if (mListener != null) {
                        mListener.onSingleSelect(mCurrentZone);
                    }

                    dismiss();
                } else {
                    address.setSm(2);
                    for (Address a : mCurrentZones) {
                        if (a.getName().equals("全国")) {
                            a.setSelected(false);
                            mCurrentZones.remove(a);
                            mProvinceAdapter.notifyDataSetChanged();
                            break;
                        }
                    }

                    if (!address.isSelected()) {
                        if (mCurrentZones.size() == 5) {
                            showToast("最多只能选择5个地区");
                            return;
                        }

                        mCurrentZones.add(address);
                    } else {

                        for (Address a : mCurrentZones) {
                            if (a.getId() == address.getId()) {
                                mCurrentZones.remove(a);
                                break;
                            }
                        }
                    }

                    address.setSelected(!address.isSelected());
                    mZoneAdapter.notifyDataSetChanged();

                    //更新已选
                    updateSelected();

                    //更新历史记录
                    List<Address> datas = mHistoryAdapter.getDatas();
                    for (Address a : datas) {
                        if (a.getName().equals(address.getName())) {
                            a.setSelected(address.getIsSelected());
                            break;
                        }
                    }
                    mHistoryAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @OnClick({R.id.caProvinceRb, R.id.caCityRb, R.id.caAreaRb, R.id.caConfirmBtn, R.id.caBkView})
    public void onClick(View v) {
        if (v.getId() == R.id.caProvinceRb) {
            mViewFlipper.setDisplayedChild(0);
            mCityRb.setChecked(false);
            mAreaRb.setChecked(false);
        } else if (v.getId() == R.id.caCityRb) {

            if (mCurrentProvince == null) {
                showToast("请先选择省份");
                mCityRb.setChecked(false);
            } else {
                mAreaRb.setChecked(false);
                mProvinceRb.setChecked(false);

                mViewFlipper.setDisplayedChild(1);
            }
        } else if (v.getId() == R.id.caAreaRb) {

            if (mCurrentCity == null) {
                showToast("请先选择市");
                mAreaRb.setChecked(false);
            } else {

                mProvinceRb.setChecked(false);
                mCityRb.setChecked(false);

                mViewFlipper.setDisplayedChild(2);
            }
        } else if (v.getId() == R.id.caConfirmBtn) {//多选情况下，点击确定

            if (mListener != null) {
                //选择好后退出
                if (mCurrentZones.size() == 0 || "全国".equals(mCurrentZones.get(0).getName())) {
                    mCurrentZones.clear();
                    Address address = createDefault();
                    address.setSelected(true);
                    mCurrentZones.add(address);
                } else {
                    List<Address> results = mAddressHistoryDao.queryBuilder().where(AddressDao.Properties.Sm.eq(2)).orderAsc
                            (AddressDao.Properties.Uid).list();
                    if (results != null && results.size() > 0) {
                        mAddressHistoryDao.deleteInTx(results);
                    }
                    mAddressHistoryDao.insertInTx(mCurrentZones);
                }
                mListener.onMultiSelect(mCurrentZones);
            }

            dismiss();
        } else if (v.getId() == R.id.caBkView) {
            dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mListener != null) {
            mListener.onDialogDismiss();
        }
    }

    public interface OnSelectListener extends OnDismissListener {
        void onSingleSelect(Address address);
        void onMultiSelect(List<Address> datas);
    }

    public void setOnSelectListener(OnSelectListener l) {
        mListener = l;
    }

    public void setCurrentZones(List<Address> zones) {
        if (zones != null) {
            mCurrentZones = zones;
        }
    }
}
