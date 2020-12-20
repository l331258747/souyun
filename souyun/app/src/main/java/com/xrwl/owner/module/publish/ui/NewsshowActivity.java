package com.xrwl.owner.module.publish.ui;

import android.content.Intent;
import android.widget.TextView;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.New;
import com.xrwl.owner.module.publish.mvp.NewContract;
import com.xrwl.owner.module.publish.mvp.NewPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * 事实新闻详情列表
 * Created by www.longdw.com on 2018/7/11 下午7:47.
 */
public class NewsshowActivity extends BaseActivity<NewContract.IView, NewPresenter> implements NewContract.IView {



    @BindView(R.id.titlesTv)
    TextView mtitleTv;
    @BindView(R.id.contentTv)
    TextView mcontentTv;
    @BindView(R.id.timeTv)
    TextView mtimeTv;




    @Override
    protected NewPresenter initPresenter() {
        return new NewPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.newsshow_layout;
    }

    @Override
    protected void initViews() {

        String mId = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        String cotents = getIntent().getStringExtra("neirong");
        String addtime = getIntent().getStringExtra("addtime");
        mtimeTv.setText(addtime);
        mtitleTv.setText(title);
        mcontentTv.setText(cotents);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }


    }

    @Override
    public void onPostSuccess(BaseEntity entity) {

    }

    @Override
    public void onPostError(BaseEntity entity) {

    }

    @Override
    public void onPostError(Throwable e) {
       // mPostDialog.dismiss();
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



    @Override
    public void onRefreshSuccess(BaseEntity<List<New>> entity) {
        if (entity.getData() != null && entity.getData().size() > 0) {

//            mAdapter.setDatas(entity.getData());
//            mWrapper.notifyDataSetChanged();
           // mDatas = entity.getData();


        }
    }

    @Override
    public void onRefreshError(Throwable e) {
    }

    @Override
    public void onError(BaseEntity entity) {

    }
}
