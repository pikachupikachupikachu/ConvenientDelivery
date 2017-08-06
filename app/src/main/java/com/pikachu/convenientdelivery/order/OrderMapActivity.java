package com.pikachu.convenientdelivery.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityOrderMapBinding;
import com.pikachu.convenientdelivery.model.Order;

/**
 * OrderMapActivity
 */

public class OrderMapActivity extends BaseActivity<ActivityOrderMapBinding> {

    private Toolbar toolbar;
    private MapView mapView = null;

    private AMap aMap;

    private Order order;

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
        return R.layout.activity_order_map;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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

    private void initView(Bundle savedInstanceState) {
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("地图");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = bindingView.mapView;
        aMap = mapView.getMap();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(30.3, 120.2), 10f, 0, 0));
        aMap.moveCamera(mCameraUpdate);
        mCameraUpdate = CameraUpdateFactory.zoomTo(18);
        aMap.moveCamera(mCameraUpdate);
        mapView.onCreate(savedInstanceState);
    }

    private void initData() {
        Intent intent = getIntent();
        order = intent.getParcelableExtra("order");
    }
}
