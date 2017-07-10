package com.pikachu.convenientdelivery.base.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class BaseRecyclerViewHolder<T, D extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected D binding;

    public BaseRecyclerViewHolder(ViewGroup viewGroup, int layoutId) {
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), layoutId, viewGroup, false).getRoot());
        binding = DataBindingUtil.getBinding(this.itemView);
    }

    public abstract void onBindViewHolder(T object, final int position);

    void onBaseBindViewHolder(T object, final int position) {
        onBindViewHolder(object, position);
        binding.executePendingBindings();
    }

}