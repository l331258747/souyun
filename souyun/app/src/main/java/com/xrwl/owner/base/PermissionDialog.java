package com.xrwl.owner.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ViewFlipper;

import com.blankj.utilcode.util.PermissionUtils;
import com.xrwl.owner.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/7 下午12:45.
 */
public class PermissionDialog extends DialogFragment {

    @BindView(R.id.permissionVf)
    ViewFlipper mViewFlipper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.permission_dialog_layout, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);


        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = (int) (dm.widthPixels * 0.8);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

//        lp.windowAnimations = R.style.PopTranslateAnimStyle;
        lp.alpha = 0.95f;
        lp.dimAmount = 0.4f;
        dialogWindow.setAttributes(lp);

        setCancelable(false);
    }

    @OnClick({R.id.permissionDisagreeBtn, R.id.permissionAgreeBtn})
    public void onClick(View v) {

        if (v.getId() == R.id.permissionDisagreeBtn) {

        } else if (v.getId() == R.id.permissionAgreeBtn) {
            if (mViewFlipper.getDisplayedChild() == 0) {
                @SuppressLint("WrongConstant") PermissionUtils utils = PermissionUtils
                        .permission(Manifest.permission.CALL_PHONE)
                        .callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                mViewFlipper.showNext();
                            }

                            @Override
                            public void onDenied() {
                                mViewFlipper.showNext();

                            }
                        });
                utils.request();
            } else if (mViewFlipper.getDisplayedChild() == 1) {
                @SuppressLint("WrongConstant") PermissionUtils utils = PermissionUtils
                        .permission(Manifest.permission.GET_ACCOUNTS)
                        .callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                mViewFlipper.showNext();
                            }

                            @Override
                            public void onDenied() {
                                mViewFlipper.showNext();
                            }
                        });
                utils.request();
            } else if (mViewFlipper.getDisplayedChild() == 2) {
                @SuppressLint("WrongConstant") PermissionUtils utils = PermissionUtils
                        .permission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                        .callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                mViewFlipper.showNext();
                            }

                            @Override
                            public void onDenied() {
                                mViewFlipper.showNext();
                            }
                        });

                utils.request();
            } else if (mViewFlipper.getDisplayedChild() == 3) {
                @SuppressLint("WrongConstant") PermissionUtils utils = PermissionUtils
                        .permission(Manifest.permission.CAMERA)
                        .callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                ((IPermissionCallback) getActivity()).onCallback();
                            }

                            @Override
                            public void onDenied() {
                                ((IPermissionCallback) getActivity()).onCallback();
                            }
                        });
                utils.request();
            }
        }
    }

    public interface IPermissionCallback {
        void onCallback();
    }
}
