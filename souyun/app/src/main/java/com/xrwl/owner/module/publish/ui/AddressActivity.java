package com.xrwl.owner.module.publish.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.TitleView;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Address2;
import com.xrwl.owner.module.publish.adapter.Address2Adapter;
import com.xrwl.owner.module.publish.map.SearchLocationActivity;
import com.xrwl.owner.module.publish.mvp.AddressContract;
import com.xrwl.owner.module.publish.mvp.AddressPresenter;

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

    @BindView(R.id.baseTitleView)
    TitleView mTitleView;

    private Address2Adapter mAdapter;
    private ProgressDialog mPostDialog;
    private List<Address2> mDatas;
    private HeaderAndFooterWrapper mWrapper;

    boolean isItemClick = true;


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
        isItemClick = getIntent().getBooleanExtra("isItemClick",true);

        mTitleView.setOnTitleViewListener(new TitleView.TitleViewListener() {
            @Override
            public void onRight() {
                Intent intent = new Intent(mContext, SearchLocationActivity.class);
                intent.putExtra("title", "请选择位置");
                intent.putExtra("showName", true);
                startActivityForResult(intent, RESULT_POSITION);
            }

            @Override
            public void onBack() {
                finish();
            }
        });

        initBaseRv(mRv);

        mAdapter = new Address2Adapter(this, R.layout.address_recycler_item, new ArrayList<Address2>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);
        mRv.setAdapter(mWrapper);

        mAdapter.setOnItemClickListener(position -> {
            Address2 item = mDatas.get(position);
            HashMap<String, String> params = new HashMap<>();
            params.put("id",item.getId());
            mPostDialog = LoadingProgress.showProgress(this, "正在删除");
            mPresenter.CancelAddress(params);

        });

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(!isItemClick) return;

                mAdapter.setSelectedPos(position);

                Address2 item = mDatas.get(position);

                Intent intent = new Intent();
                intent.putExtra("title", item.des);
                intent.putExtra("lon", Double.parseDouble(item.lng));
                intent.putExtra("lat", Double.parseDouble(item.lat));
                intent.putExtra("city", item.city);
                intent.putExtra("pro", item.province);
                intent.putExtra("tel", item.tel);
                intent.putExtra("userName", item.userName);
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

        String userName = data.getStringExtra("userName");
        String tel = data.getStringExtra("tel");

        double lat = data.getDoubleExtra("lat", 0);
        double lng = data.getDoubleExtra("lon", 0);

        HashMap<String, String> params = new HashMap<>();

        params.put("userName",userName);
        params.put("tel",tel);
        params.put("des",des);
        params.put("city", city);
        params.put("province",province);
        params.put("lat", String.valueOf(lat));
        params.put("lng", String.valueOf(lng));
        params.put("userid", mAccount.getId());

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
    public void onCancelAddressError(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("删除成功");
        handleError(entity);
    }

    @Override
    public void onCancelAddressSuccess(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("删除失败");
        mPresenter.getData();

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
        if(mPostDialog != null || mPostDialog.isShowing())
            mPostDialog.dismiss();
    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
