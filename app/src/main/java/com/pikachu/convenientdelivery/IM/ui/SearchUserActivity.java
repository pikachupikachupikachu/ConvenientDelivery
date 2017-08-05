package com.pikachu.convenientdelivery.IM.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.SearchUserAdapter;
import com.pikachu.convenientdelivery.application.MyApplication;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.databinding.ActivitySearchUserBinding;
import com.pikachu.convenientdelivery.model.BaseModel;
import com.pikachu.convenientdelivery.model.User;
import com.pikachu.convenientdelivery.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;



/**
 * Created by JinBo on 2017/7/28.
 */

public class SearchUserActivity  extends BaseActivity<ActivitySearchUserBinding> implements View.OnClickListener {
    private List<User> users = new ArrayList<>();
    SearchUserAdapter adapter;
    private RecyclerView rc_view;
    private SwipeRefreshLayout sw_refresh;
    private Button btn_search;
    private Toolbar toolbar;
    private EditText et_find_name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_user;
    }


    @Override
    protected void init() {
        super.init();
        initView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rc_view.setLayoutManager(layoutManager);
        adapter = new SearchUserAdapter(users,this);
        rc_view.setAdapter(adapter);

    }

    private void initView() {
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rc_view = bindingView.rcView;
        sw_refresh = bindingView.swRefresh;
        btn_search = bindingView.btnSearch;
        et_find_name = bindingView.etFindName;

        btn_search.setOnClickListener(this);



        sw_refresh.setEnabled(true);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_search:
        //       Toast.makeText(this,"请填写用户名",Toast.LENGTH_LONG).show();
               sw_refresh.setRefreshing(true);
               query();
               break;
        }
    }

    private void query() {
        String name = et_find_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this,"请填写用户名",Toast.LENGTH_LONG).show();
            sw_refresh.setRefreshing(false);
            return;
        }
        UserModel.getInstance().queryUsers(name, BaseModel.DEFAULT_LIMIT,
                new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(list);
                            Log.e("HHH",list.toString());
                            adapter.notifyDataSetChanged();
                        } else {
                            sw_refresh.setRefreshing(false);
                            adapter.setDatas(null);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MyApplication.getContext(),e.getMessage()+ "(" + e.getErrorCode() + ")",Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );
    }



}
