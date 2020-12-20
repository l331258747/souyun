package com.xrwl.owner.module.account.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ldw.library.view.ClearEditText;
import com.xrwl.owner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by www.longdw.com on 2018/4/13 下午4:14.
 */
public class LoginView extends LinearLayout {

    @BindView(R.id.loginBtn)
    Button mLoginBtn;

    @BindView(R.id.loginUsernameEt)
    public ClearEditText mUsernameEt;

    @BindView(R.id.loginPwdEt)
    public ClearEditText mPwdEt;

    @BindView(R.id.loginCb)
    CheckBox mProtocolCb;

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this);

//        Account account = AccountUtil.getAccount(getContext());

//        mUsernameEt.setText(account.getUsername());
//        mPwdEt.setText(account.getPassword());

//        mUsernameEt.setText("吉兴荣");
//        mPwdEt.setText("123456");

        mProtocolCb.setChecked(true);
        mLoginBtn.setEnabled(true);

        mProtocolCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mUsernameEt.getText().toString().length() > 0
                        && mPwdEt.getText().toString().length() > 0
                        && isChecked) {
                    mLoginBtn.setEnabled(true);
                }
            }
        });

        initUsername();
        initPassword();
    }

    private void initUsername() {
        mUsernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0 && mPwdEt.getText().toString().length() > 0 && mProtocolCb.isChecked()) {
                    mLoginBtn.setEnabled(true);
                } else {
                    mLoginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mUsernameEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPwdEt.hideDel();
                    if (mUsernameEt.getText().toString().length() > 0) {
                        mUsernameEt.showDel();
                    }
                }
            }
        });
    }

    private void initPassword() {
        mPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0 && mUsernameEt.getText().toString().length() > 0 && mProtocolCb.isChecked()) {
                    mLoginBtn.setEnabled(true);
                } else {
                    mLoginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPwdEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mUsernameEt.hideDel();
                    if (mPwdEt.getText().toString().length() > 0) {
                        mPwdEt.showDel();
                    }
                }
            }
        });
    }

    public String getUsername() {
        return mUsernameEt.getText().toString();
    }

    public String getPwd() {
        return mPwdEt.getText().toString();
    }
}
