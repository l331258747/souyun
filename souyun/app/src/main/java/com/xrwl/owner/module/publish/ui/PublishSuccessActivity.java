package com.xrwl.owner.module.publish.ui;

import android.content.Intent;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.PostOrder;
import com.xrwl.owner.event.PublishClearCacheEvent;
import com.xrwl.owner.module.publish.bean.PublishBean;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/3 下午10:39.
 */

public class PublishSuccessActivity extends BaseActivity {
    private PublishBean mPublishBean;
    @Override
    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.publishsuccess_activity;
    }

    @Override
    protected void initViews() {

    }

    @OnClick(R.id.psOkBtn)
    public void onClick() {

        EventBus.getDefault().post(new PublishClearCacheEvent());
        finish();
    }


    public void onRefreshSuccess(BaseEntity<PostOrder> entity) {
        //mPostDialog.dismiss();

        Intent intent = new Intent(this, OrderConfirmActivity.class);
        intent.putExtra("publishBean", (Serializable) mPublishBean);
        intent.putExtra("postOrder", entity.getData());

        startActivity(intent);

        EventBus.getDefault().post(new PublishClearCacheEvent());

        finish();

    }
}
