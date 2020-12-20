package com.ldw.library.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by www.longdw.com on 2018/3/4 上午8:39.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public SpaceItemDecoration(int space) {
        this(space, space, space, space);
    }

    public SpaceItemDecoration(int l, int t, int r, int b) {
        mLeft = l;
        mTop = t;
        mRight = r;
        mBottom = b;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = mLeft;
        outRect.top = mTop;
        outRect.right = mRight;
        outRect.bottom = mBottom;
    }
}
