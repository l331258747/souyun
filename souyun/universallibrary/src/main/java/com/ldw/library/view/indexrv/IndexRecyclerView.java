package com.ldw.library.view.indexrv;

/**
 * Created by MyInnos on 31-01-2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ldw.library.R;


public class IndexRecyclerView extends RecyclerView {

    public static final String TAG = IndexRecyclerView.class.getSimpleName();

    private IndexScroller mScroller = null;
    private GestureDetector mGestureDetector = null;

    private boolean mEnabled = true;

    public int mTextSize = 12;
    public int mPreviewTextSize = 50;
    public float mBarWidth = 20;
    public float mBarMargin = 5;
    public float mPreviewPadding = 5;
    public int mBarTransparentValue = 0;
    public int mBarCornerRadius = 5;
    public int mBackgroudColor = 0x00000000;
    public int mTextColor = 0xff565656;
    public int mHighlightedTextColor = 0xff000000;

    public IndexRecyclerView(Context context) {
        super(context);
    }

    public IndexRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndexRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexRecyclerView);

            if (typedArray != null) {
                try {
                    mTextSize = getDimensionSpSize(typedArray, R.styleable.IndexRecyclerView_textSize, mTextSize);

                    mPreviewTextSize = getDimensionSpSize(typedArray, R.styleable.IndexRecyclerView_previewTextSize, mPreviewTextSize);

                    mBarWidth = getDimensionDpSize(typedArray, R.styleable.IndexRecyclerView_barWidth, mBarWidth);

                    mBarMargin = getDimensionDpSize(typedArray, R.styleable.IndexRecyclerView_barMargin, mBarMargin);

                    mPreviewPadding = getDimensionDpSize(typedArray, R.styleable.IndexRecyclerView_previewPadding, mPreviewPadding);

                    mBarCornerRadius = getDimensionDpSize(typedArray, R.styleable.IndexRecyclerView_cornerRadius, mBarCornerRadius);

                    mBarTransparentValue = getDimensionDpSize(typedArray, R.styleable.IndexRecyclerView_transparentValue, mBarTransparentValue);

                    if (typedArray.getString(R.styleable.IndexRecyclerView_backgroundColor) != null) {
                        String backgroudColor = typedArray.getString(R.styleable.IndexRecyclerView_backgroundColor);
                        mBackgroudColor = Color.parseColor(backgroudColor);
                    } else {
                        int colorStyleable = typedArray.getResourceId(R.styleable.IndexRecyclerView_backgroundColor, 0);
                        if (colorStyleable != 0) {
                            mBackgroudColor = typedArray.getResources().getColor(colorStyleable);
                        }
                    }

                    if (typedArray.getString(R.styleable.IndexRecyclerView_textColor) != null) {
                        String textColor = typedArray.getString(R.styleable.IndexRecyclerView_textColor);
                        mTextColor = Color.parseColor(textColor);
                    } else {
                        int colorStyleable = typedArray.getResourceId(R.styleable.IndexRecyclerView_textColor, 0);
                        if (colorStyleable != 0) {
                            mTextColor = typedArray.getResources().getColor(colorStyleable);
                        }
                    }

                    if (typedArray.getString(R.styleable.IndexRecyclerView_highlightedTextColor) != null) {
                        String highlightedTextColor = typedArray.getString(R.styleable.IndexRecyclerView_highlightedTextColor);
                        mHighlightedTextColor = Color.parseColor(highlightedTextColor);
                    } else {
                        int colorStyleable = typedArray.getResourceId(R.styleable.IndexRecyclerView_highlightedTextColor, 0);
                        if (colorStyleable != 0) {
                            mHighlightedTextColor = typedArray.getResources().getColor(colorStyleable);
                        }
                    }

                } finally {
                    typedArray.recycle();
                }
            }

            mScroller = new IndexScroller(context, this);
        }

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();

                int firstItemPosition = manager.findFirstVisibleItemPosition();
                if (firstItemPosition == RecyclerView.NO_POSITION) return;
                mScroller.setSelection(firstItemPosition);
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Overlay index bar
        if (mScroller != null)
            mScroller.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mEnabled) {
            // Intercept ListView's touch event
            if (mScroller != null && mScroller.onTouchEvent(ev))
                return true;

            if (mGestureDetector == null) {
                mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                            float velocityX, float velocityY) {
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }

                });
            }
            mGestureDetector.onTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mEnabled && mScroller != null && mScroller.contains(ev.getX(), ev.getY()))
            return true;

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (mScroller != null)
            mScroller.setAdapter(adapter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller != null)
            mScroller.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * @param value int to set the text size of the index bar
     */
    public void setIndexTextSize(int value) {
        mScroller.setIndexTextSize(value);
    }

    /**
     * @param value float to set the width of the index bar
     */
    public void setIndexbarWidth(float value) {
        mScroller.setIndexbarWidth(value);
    }

    /**
     * @param value float to set the margin of the index bar
     */
    public void setIndexbarMargin(float value) {
        mScroller.setIndexbarMargin(value);
    }

    /**
     * @param value int to set the preview padding
     */
    public void setPreviewPadding(int value) {
        mScroller.setPreviewPadding(value);
    }

    /**
     * @param value int to set the corner radius of the index bar
     */
    public void setIndexBarCornerRadius(int value) {
        mScroller.setIndexBarCornerRadius(value);
    }

    /**
     * @param value float to set the transparency value of the index bar
     */
    public void setIndexBarTransparentValue(int value) {
        mScroller.setIndexBarTransparentValue(value);
    }

    /**
     * @param typeface Typeface to set the typeface of the preview & the index bar
     */
    public void setTypeface(Typeface typeface) {
        mScroller.setTypeface(typeface);
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    public void setIndexBarVisibility(boolean shown) {
        mScroller.setIndexBarVisibility(shown);
        mEnabled = shown;
    }

    /**
     * @param shown boolean to show or hide the preview
     */
    public void setPreviewVisibility(boolean shown) {
        mScroller.setPreviewVisibility(shown);
    }

    /**
     * @param color The color for the index bar
     */
    public void setIndexBarColor(@ColorRes int color) {
        mScroller.setIndexBarColor(color);
    }

    /**
     * @param color The text color for the index bar
     */
    public void setIndexBarTextColor(@ColorRes int color) {
        mScroller.setIndexBarTextColor(color);
    }

    /**
     * @param color The text color for the index bar
     */
    public void setIndexbarHighLateTextColor(@ColorRes int color) {
        mScroller.setIndexBarHighLateTextColor(color);
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    public void setIndexBarHighLateTextVisibility(boolean shown) {
        mScroller.setIndexBarHighLateTextVisibility(shown);
    }

    /**
     * get the dimension pixel size from typeArray which is defined in attr
     *
     * @return the dimension pixel size
     */
    private int getDimensionSpSize(TypedArray typeArray, int styleable, int defaultValue) {
        int sizeStyleable = typeArray.getResourceId(styleable, 0);
        return sizeStyleable > 0 ? typeArray.getResources().getDimensionPixelSize(sizeStyleable) : typeArray.getDimensionPixelSize(styleable, (int) getPixelSizeBySp(defaultValue));
    }

    /**
     * get Pixel size by sp
     *
     * @param sp sp
     * @return the value of px
     */
    private float getPixelSizeBySp(int sp) {
        Resources res = this.getResources();
        final float scale = res.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    private int getDimensionDpSize(TypedArray typeArray, int styleable, float defaultValue) {
        int sizeStyleable = typeArray.getResourceId(styleable, 0);
        return sizeStyleable > 0 ? typeArray.getResources().getDimensionPixelSize(sizeStyleable) : typeArray.getDimensionPixelSize(styleable, (int) getPixelSizeByDp(defaultValue));
    }

    /**
     * get Pixel size by dp
     *
     * @param dp dp
     * @return the value of px
     */
    private float getPixelSizeByDp(float dp) {
        Resources res = this.getResources();
        final float scale = res.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }
}