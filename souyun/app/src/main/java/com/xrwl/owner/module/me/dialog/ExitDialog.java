package com.xrwl.owner.module.me.dialog;

import android.content.Intent;
import android.view.View;

import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.module.account.activity.LoginActivity;
import com.xrwl.owner.module.loading.activity.LoadingActivity;
import com.xrwl.owner.utils.AccountUtil;

import butterknife.OnClick;

/**
 * Created by www.longdw.com on 2018/4/5 上午12:36.
 */
public class ExitDialog extends BasePopDialog {
    @Override
    protected int getLayoutId() {
        return R.layout.exit_dialog;
    }

    @Override
    protected void initView() {
    }

    @OnClick({R.id.edExitTv, R.id.edCancelTv})
    public void onClick(View v) {
        if (v.getId() == R.id.edExitTv) {
            //退出到登录界面
            AccountUtil.clear(getContext());

            Account account = new Account();
            account.setFirst(false);
            AccountUtil.saveAccount(getContext(), account);

            Intent intent = new Intent(getActivity(), LoadingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            dismiss();
        }
    }
}
