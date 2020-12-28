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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ldw.library.bean.BaseEntity;
import com.ldw.library.utils.DisplayUtil;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BasePopDialog;
import com.xrwl.owner.module.publish.bean.CarManageBean;
import com.xrwl.owner.module.publish.bean.CarManageSearchBean;
import com.xrwl.owner.module.publish.mvp.CarContract;
import com.xrwl.owner.module.publish.mvp.CarPresenter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CarManageDialog extends BasePopDialog implements View.OnClickListener, CarContract.IView {

    @BindView(R.id.tv_add)
    TextView tv_add;
    @BindView(R.id.cl_add)
    ConstraintLayout cl_add;
    @BindView(R.id.cl_list)
    ConstraintLayout cl_list;


    @BindView(R.id.recycler_view2)
    RecyclerView recyclerView2;
    @BindView(R.id.search_sp)
    Spinner search_sp;
    @BindView(R.id.search_et)
    EditText search_et;
    @BindView(R.id.search_tv)
    TextView search_tv;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CarManageAdapter mAdapter;
    private CarManageAdapter2 mAdapter2;

    boolean isAdd;

    CarPresenter mPresenter;
    List<CarManageBean> list;
    List<CarManageSearchBean> list2;

    int type;

    @Override
    protected int getLayoutId() {

        mHeight = DisplayUtil.dp2px(mContext, 360);

        return R.layout.layout_car_manage;
    }

    @Override
    protected void initView() {

        search_sp.setAdapter(new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
                new String[]{ "司机姓名","司机电话","车牌号"}));
        search_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tv_add.setOnClickListener(this);
        search_tv.setOnClickListener(this);

        cl_add.setVisibility(View.GONE);
        cl_list.setVisibility(View.VISIBLE);

        initRecycler();
        initRecycler2();

        mPresenter = new CarPresenter(mContext);
        mPresenter.attach(this);

        mPresenter.getData();
    }

    //初始化recyclerview
    public void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CarManageAdapter(mContext, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            if(mListener == null) return;
            mListener.onProductSelect(list.get(position).getChezhumingcheng());
            dismiss();
        });

    }

    //初始化recyclerview
    public void initRecycler2() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView2.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView2.setLayoutManager(linearLayoutManager);
        mAdapter2 = new CarManageAdapter2(mContext, new ArrayList<>());
        recyclerView2.setAdapter(mAdapter2);

        mAdapter2.setOnItemClickListener(position -> {

            CarManageSearchBean item = list2.get(position);
            Map<String,String> params = new HashMap<String,String>();
            params.put("danweiid", item.getUserId());
            params.put("username", item.getUsername());
            params.put("chezhumingcheng", item.getUsername());
            params.put("chezhudianhua", item.getPhone());
            params.put("huocheleixing", item.getRenzhengchexing());

            mPresenter.addData(params);

        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add:
                setView(isAdd = !isAdd);
                break;
            case R.id.search_tv:
                if(TextUtils.isEmpty(search_et.getText().toString())){
                    ToastUtils.showShort("请输入");
                    return;
                }

                Map<String,String> params = new HashMap<String,String>();
                if(type == 0){
                    params.put("name",search_et.getText().toString());
                }else if(type == 1){
                    params.put("tel",search_et.getText().toString());
                }else if(type == 2){
                    params.put("chepaihao",search_et.getText().toString());
                }

                mPresenter.searchData(params);

                break;
        }
    }

    public void setView(boolean isAdd){
        cl_add.setVisibility(isAdd?View.VISIBLE:View.GONE);
        cl_list.setVisibility(isAdd?View.GONE:View.VISIBLE);
        tv_add.setText(isAdd?"返回":"添加");
    }

    @Override
    public void addDataSuccess(BaseEntity entity) {
        mPresenter.getData();
        setView(isAdd = false);
        ToastUtils.showShort("添加成功");

        search_et.setText("");
        list2 = new ArrayList<>();
        mAdapter2.setData(list2);
    }

    @Override
    public void addDataError(BaseEntity entity) {
        ToastUtils.showShort(entity.getMsg());
    }

    @Override
    public void getDataSuccess(BaseEntity<List<CarManageBean>> datas) {
        if(datas == null || datas.getData() == null)  list = new ArrayList<>();
        else list = datas.getData();
        mAdapter.setData(list);
    }

    @Override
    public void getDataError(BaseEntity entity) {
        ToastUtils.showShort(entity.getMsg());
    }

    @Override
    public void searchDataSuccess(BaseEntity<List<CarManageSearchBean>> datas) {
        if(datas == null || datas.getData() == null)  list2 = new ArrayList<>();
        else list2 = datas.getData();
        if(list2.size() == 0)
            ToastUtils.showShort("没有数据");
        mAdapter2.setData(list2);
    }

    @Override
    public void searchDataError(BaseEntity entity) {
        ToastUtils.showShort(entity.getMsg());
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<CarManageBean>> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

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

            try {
                holder.tv_name.setText(URLDecoder.decode(data.getChezhumingcheng(), "UTF-8"));
                holder.tv_phone.setText(URLDecoder.decode(data.getChezhudianhua(), "UTF-8"));
                holder.tv_car_type.setText(URLDecoder.decode(data.getHuocheleixing(), "UTF-8"));
                holder.tv_car_no.setText(URLDecoder.decode(data.getGuachepaihao(), "UTF-8"));
                holder.tv_car_load.setText(URLDecoder.decode(data.getCheliangzaizhong(), "UTF-8"));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            });
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
            TextView tv_name,tv_phone,tv_car_type,tv_car_no,tv_car_load;
            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_phone = itemView.findViewById(R.id.tv_phone);
                tv_car_type = itemView.findViewById(R.id.tv_car_type);
                tv_car_no = itemView.findViewById(R.id.tv_car_no);
                tv_car_load = itemView.findViewById(R.id.tv_car_load);
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


    private static class CarManageAdapter2 extends RecyclerView.Adapter<CarManageAdapter2.ViewHolder> {

        Context mContext;
        List<CarManageSearchBean> datas;

        public CarManageAdapter2(Context context, List<CarManageSearchBean> datas) {
            mContext = context;
            this.datas = datas;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(mContext).inflate(R.layout.item_car_manage2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (holder == null) return;
            final CarManageSearchBean data = datas.get(position);
            if (data == null) return;

            try {
                holder.tv_name.setText(URLDecoder.decode(data.getUsername(), "UTF-8"));
                holder.tv_phone.setText(URLDecoder.decode(data.getPhone(), "UTF-8"));
                holder.tv_car_type.setText(URLDecoder.decode(data.getRenzhengchexing(), "UTF-8"));
                holder.tv_car_no.setText("");
                holder.tv_car_load.setText("");
                holder.tv_status.setText(data.getRenzheng().equals("1")?"已认证":"未认证");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            holder.tv_add.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onAddClick(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void setData(List<CarManageSearchBean> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_name,tv_phone,tv_car_type,tv_car_no,tv_car_load,tv_add,tv_status;
            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_phone = itemView.findViewById(R.id.tv_phone);
                tv_car_type = itemView.findViewById(R.id.tv_car_type);
                tv_car_no = itemView.findViewById(R.id.tv_car_no);
                tv_car_load = itemView.findViewById(R.id.tv_car_load);
                tv_add = itemView.findViewById(R.id.tv_add);
                tv_status = itemView.findViewById(R.id.tv_status);
            }
        }

        OnItemClickListener mOnItemClickListener;

        public interface OnItemClickListener {
            void onAddClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }
    }
}
