package com.pikachu.convenientdelivery.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.pikachu.convenientdelivery.application.MyApplication;
import com.pikachu.convenientdelivery.db.DBManager;
import com.pikachu.convenientdelivery.util.StringUtils;

/**
 * LocationService
 */

public class LocationService extends Service implements AMapLocationListener {

    private AMapLocationClient locationClient;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setInterval(2000);
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
        locationClient.setLocationListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        locationClient.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (MyApplication.getContext() != null) {
            SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("pref", Context.MODE_PRIVATE).edit();
            editor.putLong("latitude", Double.doubleToLongBits(aMapLocation.getLatitude()));
            editor.putLong("longitude", Double.doubleToLongBits(aMapLocation.getLongitude()));
            editor.apply();
        }
    }
}
