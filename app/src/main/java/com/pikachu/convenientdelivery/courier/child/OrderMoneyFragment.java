package com.pikachu.convenientdelivery.courier.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pikachu.convenientdelivery.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderMoneyFragment extends Fragment {


    public OrderMoneyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_money, container, false);
    }

}
