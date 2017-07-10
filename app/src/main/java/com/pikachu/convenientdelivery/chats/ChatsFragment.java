package com.pikachu.convenientdelivery.chats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentChatsBinding;

/**
 * 消息
 */

public class ChatsFragment extends BaseFragment<FragmentChatsBinding> implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ARGUMENT = "argument";

    private SwipeRefreshLayout swipeRefreshLayout;

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
        showContentView();
        initView();
    }

    @Override
    public int setContent() {
        return R.layout.fragment_chats;
    }

    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

}
