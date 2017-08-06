package com.pikachu.convenientdelivery.order.child;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.OrderAdapter;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.databinding.FragmentOrderReleasedBinding;
import com.pikachu.convenientdelivery.model.Order;
import com.pikachu.convenientdelivery.model.User;
import com.pikachu.convenientdelivery.order.OrderDetailActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;

/**
 * OrderReleasedFragment
 */

public class OrderReleasedFragment extends BaseFragment<FragmentOrderReleasedBinding> implements SwipeRefreshLayout.OnRefreshListener, BaseRecyclerViewAdapter.OnItemClickListener<Order> {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private OrderAdapter adapter;

    private List<Order> orderList = new ArrayList<>();

    public static final int ORDER_DETAIL = 1;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_released;
    }

    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        recyclerView = bindingView.recyclerView;
        adapter = new OrderAdapter();
        adapter.setOnItemClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        BmobQuery<Order> query = new BmobQuery<>();
        query.include("recipient,shipper");
        query.addWhereEqualTo("recipient", BmobUser.getCurrentUser(User.class));
        query.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    orderList.clear();
                    for (Order order : list) {
                        orderList.add(order);
                    }
                    adapter.set(orderList);
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        BmobQuery<Order> query = new BmobQuery<>();
        query.include("recipient,shipper");
        query.addWhereEqualTo("recipient", BmobUser.getCurrentUser(User.class));
        query.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    orderList.clear();
                    for (Order order : list) {
                        orderList.add(order);
                    }
                    adapter.set(orderList);
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View view, Order order, int position) {
        switch (view.getId()) {
            default:
                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra("order", (Parcelable) order);
                startActivityForResult(intent, ORDER_DETAIL);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
        switch (requestCode) {
            case ORDER_DETAIL:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
    }
}
