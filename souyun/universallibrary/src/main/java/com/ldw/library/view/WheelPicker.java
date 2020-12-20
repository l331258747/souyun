package com.ldw.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ldw.library.R;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Weidongjian on 2015/8/18.
 * Modified by Longdw on 2016/11/9.
 * https://github.com/weidongjian/androidWheelView
 */
public class WheelPicker extends View {

    public enum ACTION {
        // 点击，滑翔(滑到尽头)，拖拽事件
        CLICK, FLING, DAGGLE
    }

    public interface OnItemSelectedListener {
        void onItemSelected(WheelPicker picker, int index, String data);
    }

    private float mScaleX = 1.05F;

    private Context mContext;

    public Handler mHandler;
    private GestureDetector mGestureDetector;
    public OnItemSelectedListener mListener;

    // Timer mTimer;
    private ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint mOuterTextPaint;
    private Paint mCenterTextPaint;
    private Paint mIndicatorPaint;

    private List<String> mDataList;
    private int selectedPosition;
    private String selectedItem;

    private int mTextSize;
    private int mMaxTextWidth;
    private int mMaxTextHeight;

    private int mItemTextColor;
    private int mItemSelectedTextColor;
    private int mLineColor;

    // 条目间距倍数
    private float mLineSpacingMultiplier;
    private boolean mIsLoop;

    // 第一条线Y坐标值
    private int mFirstLineY;
    private int mSecondLineY;

    private int mTotalScrollY;
    private int mSelectedItemPosition;
    private int mPreCurrentIndex;

    // 显示几个条目
    private int mItemsVisible;

    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;

    // 半圆周长
    private int mHalfCircumference;
    // 半径
    private int mRadius;

    private int mOffset = 0;
    private float mPreviousY;
    private long mStartTime = 0;

    private Rect tempRect = new Rect();

    private int mViewWidth;

    public WheelPicker(Context context) {
        this(context, null);
    }

    public WheelPicker(Context context, AttributeSet attributeset) {
        this(context, attributeset, 0);
    }

    public WheelPicker(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initLoopView(context, attributeset);
    }

    private void initLoopView(Context context, AttributeSet attrs) {
        mContext = context;
        mHandler = new MessageHandler();
        mGestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        mGestureDetector.setIsLongpressEnabled(false);

        mLineSpacingMultiplier = 2.0F;
        mItemsVisible = 9;

        mTotalScrollY = 0;
        mSelectedItemPosition = -1;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelPicker);
        mTextSize = a.getDimensionPixelSize(R.styleable.WheelPicker_wheelItemTextSize, getResources().getDimensionPixelSize(R
                .dimen.wheelpicker_itemtextsize));
        mItemTextColor = a.getColor(R.styleable.WheelPicker_wheelItemTextColor, 0xFFAFAFAF);
        mItemSelectedTextColor = a.getColor(R.styleable.WheelPicker_wheelItemSelectedTextColor, 0xFF313131);
        mLineColor = a.getColor(R.styleable.WheelPicker_wheelLineColor, 0xFFC5C5C5);
        mIsLoop = a.getBoolean(R.styleable.WheelPicker_wheelIsLoop, false);
        a.recycle();

