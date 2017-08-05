package com.pikachu.convenientdelivery.chats;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pikachu.convenientdelivery.IM.ui.ChatActivity;
import com.pikachu.convenientdelivery.IM.ui.SearchUserActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.ContactAdapter;
import com.pikachu.convenientdelivery.adapter.ConversationAdapter;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.ActivityLoginBinding;
import com.pikachu.convenientdelivery.databinding.FragmentChatsBinding;
import com.pikachu.convenientdelivery.db.Contact;
import com.pikachu.convenientdelivery.db.Conversation;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

/**
 * 消息
 */

public class ChatsFragment extends BaseFragment<FragmentChatsBinding> implements SwipeRefreshLayout.OnRefreshListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    public static final String ARGUMENT = "argument";
    private List<Conversation> conversationList =new ArrayList<>();
    private List<Contact> contactList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private RadioButton message;
    private RadioButton friend;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private MenuItem search;

    private ConversationAdapter conversationAdapter;
    private ContactAdapter contactAdapter;

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

    @Override
    public void onResume() {
        super.onResume();
        Animation animationUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        fab.startAnimation(animationUp);
        fab.setVisibility(View.VISIBLE);
    }

    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        toolbar = bindingView.toolbar;
        toolbar.inflateMenu(R.menu.tb_search);
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

        conversationAdapter = new ConversationAdapter(conversationList);
        contactAdapter = new ContactAdapter();
        initConversation();
        initContact();
        recyclerView.setAdapter(conversationAdapter);
        fab = bindingView.fab;
        fab.setOnClickListener(this);
    }

    private void initContact() {
        Contact c = new Contact("Howie",R.mipmap.head);
        contactList.add(c);
        contactAdapter.set(contactList);
    }

    private void initConversation() {
        Conversation c = new Conversation("Howie","Hello,guys","15:00","1");
        conversationList.add(c);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.message:
                recyclerView.setAdapter(conversationAdapter);
                conversationAdapter.notifyDataSetChanged();
                break;
            case R.id.friend:
                recyclerView.setAdapter(contactAdapter);
                contactAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                break;
            case R.id.action_search:

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chats;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.tb_search, menu);
        search = (MenuItem) MenuItemCompat.getActionView(toolbar.getMenu().findItem(R.id.action_search));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                jumpTo(SearchUserActivity.class, false);
                break;
        }
        return  true;
    }



}
