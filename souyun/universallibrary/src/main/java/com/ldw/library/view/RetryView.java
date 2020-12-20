/**
 * Copyright (c) www.longdw.com
 */
package com.ldw.library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldw.library.R;
import com.ldw.library.utils.DisplayUtil;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


/**
 * 用户等待界面加载数据时的loading界面</p>
 * 具体用法介绍：</p>
 * <pre class="prettyprint">
 * &lt;com.android.lt.view.retryview.RetryView
 * xmlns:retryview="http://schemas.android.com/apk/res-auto"
 * android:id="@+id/retryview"
 * android:layout_width="match_parent"
 * android:layout_height="match_parent"
 * retryview:loadingText="你好，我在加载中..."
 * retryview:loadingTextSize="6sp"
 * retryview:loadingTextColor="#ff0000"
 * retryview:failedIcon="@drawable/biz_pic_empty_view" /&lt;
 *
 * @author longdawei1988@gmail.com
 *         <p>
 *         2015年2月4日 上午9:31:31
 */
public class RetryView extends FrameLayout {

    private OnRetryListener mListener;

    private static final int DEFAULT_FAILED_TEXTSIZE = 25;
    private static final int DEFAULT_LOADING_TEXTSIZE = 35;
    private static final int DEFAULT_NODATA_TEXTSIZE = 25;

    private static final int DEFAULT_PROGRESS_SIZE = 35;
    private static final int DEFAULT_PROGRESS_COLOR = 0xff519cff;

    private static final int DEFAULT_FAILED_TEXTCOLOR = 0xff8a8a8a;
    private static final int DEFAULT_LOADING_TEXTCOLOR = 0xff8a8a8a;
    private static final int DEFAULT_NODATA_TEXTCOLOR = 0xff8a8a8a;

    private static final int DEFAULT_FAILED_ORIENTATION = 1;// vertical
    private static final int DEFAULT_LOADING_ORIENTATION = 1;

    private static final String DEFAULT_LOADING_TEXT = "正在加载...";

    private Context mContext;
    /**
     * 加载失败后的图标 可有可无
     */
    private Drawable mFailedIcon;

    /**
     * 加载失败后的文字信息
     */
    private String mFailedText;
    private int mFailedTextColor;
    private float mFailedTextSize;
    private ImageView mFailedImageView;

    /**
     * 正在加载的文字信息
     */
    private String mLoadingText;
    private int mLoadingTextColor;
    private float mLoadingTextSize;
    private String mTextFont;

    /**
     * 加载失败view的对齐方式
     */
    private int mFailedOrientation;
    /**
     * 正在加载view的对齐方式
     */
    private int mLoadingOrientation;

    private MaterialProgressBar mProgressBar;

    /**
     * 正在加载view
     */
    private LinearLayout mLoadingLayout;

    /**
     * 加载失败view
     */
    private LinearLayout mFailedLayout;

    /**
     * 没有数据view
     */
    private LinearLayout mNoDataLayout;

    private int mProgressSize;
    private int mProgressColor;

    private int mNoDataTextSize;
    private int mNoDataTextColor;
    private String mNoDataText;
    private ImageView mNoDataImageView;
    private Drawable mNoDataIcon;

    public RetryView(Context context) {
        super(context);
        init(context, null);
    }

