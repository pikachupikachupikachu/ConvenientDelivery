package com.pikachu.convenientdelivery.order;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityOrderBinding;
import com.pikachu.convenientdelivery.model.Order;
import com.pikachu.convenientdelivery.model.RecipientInfo;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 下单
 */

public class OrderActivity extends BaseActivity<ActivityOrderBinding> implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, Switch.OnCheckedChangeListener, TextWatcher {

    private Toolbar toolbar;
    private FloatingActionButton confirm;
    private EditText goodsName;
    private EditText goodsDetail;
    private ImageView goodsPhoto;
    private EditText reward;
    private RadioGroup rewardSelect;
    private Switch isGoodsSpecific;
    private Switch isAddressSpecific;
    private TextView purchasingAddressText;
    private TextView recipientInfoText;

    private Order order = new Order();
    private RecipientInfo recipientInfo;

    public static final int CHOOSE_PHOTO = 1;
    public static final int CHOOSE_LOCALE = 2;
    public static final int CHOOSE_SHIPPER_INFO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"随手快递");
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rg1:
                order.setRewardDefault(true);
                break;
            case R.id.rg2:
                order.setRewardDefault(false);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.is_goods_specific:
                if (isChecked) {
                    order.setGoodsSpecific(true);
                } else {
                    order.setGoodsSpecific(false);
                }
                break;
            case R.id.is_address_specific:
                if (isChecked) {
                    order.setAddressSpecific(true);
                } else {
                    order.setAddressSpecific(false);
                }
                break;
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, OrderActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = bindingView.toolbar;
        toolbar.setNavigationIcon(R.drawable.nav_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        confirm = bindingView.fab;
        confirm.setOnClickListener(this);
        goodsName = bindingView.goodsName;
        goodsName.addTextChangedListener(this);
        goodsDetail = bindingView.goodsDetail;
        goodsDetail.addTextChangedListener(this);
        goodsPhoto = bindingView.goodsPhoto;
        goodsPhoto.setOnClickListener(this);
        reward = bindingView.reward;
        reward.addTextChangedListener(this);
        rewardSelect = bindingView.rewardSelect;
        rewardSelect.setOnCheckedChangeListener(this);
        isGoodsSpecific = bindingView.isGoodsSpecific;
        isGoodsSpecific.setOnCheckedChangeListener(this);
        isAddressSpecific = bindingView.isAddressSpecific;
        isAddressSpecific.setOnCheckedChangeListener(this);
        purchasingAddressText = bindingView.purchasingAddressText;
        purchasingAddressText.setOnClickListener(this);
        purchasingAddressText.addTextChangedListener(this);
        recipientInfoText = bindingView.recipientInfoText;
        recipientInfoText.setOnClickListener(this);
        recipientInfoText.addTextChangedListener(this);
    }

    private void getOrder() {
        order.setGoodsName(goodsName.getText().toString());
        order.setGoodsDetail(goodsDetail.getText().toString());
//        order.setGoodsImagePath();
        if (order.isRewardDefault()) {
            order.setReward(Double.parseDouble(reward.getText().toString()));
        } else {
            order.setReward(0.0);
        }

        order.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(OrderActivity.this, "添加数据成功，返回objectId为：" + objectId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, "创建数据失败" + e.getMessage() , Toast.LENGTH_LONG).show();
                }
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
    public void onClick(View v) {
        switch (v.getId()) {
            //确定订单
            case R.id.fab:
                getOrder();
                Bundle data = new Bundle();
                data.putSerializable("order", order);
                Intent intentToOrderConfirm = new Intent(this, OrderConfirmActivity.class);
                intentToOrderConfirm.putExtras(data);
                startActivity(intentToOrderConfirm);
                break;
            //打开系统相册
            case R.id.goods_photo:
                openAlbum();
                break;
            case R.id.purchasing_address_text:
                Intent intentToChooseLocale = new Intent(this, ChooseLocaleActivity.class);
                String address = purchasingAddressText.getText().toString();
                if (!TextUtils.isEmpty(address)) {
                    intentToChooseLocale.putExtra("address", address);
                }
                startActivityForResult(intentToChooseLocale, CHOOSE_LOCALE);
                break;
            case R.id.recipient_info_text:
//                Intent intentToShipperInfo = new Intent(this, );
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(goodsName.getText()) || TextUtils.isEmpty(goodsDetail.getText()) || TextUtils.isEmpty(reward.getText()) || TextUtils.isEmpty(purchasingAddressText.getText())) {
            if (confirm.getVisibility() == View.VISIBLE) {
                Animation animationDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
                confirm.startAnimation(animationDown);
                confirm.setVisibility(View.GONE);
            }
        } else {
            if (confirm.getVisibility() == View.GONE) {
                Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
                confirm.startAnimation(animationUp);
                confirm.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case CHOOSE_LOCALE:
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("address")) {
                        String address = data.getStringExtra("address");
                        purchasingAddressText.setText(address);
                        order.setPurchasingAddress(address);
                    }
                }
                break;
            case CHOOSE_SHIPPER_INFO:
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("name") && data.hasExtra("phone") && data.hasExtra("address")) {
                        String name = data.getStringExtra("name");
                        order.setShipperName(name);
                        String phone = data.getStringExtra("phone");
                        order.setShipperPhone(phone);
                        String address = data.getStringExtra("address");
                        order.setShippingAddress(address);
                        recipientInfoText.setText("收货人： " + name + "\n联系方式： " + phone + "\n收货地址： " + address);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        order.setGoodsImagePath(path);
        return path;
    }


    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            goodsPhoto.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}
