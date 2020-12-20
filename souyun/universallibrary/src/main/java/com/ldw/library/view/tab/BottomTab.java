package com.ldw.library.view.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2017/11/24 下午4:45.
 */

public class BottomTab extends LinearLayout {

    private int mSelectedPos = -1;
    private List<BottomTabItem> mItems = new ArrayList<>();

    private OnTabSelectedListener mListener;
    private Context mContext;
    private List<Fragment> mDatas;
    private FragmentManager mFragmentManager;
    private int mContainerRes;

    public BottomTab(Context context) {
        super(context);
        init(context);
    }

    public BottomTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }


    public void addItem(final BottomTabItem tabItem) {

        //中间层 为了兼容LBadgeView
        FrameLayout badgeLayout = new FrameLayout(mContext);
        tabItem.setBadgeTargetView(badgeLayout);
        badgeLayout.setPadding(15, 0, 15, 0);
        FrameLayout.LayoutParams itemLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        itemLayoutParams.gravity = Gravity.CENTER;
        badgeLayout.addView(tabItem.getView(), itemLayoutParams);

        FrameLayout mainLayout = new FrameLayout(mContext);
        FrameLayout.LayoutParams subParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        subParams.gravity = Gravity.CENTER;
        mainLayout.addView(badgeLayout, subParams);

//        addView(tabItem.getView(), params);
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        addView(mainLayout, params);
        mItems.add(tabItem);

        tabItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int prePos = mSelectedPos;
                selectTab(tabItem);

                if (mSelectedPos == prePos) {
                    if (mListener != null) {
                        mListener.onTabReselected(mSelectedPos);
                    }
                    return;
                }

                selectFragment(prePos);

                if (mListener != null) {
                    mListener.onTabSelected(mSelectedPos);
                    mListener.onTabUnselected(prePos);
                }
            }
        });
    }

    private void selectTab(BottomTabItem tabItem) {
        for(int i = 0, size = mItems.size();i < size;i++) {
            BottomTabItem bti = mItems.get(i);
            bti.unSelect();
            if (tabItem == bti) {
                mSelectedPos = i;
            }
        }
        tabItem.select();
    }

    private void selectFragment(int prePos) {
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (prePos != -1) {
            ft.hide(mDatas.get(prePos));
        }

        if (!mDatas.get(mSelectedPos).isAdded()) {
            ft.add(mContainerRes, mDatas.get(mSelectedPos));
        } else {
            ft.show(mDatas.get(mSelectedPos));
        }

        ft.commit();
    }

    public void selectTab(int position) {
        for(BottomTabItem bti : mItems) {
            bti.unSelect();
        }

        int prePos = mSelectedPos;

        mSelectedPos = position;
//        mItems.get(position).select();

        selectFragment(prePos);
    }

    public void setDatas(FragmentManager fm, int containerRes, List<Fragment> datas) {
        mDatas = datas;
        mFragmentManager = fm;
        mContainerRes = containerRes;
    }

    public void setDatas(FragmentManager fm, int containerRes, List<Fragment> datas, List<BottomTabItem> tabItems) {
        mDatas = datas;
        mFragmentManager = fm;
        mContainerRes = containerRes;

        for (BottomTabItem btt : tabItems) {
            addItem(btt);
        }
    }

    public List<BottomTabItem> getItems() {
        return mItems;
    }

    public interface OnTabSelectedListener {

        /**
         * Called when a tab enters the selected state.
         *
         * @param position The position of the tab that was selected
         */
        void onTabSelected(int position);

        /**
         * Called when a tab exits the selected state.
         *
         * @param position The position of the tab that was unselected
         */
        void onTabUnselected(int position);

        /**
         * Called when a tab that is already selected is chosen again by the user. Some applications
         * may use this action to return to the top level of a category.
         *
         * @param position The position of the tab that was reselected.
         */
        void onTabReselected(int position);
    }

    public void setOnTabSelectedListener(OnTabSelectedListener l) {
        mListener = l;
    }
}
