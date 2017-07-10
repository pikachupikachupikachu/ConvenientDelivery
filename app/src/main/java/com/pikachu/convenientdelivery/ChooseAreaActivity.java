package com.pikachu.convenientdelivery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityChooseAreaBinding;

/**
 * ChooseArea
 */

public class ChooseAreaActivity extends BaseActivity<ActivityChooseAreaBinding> {

    public static void start(Context context) {
        Intent intent = new Intent(context, ChooseAreaActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
