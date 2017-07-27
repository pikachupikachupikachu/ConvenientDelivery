package com.pikachu.convenientdelivery.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public abstract class BaseActivity<VB extends ViewDataBinding> extends AppCompatActivity {

    protected VB bindingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindingView = DataBindingUtil.setContentView(this,getLayoutId());


        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    /**
     * 返回子Activity布局ID的抽象方法
     * @return
     */
    protected abstract int getLayoutId();


    protected void init(){}



    /**
     * 封装Toast
     * @param s
     */
    public void showToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    /**
     * 封装界面跳转
     */

    public void jumpTo(Class<?> clazz, boolean isFinish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if(isFinish) {
            finish();
        }
    }

    public void jumpTo(Intent intent, boolean isFinish) {
        startActivity(intent);
        if(isFinish) {
            finish();
        }
    }



}
