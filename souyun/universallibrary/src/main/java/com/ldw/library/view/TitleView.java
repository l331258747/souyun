package com.ldw.library.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ldw.library.R;


/**
 * 具体用法介绍：</p>
 * <pre><code>
  &lt;com.lt.pms.commonbusiness.view.TitleView
  android:layout_width="match_parent"
  android:layout_height="?actionBarSize"
  titleview:backImage="@drawable/btn_back_selector"
  titleview:moreImage="@drawable/btn_more_selector"
  titleview:rightText="办理"
  titleview:rightTextColor="@color/red"
  titleview:addImage="@drawable/ic_add"
  titleview:searchImage="@drawable/ic_search"
  titleview:titleText="我是"
  titleview:titleSingle="true"
  android:background="@color/colorPrimary"/&gt;

  按钮排列顺序，自左向右：搜索/添加/文字/更多
 * Created by www.longdw.com on 2017/11/3 下午3:01.
 */

public class TitleView extends RelativeLayout implements View.OnClickListener {

    private Context mContext;

    /**
     * 图标默认大小 单位dp
     */
    private int defaultIconSize = 20;
    /**
     * 图标文字大小 单位sp
     */
    private int defaultTextSize = 18;
    /**
     * 默认padding 单位dp
     */
    private int defaultPadding = 12;
    private int defaultColor = 0xffffffff;

    private int defaultBaseLineColor = 0xff888888;

    private ImageView mMoreIv;
    private TextView mRightTv;
    private ImageView mAddIv;
    private ImageView mSearchIv;
    private ImageView mBackIv;
    private ImageView mWebExitIv;
    private TextView mTitleTv;
    private TypedArray mTypedArray;
    private OnTitleViewListener mListener;

    private View mCustomView;
    private View mLineView;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        initBaseLine(mTypedArray);
        initBack(mTypedArray);
        //如果内置浏览器 左上角增加x按钮，可快捷退出
        initWebExit(mTypedArray);
//        if (!mTypedArray.hasValue(R.styleable.TitleView_customView)) {
//
//        }

        initTitle(mTypedArray);

        initMore(mTypedArray);
        initRightText(mTypedArray);
        initAdd(mTypedArray);
        initSearch(mTypedArray);

        initCustomView(mTypedArray);

