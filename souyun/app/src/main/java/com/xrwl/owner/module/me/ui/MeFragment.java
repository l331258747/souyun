package com.xrwl.owner.module.me.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldw.library.mvp.BaseMVP;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseFragment;
import com.xrwl.owner.bean.Account;
import com.xrwl.owner.module.account.activity.ModifyPwdActivity;
import com.xrwl.owner.module.home.ui.OwnerAuthActivity;
import com.xrwl.owner.module.me.dialog.ExitDialog;
import com.xrwl.owner.module.order.driver.ui.DriverOrderActivity;
import com.xrwl.owner.module.order.owner.ui.OwnerOrderActivity;
import com.xrwl.owner.module.publish.ui.AddressActivity;
import com.xrwl.owner.module.publish.ui.ReceiptActivity;
import com.xrwl.owner.utils.AccountUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;

/**
 * Created by www.longdw.com on 2018/3/31 下午2:39.
 */

public class MeFragment extends BaseFragment {
    public static final int RESULT_POSITION_START = 222;//发货定位
    public static final int RESULT_POSITION_END = 333;//到货定位
    @BindView(R.id.meItemNameTv)
    TextView appyonghuming;

    @BindView(R.id.meItemPhoneTv)
    TextView appdianhua;

    @BindView(R.id.yhj)
    TextView appyhj;

    @BindView(R.id.yhk)
    TextView appyhk;

    @BindView(R.id.ye)
    TextView appye;


    @BindView(R.id.addressTv)//地址
            TextView appaddressTv;

    @BindView(R.id.invoiceTv)//发票
            TextView appinvoiceTv;


    @BindView(R.id.myorders)
    TextView wodedingdan;


    @BindView(R.id.myrenzheng)
    TextView shimingrenzheng;

    @BindView(R.id.jine)
    TextView renminbi;

    @BindView(R.id.lianxikefu)
    TextView wodekefu;

    @BindView(R.id.xiugaimima)
    TextView wodexiugaimima;

    @BindView(R.id.tuichudenglu)
    TextView wotuichudenglu;


    @BindView(R.id.wyhzTv)
    TextView wodewyhz;


    @BindView(R.id.zxgmTv)
    TextView wodezxgmTv;


    @BindView(R.id.yhjpic)
    ImageView youhuijuanpic;

    @BindView(R.id.yhkpic)
    ImageView yinhangkapic;

    @BindView(R.id.yepic)
    ImageView yuepic;

    @BindView(R.id.gerenxinxi)
    LinearLayout wodexinxi;

    @BindView(R.id.wddd)
    LinearLayout appwodedingdan;


    @BindView(R.id.smrz)
    LinearLayout appshimingrenzheng;

    @BindView(R.id.je)
    LinearLayout appjine;

    @BindView(R.id.lxkf)
    LinearLayout applianxikefu;

    @BindView(R.id.xgmm)
    LinearLayout appxiugaimima;

    @BindView(R.id.tcdl)
    LinearLayout apptuichudenglu;

    private Account mAccount;

    public static MeFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);

        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    protected BaseMVP.BasePresenter initPresenter() {
        return null;
    }


    protected int getLayoutId() {
        return R.layout.me_fragment;
    }


    protected void initView(View view) {
        mAccount = AccountUtil.getAccount(mContext);

        if (mAccount.getName() == "0" || TextUtils.isEmpty(mAccount.getName())) {
            appyonghuming.setText("");
        } else {
            try {
                appyonghuming.setText(URLDecoder.decode(mAccount.getName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        appdianhua.setText(mAccount.getPhone());
        youhuijuanpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //优惠卷pic
                Intent intent = new Intent(mContext, CouponActivity.class);
                intent.putExtra("title", "优惠卷");
                startActivity(intent);
            }
        });
        yinhangkapic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //银行卡pic
                Intent intent = new Intent(mContext, BankActivity.class);
                intent.putExtra("title", "绑定银行卡");
                startActivity(intent);
            }
        });
        yuepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //鱼额pic
                Intent intent = new Intent(mContext, BankyueActivity.class);
                intent.putExtra("title", "金        额");
                startActivity(intent);
            }
        });
        appyhj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //优惠卷pic
                Intent intent = new Intent(mContext, CouponActivity.class);
                intent.putExtra("title", "优惠卷");
                startActivity(intent);
            }
        });
        appyhk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //银行卡pic
                Intent intent = new Intent(mContext, BankActivity.class);
                intent.putExtra("title", "绑定银行卡");
                startActivity(intent);
            }
        });
        appye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //鱼额pic
                Intent intent = new Intent(mContext, BankyueActivity.class);
                intent.putExtra("title", "金        额");
                startActivity(intent);
            }
        });
        appwodedingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAccount.isOwner()) {
                    Intent intent = new Intent(mContext, OwnerOrderActivity.class);
                    intent.putExtra("title", "我的订单");
                    startActivity(intent);
                } else if (mAccount.isDriver()) {
                    Intent intent = new Intent(mContext, DriverOrderActivity.class);
                    intent.putExtra("title", "我的订单");
                    startActivity(intent);
                }
            }
        });
        appshimingrenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, OwnerAuthActivity.class));
            }
        });
        appjine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BankActivity.class);
                intent.putExtra("title", "金        额");
                startActivity(intent);
            }
        });
        appaddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, AddressActivity.class), RESULT_POSITION_START);
            }
        });
        appinvoiceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, ReceiptActivity.class), RESULT_POSITION_START);
            }
        });
        applianxikefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "0357-2591666");
                intent.setData(data);
                startActivity(intent);
//                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
//                        || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//
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
//                    new AlertDialog.Builder(mContext).setMessage("请先打开电话权限")
//                            .setNegativeButton("取消", null)
//                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    PermissionUtils.openAppSettings();
//                                }
//                            }).show();
//                }
            }
        });
        appxiugaimima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ModifyPwdActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("title", "修改密码");
                startActivity(intent);
            }
        });
        apptuichudenglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitDialog dialog = new ExitDialog();
                dialog.show(getFragmentManager(), "exit");
            }
        });
        wodexinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MeInfoActivity.class);
                intent.putExtra("title", "个人信息");
                startActivity(intent);

            }
        });
        wodewyhz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddHezuoActivity.class);
                intent.putExtra("title", "我要合作");
                startActivity(intent);
            }
        });

        wodezxgmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZaiXianGouMai_Activity.class);
                intent.putExtra("title", "在线加油、加气");
                startActivity(intent);
            }
        });


    }


}
