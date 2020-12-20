package com.xrwl.owner.module.find.dialog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.view.GridSpacingItemDecoration;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.BusModel;
import com.xrwl.owner.bean.ProductSearch;
import com.xrwl.owner.module.find.adapter.BusModelAdapter;
import com.xrwl.owner.module.find.adapter.CategoryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 货物 高级搜索
 * Created by www.longdw.com on 2018/4/11 下午4:39.
 */
public class ProductSearchDialog extends BaseFindDialog {

    private static final String TAG = ProductSearchDialog.class.getSimpleName();
    @BindView(R.id.psProductNameEt)
    EditText mNameEt;

    @BindView(R.id.psShortTypeRv)
    RecyclerView mShortTypeRv;
    @BindView(R.id.psLongTypeRv)
    RecyclerView mLongTypeRv;
    @BindView(R.id.psCategoryRv)
    RecyclerView mCategoryRv;

    @BindView(R.id.psStartDateTv)
    TextView mStartDateTv;

    @BindView(R.id.psEndDateTv)
    TextView mEndDateTv;
    private OnProductSearchListener mListener;
    private Calendar mCalendar;
    private BusModelAdapter mShortAdapter;
    private BusModelAdapter mLongAdapter;
    private CategoryAdapter mCategoryAdapter;
    private int mCategory = -1;

    private ProductSearch mPs;

    public static ProductSearchDialog newInstance(int y, ProductSearch ps) {

        Bundle args = new Bundle();
        args.putInt("y", y);
        args.putSerializable("data", ps);
        ProductSearchDialog fragment = new ProductSearchDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.productsearch_dialog_layout;
    }

    @Override
    protected void init() {
        mCalendar = Calendar.getInstance();
        mShortTypeRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mShortTypeRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));

        mLongTypeRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mLongTypeRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));

        mCategoryRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mCategoryRv.addItemDecoration(new GridSpacingItemDecoration(4, 8, false));

        mPs = (ProductSearch) getArguments().getSerializable("data");

        if (mPs == null) {
            mPs = new ProductSearch();
        }

        if (!TextUtils.isEmpty(mPs.productName)) {
            mNameEt.setText(mPs.productName);
        }
        if (!TextUtils.isEmpty(mPs.startDate)) {
            mStartDateTv.setText(mPs.startDate);
        }
        if (!TextUtils.isEmpty(mPs.endDate)) {
            mEndDateTv.setText(mPs.endDate);
        }

        initShort();
        initLong();
        initCategory();
    }

    private void initShort() {
        String[] busmodels = getResources().getStringArray(R.array.shortbusmodel);

        final List<BusModel> datas = new ArrayList<>();

        for (String item : busmodels) {
            BusModel bm = new BusModel();
            bm.setName(item);
            datas.add(bm);

            if (mPs.shortNames != null && mPs.shortNames.contains(item)) {
                bm.setSelected(false);
            }
        }

        mShortAdapter = new BusModelAdapter(getContext(), R.layout.productsearch_busmodel_recycler_item, datas);
        mShortTypeRv.setAdapter(mShortAdapter);

        mShortAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                BusModel bm = datas.get(position);
                bm.setSelected(!bm.isSelected());
                mShortAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initLong() {
        String[] busmodels = getResources().getStringArray(R.array.longbusmodel);

        final List<BusModel> datas = new ArrayList<>();
        for (String item : busmodels) {
            BusModel bm = new BusModel();
            bm.setName(item);
            datas.add(bm);

            if (mPs.longNames != null && mPs.longNames.contains(item)) {
                bm.setSelected(true);
            }
        }

        mLongAdapter = new BusModelAdapter(getContext(), R.layout.productsearch_busmodel_recycler_item, datas);
        mLongTypeRv.setAdapter(mLongAdapter);

        mLongAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                BusModel bm = datas.get(position);
                bm.setSelected(!bm.isSelected());
                mLongAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initCategory() {
        String[] categories = getResources().getStringArray(R.array.category);

        final List<String> datas = new ArrayList<>();
        for (String item : categories) {
            datas.add(item);
        }

        mCategoryAdapter = new CategoryAdapter(getContext(), R.layout.productsearch_busmodel_recycler_item, datas);
        mCategoryRv.setAdapter(mCategoryAdapter);

        mCategoryAdapter.setSelectedPos(mPs.category);

        mCategoryAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mCategoryAdapter.setSelectedPos(position);
                mCategoryAdapter.notifyDataSetChanged();

                mCategory = position;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @OnClick({R.id.psBkView, R.id.psStartDateTv, R.id.psEndDateTv, R.id.psConfirmBtn})
    public void onClick(View v) {
        if (v.getId() == R.id.psBkView) {
            dismiss();
        } else if (v.getId() == R.id.psStartDateTv) {
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DATE);
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mStartDateTv.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }, year, month, day);
            dialog.show();
        } else if (v.getId() == R.id.psEndDateTv) {
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DATE);
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mEndDateTv.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }, year, month, day);
            dialog.show();
        } else if (v.getId() == R.id.psConfirmBtn) {
            String start = mStartDateTv.getText().toString();
            String end = mEndDateTv.getText().toString();
            if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    int diff = format.parse(start).compareTo(format.parse(end));
                    if (diff > 0) {
                        showToast("结束时间不能大于开始时间");
                        return;
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage(), e);
                }
            }

            String name = mNameEt.getText().toString();

            List<BusModel> datas = mShortAdapter.getDatas();
            List<String> shortNames = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            for (BusModel bm : datas) {
                if (bm.isSelected()) {
                    sb.append(bm.getName());
                    shortNames.add(bm.getName());
                }
            }

            List<BusModel> longDatas = mLongAdapter.getDatas();
            List<String> longNames = new ArrayList<>();
            StringBuffer longSb = new StringBuffer();
            for (BusModel bm : longDatas) {
                if (bm.isSelected()) {
                    longSb.append(bm.getName());
                    longNames.add(bm.getName());
                }
            }

            ProductSearch ps = mPs;
            ps.productName = name;
            ps.shortTrucks = sb.toString();
            ps.longTrucks = longSb.toString();
            ps.category = mCategory;
            ps.startDate = start;
            ps.endDate = end;
            ps.shortNames = shortNames;
            ps.longNames = longNames;

            if (mListener != null) {
                mListener.onProductSearch(ps);
            }

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

    public void setOnDismissListener(OnProductSearchListener l) {
        mListener = l;
    }

    public interface OnProductSearchListener extends OnDismissListener {
        void onProductSearch(ProductSearch ps);
    }
}
