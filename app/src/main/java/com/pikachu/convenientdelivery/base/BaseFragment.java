package com.pikachu.convenientdelivery.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<VB extends ViewDataBinding> extends Fragment {

    protected VB bindingView;
    private BaseActivity baseActivity;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bindingView = DataBindingUtil.inflate(inflater, this.getLayoutId(), container, false);

        init();

        return bindingView.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void init(){}

    /**
     * 抽象方法，由子Fragment实现，获取布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 封装 Toast
     */
    public void showToast(String s){
        baseActivity.showToast(s);
    }

    /**
     * 封装界面跳转
     */

    public void jumpTo(Class<?> clazz, boolean isFinish){
        baseActivity.jumpTo(clazz, isFinish);
    }

    public void jumpTo(Intent intent, boolean isFinish){
        baseActivity.jumpTo(intent, isFinish);
    }

}