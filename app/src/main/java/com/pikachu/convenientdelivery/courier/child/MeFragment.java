package com.pikachu.convenientdelivery.courier.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentMeBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment<FragmentMeBinding> {
    private static final String TAG = "me_fragment";

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance(String args) {
        MeFragment fragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, args);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me2;
    }

}
