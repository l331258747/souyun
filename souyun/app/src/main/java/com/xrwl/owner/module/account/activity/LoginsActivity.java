package com.xrwl.owner.module.account.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.WxCode;
import com.xrwl.owner.module.account.mvp.AccountContract;
import com.xrwl.owner.module.account.mvp.LoginPresenter;
import com.xrwl.owner.module.account.view.LoginView;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.xrwl.owner.utils.AccountUtil;
import com.xrwl.owner.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by www.longdw.com on 2018/3/25 下午4:17.
 */

public class LoginsActivity extends  BaseActivity<AccountContract.ILoginView, LoginPresenter> implements AccountContract.ILoginView {

    public static final int COUNT_DOWN = 59;
    private long firstTime=0;

    @BindView(R.id.loginCb)
    CheckBox mProtocolCb;
    @BindView(R.id.loginRootView)
    LoginView mLoginView;
    @BindView(R.id.loginCodeBtn)
    Button mCodeBtn;
    private ProgressDialog mDialog;
    private ProgressDialog mGetCodeDialog;
    private Disposable mDisposable;
    private String mCode;

//    @BindView(R.id.mpPhoneEt)
//    EditText mPhoneEt;
//
//    @BindView(R.id.mpCodeEt)
//    EditText mCodeEt;
//
//    @BindView(R.id.mpCodeBtn)
//    Button mCodeBtn;
    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.loginBtn, R.id.loginRegisterTv, R.id.driverLoginBtn,R.id.loginServiceTv,R.id.loginForgetPwdTv,R.id.loginProtocolTv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:

                String phone = mLoginView.getUsername();
                String code = mLoginView.getPwd();
                if (TextUtils.isEmpty(phone)) {
                    showToast("请输入手机号码");
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

                mDialog = LoadingProgress.showProgress(this, getString(R.string.login_logining));
                Map<String, String> params = new HashMap<>();
                params.put("tel", mLoginView.getUsername());
//                String pwd = mLoginView.getPwd();
//                pwd = CommTools.reMd5Code(pwd);
//                params.put("password", pwd);
                mPresenter.login(params);

                break;
            case R.id.loginRegisterTv:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.driverLoginBtn:
                mDialog = LoadingProgress.showProgress(this, getString(R.string.login_logining));
                Map<String, String> params1 = new HashMap<>();
                params1.put("username", mLoginView.getUsername());
//                params1.put("username", "吉兴荣1");
                String pwd1 = mLoginView.getPwd();
//                String pwd1 = "123";
                params1.put("password", pwd1);
                mPresenter.login(params1);
                break;
            case R.id.loginServiceTv:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "0357-2591666");
                intent.setData(data);
                startActivity(intent);

//                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
//                        || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    new AlertDialog.Builder(mContext).setMessage("是否拨打电话")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent2 = new Intent();
//                                    intent2.setData(Uri.parse("tel:" + Constants.PHONE_SERVICE));
//                                    intent2.setAction(Intent.ACTION_CALL);
//                                    startActivity(intent2);
//                                }
//                            }).show();
//                } else {
//                    new AlertDialog.Builder(this).setMessage("请先打开电话权限")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    PermissionUtils.openAppSettings();
//                                }
//                            }).show();
//                }
                break;
            case R.id.loginForgetPwdTv:
                Intent mpIntent = new Intent(this, ModifyPwdActivity.class);
                mpIntent.putExtra("type", 1);
                startActivity(mpIntent);
                break;
            case R.id.loginProtocolTv:
                Intent xie = new Intent();
                xie.putExtra("title", "用户协议");
                xie.putExtra("url", Constants.URL_PROTOCAL);
                xie.setClass(this, WebActivity.class);
                startActivity(xie);
                break;
//            case R.id.mpCodeBtn:
//                String phone = mPhoneEt.getText().toString();
//                if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
//                    showToast("请输入正确的手机号码");
//                    return;
//                }
//                mGetCodeDialog = LoadingProgress.showProgress(mContext, "正在发送请求...");
//             //   getCode(phone);
//                break;
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
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String msg = COUNT_DOWN - aLong + "后重新获取";
                        mCodeBtn.setText(msg);
                    }
                })
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
    public void onRefreshSuccess(BaseEntity<Account> entity) {

        mDialog.dismiss();
        
        AccountUtil.saveAccount(this, entity.getData());

        startActivity(new Intent(this, TabActivity.class));

        finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
        mDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        mDialog.dismiss();
        handleError(entity);
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
    public void onwxGetCodeSuccess(BaseEntity<WxCode> entity) {

    }

    @Override
    public void onwxGetCodeError(Throwable e) {

    }

    @Override
    public void onwxinfoGetCodeSuccess(BaseEntity<WxCode> entity) {

    }

    @Override
    public void onwxinfoGetCodeError(Throwable e) {

    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setIcon(android.R.drawable.ic_dialog_info);
//        builder.setTitle("温馨提示");
//        builder.setMessage("您正在退出16飕镖局系统，确定要继续吗？");
//        builder.setCancelable(true);
//
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                /*
//                 *  在这里实现你自己的逻辑
//                 */
//                finish();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                /*
//                 *  在这里实现你自己的逻辑
//                 */
//                Intent intent=new Intent(LoginsActivity.this,TabActivity.class);
//                startActivity(intent);
//            }
//        });
//        builder.create().show();
//
//
//        return super.onKeyDown(keyCode, event);
//    }

}
