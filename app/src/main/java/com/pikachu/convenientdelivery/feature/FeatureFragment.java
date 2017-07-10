package com.pikachu.convenientdelivery.feature;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pikachu.convenientdelivery.ChooseAreaActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentFeatureBinding;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

/**
 * 精选
 */

public class FeatureFragment extends BaseFragment<FragmentFeatureBinding> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, SearchView.OnQueryTextListener {

    public static final String ARGUMENT = "argument";

    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Banner banner;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private List<String> urlList = new ArrayList<>();

    public static FeatureFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        FeatureFragment featureFragment = new FeatureFragment();
        featureFragment.setArguments(bundle);
        return featureFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        initView();
    }

    @Override
    public int setContent() {
        return R.layout.fragment_feature;
    }

    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        toolbar = bindingView.toolbar;
        toolbar.inflateMenu(R.menu.toolbar_feature);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = bindingView.toolbarTitle;
        toolbarTitle.setOnClickListener(this);
        urlList.clear();
        urlList.add("http://image.qjwb.com.cn/group1/M00/01/1B/CggkA1fDfVSAbV7jABxuw-Jc4KE779.jpg");
        urlList.add("http://www.getdigsy.com/blog/wp-content/uploads/2016/12/industrial-hall-1630740_1280.jpg");
        urlList.add("https://www.leadbook.com/wp-content/uploads/2017/02/our-data1.jpeg");
        banner = bindingView.banner;
        banner.setImages(urlList).setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        }).start();
        recyclerView = bindingView.recyclerView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_feature, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(toolbar.getMenu().findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title:
                ChooseAreaActivity.start(getActivity());
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

}
