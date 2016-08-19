package qb.com.top_news.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.vo.User;

@ContentView(R.layout.login_layout)
public class LoginActivity extends BaseActivity {

    private static final String SHAREDPREFERENCES_NAME = "Login_Status";
    private String phone;
    private String password;

    @ViewInject(R.id.etPhone)
    EditText etPhone;

    @ViewInject(R.id.etPassword)
    EditText etPassword;

    @ViewInject(R.id.btLogin)
    Button btLogin;

    @ViewInject(R.id.login_title)
    TextView login_title;

    @ViewInject(R.id.register_title)
    TextView register_title;

    private boolean isLogin = true;//true代表登录，false代表注册
    private DbUtils db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initData();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        db = MyApplication.getDb();
    }

    @Override
    protected void setAdapter() {

    }

    @Override
    protected void initEvent() {

    }

    @OnClick(R.id.btLogin)
    public void Login(View v) throws DbException {
        phone = etPhone.getText().toString();
        password = etPassword.getText().toString();
        List<User> users = new ArrayList<>();
        if (isLogin) {
            users = db.findAll(Selector.from(User.class).where("phone", "=", phone).and("password", "=", password));
            if (users.size() != 0) {
                SharedPreferences preferences = getSharedPreferences(
                        SHAREDPREFERENCES_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLogin", true);
                editor.putInt("LoginId", users.get(0).getId());
                editor.apply();
                openActivity(MainActivity.class);
                finish();
            } else {
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            users = db.findAll(Selector.from(User.class).where("phone", "=", phone));
            if (users.size() == 0) {
                User user = new User();
                user.setPhone(phone);
                user.setPassword(password);
                db.save(user);
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                etPhone.setText("");
                etPassword.setText("");
            } else {
                Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.register_title)
    public void changeToRegister(View v) {
        if (isLogin) {
            login_title.setText(R.string.register);
            register_title.setText(R.string.login);
            btLogin.setText(R.string.register);
            etPhone.setText("");
            etPassword.setText("");
            isLogin = false;
        } else {
            login_title.setText(R.string.login);
            register_title.setText(R.string.register);
            btLogin.setText(R.string.login);
            etPhone.setText("");
            etPassword.setText("");
            isLogin = true;
        }
    }
}
