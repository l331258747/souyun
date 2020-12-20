package com.ldw.library.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.ldw.library.R;


/**
 * Created by www.longdw.com on 16/10/9 下午3:32.
 */

public class ClearEditText extends AppCompatEditText {

    public interface OnEditTextListener {
        void onEditTextNull(boolean isNull);
    }

    private static final String TAG = ClearEditText.class.getSimpleName();

    private Drawable mDelDrawable;
    private Drawable mLeftDrawable;
    private OnEditTextListener mListener;

    public ClearEditText(Context context) {
        super(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        mDelDrawable = getResources().getDrawable(R.drawable.ul_ic_delete);
        Drawable[] drawables = getCompoundDrawables();
        mLeftDrawable = drawables[0];
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    private void setDrawable() {
        boolean isNull;
        if (length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, mDelDrawable, null);
            isNull = false;
        } else {
            setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, null, null);
            isNull = true;
        }
        if (mListener != null) {
            mListener.onEditTextNull(isNull);
        }
    }

    public void setContent(String text) {
        setText(text);
        setSelection(text.length());
    }

    public void hideDel() {
        setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, null, null);
    }

    public void showDel() {
        setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, mDelDrawable, null);
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDelDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if (rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public void setTxt(String msg) {
        setText(msg);
        setSelection(msg.length());
    }

    public void setOnEditTextListener(OnEditTextListener l) {
        mListener = l;
    }
}
