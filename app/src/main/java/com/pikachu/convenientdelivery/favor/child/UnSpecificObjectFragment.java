package com.pikachu.convenientdelivery.favor.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentUnspecificObjectBinding;

/**
 * 物品待定
 */

public class UnSpecificObjectFragment extends BaseFragment<FragmentUnspecificObjectBinding> implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }



    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = bindingView.recyclerView;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_unspecific_object;
    }
}
