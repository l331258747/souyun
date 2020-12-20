package com.xrwl.owner.module.publish.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.adapter.recycler.wrapper.HeaderAndFooterWrapper;
import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.New;
import com.xrwl.owner.module.publish.adapter.NewAdapter;
import com.xrwl.owner.module.publish.mvp.NewContract;
import com.xrwl.owner.module.publish.mvp.NewPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 事实新闻列表
 * Created by www.longdw.com on 2018/7/11 下午7:47.
 */
public class NewsActivity extends BaseActivity<NewContract.IView, NewPresenter> implements NewContract.IView {

    public static final int RESULT_POSITION = 100;

    @BindView(R.id.baseRv)
    RecyclerView mRv;
    private NewAdapter mAdapter;
    private ProgressDialog mPostDialog;
    private List<New> mDatas;
    private HeaderAndFooterWrapper mWrapper;

    @Override
    protected NewPresenter initPresenter() {
        return new NewPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.news_layout;
    }

    @Override
    protected void initViews() {
        initBaseRv(mRv);

        mAdapter = new NewAdapter(this, R.layout.news_recycler_item, new ArrayList<New>());
        mWrapper = new HeaderAndFooterWrapper(mAdapter);


        mRv.setAdapter(mWrapper);



        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mAdapter.setSelectedPos(position);

                New anew=mDatas.get(position);
                Intent intent = new Intent(mContext, NewsshowActivity.class);
                intent.putExtra("id", anew.id);
                intent.putExtra("title", anew.title);
                intent.putExtra("neirong", anew.content);
                intent.putExtra("addtime", anew.addtime);
                startActivity(intent);
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
        mPresenter.getDatanew();
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

//        HashMap<String, String> params = new HashMap<>();
//
//
//        try {
//            params.put("des",URLEncoder.encode(des,"UTF-8") );
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        try {
//            params.put("city", URLEncoder.encode(city,"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        try {
//            params.put("province",URLEncoder.encode(province,"UTF-8") );
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        try {
//            params.put("lat", URLEncoder.encode(String.valueOf(lat),"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        try {
//            params.put("lng", URLEncoder.encode(String.valueOf(lng),"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        try {
//            params.put("userid", URLEncoder.encode(mAccount.getId(),"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        mPostDialog = LoadingProgress.showProgress(this, "正在添加");
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
    public void onPostshowSuccess(BaseEntity entity) {

    }

    @Override
    public void onPostshowError(BaseEntity entity) {

    }

    @Override
    public void onPostshowError(Throwable e) {

    }

//    @Override
//    public void onRefreshSuccess(BaseEntity<List<Address2>> entity) {
//        if (entity.getData() != null && entity.getData().size() > 0) {
//            mAdapter.setDatas(entity.getData());
//            mWrapper.notifyDataSetChanged();
//            mDatas = entity.getData();
//        }
//
//    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<New>> entity) {
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
