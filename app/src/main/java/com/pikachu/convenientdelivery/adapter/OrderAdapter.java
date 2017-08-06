package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewHolder;
import com.pikachu.convenientdelivery.databinding.ItemOrderBinding;
import com.pikachu.convenientdelivery.model.Order;
import com.pikachu.convenientdelivery.model.User;

import cn.bmob.v3.BmobUser;

/**
 * OrderAdapter
 */

public class OrderAdapter extends BaseRecyclerViewAdapter<Order> {

    private Context context;

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

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

        private TextView name;
        private TextView phone;
        private TextView address;
        private TextView goodsName;
        private TextView goodsDetail;
        private TextView purchasingAddress;
        private TextView reward;
        private TextView deadline;
        private LinearLayout llButton;
        private Button button1;
        private Button button2;

        private Order order;
        private int position;

        @Override
        public void onBindViewHolder(final Order order, final int position) {
            if (order != null) {
                name = binding.name;
                name.setText(order.getRecipientInfo().getName());
                phone = binding.phone;
                address = binding.address;
                address = binding.address;
                goodsName = binding.goodsName;
                goodsName.setText(order.getGoodsName());
                goodsDetail = binding.goodsDetail;
                goodsDetail.setText(order.getGoodsDetail());
                purchasingAddress = binding.purchasingAddress;
                purchasingAddress.setText(order.getPurchasingAddress());
                reward = binding.reward;
                reward.setText("赏" + order.getReward() + (order.getRewardDefault() ? "元": "%"));
                deadline = binding.deadline;
                deadline.setText(order.getDeadline() + "前");
                llButton = binding.llButton;
                button1 = binding.button1;
                button2 = binding.button2;
                if (order.getRecipient() != null && order.getRecipient().getObjectId() != null && order.getRecipient().getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                    phone.setText(order.getRecipientInfo().getPhone());
                    address.setText(order.getRecipientInfo().getAddress());
                    llButton.setVisibility(View.GONE);
                } else if (order.getShipper() != null && order.getShipper().getObjectId() != null && order.getShipper().getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                    phone.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    button1.setOnClickListener(this);
                    button1.setText("发消息");
                    button2.setOnClickListener(this);
                    button2.setText("给快递");
                } else {
                    phone.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    button1.setOnClickListener(this);
                    button1.setText("发消息");
                    button2.setOnClickListener(this);
                    button2.setText("我要帮忙");
                }
                this.order = order;
                itemView.setOnClickListener(this);
                this.position = position;
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v, order, position);
            }
        }
    }
}
