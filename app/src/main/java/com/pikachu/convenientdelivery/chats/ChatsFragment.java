package com.pikachu.convenientdelivery.chats;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.ChatAdapter;
import com.pikachu.convenientdelivery.adapter.FriendAdapter;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentChatsBinding;

/**
 * 消息
 */

public class ChatsFragment extends BaseFragment<FragmentChatsBinding> implements SwipeRefreshLayout.OnRefreshListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    public static final String ARGUMENT = "argument";

    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private RadioButton message;
    private RadioButton friend;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private ChatAdapter chatAdapter;
    private FriendAdapter friendAdapter;

    public static ChatsFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        ChatsFragment chatsFragment = new ChatsFragment();
        chatsFragment.setArguments(bundle);
        return chatsFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }


    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        toolbar = bindingView.toolbar;
        toolbar.inflateMenu(R.menu.toolbar_feature);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        radioGroup = bindingView.radioGroup;
        radioGroup.setOnCheckedChangeListener(this);
        message = bindingView.message;
        friend = bindingView.friend;
        recyclerView = bindingView.recyclerView;
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        chatAdapter = new ChatAdapter();
        friendAdapter = new FriendAdapter();
        recyclerView.setAdapter(chatAdapter);
        fab = bindingView.fab;
        fab.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.message:
                recyclerView.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();
                break;
            case R.id.friend:
                recyclerView.setAdapter(friendAdapter);
                friendAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chats;
    }
}
