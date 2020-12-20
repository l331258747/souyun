package com.xrwl.owner.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 默认从底部弹出的对话框
 * Created by www.longdw.com on 2018/4/2 下午9:03.
 */

public abstract class BasePopsDialog extends DialogFragment {

    protected String TAG;
    protected View mView;
    private Unbinder mUnbinder;





    protected int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected FragmentActivity mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TAG = this.getClass().getSimpleName();
        mContext = getActivity();
        View view = inflater.inflate(getLayoutId(), container, false);

        mView = view;

        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();

//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//
//        Window dialogWindow = getDialog().getWindow();
//        dialogWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
//
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
////        lp.x = 10;
////        lp.y = 170;
////
////        dialogWindow.setGravity(Gravity.TOP);
//
//        dialogWindow.setGravity(Gravity.BOTTOM);
//
//        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        lp.height = mHeight;
//        lp.windowAnimations = R.style.PopTranslateAnimStyle;
//        lp.alpha = 1.0f;
//        lp.dimAmount = 0.4f;
//        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    protected void initBaseRv(RecyclerView rv) {
      //  rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
      //  rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    protected <T extends View> T findViewById(int id) {
        return mView.findViewById(id);
    }

    protected abstract int getLayoutId();
    protected abstract void initView();
}
