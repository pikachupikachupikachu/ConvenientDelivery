package com.pikachu.convenientdelivery;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.chats.ChatsFragment;
import com.pikachu.convenientdelivery.databinding.ActivityHomeBinding;
import com.pikachu.convenientdelivery.db.DBManager;
import com.pikachu.convenientdelivery.favor.FavorFragment;
import com.pikachu.convenientdelivery.feature.FeatureFragment;
import com.pikachu.convenientdelivery.me.MeFragment;
import com.pikachu.convenientdelivery.order.OrderActivity;
import com.pikachu.convenientdelivery.util.StringUtils;
import com.pikachu.convenientdelivery.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<ActivityHomeBinding> implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener {

    private FeatureFragment featureFragment;
    private FavorFragment favorFragment;
    private ChatsFragment chatsFragment;
    private MeFragment meFragment;

    private BottomNavigationView bottomNavigationView;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int PERMISSION_REQUEST_CODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    private AMapLocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
//        initLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNeedCheck){
            checkPermissions(needPermissions);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        locationClient.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    private void initView() {
        if (DBManager.getPickedCity().equals("选择城市")) {
            Intent intent = new Intent(this, CityPickerActivity.class);
            startActivityForResult(intent, 0);
        }
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
                        DBManager.updateCurrentCity(location);
                    } else {
                        //定位失败
                    }
                }
            }
        });
        locationClient.startLocation();
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

    /**
     *
     * @param needRequestPermissionList
     * @since 2.5.0
     *
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if (null != needRequestPermissionList && needRequestPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     *
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {needRequestPermissionList.add(perm);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 检测是否所有的权限都已经授权
     * @param grantResults
     * @return
     * @since 2.5.0
     *
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     *
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     *  启动应用的设置
     *
     * @since 2.5.0
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
