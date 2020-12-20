package com.xrwl.owner.module.publish.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xrwl.owner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by www.longdw.com on 2018/4/17 上午10:59.
 */
public class PublishProductLongView extends LinearLayout {

    @BindView(R.id.ppLongNumEt)
    EditText mNumEt;
    /** 长途状态下的 吨 单价 金额 */
    @BindView(R.id.ppLongWeightEt)
    EditText mLongWeightEt;
    @BindView(R.id.ppLongSinglePriceEt)
    EditText mLongSinglePriceEt;
    @BindView(R.id.ppLongTotalPriceEt)
    EditText mLongTotalPriceEt;
    /** 长途状态下的 方 单价 金额 */
    @BindView(R.id.ppLongAreaEt)
    EditText mLongAreaEt;
    @BindView(R.id.ppLongSinglePrice2Et)
    EditText mLongSinglePrice2Et;
    @BindView(R.id.ppLongTotalPrice2Et)
    EditText mLongTotalPrice2Et;

    public PublishProductLongView(Context context) {
        super(context);
    }

    public PublishProductLongView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PublishProductLongView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        ButterKnife.bind(this);

        mLongTotalPriceEt.setKeyListener(null);
        mLongTotalPrice2Et.setKeyListener(null);
        mLongSinglePriceEt.setKeyListener(null);
        mLongSinglePrice2Et.setKeyListener(null);

        initWeight();
        initArea();
    }

    private void initWeight() {
        mLongWeightEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sp = mLongSinglePriceEt.getText().toString();
                if (s.length() > 0 && sp.length() > 0) {
                    float x = Float.parseFloat(s.toString());
                    float y = Float.parseFloat(sp);
                    mLongTotalPriceEt.setText(String.valueOf(x * y));
                } else {
                    mLongTotalPriceEt.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLongSinglePriceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String weight = mLongWeightEt.getText().toString();
                if (s.length() > 0 && weight.length() > 0) {
                    float x = Float.parseFloat(s.toString());
                    float y = Float.parseFloat(weight);

                    mLongTotalPriceEt.setText(String.valueOf(x * y));
                } else {
                    mLongTotalPriceEt.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initArea() {
        mLongAreaEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sp = mLongSinglePrice2Et.getText().toString();
                if (s.length() > 0 && sp.length() > 0) {
                    float x = Float.parseFloat(s.toString());
                    float y = Float.parseFloat(sp);

                    mLongTotalPrice2Et.setText(String.valueOf(x * y));
                } else {
                    mLongTotalPrice2Et.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mLongSinglePrice2Et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String area = mLongAreaEt.getText().toString();
                if (s.length() > 0 && area.length() > 0) {
                    float x = Float.parseFloat(s.toString());
                    float y = Float.parseFloat(area);

                    mLongTotalPrice2Et.setText(String.valueOf(x * y));
                } else {
                    mLongTotalPrice2Et.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public String getNum() {
        return mNumEt.getText().toString();
    }

    public String getLongWeight() {
        return mLongWeightEt.getText().toString();
    }

    public String getLongSinglePrice() {
        return mLongSinglePriceEt.getText().toString();
    }

    public String getLongTotalPrice() {
        return mLongTotalPriceEt.getText().toString();
    }

    public String getLongArea() {
        return mLongAreaEt.getText().toString();
    }

    public String getLongSinglePrice2() {
        return mLongSinglePrice2Et.getText().toString();
    }

    public String getLongTotalPrice2() {
        return mLongTotalPrice2Et.getText().toString();
    }
}
