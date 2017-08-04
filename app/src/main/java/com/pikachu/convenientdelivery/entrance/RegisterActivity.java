package com.pikachu.convenientdelivery.entrance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.pikachu.convenientdelivery.R;
import com.pikachu.convenientdelivery.base.BaseActivity;
import com.pikachu.convenientdelivery.databinding.ActivityRegisterBinding;
import com.pikachu.convenientdelivery.listener.PerfectClickListener;
import com.pikachu.convenientdelivery.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding> {
    public static final String FROM_REGISTER_USERNAME = "from_username";
    public static final String FROM_REGISTER_PWD = "from_pwd";

    String phone;
    String smsCode;
    String nick;
    String pwd;

    final User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        super.init();
        setSupportActionBar(bindingView.tbRegister);
        bindingView.tbRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initListener();
    }

    private void initListener() {
        bindingView.btnRegister.setOnClickListener(listener);
        bindingView.tvSms.setOnClickListener(listener);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register:
                    register();
                    break;
                case R.id.tv_sms:
                    requestCode();
                    break;
            }
        }
    };

    /**
     * 注册逻辑，注册成功后，跳转到登录界面
     */

    private void register() {
        if (checkData()) {
            user.setNick(nick);
            user.setPassword(pwd);
            user.setUsername(phone);
            user.setPhone(phone);
            verifySmsCode();
        }
    }

    /**
     * 验证短信
     */
    private void verifySmsCode() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("正在注册...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        BmobSMS.verifySmsCode(phone, smsCode, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null) {
                                showToast("注册成功,请登录");
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra(FROM_REGISTER_USERNAME, phone);
                                intent.putExtra(FROM_REGISTER_PWD, pwd);
                                jumpTo(intent, true);

                                progress.dismiss();
                            } else {
                                if (e.getErrorCode() == 202) {
                                    showToast("该手机号已经注册");
                                } else {
                                    showToast("错误代码" + e.getErrorCode());
                                    Log.e("errorCode", "" + e.getErrorCode());
                                }

                                progress.dismiss();
                            }

                        }

                    });
                } else {
                    showToast("手机号码验证失败！" + e.getMessage());
                    progress.dismiss();
                }
            }
        });
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
     * 判断输入的合法性
     */

    public boolean checkData() {
        phone = bindingView.etPhone.getText().toString();
        smsCode = bindingView.etSms.getText().toString();
        nick = bindingView.etNick.getText().toString();
        pwd = bindingView.etPwd.getText().toString();
        bindingView.tlPhone.setError(null);
        bindingView.tlSms.setError(null);
        bindingView.tlPwd.setError(null);
        bindingView.tlNick.setError(null);
        bindingView.tlPwd.setError(null);

        if (TextUtils.isEmpty(phone)) {
            bindingView.tlPhone.setError("手机号码不能为空");
            return false;
        } else if (!isPhoneNum(phone)) {
            bindingView.tlPhone.setError("手机号码格式错误");
            return false;
        } else if (TextUtils.isEmpty(smsCode)) {
            bindingView.tlSms.setError("验证码不能为空");
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            bindingView.tlPwd.setError("密码不能为空");
            return false;
        } else if (TextUtils.isEmpty(nick)) {
            bindingView.tlNick.setError("昵称不能为空");
            return false;
        } else if (!isStringFormatCorrect(nick)) {
            bindingView.tlNick.setError("昵称只能为数字、字母、下划线、中文");
            return false;
        } else if (pwd.length() < 6) {
            bindingView.tlPwd.setError("密码长度至少为6位");
            return false;
        }
        return true;
    }


    /**
     * 利用正则表达式，判断手机号码合法性
     *
     * @param str
     * @return
     */
    private boolean isPhoneNum(String str) {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 利用正则表达式，判断昵称合法性，只能为数字、字母、下划线、中文
     *
     * @param str
     * @return
     */

    public boolean isStringFormatCorrect(String str) {
        String strPattern = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
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
