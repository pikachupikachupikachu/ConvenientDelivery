package com.pikachu.convenientdelivery.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityEditRecipientInfoBinding;
import com.pikachu.convenientdelivery.model.User;

/**
 * 编辑收货地址
 */

public class EditRecipientInfoActivity extends BaseActivity<ActivityEditRecipientInfoBinding> {

    private Toolbar toolbar;
    private EditText nick;
    private EditText phone;
    private EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        initView();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_recipient_info;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                String nickText = nick.getText().toString();
                String phoneText = phone.getText().toString();
                String addressText = address.getText().toString();
                if (TextUtils.isEmpty(nickText) || nickText.length() < 2) {
                    Toast.makeText(this, "收货人姓名至少2个字符", Toast.LENGTH_SHORT).show();
                } else if (nickText.length() > 15) {
                    Toast.makeText(this, "收货人姓名至多15个字符", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(phoneText)) {
                        Toast.makeText(this, "请填写联系电话", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isEmpty(addressText)) {
                            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show();
                        } else if (addressText.length() < 5) {
                            Toast.makeText(this, "详细地址描述信息不得少于5个字符", Toast.LENGTH_SHORT).show();
                        } else if (addressText.length() > 60) {
                            Toast.makeText(this, "详细地址描述信息不得多于60个字符", Toast.LENGTH_SHORT).show();
                        } else {
                            User recipientInfo = new User();
                            recipientInfo.setNick(nick.getText().toString());
                            recipientInfo.setPhone(phone.getText().toString());
                            recipientInfo.setAddress(address.getText().toString());
                            //上传Bomb
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.toolbar_done, menu);
        return true;
    }

    private void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nick = bindingView.nick;
        phone = bindingView.phone;
        address = bindingView.address;
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("recipient_info")) {
            User user = intent.getParcelableExtra("recipient_info");
            nick.setText(user.getNick());
            phone.setText(user.getPhone());
            address.setText(user.getAddress());
            getSupportActionBar().setTitle("编辑地址");
        } else {
            getSupportActionBar().setTitle("添加新地址");
        }
    }

}
