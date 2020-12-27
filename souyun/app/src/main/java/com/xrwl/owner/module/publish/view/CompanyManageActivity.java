package com.xrwl.owner.module.publish.view;

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
import com.ldw.library.bean.BaseEntity;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.xrwl.owner.R;
import com.xrwl.owner.base.BaseActivity;
import com.xrwl.owner.module.publish.bean.CompanyManageBean;
import com.xrwl.owner.module.publish.mvp.CompanyContract;
import com.xrwl.owner.module.publish.mvp.CompanyPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class CompanyManageActivity extends BaseActivity<CompanyContract.IView, CompanyPresenter> implements CompanyContract.IView,View.OnClickListener {

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
    @BindView(R.id.sel_address)
    TextView sel_address;
    @BindView(R.id.et_address_des)
    EditText et_address_des;
    @BindView(R.id.btn_send)
    TextView btn_send;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CityPicker mCP;

    private CompanyManageAdapter mAdapter;

    boolean isAdd;

    String province,city , district;

    List<CompanyManageBean> list;

    @Override
    protected CompanyPresenter initPresenter() {
        return new CompanyPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_company_manage2;
    }

    @Override
    protected void initViews() {
        tv_add.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        sel_address.setOnClickListener(this);

        cl_add.setVisibility(View.GONE);
        cl_list.setVisibility(View.VISIBLE);

        initCityPicher();
        initRecycler();

        mPresenter.getData();
    }

    private void initCityPicher() {
        mCP = new CityPicker.Builder(mContext)
                .textSize(20)
                //地址选择
                .title("地址选择")
                .backgroundPop(0xa0000000)
                //文字的颜色
                .titleBackgroundColor("#0CB6CA")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                //滑轮文字的颜色
                .textColor(R.color.colorPrimary)
                //省滑轮是否循环显示
                .provinceCyclic(true)
                //市滑轮是否循环显示
                .cityCyclic(false)
                //地区（县）滑轮是否循环显示
                .districtCyclic(false)
                //滑轮显示的item个数
                .visibleItemsCount(7)
                //滑轮item间距
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();

        //监听
        mCP.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省
                province = citySelected[0];
                //市
                city = citySelected[1];
                //区。县。（两级联动，必须返回空）
                district = citySelected[2];
                //邮证编码
                String code = citySelected[3];
                if(sel_address != null)
                    sel_address.setText(province + city + district);
            }

            @Override
            public void onCancel() {


            }
        });
    }



    //初始化recyclerview
    public void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CompanyManageAdapter(mContext, new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            mListener.onProductSelect(list.get(position).getName());
            finish();
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add:
                setView(isAdd = !isAdd);
                break;
            case R.id.btn_send:
                if(TextUtils.isEmpty(et_name.getText().toString())){
                    ToastUtils.showShort("请输入单位名称");
                    return;
                }

                Map<String,String> params = new HashMap<String,String>();
                params.put("danweidezhi",et_name.getText().toString());
                params.put("sheng",province);
                params.put("shi",city);
                params.put("xian",district);
                params.put("xiangxidezhi",et_address_des.getText().toString());
                params.put("lianxidianhua",et_phone.getText().toString());
                mPresenter.addData(params);
                break;
            case R.id.sel_address:
                mCP.show();
                break;
        }
    }

    public void setView(boolean isAdd){
        cl_add.setVisibility(isAdd?View.VISIBLE:View.GONE);
        cl_list.setVisibility(isAdd?View.GONE:View.VISIBLE);
        tv_add.setText(isAdd?"返回":"添加");
    }

    @Override
    public void onRefreshSuccess(BaseEntity<List<CompanyManageBean>> entity) {

    }

    @Override
    public void onRefreshError(Throwable e) {

    }

    @Override
    public void onError(BaseEntity entity) {

    }


    @Override
    public void addDataSuccess(BaseEntity entity) {
        mPresenter.getData();
        setView(isAdd = false);
        showToast("添加成功");

        et_name.setText("");
        et_address_des.setText("");
        sel_address.setText("");
        et_phone.setText("");
    }

    @Override
    public void addDataError(BaseEntity entity) {
        showToast(entity.getMsg());
    }

    @Override
    public void getDataSuccess(BaseEntity<List<CompanyManageBean>> datas) {
        list = new ArrayList<>();
        for (int i =0;i<10;i++){
            CompanyManageBean item = new CompanyManageBean();
            item.setName("" + i);
            list.add(item);
        }
        mAdapter.setData(list);
    }

    @Override
    public void getDataError(BaseEntity entity) {
        showToast(entity.getMsg());
    }

    private static class CompanyManageAdapter extends RecyclerView.Adapter<CompanyManageAdapter.ViewHolder> {

        Context mContext;
        List<CompanyManageBean> datas;

        public CompanyManageAdapter(Context context, List<CompanyManageBean> datas) {
            mContext = context;
            this.datas = datas;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(mContext).inflate(R.layout.item_company_manage, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (holder == null) return;
            final CompanyManageBean data = datas.get(position);
            if (data == null) return;

            holder.tv_name.setText(data.getName());
            holder.tv_phone.setText(data.getPhone());
            holder.tv_address.setText(data.getAddress());
            holder.tv_address_des.setText(data.getAddressDes());

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> mOnItemClickListener.onClick(position));
            }

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void setData(List<CompanyManageBean> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_name,tv_phone,tv_address,tv_address_des;
            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_phone = itemView.findViewById(R.id.tv_phone);
                tv_address = itemView.findViewById(R.id.tv_address);
                tv_address_des = itemView.findViewById(R.id.tv_address_des);

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
