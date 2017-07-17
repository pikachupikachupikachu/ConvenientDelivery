package com.pikachu.convenientdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.chats.ChatsFragment;
import com.pikachu.convenientdelivery.databinding.ActivityHomeBinding;
import com.pikachu.convenientdelivery.databinding.ActivityLoginBinding;
import com.pikachu.convenientdelivery.favor.FavorFragment;
import com.pikachu.convenientdelivery.feature.FeatureFragment;
import com.pikachu.convenientdelivery.me.MeFragment;
import com.pikachu.convenientdelivery.order.OrderActivity;
import com.pikachu.convenientdelivery.util.Utility;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    private FeatureFragment featureFragment;
    private FavorFragment favorFragment;
    private ChatsFragment chatsFragment;
    private MeFragment meFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    private void initView() {
        bottomNavigationView = bindingView.bottomNavigationView;
        bottomNavigationView.findViewById(R.id.feature).performClick();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemReselectedListener(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (featureFragment == null) {
            featureFragment = FeatureFragment.newInstance("Feature Fragment");
        }
        transaction.replace(R.id.content, featureFragment);
        transaction.commit();
        final View rootView = bindingView.root;
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > Utility.dpToPx(HomeActivity.this, 200)) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.feature:
                if (featureFragment == null) {
                    featureFragment = FeatureFragment.newInstance("Feature Fragment");
                }
                transaction.replace(R.id.content, featureFragment);
                break;
            case R.id.favor:
                if (favorFragment == null) {
                    favorFragment = FavorFragment.newInstance("Favor Fragment");
                }
                transaction.replace(R.id.content, favorFragment);
                break;
            case R.id.order:
                OrderActivity.start(this);
                return false;
            case R.id.chats:
                if (chatsFragment == null) {
                    chatsFragment = ChatsFragment.newInstance("Chats Fragment");
                }
                transaction.replace(R.id.content, chatsFragment);
                break;
            case R.id.me:
                if (meFragment == null) {
                    meFragment = MeFragment.newInstance("Me Fragment");
                }
                transaction.replace(R.id.content, meFragment);
                break;
            default:
                if (featureFragment == null) {
                    featureFragment = FeatureFragment.newInstance("Feature Fragment");
                }
                transaction.replace(R.id.content, featureFragment);
                break;
        }
        transaction.commit();
        return true;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.feature:
                featureFragment.onRefresh();
                break;
            case R.id.favor:
                break;
            case R.id.order:
                break;
            case R.id.chats:
                chatsFragment.onRefresh();
                break;
            case R.id.me:
                meFragment.onRefresh();
                break;
            default:
                break;
        }
    }


}
