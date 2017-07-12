package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewHolder;
import com.pikachu.convenientdelivery.databinding.ItemChatBinding;
import com.pikachu.convenientdelivery.db.Chat;

/**
 * ChatAdapter
 */

public class ChatAdapter extends BaseRecyclerViewAdapter<Chat> {

    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        return new ViewHolder(parent, R.layout.item_chat);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<Chat, ItemChatBinding> {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(Chat chat, int position) {
            if (chat != null) {
                binding.setData(chat);
            }
        }

    }

}
