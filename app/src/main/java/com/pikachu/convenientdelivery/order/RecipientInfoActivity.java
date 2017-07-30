package com.pikachu.convenientdelivery.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.RecipientInfoAdapter;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.base.adapter.BaseRecyclerViewAdapter;
import com.pikachu.convenientdelivery.databinding.ActivityRecipientInfoBinding;
import com.pikachu.convenientdelivery.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货地址
 */

public class RecipientInfoActivity extends BaseActivity<ActivityRecipientInfoBinding> implements View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener<User> {

    private Toolbar toolbar;
    private FloatingActionButton add;
    private RecyclerView recyclerView;

    private RecipientInfoAdapter adapter;

    private List<User> recipientInfoList = new ArrayList<>();

    public static final int EDIT_RECIPIENT_INFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        initView();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recipient_info;
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

    private void initView() {
        toolbar = bindingView.toolbar;
        toolbar.setTitle("选择收货地址");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add = bindingView.fab;
        add.setOnClickListener(this);
        recyclerView = bindingView.recyclerView;
        adapter = new RecipientInfoAdapter();
        adapter.setOnItemClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        recipientInfoList.clear();
        //从云端加载数据
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intentToEditRecipientInfo = new Intent(this, EditRecipientInfoActivity.class);
                startActivityForResult(intentToEditRecipientInfo, EDIT_RECIPIENT_INFO);
                break;
        }
    }

    @Override
    public void onClick(View view, User user, int position) {
        switch (view.getId()) {
            case R.id.item_recipient:
                Intent intent = new Intent();
                intent.putExtra("recipient_info", (Parcelable) user);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.edit:
                Intent intentToEditRecipientInfo = new Intent(this, EditRecipientInfoActivity.class);
                intentToEditRecipientInfo.putExtra("recipient", (Parcelable) user);
                startActivityForResult(intentToEditRecipientInfo, EDIT_RECIPIENT_INFO);
                break;
            case R.id.delete:
                //从云端删除相应RecipientInfo 成功后从本地重新加载
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_RECIPIENT_INFO:
                if (resultCode == RESULT_OK) {
                    //从云端同步数据
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
