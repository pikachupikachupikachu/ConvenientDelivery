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
import com.pikachu.convenientdelivery.model.RecipientInfo;
import com.pikachu.convenientdelivery.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 收货地址
 */

public class RecipientInfoActivity extends BaseActivity<ActivityRecipientInfoBinding> implements View.OnClickListener, BaseRecyclerViewAdapter.OnItemClickListener<RecipientInfo> {

    private Toolbar toolbar;
    private FloatingActionButton add;
    private RecyclerView recyclerView;

    private RecipientInfoAdapter adapter;

    private User user;
    private List<RecipientInfo> recipientInfoList = new ArrayList<>();

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
        BmobQuery<User> query = new BmobQuery<>();
        user = BmobUser.getCurrentUser(User.class);
        query.getObject(user.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    recipientInfoList.clear();
                    for (RecipientInfo recipientInfo : user.getRecipientInfoList()) {
                        recipientInfoList.add(recipientInfo);
                    }
                    adapter.set(recipientInfoList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
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
    public void onClick(View view, final RecipientInfo recipientInfo, int position) {
        switch (view.getId()) {
            case R.id.edit:
                Intent intentToEditRecipientInfo = new Intent(this, EditRecipientInfoActivity.class);
                intentToEditRecipientInfo.putExtra("position", position);
                intentToEditRecipientInfo.putExtra("recipient_info", (Parcelable) recipientInfo);
                startActivityForResult(intentToEditRecipientInfo, EDIT_RECIPIENT_INFO);
                break;
            case R.id.delete:
                user.removeRecipientInfo(position);
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            adapter.set(recipientInfoList);
                            adapter.notifyDataSetChanged();
                            showToast("删除成功");
                        }
                    }
                });
                break;
            default:
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_RECIPIENT_INFO:
                if (resultCode == RESULT_OK) {
                    user = BmobUser.getCurrentUser(User.class);
                    BmobQuery<User> query = new BmobQuery<>();
                    query.getObject(user.getObjectId(), new QueryListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                recipientInfoList = user.getRecipientInfoList();
                                adapter.set(recipientInfoList);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
                break;
        }
    }
}
