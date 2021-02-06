package com.xrwl.owner.module.publish.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.PostOrder;
import com.xrwl.owner.module.publish.adapter.CargoAdapter;
import com.xrwl.owner.module.publish.bean.CargoBean;
import com.xrwl.owner.module.publish.mvp.CargoContract;
import com.xrwl.owner.module.publish.mvp.CargoPresenter;
import com.xrwl.owner.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CargoActivity extends BaseActivity<CargoContract.IView, CargoPresenter> implements CargoContract.IView {

    @BindView(R.id.recycler_view1)
    RecyclerView recycler_view1;

    @BindView(R.id.recycler_view2)
    RecyclerView recycler_view2;


    private CargoAdapter mAdapter1;
    private CargoAdapter mAdapter2;

    private List<CargoBean> mDatas1;
    private List<CargoBean> mDatas2;

    private ProgressDialog mPostDialog;

    @Override
    protected CargoPresenter initPresenter() {
        return new CargoPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cargo;
    }

    @Override
    protected void initViews() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recycler_view1.setLayoutManager(linearLayoutManager);
        mAdapter1 = new CargoAdapter(mContext, new ArrayList<>(),0);
        recycler_view1.setAdapter(mAdapter1);

        mAdapter1.setOnItemClickListener(position -> {

            if(!MyUtils.isFastClick())
                return;

            mPostDialog = LoadingProgress.showProgress(this, "正在添加");

            Map<String, String> params = new HashMap<>();
            mAdapter1.setId(mDatas1.get(position).getId());
            params.put("listId", mDatas1.get(position).getId() + "");
            mPresenter.getList2(params);

        });

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recycler_view2.setLayoutManager(linearLayoutManager2);
        mAdapter2 = new CargoAdapter(mContext, new ArrayList<>(),1);
        recycler_view2.setAdapter(mAdapter2);

        mAdapter2.setOnItemClickListener(position -> {
            //选择退出
            Intent intent = new Intent();
            intent.putExtra("name", mDatas2.get(position).getName());
            setResult(RESULT_OK, intent);
            finish();
        });


        Map<String, String> params = new HashMap<>();
        mPresenter.getList(params);

    }

    @Override
    public void getListSuccessa(BaseEntity<List<CargoBean>> datas) {
        if(datas == null || datas.getData() == null) mDatas1 = new ArrayList<>();
        else mDatas1 = datas.getData();
        mAdapter1.setData(mDatas1);

        if(mDatas1.size() > 0){
            mPostDialog = LoadingProgress.showProgress(this, "正在添加");

            Map<String, String> params = new HashMap<>();
            mAdapter1.setId(mDatas1.get(0).getId());
            params.put("listId", mDatas1.get(0).getId() + "");
            mPresenter.getList2(params);
        }
    }

    @Override
    public void getListError(BaseEntity entity) {
        showToast("加载失败");
        handleError(entity);
    }

    @Override
    public void getListException(Throwable e) {
        showNetworkError();
    }

    @Override
    public void getListSuccessa2(BaseEntity<List<CargoBean>> entity) {
        mPostDialog.dismiss();
        if (entity.getData() != null && entity.getData().size() > 0) {
            mAdapter2.setData(entity.getData());
            mDatas2 = entity.getData();
        }
    }

    @Override
    public void getListError2(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("加载失败");
        handleError(entity);
    }

    @Override
    public void getListException2(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<PostOrder> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