        mTypedArray.recycle();
    }

    private void initBaseLine(TypedArray typedArray) {
        boolean show = typedArray.getBoolean(R.styleable.TitleView_titleBaseLine, false);
        int color = typedArray.getColor(R.styleable.TitleView_titleBaseLineColor, 0);
        int image = typedArray.getResourceId(R.styleable.TitleView_titleBaseLineImage, 0);
        if (show || color != 0 || image != 0) {
            mLineView = new View(mContext);
            mLineView.setId(R.id.baseLineView);
            LayoutParams params;
            if (color == 0 && image == 0) {
                mLineView.setBackgroundColor(getTextColor(typedArray, R.styleable.TitleView_titleBaseLineColor, defaultBaseLineColor));
            } else if (color != 0) {
                mLineView.setBackgroundColor(getTextColor(typedArray, R.styleable.TitleView_titleBaseLineColor, defaultBaseLineColor));
            } else {
                mLineView.setBackgroundResource(image);
            }
            params = new LayoutParams(LayoutParams.MATCH_PARENT, (int)getPixelSizeByDp(1));

            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addView(mLineView, params);
        }
    }

    /**
     * 返回按钮
     */
    private void initBack(TypedArray typedArray) {

        int backImageResId = typedArray.getResourceId(R.styleable.TitleView_backImage, 0);
        if (backImageResId == 0) {
            return;
        }

        boolean backGone = typedArray.getBoolean(R.styleable.TitleView_backGone, false);

        LayoutParams params = initIconLayoutParams(typedArray);
        mBackIv = createImageView(mContext, R.id.baseBackIv, backImageResId, params);
        int padding = getDimensionDpSize(typedArray, R.styleable.TitleView_padding, defaultPadding);
        params.setMargins(padding, 0, 0, 0);
        addView(mBackIv);

        if (backGone) {
            mBackIv.setVisibility(View.INVISIBLE);
        } else {
            mBackIv.setOnClickListener(this);
        }
    }

    /**
     * 快捷退出按钮
     */
    private void initWebExit(TypedArray typedArray) {
        int exitImageResId = typedArray.getResourceId(R.styleable.TitleView_webExitImage, 0);
        if (exitImageResId == 0) {
            return;
        }
        LayoutParams params = initIconLayoutParams(typedArray);
        mWebExitIv = createImageView(mContext, R.id.baseWebExitIv, exitImageResId, params);
        mWebExitIv.setOnClickListener(this);
        int padding = getDimensionDpSize(typedArray, R.styleable.TitleView_padding, defaultPadding);
        params.setMargins(padding, 0, 0, 0);

        if (mBackIv != null) {
            params.addRule(RelativeLayout.RIGHT_OF, mBackIv.getId());
        }

        addView(mWebExitIv);
    }

    /**
     * 右边更多
     */
    private void initMore(TypedArray typedArray) {
        int moreImageResId = typedArray.getResourceId(R.styleable.TitleView_moreImage, 0);
        if (moreImageResId == 0) {
            return;
        }
        LayoutParams params = initIconLayoutParams(typedArray);
        mMoreIv = createImageView(mContext, R.id.baseMoreIv, moreImageResId, params);
        mMoreIv.setOnClickListener(this);
        int padding = getDimensionDpSize(typedArray, R.styleable.TitleView_padding, defaultPadding);
        params.setMargins(0, 0, padding, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(mMoreIv);
    }

    /**
     * 右边文字
     */
    private void initRightText(TypedArray typedArray) {
        int rightTextResId = typedArray.getResourceId(R.styleable.TitleView_rightText, 0);
        CharSequence charSequence = rightTextResId > 0 ?
                typedArray.getResources().getText(rightTextResId) : typedArray.getString(R.styleable.TitleView_rightText);
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        LayoutParams params = initLayoutParams();
        mRightTv = createTextView(mContext, R.id.baseRightTv, charSequence, params);
        mRightTv.setOnClickListener(this);
        if (mMoreIv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        int padding = getDimensionDpSize(typedArray, R.styleable.TitleView_padding, defaultPadding);
        params.setMargins(0, 0, padding, 0);

        int textSize = getDimensionSpSize(typedArray, R.styleable.TitleView_rightTextSize, defaultTextSize);
        mRightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        int colorDrawable = typedArray.getResourceId(R.styleable.TitleView_rightTextColor, 0);
        if (colorDrawable != 0) {
            mRightTv.setTextColor(getResources().getColorStateList(colorDrawable));
        } else {
            mRightTv.setTextColor(getTextColor(typedArray, R.styleable.TitleView_rightTextColor, defaultColor));
        }

        addView(mRightTv);
    }

    /**
     * 右边添加
     */
    private void initAdd(TypedArray typedArray) {
        int addImageResId = typedArray.getResourceId(R.styleable.TitleView_addImage, 0);
        if (addImageResId == 0) {
            return;
        }
        LayoutParams params = initIconLayoutParams(typedArray);
        mAddIv = createImageView(mContext, R.id.baseAddIv, addImageResId, params);
        mAddIv.setOnClickListener(this);
        if (mRightTv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
        } else if (mMoreIv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        int padding = getDimensionDpSize(typedArray, R.styleable.TitleView_padding, defaultPadding);
        params.setMargins(0, 0, padding, 0);
        addView(mAddIv);
    }

    /**
     * 右边搜索
     */
    private void initSearch(TypedArray typedArray) {
        int searchImageResId = typedArray.getResourceId(R.styleable.TitleView_searchImage, 0);
        if (searchImageResId == 0) {
            return;
        }
        LayoutParams params = initIconLayoutParams(typedArray);
        mSearchIv = createImageView(mContext, R.id.baseSearchIv, searchImageResId, params);
        mSearchIv.setOnClickListener(this);
        if (mAddIv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mAddIv.getId());
        } else if (mRightTv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
        } else if (mMoreIv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        int padding = getDimensionDpSize(typedArray, R.styleable.TitleView_padding, defaultPadding);
        params.setMargins(0, 0, padding, 0);
        addView(mSearchIv);
    }

    /**
     * 标题
     */
    private void initTitle(TypedArray typedArray) {
        int titleResId = typedArray.getResourceId(R.styleable.TitleView_titleText, 0);
        CharSequence charSequence = titleResId > 0 ? typedArray.getResources().getText(titleResId) : typedArray.getString(R
                .styleable.TitleView_titleText);
//        if (TextUtils.isEmpty(charSequence)) {
//            return;
//        }

        int gravity = typedArray.getInt(R.styleable.TitleView_titleGravity, Gravity.LEFT);
        LayoutParams params;
        if (gravity == Gravity.LEFT || gravity == Gravity.START) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            mTitleTv = createTextView(mContext, R.id.baseTitleTv, charSequence, params);
            mTitleTv.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mTitleTv = createTextView(mContext, R.id.baseTitleTv, charSequence, params);
            mTitleTv.setGravity(Gravity.CENTER);
        }

        if (mBackIv != null) {
            params.addRule(RelativeLayout.RIGHT_OF, mBackIv.getId());
        } else if (mWebExitIv != null) {
            params.addRule(RelativeLayout.RIGHT_OF, mWebExitIv.getId());
        } else {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }

        if (mSearchIv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mSearchIv.getId());
        } else if (mAddIv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mAddIv.getId());
        } else if (mRightTv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
        } else if (mMoreIv != null) {
            params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
        }
//        else {
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        }

        int padding = getDimensionDpSize(typedArray, R.styleable.TitleView_padding, defaultPadding);
        params.setMargins(padding, 0, padding, 0);

        int textSize = getDimensionSpSize(typedArray, R.styleable.TitleView_titleTextSize, defaultTextSize);
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mTitleTv.setEllipsize(TextUtils.TruncateAt.END);

        mTitleTv.setTextColor(getTextColor(typedArray, R.styleable.TitleView_titleTextColor, defaultColor));

        boolean single = typedArray.getBoolean(R.styleable.TitleView_titleSingle, false);
        if (single) {
            mTitleTv.setMaxLines(1);
        } else {
            mTitleTv.setMaxLines(2);
        }

        addView(mTitleTv);
    }

    private void initCustomView(TypedArray typedArray) {
        int resId = typedArray.getResourceId(R.styleable.TitleView_customView, 0);
        if (resId != 0) {
            View customView = LayoutInflater.from(mContext).inflate(resId, null);

            int width = typedArray.getInt(R.styleable.TitleView_customViewWidth, LayoutParams.MATCH_PARENT);
            LayoutParams params;
            if (width == LayoutParams.MATCH_PARENT) {
                params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, mTitleTv.getId());
                params.addRule(RelativeLayout.CENTER_VERTICAL);
            } else {
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
            }


            if (width == LayoutParams.MATCH_PARENT) {
                if (mAddIv != null) {
                    params.addRule(RelativeLayout.LEFT_OF, mAddIv.getId());
                } else if (mRightTv != null) {
                    params.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
                } else if (mMoreIv != null) {
                    params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
                } else if (mSearchIv != null) {
                    params.addRule(RelativeLayout.LEFT_OF, mSearchIv.getId());
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
            }

            if (mLineView != null) {
                params.addRule(RelativeLayout.ABOVE, mLineView.getId());
            }

            int padding = getDimensionDpSize(typedArray, R.styleable.TitleView_padding, defaultPadding);
            params.setMargins(0, 0, padding, 0);

            addView(customView, params);

            mCustomView = customView;
        }
    }

    private LayoutParams initIconLayoutParams(TypedArray typedArray) {
//        int iconSizeResId = typedArray.getResourceId(R.styleable.TitleView_iconSize, defaultIconSize);
//        if (iconSizeResId == 0) {
//            return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        }
        int iconSize = getDimensionDpSize(typedArray, R.styleable.TitleView_iconSize, defaultIconSize);
        LayoutParams params = new LayoutParams(iconSize, iconSize);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        return params;
    }

    private LayoutParams initLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    private ImageView createImageView(Context context, int id, int resId, LayoutParams params) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(params);
        imageView.setImageResource(resId);
