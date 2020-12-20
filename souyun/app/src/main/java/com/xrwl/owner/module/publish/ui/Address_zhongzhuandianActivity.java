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

import com.xrwl.owner.bean.Address2zhongzhuandian;

import com.xrwl.owner.module.publish.adapter.Address2zhongzhuandianAdapter;
import com.xrwl.owner.module.publish.map.SearchLocationActivity;

import com.xrwl.owner.module.publish.mvp.AddresszhongzhuandianContract;
import com.xrwl.owner.module.publish.mvp.AddresszhongzhuandianPresenter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 中转地址选择
 * Created by www.longdw.com on 2018/7/11 下午7:47.
 */
public class Address_zhongzhuandianActivity extends BaseActivity<AddresszhongzhuandianContract.IView, AddresszhongzhuandianPresenter> implements AddresszhongzhuandianContract.IView {

    public static final int RESULT_POSITION = 100;

    @BindView(R.id.baseRv)
    RecyclerView mRv;
    private Address2zhongzhuandianAdapter mAdapter;
    private ProgressDialog mPostDialog;
    private List<Address2zhongzhuandian> mDatas;
    private HeaderAndFooterWrapper mWrapper;

    @Override
    protected AddresszhongzhuandianPresenter initPresenter() {
        return new AddresszhongzhuandianPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.address_zhongzhuandian;
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAdapter = new Address2zhongzhuandianAdapter(this, R.layout.address_recycler_item_zhongzhuandian, new ArrayList<Address2zhongzhuandian>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

        View footerView = LayoutInflater.from(this).inflate(R.layout.address_add_layout_zhongzhuandian, mRv, false);
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
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position)
            {
                mAdapter.setSelectedPos(position);
                Address2zhongzhuandian item = mDatas.get(position);
                Intent intent = new Intent();
                intent.putExtra("id",item.id);
                intent.putExtra("Province", item.Province);
                intent.putExtra("nameqin", item.name);
                intent.putExtra("city", item.city);
                intent.putExtra("district", item.district);
                intent.putExtra("Xaxis", item.Xaxis);
                intent.putExtra("Yaxis", item.Yaxis);
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
        String id = data.getStringExtra("id");
        String city = data.getStringExtra("city");
        String Province = data.getStringExtra("Province");
        String name = data.getStringExtra("name");
        String district = data.getStringExtra("district");
        String Xaxis = data.getStringExtra("Xaxis");
        String Yaxis = data.getStringExtra("Yaxis");

        HashMap<String, String> params = new HashMap<>();


        try {
            params.put("city",URLEncoder.encode(city,"UTF-8") );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("Province", URLEncoder.encode(Province,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("name",URLEncoder.encode(name,"UTF-8") );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            params.put("district", URLEncoder.encode(district,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            params.put("Xaxis", URLEncoder.encode(Xaxis,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            params.put("Yaxis", URLEncoder.encode(Yaxis,"UTF-8"));
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
    public void onRefreshSuccess(BaseEntity<List<Address2zhongzhuandian>> entity) {
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
