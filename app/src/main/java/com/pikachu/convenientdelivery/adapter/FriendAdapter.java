package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewHolder;
import com.pikachu.convenientdelivery.databinding.ItemContactBinding;
import com.pikachu.convenientdelivery.db.Friend;

/**
 * FriendAdapter
 */

public class FriendAdapter extends BaseRecyclerViewAdapter<Friend> {

    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        return new ViewHolder(parent, R.layout.item_contact);
    }

    private class ViewHolder extends BaseRecyclerViewHolder<Friend, ItemContactBinding> {

        ViewHolder(ViewGroup parent, int layout) {
            super(parent, layout);
        }

        @Override
        public void onBindViewHolder(Friend friend, int position) {
            if (friend != null) {
                binding.setData(friend);
            }
        }

    }

}
