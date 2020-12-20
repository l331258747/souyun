package com.xrwl.owner.module.account.activity;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.WxCode;
import com.xrwl.owner.module.account.mvp.AccountContract;
import com.xrwl.owner.module.account.mvp.ModifyPresenter;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 修改密码
 * Created by www.longdw.com on 2018/3/25 下午9:20.
 */

public class ModifyPwdActivity extends BaseActivity<AccountContract.IModifyView, ModifyPresenter> implements AccountContract.IModifyView {

    public static final int COUNT_DOWN = 5;

    @BindView(R.id.phoneAndCodeLayout)
    View mPhoneAndCodeView;//短信验证码布局
    @BindView(R.id.mpOldPwdEt)
    EditText mOldEt;

    @BindView(R.id.mpPhoneEt)
    EditText mPhoneEt;

    @BindView(R.id.mpCodeEt)
    EditText mCodeEt;

    @BindView(R.id.mpCodeBtn)
    Button mCodeBtn;

    @BindView(R.id.mpNewPwdEt)
    EditText mNewPwdEt;

    @BindView(R.id.mpNewPwd2Et)
    EditText mNewPwd2Et;

    @BindView(R.id.mpModifyBtn)
    Button mModifyBtn;


    private Disposable mDisposable;
    private ProgressDialog mDialog;

    /** 区分是1：找回密码 2：修改密码 */
    private int mType;
    private String mCode;
    private ProgressDialog mGetCodeDialog;

    @Override
    protected ModifyPresenter initPresenter() {
        return new ModifyPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.modifypassword_activity;
    }
    @Override
    protected void initViews() {

        mType = getIntent().getIntExtra("type", -1);
        if (mType == 2) {//修改密码
            setTitleText("修改密码");
            mModifyBtn.setText("修改密码");
            mPhoneAndCodeView.setVisibility(View.GONE);
//            mOldEt.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.mpModifyBtn, R.id.mpCodeBtn})
    public void onClick(View v) {
        if (v.getId() == R.id.mpModifyBtn) {
            String phone = mPhoneEt.getText().toString();
            if (mType == 1) {
                String code = mCodeEt.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    showToast("请输入短信验证码");
                    return;
                }
                if (TextUtils.isEmpty(mCode) || !code.equals(mCode)) {
                    showToast("验证码不正确");
                    return;
                }
                if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
                    showToast("请输入正确的手机号码");
                    return;
                }
            }
//            else if (mType == 2) {
//                String oldPwd = mOldEt.getText().toString();
//                if (TextUtils.isEmpty(oldPwd)) {
//                    showToast("请先输入旧密码");
//                    return;
//                }
//            }
            String newPwd = mNewPwdEt.getText().toString();
            String newPwd2 = mNewPwd2Et.getText().toString();
            if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(newPwd2)) {
                showToast("请输入新密码和确认密码");
                return;
            }
            if (!newPwd.equals(newPwd2)) {
                showToast("两次输入的密码不一致");
                return;
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("pwd", newPwd);
            params.put("tel", phone);

            if (mType == 1) {
                mDialog = LoadingProgress.showProgress(this, "密码找回中，请稍等...");
                params.put("code", mCode);
                mPresenter.retrieve(params);
            } else if (mType == 2) {
                mDialog = LoadingProgress.showProgress(this, "密码修改中，请稍等...");
                mPresenter.modify(params);
            }

        } else if (v.getId() == R.id.mpCodeBtn) {
            String phone = mPhoneEt.getText().toString();
            if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
                showToast("请输入正确的手机号码");
                return;
            }

            mGetCodeDialog = LoadingProgress.showProgress(mContext, "正在发送请求...");
            getCode(phone);
        }
    }

    private void getCode(String phone) {
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
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public void onRefreshSuccess(BaseEntity entity) {
        mDialog.dismiss();
        showToast("密码修改成功");
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
        mCode = entity.getData().code;
        showToast("短信已发送");
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
}
