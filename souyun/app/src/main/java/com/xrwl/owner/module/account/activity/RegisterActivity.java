package com.xrwl.owner.module.account.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.ldw.library.bean.BaseEntity;
import com.ldw.library.view.dialog.LoadingProgress;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.bean.MsgCode;
import com.xrwl.owner.bean.Register;
import com.xrwl.owner.bean.WxCode;
import com.xrwl.owner.module.account.mvp.AccountContract;
import com.xrwl.owner.module.account.mvp.RegisterPresenter;
import com.xrwl.owner.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
 * Created by www.longdw.com on 2018/3/25 下午9:20.
 */

public class RegisterActivity extends BaseActivity<AccountContract.IRegisterView, RegisterPresenter> implements AccountContract.IRegisterView {

    public static final int COUNT_DOWN = 59;

    @BindView(R.id.registerUsernameEt)
    EditText mUsernameEt;

    @BindView(R.id.registerPwdEt)
    EditText mPwdEt;

    @BindView(R.id.registerPhoneEt)
    EditText mPhoneEt;

    @BindView(R.id.registerCodeEt)
    EditText mCodeEt;

    @BindView(R.id.registerIdEt)
    EditText mIdEt;

    @BindView(R.id.registerProtocolCb)
    CheckBox mCheckBox;

    @BindView(R.id.registerCodeBtn)
    Button mCodeBtn;

    @BindView(R.id.rg_tab_group)
    RadioGroup rgTabGroup;

//    @BindView(R.id.carSp)
//    Spinner cartype;

    private Disposable mDisposable;
    private SimpleDateFormat mDateFormat;
    private ProgressDialog mDialog;
    private String mCode;
    private ProgressDialog mGetCodeDialog;


    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.register_activity;
    }

    @Override
    protected void initViews() {
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
          StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
          StrictMode.setThreadPolicy(policy);
    }

    @OnClick({R.id.registerBtn, R.id.registerCodeBtn, R.id.registerProtocolTv})
    public void OnClick(View v) {
        if (v.getId() == R.id.registerBtn) {
            if (!mCheckBox.isChecked()) {
                showToast("请先阅读并同意用户协议");
                return;
            }
            String username = mUsernameEt.getText().toString();
            if (TextUtils.isEmpty(username)) {
                showToast("请输入姓名");
                return;
            }
            String pwd = mPwdEt.getText().toString();
//            if (TextUtils.isEmpty(pwd)) {
//                showToast("请输入密码");
//                return;
//            }
            String codes = mCodeEt.getText().toString();
            if (TextUtils.isEmpty(codes)) {
                showToast("请输入短信验证码");
                return;
            }
            if (TextUtils.isEmpty(mCode) || !codes.equals(mCode)) {
                showToast("验证码不正确");
                return;
            }
            String id ="18803578808";
 //           String id = mIdEt.getText().toString();
//            if (!IdCheckUtil.is18ByteIdCardComplex(id)) {
//                showToast("请输入正确的身份证号码");
//                return;
//            }
            String userType = "0" ;

//            switch (rgTabGroup.getCheckedRadioButtonId()) {
//                case R.id.rb_owner:
//                    userType = "0";
//                    break;
//                case R.id.rb_driver:
//                    userType = "1";
//                  //  mLocationBtn.setVisibility(View.GONE);
//                   // spcartype.setVisibility(View.VISIBLE);
//                    break;
//            }


           // String renzhengchexing = cartype.getSelectedItem().toString();

            String phone = mPhoneEt.getText().toString();

            mDialog = LoadingProgress.showProgress(this, "注册中，请稍等...");

            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", "123456");
            params.put("phone", phone);
            params.put("regdate", mDateFormat.format(new Date()));
            params.put("type", "0");
            params.put("invitePhone", "0");//代表没有输入身份证
            params.put("code", codes);
            params.put("renzhengchexing","0");//0代表没有车型

            //开始注册
            mPresenter.register(params);


            //开始工会注册

            //  mPresenter.registergonghui(phone,"android");
            // 模拟http请求，提交数据到服务器
            String path = "https://gh.jishanhengrui.com/api/user/public/register?username="+phone+"&device_type=android";
            try {
                URL url = new URL(path);
                // 2.建立一个http连接
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                // 3.设置一些请求方式
                conn.setRequestMethod("GET");// 注意GET单词字幕一定要大写
                conn.setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");

                int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
                // // 503服务器内部错误
                if (code == 1) {
                    InputStream is = conn.getInputStream();
                    // 把is的内容转换为字符串
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = is.read(buffer)) != -1) {
                        bos.write(buffer, 0, len);
                    }
                    String result = new String(bos.toByteArray());
                    is.close();
                    //Toast.makeText(this, result, 0).show();
                    showToast("注册成功");

                } else {
                    showToast("请求失败，该用户已经注册");
                    //Toast.makeText(this, "请求失败，失败原因: " + code, ).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(this, "请求失败，请检查logcat日志控制台", 0).show();
                showToast("请求失败。请检查网络");
            }







            /**兴荣商城同步用户**/
            //post提交参数
            //参数
            Map<String,String> paramsa = new HashMap<String,String>();
            paramsa.put("mobile",phone);
            paramsa.put("goods_id", "123456");
            paramsa.put("present_price", "123456");
            paramsa.put("num", "0");
            paramsa.put("price", "");
            paramsa.put("order_number", "");

            //服务器请求路径
            String strUrlPath = "https://gh.jishanhengrui.com/api/goods/order/create_order";
            String strResult=submitPostData(strUrlPath,paramsa, "utf-8");
            showToast(strResult);
            // m_lblPostResult.setText(strResult);
            // openToast("提交完成");

        } else if (v.getId() == R.id.registerCodeBtn) {//获取验证码
            String phone = mPhoneEt.getText().toString();
            if (phone.length() == 0 || !phone.startsWith("1") || phone.length() != 11) {
                showToast("请输入正确的手机号码");
                return;
            }

            mGetCodeDialog = LoadingProgress.showProgress(mContext, "正在发送请求...");
            getCode(phone);
        } else if (v.getId() == R.id.registerProtocolTv) {//用户协议
            Intent xie = new Intent();
            xie.putExtra("title", "用户协议");
            xie.putExtra("url", Constants.URL_PROTOCAL);
            xie.setClass(this, WebActivity.class);
            startActivity(xie);
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
    public void onRefreshSuccess(BaseEntity<Register> entity) {
        mDialog.dismiss();
        Register r = entity.getData();
        if (!TextUtils.isEmpty(r.status) && r.status.equals("0")) {
            new AlertDialog.Builder(this)
                    .setMessage(entity.getMsg())
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPhoneEt.setText("");
                            mCodeEt.setText("");
                        }
                    }).show();
        } else {
            showToast("注册成功");
            finish();
        }
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
                            mPhoneEt.setText("");
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


    /*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     */
    public static String submitPostData(String strUrlPath, Map<String, String> params, String encode) {

        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {

//String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
            URL url = new URL(strUrlPath);

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
//获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
//e.printStackTrace();
            return "err: " + e.getMessage().toString();
        }
        return "-1";
    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

}
