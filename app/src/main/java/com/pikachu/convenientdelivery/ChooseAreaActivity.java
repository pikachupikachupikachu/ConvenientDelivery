package com.pikachu.convenientdelivery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityChooseAreaBinding;

/**
 * ChooseArea
 */

public class ChooseAreaActivity extends BaseActivity<ActivityChooseAreaBinding> {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        showContentView();
        initView();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ChooseAreaActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.nav_close);
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

}
