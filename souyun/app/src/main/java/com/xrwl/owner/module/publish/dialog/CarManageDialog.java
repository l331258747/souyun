package com.xrwl.owner.module.publish.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ldw.library.utils.DisplayUtil;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.xrwl.owner.module.publish.bean.CarManageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CarManageDialog extends BasePopDialog implements View.OnClickListener {

    @BindView(R.id.tv_add)
    TextView tv_add;
    @BindView(R.id.cl_add)
    ConstraintLayout cl_add;
    @BindView(R.id.cl_list)
    ConstraintLayout cl_list;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_car_type)
    EditText et_car_type;
    @BindView(R.id.et_car_no)
    EditText et_car_no;
    @BindView(R.id.et_car_load)
    EditText et_car_load;
    @BindView(R.id.et_jiashiz)
    EditText et_jiashiz;
    @BindView(R.id.btn_send)
    TextView btn_send;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CarManageAdapter mAdapter;

    boolean isAdd;

    @Override
    protected int getLayoutId() {

        mHeight = DisplayUtil.dp2px(mContext, 360);

        return R.layout.layout_car_manage;
    }

    @Override
    protected void initView() {

        tv_add.setOnClickListener(this);
        btn_send.setOnClickListener(this);

        cl_add.setVisibility(View.GONE);
        cl_list.setVisibility(View.VISIBLE);


        initRecycler();
    }

    //初始化recyclerview
    public void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CarManageAdapter(mContext, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        getData();
    }

    private void getData() {
        List<CarManageBean> list = new ArrayList<>();
        for (int i =0;i<10;i++){
            CarManageBean item = new CarManageBean();
            item.setName("" + i);
            list.add(item);
        }
        mAdapter.setData(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add:
                isAdd = !isAdd;
                cl_add.setVisibility(isAdd?View.VISIBLE:View.GONE);
                cl_list.setVisibility(isAdd?View.GONE:View.VISIBLE);
                tv_add.setText(isAdd?"返回":"添加");
                break;
            case R.id.btn_send:
                if(TextUtils.isEmpty(et_name.getText().toString())){
                    ToastUtils.showShort("请输入车主名称");
                    return;
                }
                if(TextUtils.isEmpty(et_phone.getText().toString())){
                    ToastUtils.showShort("请输入联系电话");
                    return;
                }

                //TODO 请求 成功后

                break;
        }
    }

    private static class CarManageAdapter extends RecyclerView.Adapter<CarManageAdapter.ViewHolder> {

        Context mContext;
        List<CarManageBean> datas;

        public CarManageAdapter(Context context, List<CarManageBean> datas) {
            mContext = context;
            this.datas = datas;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(mContext).inflate(R.layout.item_car_manage, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (holder == null) return;
            final CarManageBean data = datas.get(position);
            if (data == null) return;

            holder.tv_name.setText(data.getName());
            holder.tv_phone.setText(data.getPhone());
            holder.tv_car_type.setText(data.getCarType());
            holder.tv_car_no.setText(data.getCarNo());
            holder.tv_car_load.setText(data.getCarLoad());
            holder.tv_jiashizheng.setText(data.getJiashizheng());

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> mOnItemClickListener.onClick(position));
            }

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void setData(List<CarManageBean> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_name,tv_phone,tv_car_type,tv_car_no,tv_car_load,tv_jiashizheng;
            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_phone = itemView.findViewById(R.id.tv_phone);
                tv_car_type = itemView.findViewById(R.id.tv_car_type);
                tv_car_no = itemView.findViewById(R.id.tv_car_no);
                tv_car_load = itemView.findViewById(R.id.tv_car_load);
                tv_jiashizheng = itemView.findViewById(R.id.tv_jiashizheng);
            }
        }

        OnItemClickListener mOnItemClickListener;

        public interface OnItemClickListener {
            void onClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }
    }

    private OnProductSelectListener mListener;

    public void setOnProductSelectListener(OnProductSelectListener l) {
        mListener = l;
    }

    public interface OnProductSelectListener {
        void onProductSelect(String name);
    }

}
