package com.pikachu.convenientdelivery.entrance;

import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pikachu.convenientdelivery.HomeActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.model.User;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends BaseActivity {

    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void init() {
        super.init();
//        判断用户是否登录的跳转逻辑
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BmobUser.getCurrentUser(User.class) == null) {
                    jumpTo(LoginActivity.class, true);
                } else {
                    jumpTo(HomeActivity.class, true);
                }
            }
        }, 2000);
    }
}
