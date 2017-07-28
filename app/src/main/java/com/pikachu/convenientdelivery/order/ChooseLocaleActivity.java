package com.pikachu.convenientdelivery.order;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.pikachu.convenientdelivery.CityPickerActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.SearchResultsAdapter;
import com.pikachu.convenientdelivery.application.MyApplication;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityChooseLocaleBinding;
import com.pikachu.convenientdelivery.db.DBManager;
import com.pikachu.convenientdelivery.model.Result;
import com.pikachu.convenientdelivery.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ChooseLocaleActivity extends BaseActivity<ActivityChooseLocaleBinding> implements AMap.OnMyLocationChangeListener, Inputtips.InputtipsListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, ListView.OnItemClickListener, PoiSearch.OnPoiSearchListener, LocationSource, AMapLocationListener, AMap.OnCameraChangeListener, AMap.OnMapLoadedListener, GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener {

    private AMap aMap;

    private Toolbar toolbar;
    private SearchView searchView;
    private MapView mapView = null;
    private ListView listViewSearchResult;
    private ListView listViewAddress;

    private String city;
    private List<Result> resultList;
    private int selectedPosition = -1;
    private SearchResultsAdapter adapter;
    private OnLocationChangedListener listener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationClientOption;
    private InputtipsQuery inputtipsQuery;
    private Inputtips inputtips;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private Marker marker;
    private LatLonPoint searchPoint;
    private GeocodeSearch geocodeSearch;

    private Boolean queryChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        initView(savedInstanceState);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_locale;
    }

    private void initView(Bundle savedInstanceState) {
        toolbar = bindingView.toolbar;
        toolbar.setTitle("选择地址");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = bindingView.searchView;
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setOnSearchClickListener(this);
        mapView = bindingView.mapView;
        aMap = mapView.getMap();
        aMap.setOnMyLocationChangeListener(this);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLoadedListener(this);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(true);
        mapView.onCreate(savedInstanceState);
        listViewSearchResult = bindingView.listViewSearchResult;
        listViewSearchResult.setOnItemClickListener(this);
        listViewAddress = bindingView.listViewAddress;
        listViewAddress.setOnItemClickListener(this);
        adapter = new SearchResultsAdapter(getApplicationContext());
        listViewSearchResult.setAdapter(adapter);
        listViewAddress.setAdapter(adapter);
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        city = DBManager.getPickedCity();
        if (city.equals("选择城市")) {
            city = "北京";
        }
        if (intent.hasExtra("address")) {
            String address = intent.getStringExtra("address");
            inputtipsQuery = new InputtipsQuery(address, city);
            inputtipsQuery.setCityLimit(true);
            inputtips = new Inputtips(this, inputtipsQuery);
            inputtips.setInputtipsListener(this);
            inputtips.requestInputtipsAsyn();
        }
        if (MyApplication.getContext() != null) {
            SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
            if (preferences != null) {
                LatLng latLng = new LatLng(Double.longBitsToDouble(preferences.getLong("latitude", 0)), Double.longBitsToDouble(preferences.getLong("longitude", 0)));
                if (!(latLng.latitude == 0 && latLng.longitude == 0)) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.toolbar_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                String address = searchView.getQuery().toString();
                if (!TextUtils.isEmpty(address)) {
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (MyApplication.getContext() != null) {
            SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
            if (preferences != null) {
                LatLng latLng = new LatLng(Double.longBitsToDouble(preferences.getLong("latitude", 0)), Double.longBitsToDouble(preferences.getLong("longitude", 0)));
                if (!(latLng.latitude == 0 && latLng.longitude == 0)) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                } else {
                    Toast.makeText(this, "位置信息未知", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        inputtipsQuery = new InputtipsQuery(query, city);
        inputtipsQuery.setCityLimit(true);
        inputtips = new Inputtips(this, inputtipsQuery);
        inputtips.setInputtipsListener(this);
        inputtips.requestInputtipsAsyn();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!queryChange)
            return false;
        mapView.setVisibility(View.GONE);
        listViewSearchResult.setVisibility(View.VISIBLE);
        listViewAddress.setVisibility(View.GONE);
        String query = newText.trim();
        if (!TextUtils.isEmpty(query)) {
            inputtipsQuery = new InputtipsQuery(query, city);
            inputtipsQuery.setCityLimit(true);
            inputtips = new Inputtips(this, inputtipsQuery);
            inputtips.setInputtipsListener(this);
            inputtips.requestInputtipsAsyn();
        }
        return true;
    }

    @Override
    public boolean onClose() {
        searchView.onActionViewCollapsed();
        listViewSearchResult.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
        listViewAddress.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public void onGetInputtips(List<Tip> tipsList, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            resultList = new ArrayList<>();
            for (Tip tip : tipsList) {
                resultList.add(new Result(tip.getName(), tip.getAddress(), tip.getPoint()));
            }
            adapter.setData(resultList);
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showError(getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Result result = (Result) parent.getItemAtPosition(position);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        switch (parent.getId()) {
            case R.id.list_view_search_result:
                if (result != null) {
                    LatLonPoint point = result.getPoint();
                    if (point != null) {
                        queryChange = false;
                        searchView.setQuery(result.getName(), false);
                        selectedPosition = position;
                        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        queryChange = true;
                        mapView.setVisibility(View.VISIBLE);
                        listViewSearchResult.setVisibility(View.GONE);
                        listViewAddress.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.list_view_address:
                if (result != null) {
                    LatLonPoint point = result.getPoint();
                    if (point != null && position != selectedPosition) {
                        queryChange = false;
                        searchView.onActionViewExpanded();
                        searchView.setQuery(result.getName(), false);
                        selectedPosition = position;
                        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                        queryChange = true;
                    }
                }
                break;
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {
                    List<PoiItem> poiItems = poiResult.getPois();
                    resultList = new ArrayList<>();
                    if (poiItems != null) {
                        for (PoiItem poiItem : poiItems) {
                            resultList.add(new Result(poiItem.getTitle(), poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet(), poiItem.getLatLonPoint()));
                        }
                        queryChange = false;
                        searchView.onActionViewExpanded();
                        searchView.setQuery(resultList.get(0).getName(), false);
                        adapter.setData(resultList);
                        adapter.notifyDataSetChanged();
                        View view = this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        queryChange = true;
                    } else {
                        Toast.makeText(ChooseLocaleActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(ChooseLocaleActivity.this, "无搜索结果", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        listener = onLocationChangedListener;
//        if (locationClient == null) {
//            locationClient = new AMapLocationClient(this);
//            locationClientOption = new AMapLocationClientOption();
//            locationClient.setLocationListener(this);
//            locationClientOption.setOnceLocation(true);
//            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            locationClient.setLocationOption(locationClientOption);
//            locationClient.startLocation();
//        }
    }

    @Override
    public void deactivate() {
//        listener = null;
//        if (locationClient != null) {
//            locationClient.stopLocation();
//            locationClient.onDestroy();
//        }
//        locationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (listener != null && aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            listener.onLocationChanged(aMapLocation);
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            searchPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (marker != null) {
            if (searchPoint != null) {
                RegeocodeQuery query = new RegeocodeQuery(searchPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                geocodeSearch.getFromLocationAsyn(query);
            }
            final LatLng latLng = marker.getPosition();
            Point point =  aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this, 125);
            LatLng target = aMap.getProjection().fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if(input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            marker.setAnimation(animation);
            //开始动画
            marker.startAnimation();
        }
        searchPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
    }

    @Override
    public void onMapLoaded() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        marker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f,0.5f));
        //设置Marker在屏幕上,不跟随地图移动
        marker.setPositionByPixels(screenPosition.x,screenPosition.y);
        marker.setZIndex(1);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                query = new PoiSearch.Query("", "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                query.setCityLimit(true);
                query.setPageSize(20);
                if (searchPoint != null) {
                    poiSearch = new PoiSearch(this, query);
                    poiSearch.setOnPoiSearchListener(this);
                    poiSearch.setBound(new PoiSearch.SearchBound(searchPoint, 1000, true));//
                    poiSearch.searchPOIAsyn();
                }
            }
        } else {
            Toast.makeText(ChooseLocaleActivity.this, "error code is " + errorCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_pick_city:
                Intent intent = new Intent(this, CityPickerActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.search_view:
                queryChange = true;
                break;
        }
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