        initPaints();
    }

    private void initPaints() {
        mOuterTextPaint = new Paint();
        mOuterTextPaint.setColor(mItemTextColor);
        mOuterTextPaint.setAntiAlias(true);
        mOuterTextPaint.setTypeface(Typeface.MONOSPACE);
        mOuterTextPaint.setTextSize(mTextSize);

        mCenterTextPaint = new Paint();
        mCenterTextPaint.setColor(mItemSelectedTextColor);
        mCenterTextPaint.setAntiAlias(true);
        mCenterTextPaint.setTextScaleX(mScaleX);
        mCenterTextPaint.setTypeface(Typeface.MONOSPACE);
        mCenterTextPaint.setTextSize(mTextSize);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setColor(mLineColor);
        mIndicatorPaint.setAntiAlias(true);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        remeasure();
//        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);

        mMeasuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            int maxWidth = mMeasuredWidth;
            result = this.getPaddingLeft() + this.getPaddingRight() + maxWidth;//MeasureSpec.UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            int maxHeight = mMeasuredHeight;
            result = this.getPaddingTop() + this.getPaddingBottom() + maxHeight;//MeasureSpec.UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;

        remeasure();
    }

    private void remeasure() {
        if (mDataList == null) {
            return;
        }

        measureTextWidthHeight();

        mHalfCircumference = (int) (mMaxTextHeight * mLineSpacingMultiplier * (mItemsVisible - 1));
        mMeasuredHeight = (int) ((mHalfCircumference * 2) / Math.PI);
        mRadius = (int) (mHalfCircumference / Math.PI);
        if (mMeasuredWidth == 0) {
            mMeasuredWidth = mMaxTextWidth + mPaddingLeft + mPaddingRight;
        }
        mFirstLineY = (int) ((mMeasuredHeight - mLineSpacingMultiplier * mMaxTextHeight) / 2.0F);
        mSecondLineY = (int) ((mMeasuredHeight + mLineSpacingMultiplier * mMaxTextHeight) / 2.0F);
        if (mSelectedItemPosition == -1) {
            if (mIsLoop) {
                mSelectedItemPosition = (mDataList.size() + 1) / 2;
            } else {
                mSelectedItemPosition = 0;
            }
        }

        mPreCurrentIndex = mSelectedItemPosition;
    }

    private void measureTextWidthHeight() {
        for (int i = 0; i < mDataList.size(); i++) {
            String s1 = mDataList.get(i);
            mCenterTextPaint.getTextBounds(s1, 0, s1.length(), tempRect);
            int textWidth = tempRect.width();
            if (textWidth > mMaxTextWidth) {
                mMaxTextWidth = (int) (textWidth * mScaleX);
            }

            mCenterTextPaint.getTextBounds("\u661F\u671F", 0, 2, tempRect); // 星期
            int textHeight = tempRect.height();
            if (textHeight > mMaxTextHeight) {
                mMaxTextHeight = textHeight;
            }
        }

    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemHeight = mLineSpacingMultiplier * mMaxTextHeight;
            mOffset = (int) ((mTotalScrollY % itemHeight + itemHeight) % itemHeight);
            if ((float) mOffset > itemHeight / 2.0F) {
                mOffset = (int) (itemHeight - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(), 0, 10, TimeUnit.MILLISECONDS);
    }

    private void scrollBy(float velocityY) {
        cancelFuture();
        // 修改这个值可以改变滑行速度
        int velocityFling = 10;
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(velocityY), 0, velocityFling, TimeUnit.MILLISECONDS);
    }

    private void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    public void setLoop(boolean loop) {
        mIsLoop = loop;
    }

    public void setTextSize(float size) {
        if (size > 0.0F) {
            DisplayMetrics dm = getResources().getDisplayMetrics();
            mTextSize = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX, size, dm);
//            mTextSize = (int) (mContext.getResources().getDisplayMetrics().density * size);
            mOuterTextPaint.setTextSize(mTextSize);
            mCenterTextPaint.setTextSize(mTextSize);
        }
    }

    public void setSelectedItemPosition(int position) {
        if (position < 0) {
            this.mSelectedItemPosition = 0;
        } else {
            if (mDataList != null && mDataList.size() > position) {
                this.mSelectedItemPosition = position;
            }
        }
    }

    public final void setOnItemSelectedListener(OnItemSelectedListener OnItemSelectedListener) {
        mListener = OnItemSelectedListener;
    }

    public void setData(List<String> items) {
//        mMaxTextWidth = 0;
//        mMaxTextHeight = 0;
//        tempRect = new Rect();
        this.mDataList = items;
        remeasure();
        invalidate();
    }

    public List<String> getData() {
        return mDataList;
    }

    @Override
    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    @Override
    public int getPaddingRight() {
        return mPaddingRight;
    }

    // 设置左右内边距
    public void setViewPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingRight = right;
    }

    public final int getSelectedPosition() {
        return selectedPosition;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void onItemSelected() {
        if (mListener != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListener.onItemSelected(WheelPicker.this, getSelectedPosition(), getSelectedItem());
                }
            }, 200L);
        }
    }


    /**
     * 设置中间文字的scaleX的值，如果为1.0，则没有错位效果,
     * link https://github.com/weidongjian/androidWheelView/issues/10
     *
     * @param scaleX
     */
    public void setScaleX(float scaleX) {
        this.mScaleX = scaleX;
    }


