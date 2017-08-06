package com.pikachu.convenientdelivery.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pikachu.convenientdelivery.IM.ui.ChatActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.adapter.LogisticsInfoAdapter;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityOrderDetailBinding;
import com.pikachu.convenientdelivery.model.LogisticsInfo;
import com.pikachu.convenientdelivery.model.Order;

import java.util.List;

/**
 * OrderDetail
 */

public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding> implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView name;
    private TextView phone;
    private TextView address;
    private TextView goodsName;
    private TextView goodsDetail;
    private TextView purchasingAddress;
    private TextView reward;
    private TextView deadline;
    private LinearLayout llButton;
    private Button button1;
    private Button button2;
    private TextView status;
    private ListView logisticsInfoListView;

    private LogisticsInfoAdapter adapter;

    private Order order;
    private List<LogisticsInfo> logisticsInfo;

    public static final int ORDER_MAP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        initView();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void initView() {
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("订单信息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = bindingView.name;
        phone = bindingView.phone;
        address = bindingView.address;
        goodsName = bindingView.goodsName;
        goodsDetail = bindingView.goodsDetail;
        purchasingAddress = bindingView.purchasingAddressText;
        reward = bindingView.reward;
        deadline = bindingView.deadline;
        llButton = bindingView.llButton;
        button1 = bindingView.button1;
        button1.setOnClickListener(this);
        button2 = bindingView.button2;
        button2.setOnClickListener(this);
        status = bindingView.status;
        logisticsInfoListView = bindingView.logisticsInfoListView;
    }

    private void initData() {
        Intent intent = getIntent();
        order = intent.getParcelableExtra("order");
        name.setText(order.getRecipientInfo().getName());
//        if (order.getRecipient().getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
//            phone.setText(order.getRecipientInfo().getPhone());
//            address.setText(order.getRecipientInfo().getAddress());
//        } else if (order.getShipper().getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
//            llButton.setVisibility(View.GONE);
//            phone.setVisibility(View.GONE);
//            address.setVisibility(View.GONE);
//        } else {
//            llButton.setVisibility(View.GONE);
//            phone.setVisibility(View.GONE);
//            address.setVisibility(View.GONE);
//        }
//        if (order.getCourier() == null) {
//            button1.setVisibility(View.GONE);
//        }
        goodsName.setText(order.getGoodsName());
        goodsDetail.setText(order.getGoodsDetail());
        purchasingAddress.setText(order.getPurchasingAddress());
        reward.setText("赏" + order.getReward() + (order.getRewardDefault() ? "元": "%"));
        deadline.setText(order.getDeadline() + "前");
        button1.setText("查看地图");
        button2.setText("发消息");
        switch (order.getStatus()) {
            case Order.WAITING:
                status.setText("待接单");
                break;
            case Order.PURCHASING:
                status.setText("购买中");
                break;
            case Order.NONDELIVERY:
                status.setText("已购买");
                break;
            case Order.SHIPPING:
                status.setText("运送中");
                break;
            case Order.UNCONFIRMED:
                status.setText("待确认");
                break;
            case Order.UNCOMMENT:
                status.setText("待分享");
                break;
            case Order.FINISHED:
                status.setText("已完成");
                break;
            case Order.ERROR:
                status.setText("订单异常待处理");
                break;
        }
        logisticsInfo = order.getLogisticsInfo();
        adapter = new LogisticsInfoAdapter(getApplicationContext());
        adapter.setData(logisticsInfo);
        logisticsInfoListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Intent intent = new Intent(this, OrderMapActivity.class);
                intent.putExtra("order", (Parcelable) order);
                startActivityForResult(intent, ORDER_MAP);
                break;
            case R.id.button2:
                Intent intentToChatActivity = new Intent(this, ChatActivity.class);
                startActivity(intentToChatActivity);
                break;
        }
    }
}
