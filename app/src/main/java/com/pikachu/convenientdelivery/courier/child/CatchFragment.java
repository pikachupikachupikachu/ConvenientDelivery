package com.pikachu.convenientdelivery.courier.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.melnykov.fab.FloatingActionButton;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.courier.CatchMapActivity;
import com.pikachu.convenientdelivery.databinding.FragmentCatchBinding;
import com.pikachu.convenientdelivery.listener.PerfectClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatchFragment extends BaseFragment<FragmentCatchBinding> {
    private Button btn_catch1;
    private Button btn_catch2;
    private Button btn_catch3;
    private FloatingActionButton fab;

    private static final String TAG = "catch_fragment";

    public CatchFragment() {
        // Required empty public constructor
    }

    public static CatchFragment newInstance(String args) {
        CatchFragment fragment = new CatchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, args);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_catch;
    }

    @Override
    protected void init() {
        super.init();
        initViews();
    }

    private void initViews(){
        btn_catch1 = bindingView.btnCatch1;

        fab = bindingView.fabMap;
        btn_catch1.setOnClickListener(listener);

        fab.setOnClickListener(listener);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()){
                case R.id.btn_catch1:
                    showToast("我要抢单");
                    break;
                case R.id.fab_map:
                    jumpTo(CatchMapActivity.class,false);
                    break;
            }

        }
    };
}
