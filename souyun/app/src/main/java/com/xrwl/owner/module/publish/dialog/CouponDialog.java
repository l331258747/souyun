package com.xrwl.owner.module.publish.dialog;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.utils.Utils;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.xrwl.owner.module.publish.adapter.CouponAdapter;
import com.xrwl.owner.module.publish.bean.Coupon;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * 优惠券
 * Created by www.longdw.com on 2018/4/14 下午10:22.
 */
public class CouponDialog extends BasePopDialog {

    @BindView(R.id.couponDialogRv)
    RecyclerView mRv;
    private OnCouponItemClickListener mListener;
    private int mPrice;

    public static CouponDialog newInstance(int price) {

        Bundle args = new Bundle();
        args.putInt("price", price);
        CouponDialog fragment = new CouponDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.coupon_dialog_layout;
    }

    @Override
    protected void initView() {
        initBaseRv(mRv);

        mPrice = getArguments().getInt("price");

        try {
            JSONObject jsonObject = new JSONObject(Utils.getAssetsString(mContext, "coupon.json"));
            final Coupon coupon = Coupon.parseJson(jsonObject);

            CouponAdapter adapter = new CouponAdapter(getContext(), R.layout.coupon_dialog_recycler_item, coupon.data, mPrice);
            mRv.setAdapter(adapter);

            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Coupon c = coupon.data.get(position);
                    if (mPrice >= Integer.parseInt(c.full)) {
                        mListener.onItemClick(c, position);
                        dismiss();
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    public void setOnCouponItemClickListener(OnCouponItemClickListener l) {
        mListener = l;
    }

    public interface OnCouponItemClickListener {
        void onItemClick(Coupon coupon, int position);
    }
}
