package com.xrwl.owner.module.publish.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.utils.DisplayUtil;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.xrwl.owner.module.publish.bean.DzNameManageBean;
import com.xrwl.owner.module.publish.mvp.DzNameContract;
import com.xrwl.owner.module.publish.mvp.DzNamePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/2 下午8:42.
 */

public class ProductDialog2 extends BasePopDialog implements DzNameContract.IView {

    @BindView(R.id.ppDialogRv)
    RecyclerView mRv;
    @BindView(R.id.ppDialogEt)
    EditText mEt;

    private ProductAdapter mAdapter;
    private OnProductSelectListener mListener;

    DzNamePresenter mPresenter;
    List<DzNameManageBean> list;

    @Override
    protected int getLayoutId() {

        mHeight = DisplayUtil.dp2px(mContext, 300);

        return R.layout.publish_product_dialog;
    }

    @Override
    protected void initView() {

        mPresenter = new DzNamePresenter(mContext);
        mPresenter.attach(this);

        initBaseRv(mRv);
        initList();

        mPresenter.getData();
    }

    private void initList() {
        mAdapter = new ProductAdapter(mContext, R.layout.publish_product_dialog_recycler_item, new ArrayList<>());
        mRv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(mListener == null) return;
                mListener.onProductSelect(list.get(position).getName());
                dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @OnClick(R.id.ppDialogAddIv)
    public void onClick() {
        if(TextUtils.isEmpty(mEt.getText().toString())){
            ToastUtils.showShort("请输入货物名称");
            return;
        }

        Map<String,String> params = new HashMap<String,String>();
        params.put("name", mEt.getText().toString());
        mPresenter.addData(params);
    }


    @Override
    public void onRefreshSuccess(BaseEntity<List<DzNameManageBean>> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }

    @Override
    public void addDataSuccess(BaseEntity entity) {
        mPresenter.getData();
        ToastUtils.showShort("添加成功");
        mEt.setText("");
    }

    @Override
    public void addDataError(BaseEntity entity) {
        ToastUtils.showShort(entity.getMsg());
    }

    @Override
    public void getDataSuccess(BaseEntity<List<DzNameManageBean>> datas) {
        if(datas == null || datas.getData() == null)  list = new ArrayList<>();
        else list = datas.getData();
        mAdapter.setDatas(list);
    }

    @Override
    public void getDataError(BaseEntity entity) {
        ToastUtils.showShort(entity.getMsg());
    }

    private class ProductAdapter extends CommonAdapter<DzNameManageBean> {

        public ProductAdapter(Context context, int layoutId, List<DzNameManageBean> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, DzNameManageBean product, int position) {
            holder.setText(R.id.pdItemTitleTv, product.getName());
        }
    }

    public void setOnProductSelectListener(OnProductSelectListener l) {
        mListener = l;
    }

    public interface OnProductSelectListener {
        void onProductSelect(String name);
    }
}
