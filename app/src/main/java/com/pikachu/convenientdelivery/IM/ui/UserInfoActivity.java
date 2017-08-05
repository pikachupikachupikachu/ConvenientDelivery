package com.pikachu.convenientdelivery.IM.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;


import com.orhanobut.logger.Logger;
import com.pikachu.convenientdelivery.IM.ui.bean.AddFriendMessage;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityUserInfoBinding;
import com.pikachu.convenientdelivery.model.User;
import com.pikachu.convenientdelivery.util.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;



/**
 * Created by JinBo on 2017/7/29.
 */

public class UserInfoActivity extends BaseActivity<ActivityUserInfoBinding> implements View.OnClickListener {
    private ImageView iv_avator;
    private TextView  tv_name;
    private Toolbar toolbar;
    private Button btn_add_friend;
    private Button btn_chat;
    BmobIMUserInfo info;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void init() {
        initView();
    }



    private void initView() {
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("个人资料");
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("u");

        //user=(User)getBundle().getSerializable("u");

        iv_avator = bindingView.ivAvator;
        tv_name = bindingView.tvName;
        tv_name.setText(user.getNick());
        Log.e("123",user.getNick());
        if(user.getAvatar()!=null){
            ImageLoader.with(this,user.getAvatar().getUrl(),iv_avator);
        }
        btn_add_friend = bindingView.btnAddFriend;
        btn_chat = bindingView.btnChat;

        btn_add_friend.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        if(user.getAvatar() == null)
        {
            info = new BmobIMUserInfo(user.getObjectId(),user.getUsername(),"http://www.feizl.com/upload2007/2013_09/1309202131969410.jpg");
        }else{
            info = new BmobIMUserInfo(user.getObjectId(),user.getUsername(),user.getAvatar().getUrl());
        }
        Log.e("123",info.toString());

        //tv_name.setText(user.getUsername());
    }

    public static void actionstart(Context context, Bundle bundle) {
        Intent intent = new Intent(context,UserInfoActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_add_friend:
               sendAddFriendMessage();
               break;
           case R.id.btn_chat:
    /*          BmobIM.getInstance().updateUserInfo(info);
               BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,false,null);
               Bundle bundle = new Bundle();
               bundle.putSerializable("c", c);
               startActivity(ChatActivity.class, bundle);
               User user = BmobUser.getCurrentUser(User.class);
               Log.v("user name",user.getUsername());
               BmobIM.connect(user.getObjectId(), new ConnectListener() {
                   @Override
                   public void done(String uid, BmobException e) {
                       if (e == null) {
                           Logger.i("connect success");
                       } else {
                           Logger.e("connect error",e.getErrorCode() + "/" + e.getMessage());
                       }
                   }
               });
               */

     //         toast("buttonclick");
     //          chat();
              Intent intent = new Intent(this,ChatActivity.class);
             startActivity(intent);
                 break;
           default:
               break;
       }
    }
    private void chat() {
        //TODO 会话：4.1、创建一个常态会话入口，陌生人聊天
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("c", conversationEntrance);
        startActivity(ChatActivity.class, bundle, false);
    }

    private void sendAddFriendMessage() {
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
     //   BmobIM.getInstance().updateUserInfo(info);
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg =new AddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String,Object> map =new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
        map.put("avatar",currentUser.getAvatar());//发送者的头像
        map.put("uid",currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    showToast("好友请求发送成功，等待验证");
                } else {//发送失败
                    showToast("发送失败:" + e.getMessage());
                }
            }
        });
    }
}
