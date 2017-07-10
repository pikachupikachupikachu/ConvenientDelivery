package com.pikachu.convenientdelivery.base.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected OnItemClickListener<T> listener;
    private List<T> dataList = new ArrayList<>();

    public void addAll(List<T> data) {
        this.dataList.addAll(data);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.onBaseBindViewHolder(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener<T> {
        void onClick(T t, int position);
    }

}
