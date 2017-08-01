package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewHolder;
import com.pikachu.convenientdelivery.databinding.ItemRecipientInfoBinding;
import com.pikachu.convenientdelivery.model.RecipientInfo;

/**
 * RecipientInfoAdapter
 */

public class RecipientInfoAdapter extends BaseRecyclerViewAdapter<RecipientInfo> {

    private Context context;

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        return new ViewHolder(parent, R.layout.item_recipient_info);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<RecipientInfo, ItemRecipientInfoBinding> implements View.OnClickListener {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        private int position;

        @Override
        public void onBindViewHolder(final RecipientInfo recipientInfo, final int position) {
            if (recipientInfo != null) {
                itemView.setTag(recipientInfo);
                itemView.setId(position);
                binding.name.setText(recipientInfo.getName());
                binding.phone.setText(recipientInfo.getPhone());
                binding.address.setText(recipientInfo.getAddress());
                itemView.setOnClickListener(this);
                binding.edit.setOnClickListener(this);
                binding.delete.setOnClickListener(this);
                this.position = position;
            }
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onClick(v, (RecipientInfo) v.getTag(), position);
            }
        }
    }
}
