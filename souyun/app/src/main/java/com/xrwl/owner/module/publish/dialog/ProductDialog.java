package com.xrwl.owner.module.publish.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.ldw.library.utils.DisplayUtil;
import com.xrwl.owner.MyApplication;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.xrwl.owner.module.publish.bean.Product;
import com.xrwl.owner.module.publish.bean.ProductDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/2 下午8:42.
 */

public class ProductDialog extends BasePopDialog {

    @BindView(R.id.ppDialogRv)
    RecyclerView mRv;
    @BindView(R.id.ppDialogEt)
    EditText mEt;

    private ProductDao mDao;
    private ProductAdapter mAdapter;
    private OnProductSelectListener mListener;

    @Override
    protected int getLayoutId() {

        mHeight = DisplayUtil.dp2px(mContext, 300);

        return R.layout.publish_product_dialog;
    }

    @Override
    protected void initView() {
        initBaseRv(mRv);

        mDao = ((MyApplication)getActivity().getApplication()).getDaoSession().getProductDao();

        initList();
    }

    private void initList() {
        final List<Product> datas = mDao.queryBuilder().orderDesc(ProductDao.Properties.Id).list();
        if (mAdapter == null) {
            mAdapter = new ProductAdapter(mContext, R.layout.publish_product_dialog_recycler_item, datas);
            mRv.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    mListener.onProductSelect(datas.get(position).getName());
                    dismiss();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } else {
            mAdapter.setDatas(datas);
        }
    }

    @OnClick(R.id.ppDialogAddIv)
    public void onClick() {
        String name = mEt.getText().toString();
        if (TextUtils.isEmpty(name)) {
            return;
        }
        Query<Product> query = mDao.queryBuilder().where(ProductDao.Properties.Name.eq(name)).build();
        if (query.list().size() > 0) {
            mDao.delete(query.list().get(0));
        }
        Product p = new Product();
        p.setName(name);
        mDao.save(p);

        mListener.onProductSelect(name);

        dismiss();
    }

    private class ProductAdapter extends CommonAdapter<Product> {

        public ProductAdapter(Context context, int layoutId, List<Product> datas) {
            super(context, layoutId, datas);
        }

        @Override
        protected void convert(ViewHolder holder, Product product, int position) {
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
