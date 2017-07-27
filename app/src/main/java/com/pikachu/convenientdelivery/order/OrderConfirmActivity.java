package com.pikachu.convenientdelivery.order;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.app.Activity;
import android.widget.Toast;


import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityOrderConfirmBinding;
import com.pikachu.convenientdelivery.model.Order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;

/**
 * Created by JinBo on 2017/7/20.
 */

public class OrderConfirmActivity extends BaseActivity<ActivityOrderConfirmBinding> implements View.OnClickListener  {

    private Toolbar toolbar;
    private Button publish;
    private Button cancel;

    private TextView requirement;
    private TextView message;
    private TextView goods;
    private TextView locale;
    private TextView pay;
    private TextView orderID;
    private TextView tv;
    private TextView percent;
    private ImageView picture;
    private CardView addressView;
    private boolean priceway;

    
    ProgressDialog dialog;

    Order order = new Order();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BP.init("560ac477463924eba4cfca3a00353215");
        initView();
        MyOrder();
    }

    private void MyOrder() {
        Intent intent = getIntent();
        order=(Order) intent.getSerializableExtra("Order");
        requirement.setText(order.getRequirement());
        message.setText(order.getMessage());
        goods.setText(order.getGoods());
        locale.setText(order.getLocale());
        pay.setText(order.getPay());
        Bitmap bitmap = BitmapFactory.decodeFile(order.getImagePath());
        picture.setImageBitmap(bitmap);

        priceway = order.getPriceway();
    }

    private void initView() {
        toolbar = bindingView.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        publish = bindingView.publish;
        cancel = bindingView.cancel;
        requirement = bindingView.requirement;
        message = bindingView.message;
        goods = bindingView.goods;
        locale = bindingView.locale;
        pay = bindingView.pay;
        orderID = bindingView.orderID;
        tv = bindingView.tv;
        picture = bindingView.picture;
        percent = bindingView.percent;
        addressView = bindingView.addressView;

        publish.setOnClickListener(this);
        cancel.setOnClickListener(this);
        addressView.setOnClickListener(this);
        if(priceway){
            percent.setVisibility(View.GONE);
        }else{
            percent.setVisibility(View.VISIBLE);
        }

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
    protected int getLayoutId() {
        return R.layout.activity_order_confirm;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.publish :
                payOrder();
                break;
            case R.id.addressView:
                break;
            case R.id.cancel:
                onBackPressed();
                break;
        }
    }


    //pay 方法
    private void payOrder() {
        showDialog("正在获取订单...\nSDK版本号:" + BP.getPaySdkVersion());

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        BP.pay(order.getGoods(), order.getMessage(),Double.parseDouble(order.getPay()), true, new PListener(){
            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(OrderConfirmActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
                tv.append(order.getGoods() + "'s pay status is unknow\n\n");
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(OrderConfirmActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                tv.append(order.getGoods() + "'s pay status is success\n\n");
                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                orderID.setText(orderId);
                tv.append(order.getGoods() + "'s orderid is " + orderId + "\n\n");
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    Toast.makeText(
                            OrderConfirmActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                    installApk("bp.db");
                } else {
                    Toast.makeText(OrderConfirmActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }
                tv.append(order.getGoods() + "'s pay status is fail, error code is \n"
                       + code + " ,reason is " + reason + "\n\n");
                hideDialog();
            }
        });
    }


    private static final int REQUESTPERMISSION = 101;

    private void installApk(String s) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
        } else {
            installBmobPayPlugin(s);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTPERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    installBmobPayPlugin("bp.db");
                } else {
                    //提示没有权限，安装不了
                    Toast.makeText(OrderConfirmActivity.this,"您拒绝了权限，这样无法安装支付插件",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

     void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }
}