//        imageView.setMinimumWidth((int)getPixelSizeByDp(minViewWidth));
        imageView.setId(id);
        return imageView;
    }

    private TextView createTextView(Context context, int id, CharSequence charSequence, LayoutParams params) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(params);
        if (!TextUtils.isEmpty(charSequence)) {
            textView.setText(charSequence);
        }
        textView.setGravity(Gravity.CENTER);
        textView.setId(id);
        return textView;
    }

    /**
     * get Pixel size by dp
     *
     * @param dp dp
     * @return the value of px
     */
    private float getPixelSizeByDp(int dp) {
        Resources res = this.getResources();
        final float scale = res.getDisplayMetrics().density;
        return dp * scale + 0.5f;
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

    /**
     * get the dimension pixel size from typeArray which is defined in attr
     *
     * @return the dimension pixel size
     */
    private int getDimensionSpSize(TypedArray typeArray, int styleable, int defaultValue) {
//        int sizeStyleable = typeArray.getResourceId(styleable, 0);
//        return sizeStyleable > 0 ? typeArray.getResources().getDimensionPixelSize(sizeStyleable) : ;
        return typeArray.getDimensionPixelSize(styleable, (int) getPixelSizeBySp(defaultValue));
    }

    private int getDimensionDpSize(TypedArray typeArray, int styleable, int defaultValue) {
//        int resId = typeArray.getResourceId(styleable, 0);
//        return resId > 0 ? typeArray.getResources().getDimensionPixelSize(resId) : typeArray.getDimensionPixelSize(styleable,
//                defaultValue);
        return typeArray.getDimensionPixelSize(styleable, (int) getPixelSizeByDp(defaultValue));
    }

    private int getTextColor(TypedArray typeArray, int styleable, int defaultValue) {
//        int textColorStyleable = typeArray.getResourceId(styleable, 0);
//        if (textColorStyleable != 0) {
//            return typeArray.getResources().getColor(textColorStyleable);
//        } else {
//            return typeArray.getColor(styleable, defaultValue);
//        }
        return typeArray.getColor(styleable, defaultValue);
    }

    public void setBackVisible() {
        if (mBackIv != null) {
            mBackIv.setVisibility(View.VISIBLE);
            mBackIv.setOnClickListener(this);
        }
    }

    public ImageView getMoreIv() {
        return mMoreIv;
    }

    public void setMoreVisibility(int visibility) {

        if (visibility == View.GONE) {
            if (mMoreIv == null) {
                return;
            }
            removeView(mMoreIv);
            mMoreIv = null;
            if (mRightTv != null) {
                LayoutParams params = (LayoutParams) mRightTv.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            } else if (mAddIv != null) {
                LayoutParams params = (LayoutParams) mAddIv.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            } else if (mSearchIv != null) {
                LayoutParams params = (LayoutParams) mSearchIv.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            boolean shouldModify = false;
            LayoutParams titleParams = (LayoutParams) mTitleTv.getLayoutParams();
            int[] rules = titleParams.getRules();
            for(int rule : rules) {
                if (rule == R.id.baseMoreIv) {
                    shouldModify = true;
                    break;
                }
            }
            if (shouldModify) {
                titleParams.addRule(RelativeLayout.LEFT_OF, 0);
                titleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
        } else {
            if (mMoreIv != null) {
                return;
            }
            initMore(mTypedArray);
            if (mMoreIv == null) {
                //有时候勿调用方法，如xml中没写对应的语句是直接创建不了的
                return;
            }
            if (mRightTv != null) {
                LayoutParams params = (LayoutParams) mRightTv.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
            } else if (mAddIv != null) {
                LayoutParams params = (LayoutParams) mAddIv.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
            } else if (mSearchIv != null) {
                LayoutParams params = (LayoutParams) mSearchIv.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
            }

            boolean shouldModify = false;
            LayoutParams titleParams = (LayoutParams) mTitleTv.getLayoutParams();
            int[] rules = titleParams.getRules();
            for(int rule : rules) {
                if (rule == RelativeLayout.TRUE) {
                    shouldModify = true;
                    break;
                }
            }
            if (shouldModify) {
                titleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                titleParams.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
            }
        }
    }

    public TextView getRightTv() {
        return mRightTv;
    }

    public void setRightVisibility(int visibility) {
        if (visibility == View.GONE) {
            if (mRightTv == null) {
                return;
            }
            removeView(mRightTv);
            mRightTv = null;
            if (mAddIv != null) {
                LayoutParams params = (LayoutParams) mAddIv.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                if (mMoreIv != null) {
                    params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
            } else if (mSearchIv != null) {
                LayoutParams params = (LayoutParams) mSearchIv.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                if (mMoreIv != null) {
                    params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
            }

            boolean shouldModify = false;
            LayoutParams titleParams = (LayoutParams) mTitleTv.getLayoutParams();
            int[] rules = titleParams.getRules();
            for(int rule : rules) {
                if (rule == R.id.baseRightTv) {
                    shouldModify = true;
                    break;
                }
            }

            if (shouldModify) {
                if (mMoreIv != null) {
                    titleParams.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
                } else {
                    titleParams.addRule(RelativeLayout.LEFT_OF, 0);
                    titleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
            }
        } else {
            if (mRightTv != null) {
                return;
            }
            initRightText(mTypedArray);
            if (mRightTv == null) {
                //有时候勿调用方法，如xml中没写对应的语句是直接创建不了的
                return;
            }
            if (mAddIv != null) {
                LayoutParams params = (LayoutParams) mAddIv.getLayoutParams();
                params.addRule(LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
            } else if (mSearchIv != null) {
                LayoutParams params = (LayoutParams) mAddIv.getLayoutParams();
                params.addRule(LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
            }

            boolean shouldModify = false;
            LayoutParams titleParams = (LayoutParams) mTitleTv.getLayoutParams();
            int[] rules = titleParams.getRules();
            for(int rule : rules) {
                if (rule == R.id.baseMoreIv) {
                    shouldModify = true;
                    break;
                }
            }

            if (shouldModify) {
                titleParams.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
            }
        }
    }

    public ImageView getAddIv() {
        return mAddIv;
    }

    public void setAddVisibility(int visibility) {
        if (visibility == View.GONE) {
            if (mAddIv == null) {
                return;
            }
            removeView(mAddIv);
            mAddIv = null;
            if (mSearchIv != null) {
                LayoutParams params = (LayoutParams) mSearchIv.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                if (mRightTv != null) {
                    params.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
                } else if (mMoreIv != null) {
                    params.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
            }

            boolean shouldModify = false;
            LayoutParams titleParams = (LayoutParams) mTitleTv.getLayoutParams();
            int[] rules = titleParams.getRules();
            for(int rule : rules) {
                if (rule == R.id.baseAddIv) {
                    shouldModify = true;
                    break;
                }
            }

            if (shouldModify) {
                if (mRightTv != null) {
                    titleParams.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
                } else if (mMoreIv != null) {
                    titleParams.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
                } else {
                    titleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
            }
        } else {
            if (mAddIv != null) {
                return;
            }
            initAdd(mTypedArray);

            if (mAddIv == null) {
                //有时候勿调用方法，如xml中没写对应的语句是直接创建不了的
                return;
            }

            if (mSearchIv != null) {
                LayoutParams params = (LayoutParams) mSearchIv.getLayoutParams();
                params.addRule(LEFT_OF, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                params.addRule(RelativeLayout.LEFT_OF, mAddIv.getId());
            }

            boolean shouldModify = false;
            LayoutParams titleParams = (LayoutParams) mTitleTv.getLayoutParams();
            int[] rules = titleParams.getRules();
            for(int rule : rules) {
                if (rule == R.id.baseRightTv || rule == R.id.baseMoreIv || rule == RelativeLayout.TRUE) {
                    shouldModify = true;
                    break;
                }
            }
            if (shouldModify) {
                titleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                titleParams.addRule(RelativeLayout.LEFT_OF, mAddIv.getId());
            }
        }
    }

    public ImageView getSearchIv() {
        return mSearchIv;
    }

    public void setSearchVisibility(int visibility) {
        if (visibility == View.GONE) {
            if (mSearchIv == null) {
                return;
            }
            removeView(mSearchIv);
            mSearchIv = null;

            LayoutParams titleParams = (LayoutParams) mTitleTv.getLayoutParams();
            if (mAddIv != null) {
                titleParams.addRule(RelativeLayout.LEFT_OF, mAddIv.getId());
            } else if (mRightTv != null) {
                titleParams.addRule(RelativeLayout.LEFT_OF, mRightTv.getId());
            } else if (mMoreIv != null) {
                titleParams.addRule(RelativeLayout.LEFT_OF, mMoreIv.getId());
            } else {
                titleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }

        } else {
            if (mSearchIv != null) {
                return;
            }
            initSearch(mTypedArray);
            if (mSearchIv == null) {
                //有时候误调用此方法，如xml中没写对应的语句是直接创建不了的
                return;
            }

            LayoutParams titleParams = (LayoutParams) mTitleTv.getLayoutParams();
            titleParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            titleParams.addRule(RelativeLayout.LEFT_OF, mSearchIv.getId());
        }
    }

    public TextView getTitleView() {
        return mTitleTv;
    }

    public void setTitle(String text) {
        mTitleTv.setText(text);
    }

    public void setRightTvEnable(boolean enable) {
        mRightTv.setEnabled(enable);
    }

    public void setOnTitleViewListener(OnTitleViewListener l) {
        mListener = l;
    }

    public View getCustomView() {
        return mCustomView;
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        int id = v.getId();
        if (id == R.id.baseBackIv) {
            mListener.onBack();
        } else if (id == R.id.baseSearchIv) {
            mListener.onSearch();
        } else if (id == R.id.baseAddIv) {
            mListener.onAdd();
        } else if (id == R.id.baseRightTv) {
            mListener.onRight();
        } else if (id == R.id.baseMoreIv) {
            mListener.onMore();
        } else if (id == R.id.baseWebExitIv) {
            mListener.onWebExit();
        }
    }

    public interface OnTitleViewListener {
        void onBack();
        void onSearch();
        void onAdd();
        void onRight();
        void onMore();
        void onWebExit();
    }

    public static class TitleViewListener implements OnTitleViewListener {

        @Override
        public void onBack() {
        }

        @Override
        public void onSearch() {
        }

        @Override
        public void onAdd() {
        }

        @Override
        public void onRight() {
        }

        @Override
        public void onMore() {
        }

        @Override
        public void onWebExit() {

        }
    }
}
