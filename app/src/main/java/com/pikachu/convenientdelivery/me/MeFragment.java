package com.pikachu.convenientdelivery.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentMeBinding;

/**
 * 我的
 */

public class MeFragment extends BaseFragment<FragmentMeBinding> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public static final String ARGUMENT = "argument";

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView headShot;
    private TextView name;
    private TextView whatsUp;
    private ImageButton detail;

    public static MeFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        MeFragment meFragment = new MeFragment();
        meFragment.setArguments(bundle);
        return meFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        initView();
    }

    @Override
    public int setContent() {
        return R.layout.fragment_me;
    }

    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        headShot = bindingView.headShot;
        name = bindingView.name;
        whatsUp = bindingView.whatsUp;
        detail = bindingView.detail;
        detail.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail:
                break;
        }
    }
}
