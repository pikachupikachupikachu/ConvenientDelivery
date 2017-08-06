package com.pikachu.convenientdelivery.courier.child;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.PageAdapter;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentCourierOrderBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends BaseFragment<FragmentCourierOrderBinding> {
    private static final String TAG = "order_fragment";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private OrderFinishFragment orderFinishFragment;
    private OrderMoneyFragment orderMoneyFragment;
    private OrderOnFragment orderOnFragment;
    private PageAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public static OrderFragment newInstance(String args) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, args);
        fragment.setArguments(bundle);
        return fragment;
    }

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_courier_order;
    }

    @Override
    protected void init() {
        super.init();
        initViews();
    }

    private void initViews() {
        viewPager = bindingView.viewPager;
        tabLayout = bindingView.tablayout;
        orderFinishFragment = new OrderFinishFragment();
        orderMoneyFragment = new OrderMoneyFragment();
        orderOnFragment = new OrderOnFragment();
        fragments.add(orderOnFragment);
        fragments.add(orderMoneyFragment);
        fragments.add(orderFinishFragment);
        titles.add("当前任务");
        titles.add("未结账");
        titles.add("已完成");
        adapter = new PageAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
