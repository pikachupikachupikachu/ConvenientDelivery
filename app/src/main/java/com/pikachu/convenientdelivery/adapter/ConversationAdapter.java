package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewHolder;
import com.pikachu.convenientdelivery.databinding.ItemConversationBinding;
import com.pikachu.convenientdelivery.db.Conversation;

import java.util.List;

/**
 * ConversationAdapter
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private List<Conversation> conversationList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_recent_name;
        TextView tv_recent_msg;
        TextView tv_recent_time;
        TextView tv_recent_unread;

        public ViewHolder(View view) {
            super(view);
            tv_recent_name = (TextView) view.findViewById(R.id.tv_recent_name);
            tv_recent_msg = (TextView) view.findViewById(R.id.tv_recent_msg);
            tv_recent_time = (TextView) view.findViewById(R.id.tv_recent_time);
            tv_recent_unread = (TextView) view.findViewById(R.id.tv_recent_unread);
        }
    }
    public ConversationAdapter(List<Conversation> conversationList) {
        this.conversationList = conversationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);
        holder.tv_recent_name.setText(conversation.getTv_recent_name());
        holder.tv_recent_msg.setText(conversation.getTv_recent_msg());
        holder.tv_recent_time.setText(conversation.getTv_recent_time());
        holder.tv_recent_unread.setText(conversation.getTv_recent_unread());
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }


}

