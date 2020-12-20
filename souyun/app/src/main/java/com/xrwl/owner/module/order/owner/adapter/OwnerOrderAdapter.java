package com.xrwl.owner.module.order.owner.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.ldw.library.adapter.recycler.CommonAdapter;
import com.ldw.library.adapter.recycler.base.ViewHolder;
import com.xrwl.owner.R;
import com.xrwl.owner.bean.Order;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/4/4 下午1:20.
 */
public class OwnerOrderAdapter extends CommonAdapter<Order> {
    private String mType;
    public OwnerOrderAdapter(Context context, int layoutId, List<Order> datas) {
        super(context, layoutId, datas);
    }
    public void setType(String type) {
        mType = type;
    }
    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }

        return false;
    }

    @Override
    protected void convert(ViewHolder holder, Order myOrder, int position) {



//        holder.setText(R.id.myOrderItemStartTv, myOrder.getStartPos());
//        holder.setText(R.id.myOrderItemEndTv, myOrder.getEndPos());

        holder.setText(R.id.myOrderItemStartTv, myOrder.getStart_provinces()+myOrder.getStart_poss()+myOrder.getStart_areas());
        holder.setText(R.id.myOrderItemEndTv, myOrder.getEnd_provinces()+myOrder.getEnd_poss()+myOrder.getEnd_areas());

        holder.setText(R.id.myOrderItemProductNameTv, "货名：" + myOrder.getProductName());

        //View dotViewdriver = holder.getView(R.id.myOrderItemDriverTv);




        if(TextUtils.isEmpty(myOrder.getTruck()))
        {
            holder.setText(R.id.myOrderItemTruckTv, "车型：无车型需求");
        }
        else
        {
            holder.setText(R.id.myOrderItemTruckTv, "车型：" + myOrder.getTruck());
        }


        //holder.setText(R.id.myOrderItemPropertyTv, myOrder.getWeight() + "吨/" + myOrder.getArea() + "方/" + myOrder.getNum() + "件");
        String dundun="0";String fangfang="0";String jianjian="0";
        if(TextUtils.isEmpty(myOrder.getWeight())||myOrder.getWeight().length()==0)
        {
            dundun="0";
        }
        else
        {
            dundun=myOrder.getWeight();
        }
        if(TextUtils.isEmpty(myOrder.getArea())||myOrder.getArea().length()==0)
        {
            fangfang="0";
        }
        else
        {
            fangfang=myOrder.getArea();
        }
        if(TextUtils.isEmpty(myOrder.getNum())||myOrder.getNum().length()==0)
        {
            jianjian="0";
        }
        else
        {
            jianjian=myOrder.getNum();
        }


        if(dundun.equals("0") && fangfang.equals("0") && jianjian.equals("0"))
        {
            holder.setText(R.id.myOrderItemPropertyTv, "整车");
        }
        else
        {
            holder.setText(R.id.myOrderItemPropertyTv, dundun+"吨/"+fangfang+"方/"+jianjian+"件");
        }
//        TextView driverTv = holder.getView(R.id.myOrderItemDriverTv);
//
//            try {
//                driverTv.setText(URLDecoder.decode(myOrder.getDriver(),"UTF-8"));
//                isEmpty(myOrder.getDriver());
//                if (TextUtils.isEmpty(myOrder.getDriver()) || myOrder.getDriver().equals("") ) {
//
//                    driverTv.setVisibility(View.GONE);
//                } else {
//                    driverTv.setVisibility(View.VISIBLE);
//                }
//
//                } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }


        holder.setText(R.id.myOrderItemDateTv, myOrder.getDate());

        holder.setText(R.id.kilo, myOrder.getKilo()+" 公里");

        holder.setText(R.id.freight, myOrder.getFreight()+" 元");

        holder.setText(R.id.start_descss, myOrder.getStart_descss());

        holder.setText(R.id.end_descss, myOrder.getEnd_descss());

       // TextView hitcishu = holder.getView(R.id.cishuTv);
       // hitcishu.setText("查看次数："+String.valueOf(myOrder.getHit()));




        //ImageView statusIv = holder.getView(R.id.myOrderItemStatusIv);



       // View dotView = holder.getView(R.id.cishuTv);

      //  View dotView = holder.getView(R.id.isjiedanTv);

        if (mType.equals("0")) {//未接单

            holder.setText(R.id.isjiedanTv, "未接单");
//            statusIv.setImageResource(R.drawable.ic_order_default);
          //dotView.setVisibility(View.VISIBLE);
//           dotViewdriver.setVisibility(View.GONE);
        } else if (mType.equals("1")) {//已接单
            holder.setText(R.id.isjiedanTv, "已接单");
//            statusIv.setImageResource(R.drawable.ic_order_received);
//            dotView.setVisibility(View.GONE);
//            dotViewdriver.setVisibility(View.VISIBLE);
        } else if (mType.equals("2")) {//运输中
            holder.setText(R.id.isjiedanTv, "运输中");
//            statusIv.setImageResource(R.drawable.ic_order_transport);
//           dotView.setVisibility(View.GONE);
//            dotViewdriver.setVisibility(View.VISIBLE);
        } else if (mType.equals("3")) {//已完成
            holder.setText(R.id.isjiedanTv, "已完成");
//            statusIv.setImageResource(R.drawable.ic_order_finished);
//           dotView.setVisibility(View.GONE);
//            dotViewdriver.setVisibility(View.VISIBLE);
        }
    }
}
