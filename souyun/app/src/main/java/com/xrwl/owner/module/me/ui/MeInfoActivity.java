package com.xrwl.owner.module.me.ui;

import android.widget.TextView;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/4/3 下午9:40.
 */

public class MeInfoActivity extends BaseActivity {

    @BindView(R.id.miNameTv)
    TextView mNameTv;
    @BindView(R.id.miPhoneTv)
    TextView mPhoneTv;
    @BindView(R.id.miTypeTv)
    TextView mTypeTv;

    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.meinfo_activity;
    }

    @Override
    protected void initViews() {
        if (mAccount.isOwner()) {
            mTypeTv.setText("货    主");
        } else {
            mTypeTv.setText("司    机");
        }
        try {
            mNameTv.setText(URLDecoder.decode(mAccount.getName(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
       // mNameTv.setText(mAccount.getName());
        mPhoneTv.setText(mAccount.getPhone());
    }
}
