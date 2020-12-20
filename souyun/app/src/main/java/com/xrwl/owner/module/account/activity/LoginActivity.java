package com.xrwl.owner.module.account.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.Log;
import com.xrwl.owner.MyApplication;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.WxCode;
import com.xrwl.owner.module.account.mvp.AccountContract;
import com.xrwl.owner.module.account.mvp.LoginPresenter;
import com.xrwl.owner.module.account.view.LoginView;
import com.xrwl.owner.module.home.ui.OwnerAuthtelActivity;
import com.xrwl.owner.module.tab.activity.TabActivity;
import com.xrwl.owner.utils.AccountUtil;
import com.xrwl.owner.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

public class LoginActivity extends BaseActivity<AccountContract.ILoginView, LoginPresenter> implements AccountContract.ILoginView {


    private WxCode wxcodebean;


    public static final int COUNT_DOWN = 59;

    private long firstTime=0;

    @BindView(R.id.loginCb)
    CheckBox mProtocolCb;

    @BindView(R.id.loginRootView)
    LoginView mLoginView;
    @BindView(R.id.loginCodeBtn)
    Button mCodeBtn;

    //微信发起登录
    @BindView(R.id.wxLoginBtn)
    ImageButton mwxBtn;


    private ProgressDialog mDialog;
    private ProgressDialog mGetCodeDialog;
    private Disposable mDisposable;
    private String mCode;
    private String nickname;
    private String openid;
    private String unionid;
    // 1.声明一个statid的IWXAPI 以及APPID

    public static IWXAPI wx_api; //全局的微信api对象
    public  String APP_ID = "wx40197add197dfbf6"; //替换为申请到的app id
    public  String APP_SECRET = "af3ea0b2a6eac75ca728da05a8767715"; //替换为申请到的app id
    public String path ="";

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
        // 2.初始化微信SDK
        wx_api = WXAPIFactory.createWXAPI(mContext, APP_ID, true);
        wx_api.registerApp(APP_ID);
        Intent intent =getIntent();
        nickname = getIntent().getStringExtra("nickname");
        openid = getIntent().getStringExtra("openid");
        unionid = getIntent().getStringExtra("unionid");
        if(!TextUtils.isEmpty(unionid))
        {
            mDialog = LoadingProgress.showProgress(this, getString(R.string.login_logining));
            // mPresenter.wxtoken(wxCode);
            String result = "";
            try {
                result = URLEncoder.encode(nickname, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                android.util.Log.e(TAG, e.getMessage(), e);
            }
            Log.d("nickname",result);
            Log.d("openid",openid);
            Log.d("unionid",unionid);
            //执行微信登陆
            Map<String, String> params = new HashMap<>();
            params.put("nickname", result);
            params.put("openid", openid);
            params.put("unionid", unionid);
            mPresenter.loginwx(params);

        }
   }

   @OnClick({R.id.loginBtn, R.id.loginRegisterTv, R.id.driverLoginBtn,R.id.loginServiceTv,R.id.loginForgetPwdTv,R.id.loginProtocolTv,R.id.wxLoginBtn})
    public void onClick(View v) {
       switch (v.getId()) {

           case R.id.loginBtn:

               String phone = mLoginView.getUsername();
               String code = mLoginView.getPwd();

               /**
                * 项目需求跳过短信验证
                */

               if (phone.equals("18803578808")) {

               } else {
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
               }
               mDialog = LoadingProgress.showProgress(this, getString(R.string.login_logining));
               Map<String, String> params = new HashMap<>();
               params.put("tel", mLoginView.getUsername());
               mPresenter.login(params);
               break;
           case R.id.loginRegisterTv:
               startActivity(new Intent(this, RegisterActivity.class));
               break;
           case R.id.wxLoginBtn:
               if (!MyApplication.mWXapi.isWXAppInstalled()) {
                   Toast.makeText(this, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
                   return;
               }
               final SendAuth.Req req = new SendAuth.Req();
               req.scope = "snsapi_userinfo";
               req.state = "skit_wx_login";//这个字段可以任意更改
               MyApplication.mWXapi.sendReq(req);
               break;
           case R.id.driverLoginBtn:
               mDialog = LoadingProgress.showProgress(this, getString(R.string.login_logining));
               Map<String, String> params1 = new HashMap<>();
               params1.put("username", mLoginView.getUsername());
               String pwd1 = mLoginView.getPwd();
               params1.put("password", pwd1);
               mPresenter.login(params1);
               break;
           case R.id.loginServiceTv:
               callPhone();
//               if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
//                       || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                   new AlertDialog.Builder(mContext).setMessage("是否拨打电话")
//                           .setNegativeButton("取消", null)
//                           .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
//                               @Override
//                               public void onClick(DialogInterface dialog, int which) {
//                                   Intent intent2 = new Intent();
//                                   intent2.setData(Uri.parse("tel:" + Constants.PHONE_SERVICE));
//                                   intent2.setAction(Intent.ACTION_CALL);
//                                   startActivity(intent2);
//                               }
//                           }).show();
//               } else {
//                   new AlertDialog.Builder(this).setMessage("请先打开电话权限")
//                           .setNegativeButton("取消", null)
//                           .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                               @Override
//                               public void onClick(DialogInterface dialog, int which) {
//                                   PermissionUtils.openAppSettings();
//                               }
//                           }).show();
//               }
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

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + "0357-2591666");
        intent.setData(data);
        startActivity(intent);
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

        Account accountaa=entity.getData();
        String weixinphone=accountaa.phone;
        if(!TextUtils.isEmpty(weixinphone))
        {

            startActivity(new Intent(this, TabActivity.class));
        }

       else
        {

            Intent intent = new Intent(mContext, OwnerAuthtelActivity.class);
            intent.putExtra("nickname", nickname);
            intent.putExtra("openid", openid);
            intent.putExtra("unionid", unionid);
            intent.putExtra("userid",mAccount.getId());
            startActivity(intent);

        }

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

       wxcodebean=entity.getData();
       String openid=wxcodebean.openid;
       String access_token=wxcodebean.access_token;

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
    /**
     *下面方法只使用与微信拒绝后无法登陆的时候操作    finish 掉透明界面  但是必须加 android:launchMode="singleInstance"
     * **/
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){//如果点击了返回键
            //声明并初始化弹出对象
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("提示：");
            builder.setMessage("是否退出");
            //设置确认按钮
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();//退出程序
                }
            });
            //设置取消按钮
            builder.setPositiveButton("取消",null);
            //显示弹框
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }







}
