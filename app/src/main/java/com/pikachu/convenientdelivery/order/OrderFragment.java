package com.pikachu.convenientdelivery.order;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pikachu.convenientdelivery.CityPickerActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.base.adapter.BaseFragmentPagerAdapter;
import com.pikachu.convenientdelivery.databinding.FragmentOrderBinding;
import com.pikachu.convenientdelivery.databinding.ToolbarPickCityBinding;
import com.pikachu.convenientdelivery.db.DBManager;
import com.pikachu.convenientdelivery.order.child.OrderReceivedFragment;
import com.pikachu.convenientdelivery.order.child.OrderReleasedFragment;

import java.util.ArrayList;

import static android.content.Context.SEARCH_SERVICE;

/**
 * OrderFragment
 */

public class OrderFragment extends BaseFragment<FragmentOrderBinding> implements View.OnClickListener, SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener {

    public static final String ARGUMENT = "argument";

    private Toolbar toolbar;
    private ToolbarPickCityBinding toolbarPickCityBinding;
    private TabLayout tabOrder;
    private ViewPager vpOrder;
    private SearchView searchView;
    private FloatingActionButton fab;

    private ArrayList<Fragment> fragmentList = new ArrayList<>(2);
    private ArrayList<String> titleList = new ArrayList<>(2);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void onResume() {
        super.onResume();
        Animation animationUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        fab.startAnimation(animationUp);
        fab.setVisibility(View.VISIBLE);
    }

    public static OrderFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        OrderFragment orderFragment = new OrderFragment();
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

    private void initView() {
        toolbar = bindingView.toolbar;
        toolbar.inflateMenu(R.menu.toolbar_search);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarPickCityBinding = bindingView.toolbarPickCity;
        toolbarPickCityBinding.pickCity.setOnClickListener(this);
        tabOrder = bindingView.tabOrder;
        vpOrder = bindingView.vpOrder;
        vpOrder.addOnPageChangeListener(this);
        fragmentList.clear();
        fragmentList.add(new OrderReleasedFragment());
        fragmentList.add(new OrderReceivedFragment());
        titleList.clear();
        titleList.add("我发布的订单");
        titleList.add("我接到的订单");
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        vpOrder.setAdapter(adapter);
        vpOrder.setOffscreenPageLimit(2);
        adapter.notifyDataSetChanged();
        tabOrder.setTabMode(TabLayout.MODE_FIXED);
        tabOrder.setupWithViewPager(vpOrder);
        fab = bindingView.fab;
        fab.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_search, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(toolbar.getMenu().findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                String city = DBManager.getPickedCity();
                toolbarPickCityBinding.pickedCity.setText(city);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_pick_city:
                Intent intent = new Intent(getActivity(), CityPickerActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.fab:
                OrderActivity.start(getContext());
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
