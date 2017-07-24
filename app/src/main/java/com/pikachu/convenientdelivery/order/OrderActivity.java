package com.pikachu.convenientdelivery.order;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityOrderBinding;
import com.pikachu.convenientdelivery.model.Order;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 下单
 */

public class OrderActivity extends BaseActivity<ActivityOrderBinding> implements View.OnClickListener {

    private Toolbar toolbar;
    private Button confirmord;
    private EditText requirement;
    private EditText message;
    private EditText goods;
    private EditText locale;
    private EditText pay;

    private RadioGroup rg;
    private TextView percent;
    private ImageButton photo;
    private ImageView myphoto;
    private ImageButton choose_locale;



    Order order =new Order();
    public static  final int CHOOSE_PHOTO = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"随手快递");
        initView();
    }

    private void getOrder() {
        order.setRequirement(requirement.getText().toString());
        order.setMessage(message.getText().toString());
        order.setGoods(goods.getText().toString());
        order.setLocale(locale.getText().toString());
        order.setPay(pay.getText().toString());
        order.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Toast.makeText(OrderActivity.this, "添加数据成功，返回objectId为："+objectId, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OrderActivity.this,"创建数据失败"+e.getMessage() , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, OrderActivity.class);
        context.startActivity(intent);
    }

    private void initView() {
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        confirmord = bindingView.confirmord;
        requirement = bindingView.requirement;
        message = bindingView.message;
        goods = bindingView.goods;
        locale = bindingView.locale;
        pay = bindingView.pay;
        rg = bindingView.rg;
        percent = bindingView.percent;
        photo = bindingView.photo;
        myphoto = bindingView.myphoto;
        choose_locale = bindingView.chooseLocale;

        confirmord.setOnClickListener(this);
        photo.setOnClickListener(this);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.rg1)
                {
                    percent.setVisibility(View.GONE);
                    order.setPriceway(true);
                }
                else if (i == R.id.rg2){
                    percent.setVisibility(View.VISIBLE);
                    order.setPriceway(false);
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
            case R.id.confirmord:
                getOrder();
                Bundle data =new Bundle();
                data.putSerializable("Order",order);
                Intent intent = new Intent(v.getContext(), OrderConfirmActivity.class);
                intent.putExtras(data);
                v.getContext().startActivity(intent);
                break;
            //打开系统相册
            case R.id.photo:
                if(ContextCompat.checkSelfPermission(OrderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(OrderActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else{
                    openAlbum();
                }
                break;
            //搜索选择地点
            case R.id.choose_locale:
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
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
        order.setImagePath(path);
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            myphoto.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}
