package com.pikachu.convenientdelivery.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected OnItemClickListener<T> listener;
    private List<T> dataList = new ArrayList<>();

    public List<T> get() {
        return dataList;
    }

    public void set(List<T> data) {
        dataList = data;
    }

    public void add(T t) {
        dataList.add(t);
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
        void onClick(View view, T t, int position);
    }

}
