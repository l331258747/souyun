package com.xrwl.owner.module.home.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Auth;
import com.xrwl.owner.module.home.mvp.DriverAuthContract;
import com.xrwl.owner.module.home.mvp.DriverAuthPresenter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 司机实名认证
 * Created by www.longdw.com on 2018/4/4 下午10:29.
 */
public class DriverAuthActivity extends BaseActivity<DriverAuthContract.IView, DriverAuthPresenter> implements DriverAuthContract
        .IView {

    public static final int RESULT_ID = 100;
    public static final int RESULT_AVATAR = 200;
    public static final int RESULT_DRIVER = 300;
    public static final int RESULT_BOOK = 400;

    @BindView(R.id.authIdIv)
    ImageView mIdIv;//身份证
    @BindView(R.id.authNameEt)
    EditText mNameEt;//姓名
    @BindView(R.id.authAvatarIv)
    ImageView mAvatarIv;//本人照片

    @BindView(R.id.authDriverIv)
    ImageView mDriverIv;//驾驶证
    @BindView(R.id.authBookIv)
    ImageView mBookIv;//行车本

    @BindView(R.id.authConfirmBtn)
    Button mConfirmBtn;

    @BindView(R.id.authSpinner)
    Spinner mAuthSpinner;

    private String mIdPath, mAvatarPath, mDriverPath, mBookPath;
    private ProgressDialog mGetDialog;
    private ProgressDialog mPostDialog;

    private String mCategory;

    @Override
    protected DriverAuthPresenter initPresenter() {
        return new DriverAuthPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.driver_auth_activity;
    }

    @Override
    protected void initViews() {

        mAuthSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
                new String[]{ "同城零担","冷链运输","长途整车", "长途零担", "铁路运输", "航空运输" }));
        mAuthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = (position) + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getData();
    }

    @Override
    protected void getData() {
        mGetDialog = LoadingProgress.showProgress(this, "正在加载...");
        mPresenter.getData();
    }

    @OnClick({R.id.authConfirmBtn})
    public void onClick(View v) {
        int id = v.getId();if (id == R.id.authConfirmBtn) {
            checkPost();
        }
    }

    private void checkPost() {
        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(mIdPath)) {
            showToast("请上传身份证照片");
            return;
        }
        params.put("check", "0");
        params.put("type", "0");
        params.put("pic_licence", " ");
        picMaps.put("pic_id", mIdPath);
        String name = mNameEt.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("请输入姓名");
            return;
        }
        params.put("username", name);

        if (TextUtils.isEmpty(mAvatarPath)) {
            showToast("请上传本人照片");
            return;
        }
        picMaps.put("pic_avatar", mAvatarPath);

        if (TextUtils.isEmpty(mDriverPath)) {
            showToast("请上传驾驶证");
            return;
        }
        picMaps.put("pic_drive", mDriverPath);

        if (TextUtils.isEmpty(mBookPath)) {
            showToast("请上传行车本");
            return;
        }
        picMaps.put("pic_train", mBookPath);

        params.put("category", mCategory);

        mPostDialog = LoadingProgress.showProgress(this, "正在提交...");
        mPresenter.postData(picMaps, params);
    }

    @OnClick({R.id.authIdIv, R.id.authAvatarIv, R.id.authDriverIv, R.id.authBookIv})
    public void camera(View v) {
        int id = v.getId();
        int result = RESULT_ID;
        if (id == R.id.authIdIv) {
            result = RESULT_ID;
        } else if (id == R.id.authAvatarIv) {
            result = RESULT_AVATAR;
        } else if (id == R.id.authDriverIv) {
            result = RESULT_DRIVER;
        } else if (id == R.id.authBookIv) {
            result = RESULT_BOOK;
        }

        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .compress(true)
                .circleDimmedLayer(true)
                .forResult(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (selectList == null || selectList.size() == 0) {
            return;
        }
        LocalMedia lm = selectList.get(0);
        if (requestCode == RESULT_ID) {
            if (lm.isCompressed()) {
                mIdPath = lm.getCompressPath();
            } else {
                mIdPath = lm.getPath();
            }

            Glide.with(mContext).load(Uri.fromFile(new File(mIdPath))).into(mIdIv);
        } else if (requestCode == RESULT_AVATAR) {
            if (lm.isCompressed()) {
                mAvatarPath = lm.getCompressPath();
            } else {
                mAvatarPath = lm.getPath();
            }
            Glide.with(mContext).load(Uri.fromFile(new File(mAvatarPath))).into(mAvatarIv);
        } else if (requestCode == RESULT_DRIVER) {
            if (lm.isCompressed()) {
                mDriverPath = lm.getCompressPath();
            } else {
                mDriverPath = lm.getPath();
            }
            Glide.with(mContext).load(Uri.fromFile(new File(mDriverPath))).into(mDriverIv);
        } else if (requestCode == RESULT_BOOK) {
            if (lm.isCompressed()) {
                mBookPath = lm.getCompressPath();
            } else {
                mBookPath = lm.getPath();
            }
            Glide.with(mContext).load(Uri.fromFile(new File(mBookPath))).into(mBookIv);
        }

    }

    @Override
    public void onPostSuccess(BaseEntity entity) {
        mPostDialog.dismiss();
        showToast("提交成功");
        finish();
    }

    @Override
    public void onPostError(BaseEntity entity) {
        mPostDialog.dismiss();
        handleError(entity);
    }

    @Override
    public void onPostError(Throwable e) {
        mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {
        mGetDialog.dismiss();
        Auth auth = entity.getData();
        if (auth != null) {

//            if (TextUtils.isEmpty(auth.review)) {
//                return;
//            }
//
//            if ("2".equals(auth.review)) {
//                showToast("上次提交的信息未通过审核，请重新提交");
//                return;
//            }
//
//            if ("0".equals(auth.review)) {
//                mConfirmBtn.setEnabled(false);
//                mConfirmBtn.setText("审核中");
//            } else if ("1".equals(auth.review)) {
//                mConfirmBtn.setEnabled(false);
//                mConfirmBtn.setText("审核通过");
//            }

            mNameEt.setEnabled(false);
            mNameEt.setText(auth.name);

            mAuthSpinner.setEnabled(false);
            if ("0".equals(auth.category)) {
                mAuthSpinner.setSelection(0);
            } else if ("1".equals(auth.category)) {
                mAuthSpinner.setSelection(1);
            } else {
                mAuthSpinner.setSelection(2);
            }

            if (!TextUtils.isEmpty(auth.picId)) {//身份证
                Glide.with(this).load(auth.picId).into(mIdIv);
            }
            if (!TextUtils.isEmpty(auth.picAvatar)) {//本人
                Glide.with(this).load(auth.picAvatar).into(mAvatarIv);
            }
            if (!TextUtils.isEmpty(auth.picDriver)) {//驾驶证
                Glide.with(this).load(auth.picDriver).into(mDriverIv);
            }
            if (!TextUtils.isEmpty(auth.picBook)) {//行车本
                Glide.with(this).load(auth.picBook).into(mBookIv);
            }
        }
    }

    @Override
    public void onRefreshError(Throwable e) {
        mGetDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
        mGetDialog.dismiss();
    }
}
