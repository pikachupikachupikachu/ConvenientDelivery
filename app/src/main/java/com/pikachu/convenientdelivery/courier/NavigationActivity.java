package com.pikachu.convenientdelivery.courier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.util.UIHelper;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        UIHelper.setWindowStatusBarColor(this,R.color.darkBlue);
    }
}
