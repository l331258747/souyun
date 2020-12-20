package com.xrwl.owner.module.find.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.PermissionUtils;
import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Order;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/5 下午5:52.
 */
public class FindAdapter extends CommonAdapter<Order> {
    public FindAdapter(Context context, int layoutId, List<Order> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final Order order, int position) {
        ImageView callIv = holder.getView(R.id.findItemCallIv);
        callIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && PermissionUtils.isGranted(Manifest.permission.CALL_PHONE))
                        || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                    new AlertDialog.Builder(mContext).setMessage("是否拨打电话")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setData(Uri.parse("tel:" + order.getPhone()));
                                    intent.setAction(Intent.ACTION_CALL);
                                    mContext.startActivity(intent);
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(mContext).setMessage("请先打开电话权限")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionUtils.openAppSettings();
                                }
                            }).show();
                }
            }
        });

        if (TextUtils.isEmpty(order.getPhone())) {
            callIv.setVisibility(View.GONE);
        } else {
            callIv.setVisibility(View.VISIBLE);
        }

        holder.setText(R.id.findItemStartTv, order.getStartPos());
        holder.setText(R.id.findItemEndTv, order.getEndPos());
        holder.setText(R.id.findItemProductNameTv, "货名：" + order.getProductName());
        holder.setText(R.id.findItemTruckTv, "车型：" + order.getTruck());

       // holder.setText(R.id.findItemPropertyTv, order.getWeight() + "吨/" + order.getArea() + "方/" + order.getNum() + "件");
        holder.setText(R.id.findItemPropertyTv, order.getWeight() + "吨/" + order.getArea() + "方/" );
        holder.setText(R.id.findItemDateTv, order.getDate());
    }
}
