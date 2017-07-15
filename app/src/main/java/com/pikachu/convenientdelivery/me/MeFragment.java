package com.pikachu.convenientdelivery.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pikachu.convenientdelivery.FollowerActivity;
import com.pikachu.convenientdelivery.FollowingActivity;
import com.pikachu.convenientdelivery.LikeActivity;
import com.pikachu.convenientdelivery.MyProfileActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentMeBinding;

/**
 * 我的
 */

public class MeFragment extends BaseFragment<FragmentMeBinding> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    public static final String ARGUMENT = "argument";

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView headShot;
    private TextView name;
    private TextView whatsUp;
    private ImageButton myProfile;
    private LinearLayout following;
    private LinearLayout follower;
    private LinearLayout like;

    public static MeFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        MeFragment meFragment = new MeFragment();
        meFragment.setArguments(bundle);
        return meFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        initView();
    }

    @Override
    public int setContent() {
        return R.layout.fragment_me;
    }

    private void initView() {
        swipeRefreshLayout = bindingView.swipeRefreshLayout;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        headShot = bindingView.headShot;
        name = bindingView.name;
        whatsUp = bindingView.whatsUp;
        myProfile = bindingView.myProfile;
        myProfile.setOnClickListener(this);
        following = bindingView.following;
        following.setOnClickListener(this);
        follower = bindingView.follower;
        follower.setOnClickListener(this);
        like = bindingView.like;
        like.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_profile:
                MyProfileActivity.start(getActivity());
                break;
            case R.id.following:
                FollowingActivity.start(getActivity());
                break;
            case R.id.follower:
                FollowerActivity.start(getActivity());
                break;
            case R.id.like:
                LikeActivity.start(getActivity());
                break;
        }
    }

}
