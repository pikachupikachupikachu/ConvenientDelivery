package com.pikachu.convenientdelivery.courier;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.courier.child.CatchFragment;
import com.pikachu.convenientdelivery.courier.child.MeFragment;
import com.pikachu.convenientdelivery.courier.child.MsgFragment;
import com.pikachu.convenientdelivery.courier.child.OrderFragment;
import com.pikachu.convenientdelivery.databinding.ActivityCourierBinding;
import com.pikachu.convenientdelivery.util.UIHelper;

public class CourierActivity extends BaseActivity<ActivityCourierBinding> {
    private FrameLayout frameLayout;
    private BottomNavigationBar bnvBar;
    private CatchFragment catchFragment;
    private MeFragment meFragment;
    private OrderFragment orderFragment;
    private MsgFragment msgFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_courier;
    }

    @Override
    protected void init() {
        super.init();
        initViews();
    }

    private void initViews() {
        frameLayout = bindingView.frameLayout;
        bnvBar = bindingView.bnvBar;
        UIHelper.setWindowStatusBarColor(this,R.color.darkBlue);
        initBnvBar();
        chooseFragments(0);
    }

    private void initBnvBar() {
        bnvBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);
        bnvBar.setMode(BottomNavigationBar.MODE_FIXED);
        bnvBar.addItem(new BottomNavigationItem(R.drawable.c_catch, "抢单").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.c_order, "订单")).setActiveColor(R.color.blue)
                .addItem(new BottomNavigationItem(R.drawable.c_msg, "消息")).setActiveColor(R.color.blue)
                .addItem(new BottomNavigationItem(R.drawable.c_me, "我的")).setActiveColor(R.color.blue)
                .initialise();
// 底部导航的点击跳转
        bnvBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                chooseFragments(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    /**
     * 隐藏fragment
     */

    private void hideFragments(FragmentTransaction ft) {
        if (catchFragment != null) {
            ft.hide(catchFragment);
        }
        if (orderFragment != null) {
            ft.hide(orderFragment);
        }
        if (msgFragment != null) {
            ft.hide(msgFragment);
        }
        if (meFragment != null) {
            ft.hide(meFragment);
        }
    }

    /**
     * 选择fragment
     */
    private void chooseFragments(int position) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        hideFragments(ft);
        switch (position) {
            case 0:
                if (catchFragment == null) {
                    catchFragment = CatchFragment.newInstance("catch_fragment");
                    ft.add(R.id.frameLayout, catchFragment, catchFragment.getClass().getName());
                }
                ft.show(catchFragment);
                break;
            case 1:
                if (orderFragment == null) {
                    orderFragment = OrderFragment.newInstance("order_fragment");
                    ft.add(R.id.frameLayout, orderFragment, orderFragment.getClass().getName());
                }
                ft.show(orderFragment);
                break;
            case 2:
                if (msgFragment == null) {
                    msgFragment = MsgFragment.newInstance("msg_fragment");
                    ft.add(R.id.frameLayout, msgFragment, msgFragment.getClass().getName());
                }
                ft.show(msgFragment);
                break;

            case 3:
                if (meFragment == null) {
                    meFragment = MeFragment.newInstance("me_fragment");
                    ft.add(R.id.frameLayout, meFragment, meFragment.getClass().getName());
                }
                ft.show(meFragment);
                break;
        }
        ft.commit();

    }


}