//    private String mDatas[] = new String[mItemsVisible];
    @Override
    protected void onDraw(Canvas canvas) {
        if (mDataList == null) {
            return;
        }

        String datas[] = new String[mItemsVisible];

        int change = (int) (mTotalScrollY / (mLineSpacingMultiplier * mMaxTextHeight));
        mPreCurrentIndex = mSelectedItemPosition + change % mDataList.size();

        if (!mIsLoop) {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = 0;
            }
            if (mPreCurrentIndex > mDataList.size() - 1) {
                mPreCurrentIndex = mDataList.size() - 1;
            }
        } else {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = mDataList.size() + mPreCurrentIndex;
            }
            if (mPreCurrentIndex > mDataList.size() - 1) {
                mPreCurrentIndex = mPreCurrentIndex - mDataList.size();
            }
        }

        int j2 = (int) (mTotalScrollY % (mLineSpacingMultiplier * mMaxTextHeight));
        // 设置as数组中每个元素的值
        int k1 = 0;
        while (k1 < mItemsVisible) {
            int l1 = mPreCurrentIndex - (mItemsVisible / 2 - k1);
            if (mIsLoop) {
                while (l1 < 0) {
                    l1 = l1 + mDataList.size();
                }
                while (l1 > mDataList.size() - 1) {
                    l1 = l1 - mDataList.size();
                }
                datas[k1] = mDataList.get(l1);
            } else if (l1 < 0) {
                datas[k1] = "";
            } else if (l1 > mDataList.size() - 1) {
                datas[k1] = "";
            } else {
                datas[k1] = mDataList.get(l1);
            }
            k1++;
        }
        canvas.drawLine(0.0F, mFirstLineY, mViewWidth, mFirstLineY, mIndicatorPaint);
        canvas.drawLine(0.0F, mSecondLineY, mViewWidth, mSecondLineY, mIndicatorPaint);

        int j1 = 0;
        while (j1 < mItemsVisible) {
            canvas.save();
            // L(弧长)=α（弧度）* r(半径) （弧度制）
            // 求弧度--> (L * π ) / (π * r)   (弧长X派/半圆周长)
            float itemHeight = mMaxTextHeight * mLineSpacingMultiplier;
            double radian = ((itemHeight * j1 - j2) * Math.PI) / mHalfCircumference;
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            float angle = (float) (90D - (radian / Math.PI) * 180D);
            if (angle >= 90F || angle <= -90F) {
                canvas.restore();
            } else {
                int translateY = (int) (mRadius - Math.cos(radian) * mRadius - (Math.sin(radian) * mMaxTextHeight) / 2D);
                canvas.translate(0.0F, translateY);
                canvas.scale(1.0F, (float) Math.sin(radian));
                if (translateY <= mFirstLineY && mMaxTextHeight + translateY >= mFirstLineY) {
                    // 条目经过第一条线
                    canvas.save();
                    canvas.clipRect(0, 0, mMeasuredWidth, mFirstLineY - translateY);
                    canvas.drawText(datas[j1], getTextX(datas[j1], mOuterTextPaint, tempRect), mMaxTextHeight, mOuterTextPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mFirstLineY - translateY, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(datas[j1], getTextX(datas[j1], mCenterTextPaint, tempRect), mMaxTextHeight, mCenterTextPaint);
                    canvas.restore();
                } else if (translateY <= mSecondLineY && mMaxTextHeight + translateY >= mSecondLineY) {
                    // 条目经过第二条线
                    canvas.save();
                    canvas.clipRect(0, 0, mMeasuredWidth, mSecondLineY - translateY);
                    canvas.drawText(datas[j1], getTextX(datas[j1], mCenterTextPaint, tempRect), mMaxTextHeight, mCenterTextPaint);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0, mSecondLineY - translateY, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(datas[j1], getTextX(datas[j1], mOuterTextPaint, tempRect), mMaxTextHeight, mOuterTextPaint);
                    canvas.restore();
                } else if (translateY >= mFirstLineY && mMaxTextHeight + translateY <= mSecondLineY) {
                    // 中间条目
                    canvas.clipRect(0, 0, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(datas[j1], getTextX(datas[j1], mCenterTextPaint, tempRect), mMaxTextHeight, mCenterTextPaint);
                    selectedPosition = mDataList.indexOf(datas[j1]);
                    if (selectedPosition == -1) return;
                    selectedItem = mDataList.get(selectedPosition);
                } else {
                    // 其他条目
                    canvas.clipRect(0, 0, mMeasuredWidth, (int) (itemHeight));
                    canvas.drawText(datas[j1], getTextX(datas[j1], mOuterTextPaint, tempRect), mMaxTextHeight, mOuterTextPaint);
                }
                canvas.restore();
            }
            j1++;
        }
    }

    // 绘制文字起始位置
    private int getTextX(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        // 获取到的是实际文字宽度
        int textWidth = rect.width();
        // 转换成绘制文字宽度
        textWidth *= mScaleX;

//        if (!TextUtils.isEmpty(a) && a.contains("日"))
//        System.out.println("a-->" + a
//                + "  measuredWidth-->" + mMeasuredWidth
//                + "  textWidth-->" + textWidth
//                + "  x-->" + (mMeasuredWidth - textWidth) / 2);

        return (mMeasuredWidth - textWidth) / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = mGestureDetector.onTouchEvent(event);
        float itemHeight = mLineSpacingMultiplier * mMaxTextHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartTime = System.currentTimeMillis();
                cancelFuture();
                mPreviousY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = mPreviousY - event.getRawY();
                mPreviousY = event.getRawY();

                mTotalScrollY = (int) (mTotalScrollY + dy);

                // 边界处理。
                if (!mIsLoop) {
                    float top = -mSelectedItemPosition * itemHeight;
                    float bottom = (mDataList.size() - 1 - mSelectedItemPosition) * itemHeight;

                    if (mTotalScrollY < top) {
                        mTotalScrollY = (int) top;
                    } else if (mTotalScrollY > bottom) {
                        mTotalScrollY = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            default:
                if (!eventConsumed) {
                    float y = event.getY();
                    double l = Math.acos((mRadius - y) / mRadius) * mRadius;
                    int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

                    float extraOffset = (mTotalScrollY % itemHeight + itemHeight) % itemHeight;
                    mOffset = (int) ((circlePosition - mItemsVisible / 2) * itemHeight - extraOffset);

                    if ((System.currentTimeMillis() - mStartTime) > 120) {
                        // 处理拖拽事件
                        smoothScroll(ACTION.DAGGLE);
                    } else {
                        // 处理条目点击事件
                        smoothScroll(ACTION.CLICK);
                    }
                }
                break;
        }

        invalidate();
        return true;
    }

    private final class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {

        final WheelPicker wheelPicker;

        LoopViewGestureListener(WheelPicker loopview) {
            wheelPicker = loopview;
        }

        @Override
        public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            wheelPicker.scrollBy(velocityY);
            return true;
        }
    }

    private final class MessageHandler extends Handler {
        private static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
        private static final int WHAT_SMOOTH_SCROLL = 2000;
        private static final int WHAT_ITEM_SELECTED = 3000;

        @Override
        public final void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_INVALIDATE_LOOP_VIEW:
                    invalidate();
                    break;

                case WHAT_SMOOTH_SCROLL:
                    smoothScroll(WheelPicker.ACTION.FLING);
                    break;

                case WHAT_ITEM_SELECTED:
                    onItemSelected();
                    break;
            }
        }
    }

    private final class SmoothScrollTimerTask extends TimerTask {

        int realTotalOffset = Integer.MAX_VALUE;
        int realOffset;

        @Override
        public final void run() {
            if (realTotalOffset == Integer.MAX_VALUE) {
                realTotalOffset = mOffset;
            }
            realOffset = (int) ((float) realTotalOffset * 0.1F);

            if (realOffset == 0) {
                if (realTotalOffset < 0) {
                    realOffset = -1;
                } else {
                    realOffset = 1;
                }
            }
            if (Math.abs(realTotalOffset) <= 0) {
                cancelFuture();
                mHandler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
            } else {
                mTotalScrollY = mTotalScrollY + realOffset;
                mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
                realTotalOffset = realTotalOffset - realOffset;
            }
        }
    }

    private final class InertiaTimerTask extends TimerTask {

        float a;
        final float velocityY;

        InertiaTimerTask(float velocityY) {
            super();
            this.velocityY = velocityY;
            a = Integer.MAX_VALUE;
        }

        @Override
        public final void run() {
            if (a == Integer.MAX_VALUE) {
                if (Math.abs(velocityY) > 2000F) {
                    if (velocityY > 0.0F) {
                        a = 2000F;
                    } else {
                        a = -2000F;
                    }
                } else {
                    a = velocityY;
                }
            }
            if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
                cancelFuture();
                mHandler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
                return;
            }
            int i = (int) ((a * 10F) / 1000F);
            mTotalScrollY = mTotalScrollY - i;
            if (!mIsLoop) {
                float itemHeight = mLineSpacingMultiplier * mMaxTextHeight;
                if (mTotalScrollY <= (int) ((float) (-mSelectedItemPosition) * itemHeight)) {
                    a = 40F;
                    mTotalScrollY = (int) ((float) (-mSelectedItemPosition) * itemHeight);
                } else if (mTotalScrollY >= (int) ((float) (mDataList.size() - 1 - mSelectedItemPosition) *
                        itemHeight)) {
                    mTotalScrollY = (int) ((float) (mDataList.size() - 1 - mSelectedItemPosition) * itemHeight);
                    a = -40F;
                }
            }
            if (a < 0.0F) {
                a = a + 20F;
            } else {
                a = a - 20F;
            }
            mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
        }
    }
}
