package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewHolder;
import com.pikachu.convenientdelivery.databinding.ItemOrderBinding;
import com.pikachu.convenientdelivery.model.Order;

/**
 * OrderAdapter
 */

public class OrderAdapter extends BaseRecyclerViewAdapter<Order> {

    private Context context;

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        return new ViewHolder(parent, R.layout.item_order);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<Order, ItemOrderBinding> implements View.OnClickListener {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        private int position;

        @Override
        public void onBindViewHolder(final Order order, final int position) {
            if (order != null) {
                itemView.setTag(order);
                itemView.setId(position);
                binding.setOrder(order);
                itemView.setOnClickListener(this);
                this.position = position;
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v, (Order) v.getTag(), position);
            }
        }
    }
}
