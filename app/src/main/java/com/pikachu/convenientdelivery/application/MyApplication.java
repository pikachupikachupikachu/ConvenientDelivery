package com.pikachu.convenientdelivery.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.NineGridView;
import com.pikachu.convenientdelivery.R;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

/**
 * Application
 */

public class MyApplication extends LitePalApplication {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
        Bmob.initialize(this,Constant.BMOB_APP_KEY);
        NineGridView.setImageLoader(new GlideImageLoader());
    }

    private class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.drawable.ic_default_image)//
                    .error(R.drawable.ic_default_image)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

}
