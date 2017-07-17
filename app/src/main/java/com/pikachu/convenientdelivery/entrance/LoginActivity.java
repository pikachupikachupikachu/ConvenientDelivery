package com.pikachu.convenientdelivery.entrance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pikachu.convenientdelivery.HomeActivity;
import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityLoginBinding;
import com.pikachu.convenientdelivery.listener.PerfectClickListener;
import com.pikachu.convenientdelivery.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    String phone;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        super.init();
        setSupportActionBar(bindingView.tbLogin);
        initListener();

//        从注册界面跳转过来，自动填写用户名和密码
//        从忘记密码界面跳转过来，自动填写用户名
        if (getIntent() != null) {
            Intent intent = getIntent();
            String usernameF = intent.getStringExtra(ForgetPwdActivity.FROM_FORGET_USERNAME);
            String username = intent.getStringExtra(RegisterActivity.FROM_REGISTER_USERNAME);
            String pwd = intent.getStringExtra(RegisterActivity.FROM_REGISTER_PWD);
            if(!TextUtils.isEmpty(pwd)){
                bindingView.etPwd.setText(pwd);
            }
            if(!TextUtils.isEmpty(username)){
                bindingView.etPhone.setText(username);
            }else if(!TextUtils.isEmpty(usernameF)){
                bindingView.etPhone.setText(usernameF);
            }

        }


    }

    private void initListener() {
        bindingView.btnLogin.setOnClickListener(listener);
        bindingView.tvForgetPwd.setOnClickListener(listener);
        bindingView.tvSmsLogin.setOnClickListener(listener);

        bindingView.tbLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    login();
                    break;
                case R.id.tv_forget_pwd:
                    jumpTo(ForgetPwdActivity.class, false);
                    break;
                case R.id.tv_sms_login:
                    jumpTo(SmsLoginActivity.class, false);
                    break;


            }
        }
    };

    /**
     * 渲染菜单文件
     *
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_register:
                jumpTo(RegisterActivity.class, false);
                break;
        }

        return true;
    }

    /**
     * 登录逻辑
     */
    private void login() {
        if (isCheckData()) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(android.R.style.Theme_Material_Dialog_Alert);
            progressDialog.setMessage("正在登录中...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            final User user = new User();
            user.setPassword(pwd);
            user.setUsername(phone);

            user.login(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        showToast("登录成功！");
                        jumpTo(HomeActivity.class, true);
                        progressDialog.dismiss();
                    } else {
                        showToast("用户名或密码错误！");
                        progressDialog.dismiss();
                    }
                }
            });


        }
    }

    /**
     * 检查手机号码的合法性
     */

    public boolean isPhoneNum(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检查输入数据的合法性
     */

    public boolean isCheckData() {
        phone = bindingView.etPhone.getText().toString();
        pwd = bindingView.etPwd.getText().toString();
        bindingView.tlPhone.setError(null);
        bindingView.tlPwd.setError(null);
        //判断2个输入是否为空
        if (TextUtils.isEmpty(phone)) {
            bindingView.tlPhone.setError("手机号码不能为空");
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            bindingView.tlPwd.setError("密码不能为空");
            return false;
        } else if (!isPhoneNum(phone)) {
            bindingView.tlPhone.setError("手机号码格式错误");
            return false;
        } else if (pwd.length() < 6) {
            bindingView.tlPwd.setError("密码长度至少为6位");
            return false;
        }
        return true;
    }

}
