package com.pikachu.convenientdelivery.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.pikachu.convenientdelivery.IM.ui.ChatActivity;
import com.pikachu.convenientdelivery.IM.ui.UserInfoActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.model.User;
import com.pikachu.convenientdelivery.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by JinBo on 2017/7/28.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder>{
    private List<User> users = new ArrayList<>();
    private Context context;
    public SearchUserAdapter(List<User> users,Context context) {
        this.users = users;
        this.context = context;
    }

    public void setDatas(List<User> list) {
        users.clear();
        if (null != list) {
            users.addAll(list);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        Button btn_add;
        TextView name;

        public ViewHolder(View View) {
            super(View);
            avatar = (ImageView) View.findViewById(R.id.avatar);
            btn_add = (Button) View.findViewById(R.id.btn_add);
            name = (TextView) View.findViewById(R.id.name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看个人详情
                int positon = holder.getAdapterPosition();
                User user = users.get(positon);
                Bundle bundle = new Bundle();
                bundle.putSerializable("u", user);
                Log.e("123", bundle.toString());
                UserInfoActivity.actionstart(context,bundle);
               //startActivity(UserInfoActivity.class,bundle);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        if(user.getAvatar()!=null){
            ImageLoader.withHolder(context,user.getAvatar().getUrl(),holder.avatar);
        }
        holder.name.setText(user.getNick());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public User getItem(int position){
        return users.get(position);
    }

}
