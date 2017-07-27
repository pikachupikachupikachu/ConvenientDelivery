package com.pikachu.convenientdelivery.me;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseFragment;
import com.pikachu.convenientdelivery.databinding.FragmentMeBinding;
import com.pikachu.convenientdelivery.model.User;
import com.pikachu.convenientdelivery.util.ImageLoader;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的
 */

public class MeFragment extends BaseFragment<FragmentMeBinding> implements View.OnClickListener {

    public static final String ARGUMENT = "argument";

    private TextView tvNick;
    private TextView tvIntro;
    private TextView tvLogout;
    private TextView tvPub;
    private TextView tvFollows;
    private TextView tvFans;
    private CircleImageView ivAvatar;
    private LinearLayout following;
    private LinearLayout follower;
    private LinearLayout like;
    private LinearLayout llMyInfo;

    public static MeFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        MeFragment meFragment = new MeFragment();
        meFragment.setArguments(bundle);
        return meFragment;
    }


    @Override
    protected void init() {
        super.init();
        initView();
        initListener();
    }

    private void initView() {
        ivAvatar = bindingView.ivAvatar;
        tvNick = bindingView.tvNick;
        tvIntro = bindingView.tvIntro;
        llMyInfo = bindingView.llMyInfo;
        tvLogout = bindingView.tvLogout;
        following = bindingView.following;
        follower = bindingView.follower;
        like = bindingView.like;
        tvFans = bindingView.tvFans;
        tvFollows = bindingView.tvFollows;
        tvPub = bindingView.tvPub;
    }

    private void initDatas() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            if (user.getAvatar() != null) {
                ImageLoader.with(getContext(), user.getAvatar().getUrl(), ivAvatar);
            } else {
                ivAvatar.setImageResource(R.drawable.ic_account_circle_grey_300_36dp);
            }
            tvNick.setText(user.getNick());
            tvIntro.setText(user.getIntro());
            tvPub.setText("23");
            tvFollows.setText("14");
            tvFans.setText("26");
        } else {
            ivAvatar.setImageResource(R.drawable.ic_account_circle_grey_300_36dp);
            tvNick.setText("请登录");
            tvFans.setText("0");
            tvFollows.setText("0");
            tvPub.setText("0");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initDatas();
    }

    private void initListener() {
        llMyInfo.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        following.setOnClickListener(this);
        follower.setOnClickListener(this);
        like.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_myInfo:
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
            case R.id.tv_logout:
                BmobUser.logOut();
                showToast("退出登录");
                initDatas();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }
}
