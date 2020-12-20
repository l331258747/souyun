package com.ldw.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.ldw.library.utils.DisplayUtil;


/**
 * Created by www.longdw.com on 2018/1/14 上午10:08.
 */

public class LBadgeView extends View {
    private final String TAG = "LBadgeView";
    private Context mContext;
    protected int mColorBackground;
    protected int mColorBackgroundBorder;
    protected int mColorBadgeText;

    protected float mBackgroundBorderWidth;
    protected Drawable mDrawableBackground;
    protected boolean mDrawableBackgroundClip;
    protected Bitmap mBitmapClip;

    protected View mTargetView;
    protected boolean mShowShadow;
    protected boolean mExact;
    protected int mWidth;
    protected int mHeight;

    private String mBadgeText;
    protected int mBadgeNumber;
    protected Paint.FontMetrics mBadgeTextFontMetrics;

    protected RectF mBadgeTextRect;
    protected RectF mBadgeBackgroundRect;

    private float mBadgeTextSize;
    protected float mBadgePadding;
    protected int mBadgeGravity;

    protected TextPaint mBadgeTextPaint;
    private Paint mBadgeBackgroundPaint;
    private Paint mBadgeBackgroundBorderPaint;

    protected PointF mBadgeCenter;

    protected float mGravityOffsetX;
    protected float mGravityOffsetY;

    public LBadgeView(Context context) {
        super(context);

        init(context);
    }

    public LBadgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LBadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBadgeTextRect = new RectF();
        mBadgeBackgroundRect = new RectF();
        mBadgeCenter = new PointF();

        mBadgeTextPaint = new TextPaint();
        //抗锯齿
        mBadgeTextPaint.setAntiAlias(true);
        //亚像素
        mBadgeTextPaint.setSubpixelText(true);
        //仿粗体
        mBadgeTextPaint.setFakeBoldText(true);

        mColorBackground = 0xFFE84E40;
        mColorBadgeText = 0xFFFFFFFF;
        mBadgeTextSize = DisplayUtil.dp2px(getContext(), 11);
        mBadgePadding = DisplayUtil.dp2px(getContext(), 2);

        mBadgeBackgroundPaint = new Paint();
        mBadgeBackgroundPaint.setAntiAlias(true);
        mBadgeBackgroundPaint.setStyle(Paint.Style.FILL);

        mBadgeBackgroundBorderPaint = new Paint();
        mBadgeBackgroundBorderPaint.setAntiAlias(true);
        mBadgeBackgroundBorderPaint.setStyle(Paint.Style.STROKE);

        mBadgeNumber = 0;

        mBadgeGravity = Gravity.END | Gravity.TOP;
        mGravityOffsetX = DisplayUtil.dp2px(getContext(), 1);
        mGravityOffsetY = DisplayUtil.dp2px(getContext(), 1);

