package com.xrwl.owner.module.publish.dialog;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;

import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.xrwl.owner.module.home.ui.OwnerAuthActivity;

import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/4 下午11:29.
 */
public class AuthDialog extends BasePopDialog {

    @Override
    protected int getLayoutId() {
        return R.layout.publish_auth_dialog;
    }

    @Override
    protected void initView() {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.quanxianbuzushimingrenzheng);
        mediaPlayer.start();
    }

    @OnClick({R.id.authDialogCancelTv, R.id.authDialogAuthTv})
    public void onClick(View v) {
        if (v.getId() == R.id.authDialogCancelTv) {
            dismiss();
        } else {
            getActivity().startActivity(new Intent(getActivity(), OwnerAuthActivity.class));
            dismiss();
        }
    }
}
