package com.pikachu.convenientdelivery.courier.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentMsgBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MsgFragment extends BaseFragment<FragmentMsgBinding> {
private static final String TAG = "msg_fragment";

    public MsgFragment() {
        // Required empty public constructor
    }
    public static MsgFragment newInstance(String args){
        MsgFragment fragment = new MsgFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG,args);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_msg;
    }

}
