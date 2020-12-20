package com.xrwl.owner.module.publish.view;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;

import com.xrwl.owner.bean.Address;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/18 上午10:44.
 */
public class CustomSpinner extends android.support.v7.widget.AppCompatTextView {
    private List<Address> mDatas;
    private FragmentManager mFm;
    private OnCustomSpinnerItemClickListener mListener;

    public CustomSpinner(Context context) {
        super(context);

        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setDatas(List<Address> datas, FragmentManager fm) {
        mDatas = datas;
        mFm = fm;
    }

    private void init(Context context) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatas == null) {
                    return;
                }
                final SpinnerDialog sp = new SpinnerDialog();
                sp.setOnCustomSpinnerItemClickListener(new OnCustomSpinnerItemClickListener() {
                    @Override
                    public void onItemClick(Address address) {
                        mListener.onItemClick(address);
                        setText(address.getName());
                        sp.dismiss();
                    }
                });
                sp.setData(mDatas);
                sp.show(mFm, "spinner");
            }
        });
    }

    public void setOnCustomSpinnerItemClickListener(OnCustomSpinnerItemClickListener l) {
        mListener = l;
    }

    public interface OnCustomSpinnerItemClickListener {
        void onItemClick(Address address);
    }
}
