package com.pikachu.convenientdelivery.favor;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.pikachu.convenientdelivery.CityPickerActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.base.adapter.BaseFragmentPagerAdapter;
import com.pikachu.convenientdelivery.databinding.FragmentFavorBinding;
import com.pikachu.convenientdelivery.databinding.ToolbarPickCityBinding;
import com.pikachu.convenientdelivery.db.DBManager;
import com.pikachu.convenientdelivery.favor.child.SpecificObjectFragment;
import com.pikachu.convenientdelivery.favor.child.SpecificSiteFragment;
import com.pikachu.convenientdelivery.favor.child.TotalFragment;
import com.pikachu.convenientdelivery.favor.child.UnSpecificObjectFragment;

import java.util.ArrayList;

import static android.content.Context.SEARCH_SERVICE;

/**
 * 帮忙
 */

public class FavorFragment extends BaseFragment<FragmentFavorBinding> implements View.OnClickListener, SearchView.OnQueryTextListener, ViewPager.OnPageChangeListener {

    public static final String ARGUMENT = "argument";

    private Toolbar toolbar;
    private ToolbarPickCityBinding toolbarPickCityBinding;
    private TabLayout tabFavor;
    private ViewPager vpFavor;
    private SearchView searchView;

    private ArrayList<Fragment> fragmentList = new ArrayList<>(4);
    private ArrayList<String> titleList = new ArrayList<>(4);

    public static FavorFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        FavorFragment favorFragment = new FavorFragment();
        favorFragment.setArguments(bundle);
        return favorFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favor;
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
        tabFavor = bindingView.tabFavor;
        vpFavor = bindingView.vpFavor;
        vpFavor.addOnPageChangeListener(this);
        fragmentList.clear();
        fragmentList.add(new TotalFragment());
        fragmentList.add(new SpecificObjectFragment());
        fragmentList.add(new SpecificSiteFragment());
        fragmentList.add(new UnSpecificObjectFragment());
        titleList.clear();
        titleList.add("全部");
        titleList.add("物品明确");
        titleList.add("地点明确");
        titleList.add("物品待定");
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        vpFavor.setAdapter(adapter);
        vpFavor.setOffscreenPageLimit(2);
        adapter.notifyDataSetChanged();
        tabFavor.setTabMode(TabLayout.MODE_FIXED);
        tabFavor.setupWithViewPager(vpFavor);
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
