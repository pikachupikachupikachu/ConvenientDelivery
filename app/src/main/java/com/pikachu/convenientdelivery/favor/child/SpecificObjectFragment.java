package com.pikachu.convenientdelivery.favor.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.OrderAdapter;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.databinding.FragmentSpecificObjectBinding;
import com.pikachu.convenientdelivery.model.Order;

import java.util.List;

/**
 * 物品明确
 */

public class SpecificObjectFragment extends BaseFragment<FragmentSpecificObjectBinding> implements SwipeRefreshLayout.OnRefreshListener, BaseRecyclerViewAdapter.OnItemClickListener<Order> {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private OrderAdapter adapter;

    private List<Order> orderList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_specific_object;
    }

    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = bindingView.recyclerView;
        adapter = new OrderAdapter();
        adapter.setOnItemClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view, Order order, int position) {
        switch (view.getId()) {
            case R.id.send_message:
                break;
            case R.id.favor:
                break;
            default:
                break;
        }
    }
}
