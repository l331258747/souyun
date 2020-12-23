package com.xrwl.owner.module.home.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Auth;
import com.xrwl.owner.bean.GongAnAuth;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.module.account.activity.LoginActivity;
import com.xrwl.owner.module.account.view.LoginView;
import com.xrwl.owner.module.home.mvp.AuthContract;
import com.xrwl.owner.module.home.mvp.AuthPresenter;
import com.xrwl.owner.module.tab.activity.TabActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class OwnerAuthtelActivity extends BaseActivity<AuthContract.IView, AuthPresenter> implements AuthContract.IView {

    public static final int COUNT_DOWN = 59;

    private long firstTime=0;


    @BindView(R.id.loginCodeBtn)
    Button mCodeBtn;

    @BindView(R.id.loginBtn)
    Button mLoginBtn;

    @BindView(R.id.neifanhuifanhuijian)
    ImageView mneifanhuifanhuijian;

//    @BindView(R.id.authNameEt)
//    EditText mNameEt;//姓名

    @BindView(R.id.loginRootView)
    LoginView mLoginView;
    private String mCode;
    private ProgressDialog mDialog;
    private ProgressDialog mGetCodeDialog;
    private Disposable mDisposable;
    private String nicknames="";
    private String openids="";
    private String unionids="";
   //要求，判断是那个用户操作的
    @Override
    protected AuthPresenter initPresenter() {
        return new AuthPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.owner_authtel_activity;
    }

    @Override
    protected void initViews() {
      Intent intent=getIntent();
       nicknames = getIntent().getStringExtra("nickname");
       openids = getIntent().getStringExtra("openid");
       unionids = getIntent().getStringExtra("unionid");

  }
  @OnClick({R.id.loginBtn,R.id.neifanhuifanhuijian})
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.loginBtn) {


            String phone = mLoginView.getUsername();

            String code = mLoginView.getPwd();

            if (TextUtils.isEmpty(phone)) {
                showToast("请输入正确的手机号码");
                return;
            }

            if (TextUtils.isEmpty(code)) {
                showToast("请输入短信验证码");
                return;
            }

            if (!code.equals(mCode)) {
                showToast("验证码不正确");
                return;
            }
           mPresenter.wxtel(phone,openids,unionids,nicknames);

        }
        else if(id == R.id.neifanhuifanhuijian)
        {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }

    }
    @OnClick(R.id.loginCodeBtn)
    public void getCode() {
        String phone = mLoginView.getUsername();
        if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
            showToast("请输入正确的手机号码");
            return;
        }

        mGetCodeDialog = LoadingProgress.showProgress(mContext, "正在发送请求...");

        mCodeBtn.setEnabled(false);
        mPresenter.getCode(phone);
        mDisposable =  Flowable.intervalRange(0, COUNT_DOWN, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        String msg = COUNT_DOWN - aLong + "后重新获取";
//                        if(TextUtils.isEmpty(msg))
//                        {
//                            mCodeBtn.setText("重新获取");
//                        }
//                        else {
//                            mCodeBtn.setText(msg);
//                        }
//                    }
//                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mCodeBtn.setEnabled(true);
                        mCodeBtn.setText("获取验证码");
                    }
                })
                .subscribe();
    }
    @Override
    public void onGetCodeSuccess(BaseEntity<MsgCode> entity) {
        mGetCodeDialog.dismiss();

        MsgCode mc = entity.getData();
        if (!TextUtils.isEmpty(mc.status) && mc.status.equals("0")) {
            new AlertDialog.Builder(this)
                    .setMessage(entity.getMsg())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mLoginView.mUsernameEt.setText("");
                        }
                    }).show();
        } else {
            mCode = mc.code;
            showToast("短信已发送");
        }
    }

    @Override
    public void onGetCodeError(Throwable e) {
        mGetCodeDialog.dismiss();
//        showToast("发送失败，请重试");
    }
    @Override
    public void onPostSuccess(BaseEntity entity) {

    }

    @Override
    public void onPostError(BaseEntity entity) {
      //  mPostDialog.dismiss();
        handleError(entity);
    }

    @Override
    public void onPostError(Throwable e) {
       // mPostDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void shenfenzhengSuccess(BaseEntity<GongAnAuth> entity) {

    }

    @Override
    public void shenfenzhengError(BaseEntity entity) {

    }


    @Override
    public void onRefreshSuccess(BaseEntity<Auth> entity) {
       // mGetDialog.dismiss();
        //Auth auth = entity.getData();
        if(entity.getStatusCode().equals("200"))
        {
            startActivity(new Intent(mContext, TabActivity.class));
        }
        else
        {

           startActivity(new Intent(mContext, OwnerAuthtelActivity.class));
        }
        finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
        //mGetDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        handleError(entity);
        //mGetDialog.dismiss();
    }
}
