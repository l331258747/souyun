package com.xrwl.owner.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ldw.library.adapter.recycler.MultiItemTypeAdapter;
import com.ldw.library.view.GridSpacingItemDecoration;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.xrwl.owner.R;
import com.xrwl.owner.module.account.activity.ImageLookActivity;
import com.xrwl.owner.module.publish.adapter.PhotoRvAdapter;
import com.xrwl.owner.module.publish.adapter.PhotoRvAddDelegate;
import com.xrwl.owner.module.publish.adapter.PhotoRvItemDelegate;
import com.xrwl.owner.module.publish.bean.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:35.
 */
public class PhotoRecyclerView extends RelativeLayout {

    private RecyclerView mRv;

    private List<Photo> mDatas = new ArrayList<>();
    private OnPhotoRvControlListener mListener;
    private PhotoRvAdapter mAdapter;
    private boolean mHideCamera;
    private Object context;

    public PhotoRecyclerView(Context context) {
        super(context);
    }

    public PhotoRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }

        mRv = findViewById(R.id.addShowPhotosRv);
        mRv.addItemDecoration(new GridSpacingItemDecoration(4, 10, false));
        mRv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mDatas.add(new Photo(Photo.SECTION_ADD));


    }

    public void hideCamera() {
        mHideCamera = true;
        if (mDatas.size() > 0) {
            Photo photo = mDatas.get(mDatas.size() - 1);
            if (photo.getType() == Photo.SECTION_ADD) {
                mDatas.remove(photo);
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setDatas(final List<Photo> imagePaths) {
        if (imagePaths == null || imagePaths.size() == 0) {
            return;

        }
        mDatas.clear();
        for (Photo photo : imagePaths) {
            photo.setType(Photo.SECTION_ITEM);
//            Photo photo = new Photo(Photo.SECTION_ITEM);
//            photo.setPath(path);
            mDatas.add(photo);
        }


        if (!mHideCamera) {
            mDatas.add(new Photo(Photo.SECTION_ADD));
        }
        mAdapter = new PhotoRvAdapter(getContext(), mDatas, new PhotoRvAddDelegate.OnAddListener() {
            @Override
            public void onAdd() {
                if (mListener != null) {
                    mListener.onCamera();
                }

            }
        },  new PhotoRvItemDelegate.OnItemDeleteListener() {
            @Override
            public void onItemDelete(Photo photo) {
                mDatas.remove(photo);
                imagePaths.remove(photo);
                mAdapter.notifyDataSetChanged();
            }
        });
        mRv.setAdapter(mAdapter);

/**
 *  相机点击事件
 */

        mAdapter.setOnItemClickListenerTwo(new MultiItemTypeAdapter.OnItemClickListenerTwo() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Toast.makeText(getContext(), "点击到", Toast.LENGTH_SHORT).show();
                //组织数据
                ArrayList<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>(); // 这个最好定义成成员变量
                ThumbViewInfo item;
                mThumbViewInfoList.clear();

                for (int i = 0; i < mDatas.size(); i++) {
                    Rect bounds = new Rect();
                    //new ThumbViewInfo(图片地址);
                    item = new ThumbViewInfo(mDatas.get(i).getPath());
                    mThumbViewInfoList.add(item);
                }
//打开预览界面
                GPreviewBuilder.from((Activity) getContext())
                        //是否使用自定义预览界面，当然8.0之后因为配置问题，必须要使用
                        .to(ImageLookActivity.class)
                        .setData(mThumbViewInfoList)
                        .setCurrentIndex(position)
                        .setSingleFling(true)
                        .setType(GPreviewBuilder.IndicatorType.Number)
                        // 小圆点
//  .setType(GPreviewBuilder.IndicatorType.Dot)
                        .start();//启动

            }
        });

    }

    public interface OnItemClickListenerTwo {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

    }

    public void setOnPhotoRvControlListener(OnPhotoRvControlListener l) {
        mListener = l;
    }

    public interface OnPhotoRvControlListener {
        void onCamera();
    }


}
