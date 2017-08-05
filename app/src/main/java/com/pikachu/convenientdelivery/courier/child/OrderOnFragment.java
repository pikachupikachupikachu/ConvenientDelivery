package com.pikachu.convenientdelivery.courier.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.courier.NavigationActivity;
import com.pikachu.convenientdelivery.databinding.FragmentOrderOnBinding;
import com.pikachu.convenientdelivery.listener.PerfectClickListener;
import com.pikachu.convenientdelivery.util.UIHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderOnFragment extends BaseFragment<FragmentOrderOnBinding> {
    Button btn_navigaiton1;
    Button btn_navigaiton2;

    public OrderOnFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_on;
    }

    @Override
    protected void init() {
        super.init();
        initViews();
        initListeners();
    }

    private void initViews() {
        btn_navigaiton1 = bindingView.btnNavigation1;

    }

    private void initListeners() {
        btn_navigaiton1.setOnClickListener(listener);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_navigation1:
                    jumpTo(NavigationActivity.class, false);
                    break;
            }
        }
    };
}
