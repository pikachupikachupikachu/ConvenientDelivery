package com.pikachu.convenientdelivery.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityEditRecipientInfoBinding;
import com.pikachu.convenientdelivery.model.RecipientInfo;
import com.pikachu.convenientdelivery.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 编辑收货地址
 */

public class EditRecipientInfoActivity extends BaseActivity<ActivityEditRecipientInfoBinding> {

    private Toolbar toolbar;
    private EditText name;
    private EditText phone;
    private EditText address;

    private RecipientInfo recipientInfo;

    private boolean isEdit = false;
    private int position;

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
                String nickText = name.getText().toString();
                String phoneText = phone.getText().toString();
                String addressText = address.getText().toString();
                if (TextUtils.isEmpty(nickText) || nickText.length() < 2) {
                    showToast("收货人姓名至少2个字符");
                } else if (nickText.length() > 15) {
                    showToast("收货人姓名至多15个字符");
                } else if (TextUtils.isEmpty(phoneText)) {
                    showToast("请填写联系电话");
                } else {
                    String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
                    Pattern p = Pattern.compile(regExp);
                    Matcher m = p.matcher(phoneText);
                    if (!m.find()) {
                        showToast("请填写正确的联系电话");
                    } else if (TextUtils.isEmpty(addressText)) {
                        showToast("请填写详细地址");
                    } else if (addressText.length() < 5) {
                        showToast("详细地址描述信息不得少于5个字符");
                    } else if (addressText.length() > 60) {
                        showToast("详细地址描述信息不得多于60个字符");
                    } else {
                        if (recipientInfo == null) {
                            recipientInfo = new RecipientInfo();
                        }
                        recipientInfo.setName(name.getText().toString());
                        recipientInfo.setPhone(phone.getText().toString());
                        recipientInfo.setAddress(address.getText().toString());
                        final User user = BmobUser.getCurrentUser(User.class);
                        if (isEdit) {
                            user.setRecipientInfo(position, recipientInfo);
                        } else {
                            user.addRecipientInfo(recipientInfo);
                        }
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    showToast("保存成功");
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    finish();
                                } else {
                                    showToast("错误");
                                }
                            }
                        });
                        //上传Bomb
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
        name = bindingView.name;
        phone = bindingView.phone;
        address = bindingView.address;
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra("recipient_info")) {
            isEdit = true;
            recipientInfo = intent.getParcelableExtra("recipient_info");
            position = intent.getIntExtra("position", 0);
            name.setText(recipientInfo.getName());
            phone.setText(recipientInfo.getPhone());
            address.setText(recipientInfo.getAddress());
            getSupportActionBar().setTitle("编辑地址");
        } else {
            getSupportActionBar().setTitle("添加新地址");
        }
    }

}