        mDrawableBackgroundClip = false;
    }

    public LBadgeView bindTarget(View targetView) {
        if (targetView == null) {
            throw new IllegalStateException("targetView can not be null");
        }
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        ViewParent targetParent = targetView.getParent();
        if (targetParent != null && targetParent instanceof ViewGroup) {
            mTargetView = targetView;
            if (targetParent instanceof BadgeContainer) {
                ((BadgeContainer) targetParent).addView(this);
            } else {
                ViewGroup targetContainer = (ViewGroup) targetParent;
                int index = targetContainer.indexOfChild(targetView);
                ViewGroup.LayoutParams targetParams = targetView.getLayoutParams();
                targetContainer.removeView(targetView);
                final BadgeContainer badgeContainer = new BadgeContainer(getContext());
                if(targetContainer instanceof RelativeLayout){
                    badgeContainer.setId(targetView.getId());
                }
                targetContainer.addView(badgeContainer, index, targetParams);
                badgeContainer.addView(targetView);
                badgeContainer.addView(this);
            }
        } else {
            throw new IllegalStateException("targetView must have a parent");
        }
        return this;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBadgeText != null) {
            initPaints();
            float badgeRadius = getBadgeCircleRadius();
//            Log.d(TAG, "badgeRadius======>" + badgeRadius);
            findBadgeCenter();
            drawBadge(canvas, mBadgeCenter, badgeRadius);
        }
    }

    private void initPaints() {
        showShadowImpl(mShowShadow);
        mBadgeBackgroundPaint.setColor(mColorBackground);
        mBadgeBackgroundBorderPaint.setColor(mColorBackgroundBorder);
        mBadgeBackgroundBorderPaint.setStrokeWidth(mBackgroundBorderWidth);
        mBadgeTextPaint.setColor(mColorBadgeText);
        mBadgeTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void showShadowImpl(boolean showShadow) {
        int x = DisplayUtil.dp2px(getContext(), 1);
        int y = DisplayUtil.dp2px(getContext(), 1.5f);
        mBadgeBackgroundPaint.setShadowLayer(showShadow ? DisplayUtil.dp2px(getContext(), 2f)
                : 0, x, y, 0x33000000);
    }

    private float getBadgeCircleRadius() {
        if (mBadgeText.isEmpty()) {
            return mBadgePadding;
        } else if (mBadgeText.length() == 1) {
            return mBadgeTextRect.height() > mBadgeTextRect.width() ?
                    mBadgeTextRect.height() / 2f + mBadgePadding :// * 0.5  默认这里有*0.5
                    mBadgeTextRect.width() / 2f + mBadgePadding;// * 0.5
        } else {
            return mBadgeBackgroundRect.height() / 2f;
        }
    }

    private void findBadgeCenter() {
        float rectWidth = mBadgeTextRect.height() > mBadgeTextRect.width() ?
                mBadgeTextRect.height() : mBadgeTextRect.width();
        switch (mBadgeGravity) {
            case Gravity.START | Gravity.TOP:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.START | Gravity.BOTTOM:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.END | Gravity.TOP:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.END | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.CENTER:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
            case Gravity.CENTER | Gravity.TOP:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f;
                break;
            case Gravity.CENTER | Gravity.BOTTOM:
                mBadgeCenter.x = mWidth / 2f;
                mBadgeCenter.y = mHeight - (mGravityOffsetY + mBadgePadding + mBadgeTextRect.height() / 2f);
                break;
            case Gravity.CENTER | Gravity.START:
                mBadgeCenter.x = mGravityOffsetX + mBadgePadding + rectWidth / 2f;
                mBadgeCenter.y = mHeight / 2f;
                break;
            case Gravity.CENTER | Gravity.END:
                mBadgeCenter.x = mWidth - (mGravityOffsetX + mBadgePadding + rectWidth / 2f);
                mBadgeCenter.y = mHeight / 2f;
                break;
        }
    }

    private void drawBadge(Canvas canvas, PointF center, float radius) {
        if (center.x == -1000 && center.y == -1000) {
            return;
        }
        if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
            mBadgeBackgroundRect.left = center.x - (int) radius;
            mBadgeBackgroundRect.top = center.y - (int) radius;
            mBadgeBackgroundRect.right = center.x + (int) radius;
            mBadgeBackgroundRect.bottom = center.y + (int) radius;
            if (mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawCircle(center.x, center.y, radius, mBadgeBackgroundBorderPaint);
                }
            }
        } else {
            mBadgeBackgroundRect.left = center.x - (mBadgeTextRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.top = center.y - (mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f);
            mBadgeBackgroundRect.right = center.x + (mBadgeTextRect.width() / 2f + mBadgePadding);
            mBadgeBackgroundRect.bottom = center.y + (mBadgeTextRect.height() / 2f + mBadgePadding * 0.5f);
            radius = mBadgeBackgroundRect.height() / 2f;
            if (mDrawableBackground != null) {
                drawBadgeBackground(canvas);
            } else {
                canvas.drawRoundRect(mBadgeBackgroundRect, radius, radius, mBadgeBackgroundPaint);
                if (mColorBackgroundBorder != 0 && mBackgroundBorderWidth > 0) {
                    canvas.drawRoundRect(mBadgeBackgroundRect, radius, radius, mBadgeBackgroundBorderPaint);
                }
            }
        }
        if (!mBadgeText.isEmpty()) {
            canvas.drawText(mBadgeText, center.x,
                    (mBadgeBackgroundRect.bottom + mBadgeBackgroundRect.top
                            - mBadgeTextFontMetrics.bottom - mBadgeTextFontMetrics.top) / 2f,
                    mBadgeTextPaint);
        }
    }

    private void drawBadgeBackground(Canvas canvas) {
        mBadgeBackgroundPaint.setShadowLayer(0, 0, 0, 0);
        int left = (int) mBadgeBackgroundRect.left;
        int top = (int) mBadgeBackgroundRect.top;
        int right = (int) mBadgeBackgroundRect.right;
        int bottom = (int) mBadgeBackgroundRect.bottom;
        if (mDrawableBackgroundClip) {
            right = left + mBitmapClip.getWidth();
            bottom = top + mBitmapClip.getHeight();
            canvas.saveLayer(left, top, right, bottom, null, Canvas.ALL_SAVE_FLAG);
        }
        mDrawableBackground.setBounds(left, top, right, bottom);
        mDrawableBackground.draw(canvas);
        if (mDrawableBackgroundClip) {
            mBadgeBackgroundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(mBitmapClip, left, top, mBadgeBackgroundPaint);
            canvas.restore();
            mBadgeBackgroundPaint.setXfermode(null);
            if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
                canvas.drawCircle(mBadgeBackgroundRect.centerX(), mBadgeBackgroundRect.centerY(),
                        mBadgeBackgroundRect.width() / 2f, mBadgeBackgroundBorderPaint);
            } else {
                canvas.drawRoundRect(mBadgeBackgroundRect,
                        mBadgeBackgroundRect.height() / 2, mBadgeBackgroundRect.height() / 2,
                        mBadgeBackgroundBorderPaint);
            }
        } else {
            canvas.drawRect(mBadgeBackgroundRect, mBadgeBackgroundBorderPaint);
        }
    }

    public LBadgeView setGravityOffset(float offset, boolean isDpValue) {
        return setGravityOffset(offset, offset, isDpValue);
    }

    public LBadgeView setGravityOffset(float offsetX, float offsetY, boolean isDpValue) {
        mGravityOffsetX = isDpValue ? DisplayUtil.dp2px(getContext(), offsetX) : offsetX;
        mGravityOffsetY = isDpValue ? DisplayUtil.dp2px(getContext(), offsetY) : offsetY;
        invalidate();
        return this;
    }

    public LBadgeView setExactMode(boolean isExact) {
        mExact = isExact;
        if (mBadgeNumber > 99) {
            setBadgeNumber(mBadgeNumber);
        }
        return this;
    }

    public LBadgeView setBadgeNumber(int badgeNumber) {
        mBadgeNumber = badgeNumber;
        if (mBadgeNumber < 0) {
            mBadgeText = "";
        } else if (mBadgeNumber > 99) {
            mBadgeText = mExact ? String.valueOf(mBadgeNumber) : "99+";
        } else if (mBadgeNumber > 0 && mBadgeNumber <= 99) {
            mBadgeText = String.valueOf(mBadgeNumber);
        } else if (mBadgeNumber == 0) {
            mBadgeText = null;
        }
        measureText();
        invalidate();
        return this;
    }

    public LBadgeView setBadgeTextSize(float size, boolean isSpValue) {
        mBadgeTextSize = isSpValue ? DisplayUtil.dp2px(getContext(), size) : size;
        measureText();
        invalidate();
        return this;
    }

    public LBadgeView setBadgeText(String badgeText) {
//        if (badgeText == null || "0".equals(badgeText)) {
//            badgeText = "";
//        }
        mBadgeText = badgeText;
        mBadgeNumber = 1;
        measureText();
        invalidate();
        return this;
    }

    public LBadgeView setShowShadow(boolean showShadow) {
        mShowShadow = showShadow;
        invalidate();
        return this;
    }

    public LBadgeView setBadgeBackground(Drawable drawable) {
        return setBadgeBackground(drawable, false);
    }

    public LBadgeView setBadgeBackground(Drawable drawable, boolean clip) {
        mDrawableBackgroundClip = clip;
        mDrawableBackground = drawable;
        createClipLayer();
        invalidate();
        return this;
    }

    /**
     * @param gravity only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP ,
     *                Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM ,
     *                Gravity.CENTER , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ,
     *                Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END
     */
    public LBadgeView setBadgeGravity(int gravity) {
        if (gravity == (Gravity.START | Gravity.TOP) ||
                gravity == (Gravity.END | Gravity.TOP) ||
                gravity == (Gravity.START | Gravity.BOTTOM) ||
                gravity == (Gravity.END | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER) ||
                gravity == (Gravity.CENTER | Gravity.TOP) ||
                gravity == (Gravity.CENTER | Gravity.BOTTOM) ||
                gravity == (Gravity.CENTER | Gravity.START) ||
                gravity == (Gravity.CENTER | Gravity.END)) {
            mBadgeGravity = gravity;
            invalidate();
        } else {
            throw new IllegalStateException("only support Gravity.START | Gravity.TOP , Gravity.END | Gravity.TOP , " +
                    "Gravity.START | Gravity.BOTTOM , Gravity.END | Gravity.BOTTOM , Gravity.CENTER" +
                    " , Gravity.CENTER | Gravity.TOP , Gravity.CENTER | Gravity.BOTTOM ," +
                    "Gravity.CENTER | Gravity.START , Gravity.CENTER | Gravity.END");
        }
        return this;
    }

    private void createClipLayer() {
        if (mBadgeText == null) {
            return;
        }
        if (!mDrawableBackgroundClip) {
            return;
        }
        if (mBitmapClip != null && !mBitmapClip.isRecycled()) {
            mBitmapClip.recycle();
        }
        float radius = getBadgeCircleRadius();
        if (mBadgeText.isEmpty() || mBadgeText.length() == 1) {
            mBitmapClip = Bitmap.createBitmap((int) radius * 2, (int) radius * 2,
                    Bitmap.Config.ARGB_4444);
            Canvas srcCanvas = new Canvas(mBitmapClip);
            srcCanvas.drawCircle(srcCanvas.getWidth() / 2f, srcCanvas.getHeight() / 2f,
                    srcCanvas.getWidth() / 2f, mBadgeBackgroundPaint);
        } else {
            mBitmapClip = Bitmap.createBitmap((int) (mBadgeTextRect.width() + mBadgePadding * 2),
                    (int) (mBadgeTextRect.height() + mBadgePadding), Bitmap.Config.ARGB_4444);
            Canvas srcCanvas = new Canvas(mBitmapClip);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                srcCanvas.drawRoundRect(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight(), srcCanvas.getHeight() / 2f,
                        srcCanvas.getHeight() / 2f, mBadgeBackgroundPaint);
            } else {
                srcCanvas.drawRoundRect(new RectF(0, 0, srcCanvas.getWidth(), srcCanvas.getHeight()),
                        srcCanvas.getHeight() / 2f, srcCanvas.getHeight() / 2f, mBadgeBackgroundPaint);
            }
        }
    }

    private void measureText() {
        mBadgeTextRect.left = 0;
        mBadgeTextRect.top = 0;
        if (TextUtils.isEmpty(mBadgeText)) {
            mBadgeTextRect.right = 0;
            mBadgeTextRect.bottom = 0;
        } else {
            mBadgeTextPaint.setTextSize(mBadgeTextSize);
            mBadgeTextRect.right = mBadgeTextPaint.measureText(mBadgeText);
            mBadgeTextFontMetrics = mBadgeTextPaint.getFontMetrics();
//            mBadgeTextRect.bottom = mBadgeTextFontMetrics.descent - mBadgeTextFontMetrics.ascent;
            mBadgeTextRect.bottom = mBadgeTextFontMetrics.bottom - mBadgeTextFontMetrics.top;
//            Log.d(TAG, "top=" + mBadgeTextFontMetrics.top
//                    + " bottom=" + mBadgeTextFontMetrics.bottom
//                    + " ascent=" + mBadgeTextFontMetrics.ascent
//                    + " descent=" + mBadgeTextFontMetrics.descent
//                    + " leading=" + mBadgeTextFontMetrics.leading
//                    + " badgeRect=" + mBadgeTextRect.height()
//            );
        }
        createClipLayer();
    }

    private class BadgeContainer extends ViewGroup {

        @Override
        protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
            if(!(getParent() instanceof RelativeLayout)){
                super.dispatchRestoreInstanceState(container);
            }
        }

        public BadgeContainer(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            View targetView = null, badgeView = null;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!(child instanceof LBadgeView)) {
                    targetView = child;
                } else {
                    badgeView = child;
                }
            }
            if (targetView == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                targetView.measure(widthMeasureSpec, heightMeasureSpec);
                if (badgeView != null) {
                    badgeView.measure(MeasureSpec.makeMeasureSpec(targetView.getMeasuredWidth(), MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(targetView.getMeasuredHeight(), MeasureSpec.EXACTLY));
                }
                setMeasuredDimension(targetView.getMeasuredWidth(), targetView.getMeasuredHeight());
            }
        }
    }
}