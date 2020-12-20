package com.xrwl.owner.module.publish.dialog;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.utils.Utils;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.xrwl.owner.module.publish.adapter.InsuranceAdapter;
import com.xrwl.owner.module.publish.bean.Insurance;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * 保险服务
 * Created by www.longdw.com on 2018/4/14 下午9:13.
 */
public class InsuranceDialog extends BasePopDialog {

    @BindView(R.id.idRv)
    RecyclerView mRv;
    private OnInsuranceItemClickListener mListener;

    @Override
    protected int getLayoutId() {
        return R.layout.insurance_dialog_layout;
    }

    @Override
    protected void initView() {
        initBaseRv(mRv);

        try {
            JSONObject jsonObject = new JSONObject(Utils.getAssetsString(mContext, "insurance.json"));
            final Insurance insurance = Insurance.parseJson(jsonObject);

            InsuranceAdapter adapter = new InsuranceAdapter(getContext(), R.layout.insurance_dialog_recycler_item, insurance.data);
            mRv.setAdapter(adapter);

            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    mListener.onItemClick(insurance.data.get(position), position);
                    dismiss();
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

    public void setOnInsuranceItemClickListener(OnInsuranceItemClickListener l) {
        mListener = l;
    }

    public interface OnInsuranceItemClickListener {
        void onItemClick(Insurance insurance, int position);
    }
}
