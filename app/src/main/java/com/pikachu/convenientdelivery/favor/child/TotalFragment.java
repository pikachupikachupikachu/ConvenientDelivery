package com.pikachu.convenientdelivery.favor.child;

import android.databinding.ObservableField;
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
import com.pikachu.convenientdelivery.databinding.FragmentTotalBinding;
import com.pikachu.convenientdelivery.model.Order;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 全部
 */

public class TotalFragment extends BaseFragment<FragmentTotalBinding> implements SwipeRefreshLayout.OnRefreshListener, BaseRecyclerViewAdapter.OnItemClickListener<Order> {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private OrderAdapter adapter;

    private List<Order> orderList = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_total;
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
        query.addWhereEqualTo("status", new ObservableField<>(Order.WAITING));
//        query.addWhereEqualTo("status", Order.WAITING);
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
            case R.id.send_message:
                break;
            case R.id.favor:
                break;
            default:
                break;
        }
    }
}
