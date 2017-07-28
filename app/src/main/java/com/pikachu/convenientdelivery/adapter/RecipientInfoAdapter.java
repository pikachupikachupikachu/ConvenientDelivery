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

    private class ViewHolder extends BaseRecyclerViewHolder<RecipientInfo, ItemRecipientInfoBinding> {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(final RecipientInfo recipientInfo, final int position) {
            if (recipientInfo != null) {
                binding.setData(recipientInfo);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClick(v, recipientInfo, position);
                        }
                    }
                });
            }
        }
    }
}
