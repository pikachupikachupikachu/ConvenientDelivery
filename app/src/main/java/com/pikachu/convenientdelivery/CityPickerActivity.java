package com.pikachu.convenientdelivery;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.pikachu.convenientdelivery.adapter.CityListAdapter;
import com.pikachu.convenientdelivery.adapter.CityResultListAdapter;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityCityPickerBinding;
import com.pikachu.convenientdelivery.databinding.CpViewNoSearchResultBinding;
import com.pikachu.convenientdelivery.db.DBManager;
import com.pikachu.convenientdelivery.model.City;
import com.pikachu.convenientdelivery.util.LocateState;
import com.pikachu.convenientdelivery.util.StringUtils;
import com.pikachu.convenientdelivery.util.Utility;
import com.pikachu.convenientdelivery.view.SideLetterBar;

import java.util.List;

/**
 * CityPicker
 */

public class CityPickerActivity extends BaseActivity<ActivityCityPickerBinding> implements View.OnClickListener, SearchView.OnQueryTextListener {

    private LinearLayout rootView;
    private Toolbar toolbar;
    private SearchView searchView;
    private ListView listView;
    private ListView resultListView;
    private SideLetterBar letterBar;
    private ViewGroup emptyView;

    private CityListAdapter cityAdapter;
    private CityResultListAdapter cityResultAdapter;
    private List<City> allCities;
    private DBManager dbManager;

    private AMapLocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initData();
        initView();
//        initLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rootView.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        locationClient.stopLocation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_picker;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, CityPickerActivity.class);
        context.startActivity(intent);
    }

    private void initData() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        allCities = dbManager.getAllCities();
        cityAdapter = new CityListAdapter(this, allCities);
        cityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                back(name);
            }

            @Override
            public void onLocateClick() {
            }
        });
        cityResultAdapter = new CityResultListAdapter(this, null);
    }

    private void initView() {
        rootView = bindingView.rootView;
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("选择城市");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.nav_close);
        searchView = bindingView.searchView;
        searchView.setOnQueryTextListener(this);
        listView = bindingView.listViewAllCity;
        listView.setAdapter(cityAdapter);
        TextView overlay = bindingView.tvLetterOverlay;
        letterBar = bindingView.sideLetterBar;
        letterBar.setOverlay(overlay);
        letterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = cityAdapter.getLetterPosition(letter);
                listView.setSelection(position);
            }
        });
        CpViewNoSearchResultBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.cp_view_no_search_result, null, false);
        emptyView = binding.emptyView;
        resultListView = bindingView.listViewSearchResult;
        resultListView.setAdapter(cityResultAdapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(cityResultAdapter.getItem(position).getName());
            }
        });
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > Utility.dpToPx(CityPickerActivity.this, 200)) {
                    letterBar.setVisibility(View.GONE);
                } else {
                    letterBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initLocation() {
        locationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        locationClient.setLocationOption(option);
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String location = StringUtils.extractLocation(city, district);
                        cityAdapter.updateLocateState(LocateState.SUCCESS, location);
                        DBManager.updateCurrentCity(location);
                    } else {
                        //定位失败
                        cityAdapter.updateLocateState(LocateState.FAILED, null);
                    }
                }
            }
        });
        locationClient.startLocation();
    }

    @Override
    public void onBackPressed() {
        if (!DBManager.getPickedCity().equals("选择城市")) {
            finish();
        } else {
            Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void back(String city) {
        DBManager.setPickedCity(city);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            emptyView.setVisibility(View.GONE);
            resultListView.setVisibility(View.GONE);
        } else {
            resultListView.setVisibility(View.VISIBLE);
            List<City> result = dbManager.searchCity(newText);
            if (result == null || result.size() == 0) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
                cityResultAdapter.changeData(result);
            }
        }
        return true;
    }

}
