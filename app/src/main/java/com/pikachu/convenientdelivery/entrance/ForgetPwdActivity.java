package com.pikachu.convenientdelivery.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityForgetPwdBinding;
import com.pikachu.convenientdelivery.listener.PerfectClickListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPwdActivity extends BaseActivity<ActivityForgetPwdBinding> {

    public static final String FROM_FORGET_USERNAME = "from_forge_username";

    String phone;
    String smsCode;
    String newPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }


    @Override
    protected void init() {
        super.init();

        setSupportActionBar(bindingView.tbForgetPwd);
        bindingView.tbForgetPwd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initListener();
    }

    private void initListener() {
        bindingView.tvSms.setOnClickListener(listener);
        bindingView.btnSubmit.setOnClickListener(listener);
    }

    PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.tv_sms:
                    requestCode();
                    break;
                case R.id.btn_submit:
                    resetPwd();
                    break;
            }
        }
    };

    /**
     * 提交按钮的事件，重置密码
     */
    private void resetPwd() {
        if (checkData()) {
            BmobUser.resetPasswordBySMSCode(smsCode, newPwd, new UpdateListener() {

                @Override
                public void done(BmobException ex) {
                    if (ex == null) {
                        showToast("密码重置成功");
                        Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
                        intent.putExtra(FROM_FORGET_USERNAME, phone);
                        jumpTo(intent, true);
                    } else {
                        showToast("密码重置失败" + ex.getMessage() + ex.getErrorCode());
                    }
                }
            });
        }
    }

    /**
     * 请求短信验证码的方法
     */

    private void requestCode() {
        phone = bindingView.etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            bindingView.tlPhone.setError("手机号码不能为空");
            return;
        }
        if (!isPhoneNum(phone)) {
            bindingView.tlPhone.setError("手机号码格式不正确");
            return;
        }
        MyCounter counter = new MyCounter(60000, 1000);
        counter.start();
        BmobSMS.requestSMSCode(phone, "随手快递", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    showToast("验证码发送成功！");
                } else {
                    if (e.getErrorCode() == 10010) {
                        showToast("您请求验证码过去频繁，请于1小时候重试");
                    } else {
                        showToast("验证码发送失败" + e.getErrorCode() + e.getMessage());
                    }

                }
            }
        });
    }

    /**
     * 判断输入合法性
     *
     * @return
     */
    public boolean checkData() {
        phone = bindingView.etPhone.getText().toString();
        smsCode = bindingView.etSms.getText().toString();
        newPwd = bindingView.etNewPwd.getText().toString();

        bindingView.tlSms.setError(null);
        bindingView.tlNewPwd.setError(null);
        bindingView.tlPhone.setError(null);
        if (TextUtils.isEmpty(phone)) {
            bindingView.tlPhone.setError("手机号码不能为空");
            return false;
        } else if (!isPhoneNum(phone)) {
            bindingView.tlPhone.setError("手机号码格式错误");
            return false;
        } else if (TextUtils.isEmpty(smsCode)) {
            bindingView.tlSms.setError("验证码不能为空");
            return false;
        } else if (TextUtils.isEmpty(newPwd)) {
            bindingView.tlNewPwd.setError("密码不能为空");
            return false;
        } else if (newPwd.length() < 6) {
            bindingView.tlSms.setError("密码长度至少为6位");
            return false;
        }
        return true;
    }

    /**
     * 正则表达式，判断手机号码合法性
     */

    private boolean isPhoneNum(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.find();
    }


    /**
     * 自定义计数器
     */
    class MyCounter extends CountDownTimer {

        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //倒计时过程调用的方法
        @Override
        public void onTick(long l) {
            bindingView.tvSms.setText("再次发送" + l / 1000 + "s");
            bindingView.tvSms.setClickable(false);
            bindingView.tvSms.setBackgroundColor(getResources().getColor(R.color.darkGrey));
        }

        //倒计时结束的方法
        @Override
        public void onFinish() {
            bindingView.tvSms.setText("重新发送");
            bindingView.tvSms.setClickable(true);
            bindingView.tvSms.setBackgroundColor(getResources().getColor(R.color.dark));

        }
    }
}
