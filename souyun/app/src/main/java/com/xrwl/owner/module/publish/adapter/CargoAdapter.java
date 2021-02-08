package com.xrwl.owner.module.publish.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xrwl.owner.R;
import com.xrwl.owner.module.publish.bean.CargoBean;

import java.util.List;

/**
 * Created by www.longdw.com on 2018/7/11 下午7:50.
 */
public class CargoAdapter extends RecyclerView.Adapter<CargoAdapter.ViewHolder> {

    Context mContext;
    List<CargoBean> datas;
    int type;

    public CargoAdapter(Context context, List<CargoBean> datas,int type) {
        mContext = context;
        this.datas = datas;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.cargo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder == null) return;
        final CargoBean data = datas.get(position);
        if (data == null) return;

        if(type == 0){
            if(id == data.getOneType().getId()){
                holder.tv_name.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
                holder.tv_name.setTextColor(ContextCompat.getColor(mContext,R.color.color_FA9211));
            }else {
                holder.tv_name.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_F9F7FA));
                holder.tv_name.setTextColor(ContextCompat.getColor(mContext,R.color.window_text_color));
            }

            holder.tv_name.setText(data.getOneType().getName());
        }else{
            holder.tv_name.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
            holder.tv_name.setTextColor(ContextCompat.getColor(mContext,R.color.window_text_color));

            holder.tv_name.setText(data.getName());
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setData(List<CargoBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    int id;
    public void setId(int id){
        this.id = id;
        notifyDataSetChanged();
    }

    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


}
