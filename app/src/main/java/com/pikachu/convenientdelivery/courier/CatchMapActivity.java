package com.pikachu.convenientdelivery.courier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityCatchMapBinding;
import com.pikachu.convenientdelivery.util.UIHelper;

public class CatchMapActivity extends BaseActivity<ActivityCatchMapBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_catch_map;
    }

    @Override
    protected void init() {
        super.init();
        initViews();
    }

    private void initViews(){
        UIHelper.setWindowStatusBarColor(this,R.color.darkBlue);
    }
}
