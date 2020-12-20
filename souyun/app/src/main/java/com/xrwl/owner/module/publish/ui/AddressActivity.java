package com.xrwl.owner.module.publish.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Address2;
import com.xrwl.owner.module.publish.adapter.Address2Adapter;
import com.xrwl.owner.module.publish.map.SearchLocationActivity;
import com.xrwl.owner.module.publish.mvp.AddressContract;
import com.xrwl.owner.module.publish.mvp.AddressPresenter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 地址列表
 * Created by www.longdw.com on 2018/7/11 下午7:47.
 */
public class AddressActivity extends BaseActivity<AddressContract.IView, AddressPresenter> implements AddressContract.IView {

    public static final int RESULT_POSITION = 100;

    @BindView(R.id.baseRv)
    RecyclerView mRv;
    private Address2Adapter mAdapter;
    private ProgressDialog mPostDialog;
    private List<Address2> mDatas;
    private HeaderAndFooterWrapper mWrapper;

    @Override
    protected AddressPresenter initPresenter() {
        return new AddressPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.address_layout;
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAdapter = new Address2Adapter(this, R.layout.address_recycler_item, new ArrayList<Address2>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

        View footerView = LayoutInflater.from(this).inflate(R.layout.address_add_layout, mRv, false);
        mWrapper.addFootView(footerView);
        mRv.setAdapter(mWrapper);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchLocationActivity.class);
                intent.putExtra("title", "请选择位置");
                startActivityForResult(intent, RESULT_POSITION);
            }
        });

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mAdapter.setSelectedPos(position);

                Address2 item = mDatas.get(position);

                Intent intent = new Intent();
                intent.putExtra("title", item.des);
                intent.putExtra("lon", Double.parseDouble(item.lng));
                intent.putExtra("lat", Double.parseDouble(item.lat));
                intent.putExtra("city", item.city);
                intent.putExtra("pro", item.province);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {



                return false;
            }
        });

        getData();
    }

    @Override
    protected void getData() {
        mPresenter.getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String des = data.getStringExtra("title");

        String city = data.getStringExtra("city");
        String province = data.getStringExtra("pro");

        double lat = data.getDoubleExtra("lat", 0);
        double lng = data.getDoubleExtra("lon", 0);

        HashMap<String, String> params = new HashMap<>();


        try {
            params.put("des",URLEncoder.encode(des,"UTF-8") );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("city", URLEncoder.encode(city,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("province",URLEncoder.encode(province,"UTF-8") );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("lat", URLEncoder.encode(String.valueOf(lat),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("lng", URLEncoder.encode(String.valueOf(lng),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("userid", URLEncoder.encode(mAccount.getId(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mPostDialog = LoadingProgress.showProgress(this, "正在添加");
        mPresenter.postData(params);
    }

    @Override
    public void onPostSuccess(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("添加成功");
        mPresenter.getData();
    }

    @Override
    public void onPostError(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("添加失败");
        handleError(entity);
    }

    @Override
    public void onPostError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<Address2>> entity) {
        if (entity.getData() != null && entity.getData().size() > 0) {
            mAdapter.setDatas(entity.getData());
            mWrapper.notifyDataSetChanged();
            mDatas = entity.getData();
        }

    }

    @Override
    public void onRefreshError(Throwable e) {
    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
