package com.ldw.library.view.indexrv;

/**
 * Created by MyInnos on 31-01-2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SectionIndexer;

import java.util.Arrays;
import java.util.List;

public class IndexScroller extends RecyclerView.AdapterDataObserver {

    private float mBarWidth;
    private float mBarMargin;
    private float mPreviewPadding;
    private float mDensity;
    private float mScaledDensity;
    private int mListViewWidth;
    private int mListViewHeight;
    private int mCurrentSection = -1;
    private boolean mIsIndexing = false;
    private RecyclerView mRecyclerView = null;
    private SectionIndexer mIndexer = null;
    private List<String> mSections = null;
    private RectF mIndexbarRect;

    private int mTextSize;
    private int mPreviewTextSize;
    private boolean mPreviewVisibility = false;
    private int mCornerRadius;
    private Typeface mTypeface = null;
    private boolean mBarVisibility = true;
    private boolean mShowHighlighted = true;
    private int mBackgroundColor;
    private int mTextColor;
    private int mHightedTextColor;

    private int mBackgroudAlpha;

    public IndexScroller(Context context, IndexRecyclerView rv) {

        mTextSize = rv.mTextSize;
        mPreviewTextSize = rv.mPreviewTextSize;
        mCornerRadius = rv.mBarCornerRadius;
        mBackgroundColor = rv.mBackgroudColor;
        mTextColor = rv.mTextColor;
        mHightedTextColor = rv.mHighlightedTextColor;

        mBackgroudAlpha = rv.mBarTransparentValue;

        mDensity = context.getResources().getDisplayMetrics().density;
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mRecyclerView = rv;
        setAdapter(mRecyclerView.getAdapter());

        mBarWidth = rv.mBarWidth;
        mBarMargin = rv.mBarMargin;
        mPreviewPadding = rv.mPreviewPadding;
    }

    public void draw(Canvas canvas) {

        if (mBarVisibility) {

            //画IndexBar的背景
            Paint indexbarPaint = new Paint();
            indexbarPaint.setColor(mBackgroundColor);
            indexbarPaint.setAlpha(mBackgroudAlpha);
            indexbarPaint.setAntiAlias(true);
            canvas.drawRoundRect(mIndexbarRect, mCornerRadius, mCornerRadius, indexbarPaint);

            if (mSections != null && mSections.size() > 0) {
                // Preview is shown when mCurrentSection is set
                if (mPreviewVisibility && mCurrentSection >= 0 && !mSections.get(mCurrentSection).equals("")) {
                    Paint previewPaint = new Paint();
                    previewPaint.setColor(Color.BLACK);
                    previewPaint.setAlpha(96);
                    previewPaint.setAntiAlias(true);
                    previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0, 0));

                    Paint previewTextPaint = new Paint();
                    previewTextPaint.setColor(Color.WHITE);
                    previewTextPaint.setAntiAlias(true);
                    previewTextPaint.setTextSize(mPreviewTextSize);
                    previewTextPaint.setTypeface(mTypeface);

                    float previewTextWidth = previewTextPaint.measureText(mSections.get(mCurrentSection));
                    float previewSize = 2 * mPreviewPadding + previewTextPaint.descent() - previewTextPaint.ascent();
                    RectF previewRect = new RectF((mListViewWidth - previewSize) / 2
                            , (mListViewHeight - previewSize) / 2
                            , (mListViewWidth - previewSize) / 2 + previewSize
                            , (mListViewHeight - previewSize) / 2 + previewSize);

                    //画中间的提示框
                    canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity, previewPaint);
                    canvas.drawText(mSections.get(mCurrentSection), previewRect.left + (previewSize - previewTextWidth) / 2 - 1
                            , previewRect.top + mPreviewPadding - previewTextPaint.ascent() + 1, previewTextPaint);
                    fade(300);
                }

                Paint indexPaint = new Paint();
                indexPaint.setColor(mTextColor);
                indexPaint.setAntiAlias(true);
                indexPaint.setTextSize(mTextSize);
                indexPaint.setTypeface(mTypeface);

                float sectionHeight = (mIndexbarRect.height() - 2 * mBarMargin) / mSections.size();
                float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;
                for (int i = 0; i < mSections.size(); i++) {

                    if (mShowHighlighted) {

                        if (mCurrentSection > -1 && i == mCurrentSection) {
                            indexPaint.setTypeface(Typeface.create(mTypeface, Typeface.BOLD));
                            indexPaint.setTextSize(mTextSize + 3 * mScaledDensity);
                            indexPaint.setColor(mHightedTextColor);
                        } else {
                            indexPaint.setTypeface(mTypeface);
                            indexPaint.setTextSize(mTextSize);
                            indexPaint.setColor(mTextColor);
                        }
                        float paddingLeft = (mBarWidth - indexPaint.measureText(mSections.get(i))) / 2;
                        canvas.drawText(mSections.get(i), mIndexbarRect.left + paddingLeft
                                , mIndexbarRect.top + mBarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);


                    } else {
                        float paddingLeft = (mBarWidth - indexPaint.measureText(mSections.get(i))) / 2;
                        canvas.drawText(mSections.get(i), mIndexbarRect.left + paddingLeft
                                , mIndexbarRect.top + mBarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
                    }

                }
            }
        }

    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // If down event occurs inside index bar region, start indexing
                if (contains(ev.getX(), ev.getY())) {
                    mPreviewVisibility = true;
                    // It demonstrates that the motion event started from index bar
                    mIsIndexing = true;
                    // Determine which section the point is in, and move the list to that section
                    mCurrentSection = getSectionByPoint(ev.getY());
                    scrollToPosition();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsIndexing) {
                    // If this event moves inside index bar
                    if (contains(ev.getX(), ev.getY())) {
                        // Determine which section the point is in, and move the list to that section
                        mCurrentSection = getSectionByPoint(ev.getY());
                        scrollToPosition();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsIndexing) {
                    mIsIndexing = false;
                    //为了invalidate的时候让中间框消失
//                    mCurrentSection = -1;
                    mPreviewVisibility = false;
                }
                break;
        }
        return false;
    }

    private void scrollToPosition() {
        try {
            int position = mIndexer.getPositionForSection(mCurrentSection);
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, 0);
            } else {
                layoutManager.scrollToPosition(position);
            }
        } catch (Exception e) {
            Log.d("INDEX_BAR", "Data size returns null");
        }
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mListViewWidth = w;
        mListViewHeight = h;
        mIndexbarRect = new RectF(w - mBarMargin - mBarWidth
                , mBarMargin
                , w - mBarMargin
                , h - mBarMargin);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof SectionIndexer) {
            adapter.registerAdapterDataObserver(this);
            mIndexer = (SectionIndexer) adapter;
            mSections = Arrays.asList((String[])mIndexer.getSections());
//            mSections = (String[]) mIndexer.getSections();
        }
    }

    @Override
    public void onChanged() {
        super.onChanged();
//        mSections = (String[]) mIndexer.getSections();
        mSections = Arrays.asList((String[])mIndexer.getSections());
    }

    public boolean contains(float x, float y) {
        // Determine if the point is in index bar region, which includes the right margin of the bar
        return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
    }

    private int getSectionByPoint(float y) {
        if (mSections == null || mSections.size() == 0)
            return 0;
        if (y < mIndexbarRect.top + mBarMargin)
            return 0;
        if (y >= mIndexbarRect.top + mIndexbarRect.height() - mBarMargin)
            return mSections.size() - 1;
        return (int) ((y - mIndexbarRect.top - mBarMargin) / ((mIndexbarRect.height() - 2 * mBarMargin) / mSections
                .size()));
    }

    private static final int WHAT_FADE_PREVIEW = 1;

    private void fade(long delay) {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageAtTime(WHAT_FADE_PREVIEW, SystemClock.uptimeMillis() + delay);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == WHAT_FADE_PREVIEW) {
                mRecyclerView.invalidate();
            }

        }

    };

    /**
     * @param value int to set the text size of the index bar
     */
    public void setIndexTextSize(int value) {
        mTextSize = value;
    }

    /**
     * @param value float to set the width of the index bar
     */
    public void setIndexbarWidth(float value) {
        mBarWidth = value;
    }

    /**
     * @param value float to set the margin of the index bar
     */
    public void setIndexbarMargin(float value) {
        mBarMargin = value;
    }

    /**
     * @param value int to set preview padding
     */
    public void setPreviewPadding(int value) {
        mPreviewPadding = value;
    }

    /**
     * @param value int to set the radius of the index bar
     */
    public void setIndexBarCornerRadius(int value) {
        mCornerRadius = value;
    }

    /**
     * @param value float to set the transparency of the color for index bar
     */
    public void setIndexBarTransparentValue(int value) {
        mBackgroudAlpha = value;
    }

    /**
     * @param typeface Typeface to set the typeface of the preview & the index bar
     */
    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    public void setIndexBarVisibility(boolean shown) {
        mBarVisibility = shown;
    }

    /**
     * @param shown boolean to show or hide the preview box
     */
    public void setPreviewVisibility(boolean shown) {
        mPreviewVisibility = shown;
    }

    /**
     * @param color The color for the scroll track
     */
    public void setIndexBarColor(int color) {
        mBackgroundColor = color;
    }

    /**
     * @param color The text color for the index bar
     */
    public void setIndexBarTextColor(int color) {
        mTextColor = color;
    }

    /**
     * @param color The text color for the index bar
     */
    public void setIndexBarHighLateTextColor(int color) {
        mHightedTextColor = color;
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    public void setIndexBarHighLateTextVisibility(boolean shown) {
        mShowHighlighted = shown;
    }

    void setSelection(int firstVisibleItemPosition) {
        if (mSections == null || firstVisibleItemPosition < 0)
            return;

        int section = mIndexer.getSectionForPosition(firstVisibleItemPosition);
        if (mCurrentSection != section && section >= 0) {
            mCurrentSection = section;
            mRecyclerView.invalidate();
        }
    }

}