package com.xrwl.owner.module.home.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Auth;
import com.xrwl.owner.bean.GongAnAuth;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.module.home.mvp.AuthContract;
import com.xrwl.owner.module.home.mvp.AuthPresenter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class OwnerAuthActivity extends BaseActivity<AuthContract.IView, AuthPresenter> implements AuthContract.IView {

    private static Handler handler = new Handler();

    private long TIME_LISTEN = 1000;//设置2秒的延迟

    private Runnable runnable = new Runnable() {
        @Override

        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String abc = "";
                    String bca = "";
                    String cba = "";
                    if (TextUtils.isEmpty(mNameEt.getText().toString()) || mNameEt.getText().toString().length() == 0) {
                        abc = " ";
                    } else {
                        abc = mNameEt.getText().toString();
                    }
                    if (TextUtils.isEmpty(mauthidentifiedEt.getText().toString()) || mauthidentifiedEt.getText().toString().length() == 0) {
                        bca = " ";
                    } else {
                        bca = mauthidentifiedEt.getText().toString();
                    }
                    if (TextUtils.isEmpty(mUnitNameEt.getText().toString()) || mUnitNameEt.getText().toString().length() == 0) {
                        cba = " ";
                    } else {
                        cba = mUnitNameEt.getText().toString();
                    }
                    doSomeThing(abc, bca, cba);
                }
            });
        }
    };


    public static final int RESULT_ID = 100;
    public static final int RESULT_AVATAR = 200;
    public static final int RESULT_LICENSE = 300;

    @BindView(R.id.authIdIv)
    ImageView mIdIv;//身份证
    @BindView(R.id.authNameEt)
    EditText mNameEt;//姓名
    @BindView(R.id.authAvatarIv)
    ImageView mAvatarIv;//本人照片

    @BindView(R.id.authCb)
    CheckBox mCheckBox;
    @BindView(R.id.authTotalLayout)
    View mTotalView;
    @BindView(R.id.authUnitNameEt)
    EditText mUnitNameEt;//单位名称
    @BindView(R.id.authLicenseIv)
    ImageView mLicenseIv;//营业执照
    @BindView(R.id.authDesTv)
    TextView mDesTv;
    @BindView(R.id.authCheckBoxLayout)
    View mCheckBoxView;


    @BindView(R.id.authidentifiedEt)
    EditText mauthidentifiedEt;


    @BindView(R.id.authConfirmBtn)
    Button mConfirmBtn;

    private String mIdPath, mAvatarPath, mLicensePath;
    private ProgressDialog mLoadingDialog;


    //实名认证隐藏和显示的ly
    @BindView(R.id.topLY)
    LinearLayout mtoply;

    @BindView(R.id.toprzLY)
    LinearLayout mtoprzLY;


    @BindView(R.id.diyiLY)
    LinearLayout mdiyily;
    @BindView(R.id.dierLY)
    LinearLayout mdierly;

    @BindView(R.id.diyiweiwanshanbt)
    Button mdiyiweiwanshanbt;
    @BindView(R.id.dierweiwanshanbt)
    Button mdierweiwanshanbt;
    @BindView(R.id.disanweiwanshanbt)
    Button mdisanweiwanshanbt;

    @BindView(R.id.aliyun)
    TextView maliyun;

    @BindView(R.id.fanhuizhuyejian)
    LinearLayout mfanhuizhuyejian;
    @BindView(R.id.authIdIvUn)
    RelativeLayout mauthIdIvUn;
    @BindView(R.id.authAvatarIvUn)
    RelativeLayout mauthAvatarIvUn;
    @BindView(R.id.authLicenseIvUn)
    RelativeLayout mauthLicenseIvUn;
    @BindView(R.id.mainLayout)
    LinearLayout mmainLayout;

    @Override
    protected AuthPresenter initPresenter() {
        return new AuthPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.owner_auth_activity;
    }

    @Override
    protected void initViews() {

        mtoply.setVisibility(View.VISIBLE);
        mtoprzLY.setVisibility(View.GONE);

        maliyun.setVisibility(View.VISIBLE);

        mauthIdIvUn.setVisibility(View.VISIBLE);
        mIdIv.setVisibility(View.GONE);
        mauthAvatarIvUn.setVisibility(View.VISIBLE);
        mAvatarIv.setVisibility(View.GONE);
        mDesTv.setVisibility(View.VISIBLE);

        mmainLayout.setVisibility(View.VISIBLE);//整车视图-整体
        mCheckBoxView.setVisibility(View.VISIBLE);//单选框
        mauthLicenseIvUn.setVisibility(View.VISIBLE);//图片-无
        mLicenseIv.setVisibility(View.GONE);//图片-
        mTotalView.setVisibility(View.GONE);//整车视图-图

        getData();
    }


    @Override
    protected void getData() {

        mNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(s.toString().trim())) {

                    handler.postDelayed(runnable, TIME_LISTEN);

                }
            }
        });

        mauthidentifiedEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(s.toString().trim())) {

                    handler.postDelayed(runnable, TIME_LISTEN);

                }
            }
        });
        mUnitNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(s.toString().trim())) {

                    handler.postDelayed(runnable, TIME_LISTEN);

                }
            }
        });


        // mGetDialog = LoadingProgress.showProgress(this, "正在加载...");
        mPresenter.getData();
    }
    //方法一：

    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.e("TAG", "onLayoutChange: bottom" + bottom + " ---- oldBottom" + oldBottom);
        if (bottom > oldBottom && oldBottom != 0) {
            //键盘收起
            Log.e("TAG", "onLayoutChange:  键盘被收起了");
            //执行相关操作
            String s = mNameEt.getText().toString();
//            if(!"".equals(s.trim())){
//                doSomeThing(s);
//            }
        }
    }


    //执行操作
    private void doSomeThing(String usernamess, String invitePhonesss, String unitss) {
        String a = "";
        if (TextUtils.isEmpty(usernamess) || usernamess.length() == 0) {
            a = "";
        } else {
            try {
                a = URLEncoder.encode(usernamess, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String b = "";
        if (TextUtils.isEmpty(invitePhonesss) || invitePhonesss.length() == 0) {
            b = "";
        } else {
            try {
                b = URLEncoder.encode(invitePhonesss, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String c = "";
        if (TextUtils.isEmpty(unitss) || unitss.length() == 0) {
            c = "";
        } else {
            try {
                c = URLEncoder.encode(unitss, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("username", a);
        params.put("invitePhones", b);
        params.put("unit", c);
        params.put("check", "0");
        params.put("userid", mAccount.getId());
        mPresenter.postputongData(params);
    }


    @OnClick({R.id.fanhuizhuyejian,R.id.authCb, R.id.authConfirmBtn, R.id.diyiweiwanshanbt, R.id.dierweiwanshanbt, R.id.disanweiwanshanbt
    ,R.id.authIdIvUn})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.authCb) {
            if (mCheckBox.isChecked()) {
                mTotalView.setVisibility(View.VISIBLE);
            } else {
                mTotalView.setVisibility(View.GONE);
            }
        } else if(id == R.id.fanhuizhuyejian){
            finish();
        }else if (id == R.id.authConfirmBtn) {
            checkPost();
            checkPostzhizhao();
        }
    }

    private void checkPost() {
        if (TextUtils.isEmpty(mIdPath))
            return;

        if (TextUtils.isEmpty(mAvatarPath))
            return;

        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("type", "10");
        picMaps.put("pic_id", mIdPath);
        picMaps.put("pic_avatar", mAvatarPath);

        showLoading("上传身份证...");

        mPresenter.postData(picMaps, params);
    }

    public void showLoading(String msg) {
        mLoadingDialog = LoadingProgress.showProgress(this, msg);
    }

    public void dismissLoading(){
        if(mLoadingDialog!=null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    private void checkPostzhizhao() {
        Map<String, String> picMaps = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        params.put("type", "20");
        if (TextUtils.isEmpty(mLicensePath)) {
            showToast("请上传营业执照照片");
            return;
        }
        picMaps.put("pic_licence", mLicensePath);

        mPresenter.postData(picMaps, params);
    }

    @OnClick({R.id.authIdIvUn, R.id.authAvatarIvUn, R.id.authLicenseIvUn})
    public void camera(View v) {
        int id = v.getId();
        int result = RESULT_ID;
        if (id == R.id.authIdIvUn) {
            result = RESULT_ID;
        } else if (id == R.id.authAvatarIvUn) {
            result = RESULT_AVATAR;
        } else if (id == R.id.authLicenseIvUn) {
            result = RESULT_LICENSE;
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
            checkPost();
        } else if (requestCode == RESULT_AVATAR) {
            if (lm.isCompressed()) {
                mAvatarPath = lm.getCompressPath();
            } else {
                mAvatarPath = lm.getPath();
            }
            Glide.with(mContext).load(Uri.fromFile(new File(mAvatarPath))).into(mAvatarIv);
            checkPost();
        } else if (requestCode == RESULT_LICENSE) {
            if (lm.isCompressed()) {
                mLicensePath = lm.getCompressPath();
            } else {
                mLicensePath = lm.getPath();
            }
            Glide.with(mContext).load(Uri.fromFile(new File(mLicensePath))).into(mLicenseIv);
        }

    }

    @Override
    public void onPostSuccess(BaseEntity entity) {
        // mPostDialog.dismiss();
        // showToast("提交成功");
        //  startActivity(new Intent(mContext, OwnerAuthActivity.class));
        //finish();

        showToast("提交成功");

        dismissLoading();
        showLoading("阿里身份证验证...");
        mPresenter.shenfenzheng(mIdPath);

    }

    @Override
    public void onPostError(BaseEntity entity) {
        dismissLoading();
        handleError(entity);
    }

    @Override
    public void onPostError(Throwable e) {
        dismissLoading();
        showNetworkError();
    }

    @Override
    public void shenfenzhengSuccess(BaseEntity<GongAnAuth> entity) {
        GongAnAuth dd = entity.getData();

        mNameEt.setText(dd.name);
        mauthidentifiedEt.setText(dd.num);

        showToast("验证成功");
        dismissLoading();

    }

    @Override
    public void onGetCodeSuccess(BaseEntity<MsgCode> entity) {

    }

    @Override
    public void onGetCodeError(Throwable e) {

    }

    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {
        // mGetDialog.dismiss();
        Auth auth = entity.getData();

        if (auth != null) {
            if (!"0".equals(auth.name) && !"0".equals(auth.invitePhones)) {
                if (auth.name == null && auth.invitePhones == null) {

                } else {
                    mdiyiweiwanshanbt.setText("审核中");
                    mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                }

            }

            if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar)) {
                mdierweiwanshanbt.setText("审核中");
                mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);


            }
            if (!"0".equals(auth.unit) && (!TextUtils.isEmpty(auth.picLicence))) {
                if (auth.unit == null) {

                } else {
                    mdisanweiwanshanbt.setText("审核中");
                    mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                }

            }
            if ("1".equals(auth.review)) {

                mtoply.setVisibility(View.GONE);
                mtoprzLY.setVisibility(View.VISIBLE);
                if ((!TextUtils.isEmpty(auth.name)) && (!TextUtils.isEmpty(auth.invitePhones))) {
                    mdiyiweiwanshanbt.setText("审核通过");
                    mdiyiweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                }

                if ((!TextUtils.isEmpty(auth.picId)) && !TextUtils.isEmpty(auth.picAvatar)) {
                    mdierweiwanshanbt.setText("审核通过");
                    mdierweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                }
                if ((!TextUtils.isEmpty(auth.unit)) && (!TextUtils.isEmpty(auth.picLicence))) {
                    mdisanweiwanshanbt.setText("审核通过");
                    mdisanweiwanshanbt.setTextColor(android.graphics.Color.BLUE);
                }

            }

            mCheckBox.setEnabled(true);

            if ("0".equals(auth.name)) {
                mNameEt.setText("");
            } else {
                if (auth.name == null) {

                } else {
                    try {
                        mNameEt.setText(URLDecoder.decode(auth.name, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }

            if ("0".equals(auth.invitePhones)) {
                mauthidentifiedEt.setText("");
            } else {
                if (auth.invitePhones == null) {

                } else {
                    try {
                        mauthidentifiedEt.setText(URLDecoder.decode(auth.invitePhones, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }


            if ("0".equals(auth.unit)) {
                mUnitNameEt.setText("");
            } else {
                if (auth.unit == null) {

                } else {
                    try {
                        mUnitNameEt.setText(URLDecoder.decode(auth.unit, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
            if (!TextUtils.isEmpty(auth.picId)) {//身份证
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.fanmianshili)//图片加载出来前，显示的图片
                        .fallback( R.drawable.fanmianshili) //url为空的时候,显示的图片
                        .error(R.drawable.fanmianshili);//图片加载失败后，显示的图片
                Glide.with(this).load(auth.picId).apply(options).into(mIdIv);

            }
            if (!TextUtils.isEmpty(auth.picAvatar)) {//本人
                Glide.with(this).load(auth.picAvatar).into(mAvatarIv);
            }
            if (!TextUtils.isEmpty(auth.picLicence)) {//营业执照
                Glide.with(this).load(auth.picLicence).into(mLicenseIv);
            }
//                if (auth.isCarLoad()) {
//                    mTotalView.setVisibility(View.VISIBLE);
//                    if (!TextUtils.isEmpty(auth.picLicence)) {//营业执照
//                        Glide.with(this).load(auth.picLicence).into(mLicenseIv);
//                    }
//                }


        }


    }

    @Override
    public void onRefreshError(Throwable e) {
        //mGetDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
        showToast("验证失败请重新上传");
        dismissLoading();
        //mGetDialog.dismiss();
    }
}
