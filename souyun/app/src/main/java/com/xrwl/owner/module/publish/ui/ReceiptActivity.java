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
import com.xrwl.owner.bean.Receipt;

import com.xrwl.owner.module.publish.adapter.ReceiptAdapter;
import com.xrwl.owner.module.publish.map.SearchLocationActivity;
import com.xrwl.owner.module.publish.mvp.AddressContract;
import com.xrwl.owner.module.publish.mvp.AddressPresenter;
import com.xrwl.owner.module.publish.mvp.ReceiptContract;
import com.xrwl.owner.module.publish.mvp.ReceiptPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 发票列表
 * Created by www.longdw.com on 2018/7/11 下午7:47.
 */
public class ReceiptActivity extends BaseActivity<ReceiptContract.IView, ReceiptPresenter> implements ReceiptContract.IView {

    public static final int RESULT_POSITION = 100;

    @BindView(R.id.baseRv)
    RecyclerView mRv;
    private ReceiptAdapter mAdapter;
    private ProgressDialog mPostDialog;
    private List<Receipt> mDatas;
    private HeaderAndFooterWrapper mWrapper;

    @Override
    protected ReceiptPresenter initPresenter() {
        return new ReceiptPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.receipt_layout;
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAdapter = new ReceiptAdapter(this, R.layout.receipt_recycler_item, new ArrayList<Receipt>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);

        View footerView = LayoutInflater.from(this).inflate(R.layout.receipt_add_layout, mRv, false);
        mWrapper.addFootView(footerView);
        mRv.setAdapter(mWrapper);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddReceiptActivity.class);
                intent.putExtra("title", "添加发票");
                startActivityForResult(intent, RESULT_POSITION);
            }
        });

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mAdapter.setSelectedPos(position);
                Receipt item = mDatas.get(position);
                Intent intent = new Intent();
                intent.putExtra("number", item.number);
                intent.putExtra("company", item.company);
                intent.putExtra("address", item.address);
                intent.putExtra("userid", item.userid);
                intent.putExtra("tel", item.tel);
                intent.putExtra("email", item.email);
                intent.putExtra("addtime", item.addtime);
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
//        if (data == null) {
//            return;
//        }
//
//        String number = data.getStringExtra("number");
//        String company = data.getStringExtra("company");
//        String address = data.getStringExtra("address");
//        String userid = data.getStringExtra("userid");
//        String tel = data.getStringExtra("tel");
//        String email = data.getStringExtra("email");
//        String addtime = data.getStringExtra("addtime");
//
//
//        HashMap<String, String> params = new HashMap<>();
//        params.put("number", number);
//        params.put("company", company);
//        params.put("address", address);
//        params.put("userid", mAccount.getId());
//        params.put("tel", tel);
//        params.put("email", email);
//        params.put("addtime", addtime);
//
//        mPostDialog = LoadingProgress.showProgress(this, "正在添加");
//       // mPresenter.addReceipt(params);
//        mPresenter.postData(params);
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
    public void onRefreshSuccess(BaseEntity<List<Receipt>> entity) {
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
