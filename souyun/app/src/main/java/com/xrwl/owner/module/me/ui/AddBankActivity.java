package com.xrwl.owner.module.me.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Looper;
import android.widget.EditText;
import android.widget.Spinner;

import com.ldw.library.bean.BaseEntity;
import com.tencent.mm.opensdk.utils.Log;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.event.BankListRefreshEvent;
import com.xrwl.owner.module.me.mvp.AddBankPresenter;
import com.xrwl.owner.module.me.mvp.BankContract;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 绑定银行卡
 * Created by www.longdw.com on 2018/4/5 上午12:24.
 */
public class AddBankActivity extends BaseActivity<BankContract.IAddView, AddBankPresenter> implements BankContract.IAddView {

    @BindView(R.id.addBankNumEt)
    EditText mNumEt;//账号
    @BindView(R.id.addBankNameEt)
    EditText mNameEt;//持卡人姓名
    @BindView(R.id.addBankAccountEt)
    EditText mAccountEt;//开户行
    @BindView(R.id.yinhangkaleibie)
    Spinner mYinhangkaleibie;//银行卡类别
    @BindView(R.id.suoshuyinhang)
    Spinner mSuoshuyinhang;//所属银行
    private ProgressDialog mProgressDialog;
    @BindView(R.id.addBankidentEt)
    EditText mident;//身份证

    private String num="";
    private String name="";
    private String idCard="";
    private String appyinhangkaleibie="";
    private String appsuoshuyinhang="";
    private String account ="暂时隐藏";
    @Override
    protected AddBankPresenter initPresenter() {
        return new AddBankPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.addbank_activity;
    }

    @Override
    protected void initViews() {
    }

    @Override
    public void onRefreshSuccess(BaseEntity entity) {

         //mProgressDialog.dismiss();
         EventBus.getDefault().post(new BankListRefreshEvent());
         finish();
    }

    @Override
    public void onRefreshError(Throwable e) {
        mProgressDialog.dismiss();
        showNetworkError();
    }

    @Override
    public void onError(BaseEntity entity) {
        mProgressDialog.dismiss();
        handleError(entity);
    }

    @OnClick(R.id.addBankConfirmBtn)
    public void confirm() {
         num = mNumEt.getText().toString();
//        if (TextUtils.isEmpty(num) || num.length() <= 20) {
//            showToast("请输入正确的银行卡号");
//            return;
//        }
         name = mNameEt.getText().toString();
//        if (TextUtils.isEmpty(name)) {
//            showToast("请输入持卡人姓名");
//            return;
//        }
         idCard = mident.getText().toString();
//        if (TextUtils.isEmpty(idCard)) {
//            showToast("请输入身份证");
//            return;
//        }
        //String account = mAccountEt.getText().toString();
         account ="暂时隐藏";
//        if (TextUtils.isEmpty(account)) {
//            showToast("请输入开户行");
//            return;
//        }
         appyinhangkaleibie=mYinhangkaleibie.getSelectedItem().toString();
         appsuoshuyinhang=mSuoshuyinhang.getSelectedItem().toString();


        getUserInfo(num,idCard,name); //卡号   身份证   姓名



    }



   //直接调取第三方的接口书出来
    private void getUserInfo(String numaa,String idCardAaa,String nameaa) {
        String path = "http://39.104.49.122:810/XingRongAppServer/Card/aliyunbank?accountNo="+numaa+"&idCard="+idCardAaa+"&name="+nameaa+"";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: userinfo" + e.getMessage());

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

               // Log.d(TAG, "onResponse: userinfo" + response.body().string());
                String str=response.body().string();
                 try {
                    JSONObject jsonObject=new JSONObject(str);
                    String status=jsonObject.optString("status");
                    String msg=jsonObject.optString("msg");
                    String idCards=jsonObject.optString("idCard");
                    String accountNo=jsonObject.optString("accountNo");
                    String bank=jsonObject.optString("bank");
                    String cardName=jsonObject.optString("cardName");
                    String cardType=jsonObject.optString("cardType");
                    String names=jsonObject.optString("name");
                    String sex=jsonObject.optString("sex");
                    String area=jsonObject.optString("area");
                    String province=jsonObject.optString("province");
                    String city=jsonObject.optString("city");
                    String prefecture=jsonObject.optString("prefecture");
                    String birthday=jsonObject.optString("birthday");
                    String addrCode=jsonObject.optString("addrCode");
                    String lastCode=jsonObject.optString("lastCode");



                    if(status.equals("01"))
                    {
                        //mProgressDialog = LoadingProgress.showProgress(AddBankActivity.this, "正在添加...");
                        Map<String, String> params = new HashMap<>();
                        params.put("num", num);
                        params.put("name", name);
                        params.put("account_bank", account);
                        params.put("category", appyinhangkaleibie);
                       // params.put("bank", appsuoshuyinhang);
                        params.put("check", "1");
                        params.put("status", status);
                        params.put("idCard", idCard);
                        params.put("accountNo", accountNo);
                        params.put("bank", bank);
                        params.put("cardName", cardName);
                        params.put("cardType", cardType);
                        params.put("sex", sex);
                        params.put("area", area);
                        params.put("province", province);
                        params.put("city", city);
                        params.put("prefecture", prefecture);
                        params.put("birthday", birthday);
                        params.put("addrCode", addrCode);
                        params.put("lastCode", lastCode);
                        mPresenter.addBank(params);
                        Looper.prepare();
                        new AlertDialog.Builder(mContext)
                                .setMessage("银行卡添加成功")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        Looper.loop();
                        return;

                    }
                    else if(status.equals("02"))
                    {

                        Looper.prepare();
                         new AlertDialog.Builder(mContext)
                                .setMessage("验证不通过")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        Looper.loop();
                        return;
                    }
                    else if(status.equals("202"))
                    {
                        Looper.prepare();
                        new AlertDialog.Builder(mContext)
                                .setMessage("无法验证")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        Looper.loop();
                        return;
                    }
                    else if(status.equals("203"))
                    {
                        Looper.prepare();
                        new AlertDialog.Builder(mContext)
                                .setMessage("异常情况(同一身份证号重复调用次数达到上限，请12小时后在请求)")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();   Looper.loop();
                        return;
                    }
                    else if(status.equals("204"))
                    {
                        Looper.prepare();
                        new AlertDialog.Builder(mContext)
                                .setMessage("姓名输入错误")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();   Looper.loop();
                        return;
                    }
                    else if(status.equals("205"))
                    {
                        Looper.prepare();
                        new AlertDialog.Builder(mContext)
                                .setMessage("身份证号输入错误")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();   Looper.loop();
                        return;
                    }
                    else if(status.equals("206"))
                    {
                        Looper.prepare();
                        new AlertDialog.Builder(mContext)
                                .setMessage("银行卡号输入错误")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();   Looper.loop();
                        return;
                    }
                    else
                    {
                        Looper.prepare();
                        new AlertDialog.Builder(mContext)
                                .setMessage("您输入的信息和银行信息不符合，请重新提交")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();   Looper.loop();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