    public RetryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RetryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RetryView);

        mFailedIcon = a.getDrawable(R.styleable.RetryView_failedIcon);
        if (null == mFailedIcon) {
            mFailedIcon = getResources().getDrawable(R.drawable.ul_retryview_loadfailed_bg);
        }

        mNoDataIcon = a.getDrawable(R.styleable.RetryView_nodataIcon);
        if (null == mNoDataIcon) {
            mNoDataIcon = getResources().getDrawable(R.drawable.ul_retryview_nodata_bg);
        }

        mFailedText = a.getString(R.styleable.RetryView_failedText);

        mLoadingText = a.getString(R.styleable.RetryView_loadingText);
        if (TextUtils.isEmpty(mLoadingText)) {
            mLoadingText = DEFAULT_LOADING_TEXT;
        }

        mFailedOrientation = a.getInt(R.styleable.RetryView_failedOrientation,
                DEFAULT_FAILED_ORIENTATION);

        mLoadingOrientation = a.getInt(
                R.styleable.RetryView_loadingOrientation,
                DEFAULT_LOADING_ORIENTATION);

        mFailedTextColor = a.getColor(R.styleable.RetryView_failedTextColor,
                DEFAULT_FAILED_TEXTCOLOR);

        mLoadingTextColor = a.getColor(R.styleable.RetryView_loadingTextColor,
                DEFAULT_LOADING_TEXTCOLOR);

        mTextFont = a.getString(R.styleable.RetryView_textFont);


        mFailedTextSize = a.getDimensionPixelSize(
                R.styleable.RetryView_failedTextSize, (int) DisplayUtil.getPixelSizeBySp(context, DEFAULT_FAILED_TEXTSIZE));

        mLoadingTextSize = a.getDimensionPixelSize(
                R.styleable.RetryView_loadingTextSize, (int)DisplayUtil.getPixelSizeBySp(context, DEFAULT_LOADING_TEXTSIZE));

        int progressSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PROGRESS_SIZE, dm);
        mProgressSize = a.getDimensionPixelSize(R.styleable.RetryView_progressSize, progressSize);

        mProgressColor = a.getColor(R.styleable.RetryView_progressColor, DEFAULT_PROGRESS_COLOR);

        mNoDataTextSize = a.getDimensionPixelSize(R.styleable.RetryView_nodataTextSize, (int)DisplayUtil.getPixelSizeBySp
                (context, DEFAULT_NODATA_TEXTSIZE));
        mNoDataTextColor = a.getColor(R.styleable.RetryView_nodataTextColor, DEFAULT_NODATA_TEXTCOLOR);
        int noDataRes = a.getResourceId(R.styleable.RetryView_nodataText, 0);
        if (noDataRes != 0) {
            mNoDataText = getResources().getString(noDataRes);
        } else {
            mNoDataText = a.getString(R.styleable.RetryView_nodataText);
			if (TextUtils.isEmpty(mNoDataText)) {
				mNoDataText = "暂无数据";
			}
        }

        a.recycle();

        initView();
    }

    private void initView() {

        //正在加载的view
        mLoadingLayout = new LinearLayout(mContext);
        mLoadingLayout.setOnClickListener(null);
        mLoadingLayout.setOrientation(mLoadingOrientation);
        mLoadingLayout.setGravity(Gravity.CENTER);
        mLoadingLayout.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mProgressBar = new MaterialProgressBar(mContext);
        mProgressBar.setProgressTintList(ColorStateList.valueOf(mProgressColor));
        mProgressBar.setLayoutParams(new LayoutParams(mProgressSize, mProgressSize));

        TextView loadingTv = new TextView(mContext);
        loadingTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadingTextSize);
        loadingTv.setTextColor(mLoadingTextColor);
        loadingTv.setText(mLoadingText);
        loadingTv.setGravity(Gravity.CENTER);
        if (mTextFont != null) {
            Typeface fontFace = Typeface.createFromAsset(getResources().getAssets(), mTextFont);
            loadingTv.setTypeface(fontFace);
        }

        mLoadingLayout.addView(mProgressBar);
        mLoadingLayout.addView(loadingTv, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(mLoadingLayout);

        //加载失败的view
        mFailedLayout = new LinearLayout(mContext);
        mFailedLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    showLoading();
                    mListener.onRetry();
                }
            }
        });
        mFailedLayout.setVisibility(View.GONE);
        mFailedLayout.setOrientation(mFailedOrientation);
        mFailedLayout.setGravity(Gravity.CENTER);
        mFailedLayout.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mFailedImageView = new ImageView(mContext);
        mFailedImageView.setImageDrawable(mFailedIcon);
        TextView failedTv = new TextView(mContext);
        failedTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mFailedTextSize);
        failedTv.setTextColor(mFailedTextColor);
        failedTv.setText(mFailedText);
        failedTv.setGravity(Gravity.CENTER);

        if (mTextFont != null) {
            Typeface fontFace = Typeface.createFromAsset(getResources().getAssets(), mTextFont);
            failedTv.setTypeface(fontFace);
        }

        mFailedLayout.addView(mFailedImageView);
        mFailedLayout.addView(failedTv, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(mFailedLayout);

        //没有数据view
        mNoDataLayout = new LinearLayout(mContext);
        mNoDataLayout.setOrientation(mFailedOrientation);
        mNoDataLayout.setOnClickListener(null);//要不然会点到覆盖在下面的view
        mNoDataLayout.setVisibility(View.GONE);
        mNoDataLayout.setGravity(Gravity.CENTER);
        mNoDataLayout.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mNoDataLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    showLoading();
                    mListener.onRetry();
                }
            }
        });

        mNoDataImageView = new ImageView(mContext);
        mNoDataImageView.setImageDrawable(mNoDataIcon);
        mNoDataLayout.addView(mNoDataImageView);

        TextView noDataTv = new TextView(mContext);
        noDataTv.setText(mNoDataText);
        noDataTv.setTextColor(mNoDataTextColor);
        noDataTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNoDataTextSize);
        noDataTv.setTypeface(Typeface.DEFAULT_BOLD);

        if (mTextFont != null) {
            Typeface fontFace = Typeface.createFromAsset(getResources().getAssets(), mTextFont);
            noDataTv.setTypeface(fontFace);
        }

        mNoDataLayout.addView(noDataTv, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        addView(mNoDataLayout);
    }

    /**
     * 显示正在加载的view
     */
    public void showLoading() {
        mNoDataLayout.setVisibility(View.GONE);
        mFailedLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        this.setVisibility(View.VISIBLE);

    }

    /**
     * 显示加载失败时的view
     */
    public void showError() {

        mNoDataLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        this.setVisibility(View.VISIBLE);
        mFailedLayout.setVisibility(View.VISIBLE);

    }

    /**
     * 显示没有数据时的view
     */
    public void showNoData() {

        mFailedLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        this.setVisibility(View.VISIBLE);
        mNoDataLayout.setVisibility(View.VISIBLE);

    }

    /**
     * 加载成功后显示内容
     */
    public void showContent() {
//		Animation animation = new AlphaAnimation(1.0f, 0.0f);
//		this.startAnimation(animation);
        this.setVisibility(View.GONE);
    }

    /**
     * 监听点击“重新加载”事件
     *
     * @param l
     */
    public void setOnRetryListener(OnRetryListener l) {
        mListener = l;
    }

    public RetryView setFailedIcon(int resId) {
        mFailedImageView.setImageResource(resId);
        return this;
    }

    public RetryView setNoDataIcon(int resId) {
        mNoDataImageView.setImageResource(resId);
        return this;
    }

    public RetryView hideNoDataIcon() {
        mNoDataImageView.setVisibility(View.GONE);
        return this;
    }

    public RetryView hideFailedIcon() {
        mFailedImageView.setVisibility(View.GONE);
        return this;
    }

    public interface OnRetryListener {
        void onRetry();
    }
}
