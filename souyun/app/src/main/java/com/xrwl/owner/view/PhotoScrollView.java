package com.xrwl.owner.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.xrwl.owner.R;

import java.io.File;
import java.util.List;


/**
 * Created by www.longdw.com on 2016/11/1 上午10:27.
 */

public class PhotoScrollView extends HorizontalScrollView implements View.OnClickListener {

    private LinearLayout mContainerLayout;
    private FrameLayout mAddPhotoLayout;
    private Context mContext;
    private int mItemBgSize;
    private int mItemSize;
    private OnClickListener mListener;
    private FragmentActivity mActivity;
//    private int mMarginLeft;

    public PhotoScrollView(Context context) {
        this(context, null);
    }

    public PhotoScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mContainerLayout = findViewById(R.id.add_showphotos_container_layout);
        mAddPhotoLayout = findViewById(R.id.add_showphotos_addphoto_layout);
        mAddPhotoLayout.setOnClickListener(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mItemBgSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.add_showphotos_photo_item_bg_size), dm);
        mItemSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.add_showphotos_photo_item_size), dm);
//        mMarginLeft = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.add_showphotos_photo_item_margin), dm);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onClick(v);
        }
    }

    public void setDatas(List<String> imagePaths) {
        setDatas(imagePaths, null);
    }

    @SuppressLint("CheckResult")
    public void setDatas(final List<String> imagePaths, final List<LocalMedia> localMedias) {

        mContainerLayout.removeAllViews();

        if (imagePaths == null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemSize, mItemSize);
            params.setMargins(10, 0, 0, 0);
            mContainerLayout.addView(mAddPhotoLayout, params);
            return;
        }


        int i = 0;
        for(final String path : imagePaths) {
            View itemView = View.inflate(mContext, R.layout.add_showphotos_item_layout, null);
            ImageView imageView = itemView.findViewById(R.id.add_showphotos_item_iv);
            Glide.with(mContext).load(Uri.fromFile(new File(path))).into(imageView);
            ImageView deleteIv = itemView.findViewById(R.id.add_showphotos_delete_iv);
            deleteIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    imagePaths.remove(path);
                    setDatas(imagePaths, localMedias);
                }
            });

            setListener(imageView, i, localMedias);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemBgSize, mItemBgSize);
            if (i > 0) {
                params.setMargins(10, 0, 0, 0);
            }
            mContainerLayout.addView(itemView, params);
            i++;
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemSize, mItemSize);
        params.setMargins(10, 0, 0, 0);
        mContainerLayout.addView(mAddPhotoLayout, params);
    }

    private void setListener(ImageView imageView, final int i, final List<LocalMedia> localMedias) {
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localMedias != null) {
                    PictureSelector.create(mActivity).externalPicturePreview(i, localMedias);
                }
            }
        });
    }

    public void setOnSelectListener(OnClickListener l) {
        mListener = l;
    }

    public void setActivity(FragmentActivity activity) {
        mActivity = activity;
    }
}
