package com.ldw.library.view.tab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ldw.library.R;


/**
 * Created by www.longdw.com on 2017/11/24 下午4:55.
 */

public class BottomTabItem {

    private static String normalTitleColor = "#519cff";
    private static String selectedTitleColor = "#939393";

    private Context mContext;
    private Resources mResources;
    private int mNormalDrawableRes;
    private int mSelectedRes;
    private Drawable mSelector;

    private View mTabItem;

    private View mBadgeTargetView;

    public BottomTabItem (Context context, int normalRes, int selectedRes, String title) {
        this(context, normalRes, selectedRes, null, null, title);
    }

    public BottomTabItem (Context context,
                          int normalRes,
                          int selectedRes,
                          String normalTitleColor,
                          String selectedTitleColor,
                          String title) {
        mContext = context;
        mResources = context.getResources();

        mNormalDrawableRes = normalRes;
        mSelectedRes = selectedRes;

        createSelector(normalRes, selectedRes);
        TextView textView = createTextView();
        mTabItem = textView;
        textView.setText(title);
    }

    public BottomTabItem(Context context, @DrawableRes int normalRes, @DrawableRes int selectedRes, @LayoutRes int layoutRes) {

        if (layoutRes == 0) {
            return;
        }

        mContext = context;
        mResources = context.getResources();

        mTabItem = LayoutInflater.from(context).inflate(layoutRes, null);

        mNormalDrawableRes = normalRes;
        mSelectedRes = selectedRes;

        createSelector(normalRes, selectedRes);
    }

    /**
     * 默认的item为上面图片下面文字 故使用TextView
     */
    private TextView createTextView() {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        int colors[] = new int[] { Color.parseColor(normalTitleColor), Color.parseColor(selectedTitleColor)};
        int[][] states = new int[2][];
        states[0] = new int[] { android.R.attr.state_pressed };
        states[1] = new int[] { -android.R.attr.state_pressed };
        ColorStateList colorStateList = new ColorStateList(states, colors);

        textView.setTextColor(colorStateList);

        return textView;
    }

    public View getView() {
        return mTabItem;
    }

    private void createSelector(int normalRes, int selectedRes) {
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = mResources.getDrawable(normalRes);
        Drawable selected = mResources.getDrawable(selectedRes);
        drawable.addState(new int[]{-android.R.attr.state_pressed},
                normal);
        drawable.addState(new int[]{android.R.attr.state_pressed},
                selected);
        mSelector = drawable;
    }

    public void select() {
        Drawable drawable = mResources.getDrawable(mSelectedRes);
        if (mTabItem instanceof TextView) {
            TextView textView = (TextView) mTabItem;
            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            textView.setTextColor(mResources.getColor(R.color.colorPrimary));
        } else {
            mTabItem.setBackground(drawable);
        }
    }

    public void unSelect() {
        if (mTabItem instanceof TextView) {
            TextView textView = (TextView) mTabItem;
            textView.setCompoundDrawablesWithIntrinsicBounds(null, mSelector, null, null);
            textView.setTextColor(mResources.getColor(R.color.ul_tab_text_selector));
        } else {
            mTabItem.setBackground(mSelector);
        }
    }

    public void setOnClickListener(View.OnClickListener l) {
        mTabItem.setOnClickListener(l);
    }

    public void setBadgeTargetView(View view) {
        mBadgeTargetView = view;
    }

    /** 如果要加LBadgeView 一定要加到这上面去 */
    public View getBadgeTargetView() {
        return mBadgeTargetView;
    }

}
