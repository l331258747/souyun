package com.xrwl.owner.module.publish.ui;

import android.content.Intent;

import com.ldw.library.bean.BaseEntity;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.New;
import com.xrwl.owner.module.publish.mvp.NewContract;
import com.xrwl.owner.module.publish.mvp.NewPresenter;

import java.util.List;

/**
 * 事实新闻详情列表
 * Created by www.longdw.com on 2018/7/11 下午7:47.
 */
public class TiaozhuanActivity extends BaseActivity<NewContract.IView, NewPresenter> implements NewContract.IView {
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

        startActivity(new Intent(this, PublishFragment.class));
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
