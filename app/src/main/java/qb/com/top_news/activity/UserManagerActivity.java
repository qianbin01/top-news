package qb.com.top_news.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import qb.com.top_news.R;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.vo.User;

public class UserManagerActivity extends BaseActivity {
    private RelativeLayout rlPhone, rlSignature, rlNickname, rlChange;
    private TextView tvPhone, tvSignature, tvNickname;
    private ImageView ivReturn;
    private Button btQuit;
    private DbUtils db;
    private User user;
    private static final String SHAREDPREFERENCES_NAME = "Login_Status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manager_layout);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        rlPhone = (RelativeLayout) findViewById(R.id.rlPhone);
        rlSignature = (RelativeLayout) findViewById(R.id.rlSignature);
        rlNickname = (RelativeLayout) findViewById(R.id.rlNickname);
        rlChange = (RelativeLayout) findViewById(R.id.rlChange);
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvSignature = (TextView) findViewById(R.id.tvSignature);
        tvNickname = (TextView) findViewById(R.id.tvNickname);
        btQuit = (Button) findViewById(R.id.btQuit);
    }

    @Override
    protected void initData() {
        db = MyApplication.getDb();
        initInfo();
    }

    @Override
    protected void setAdapter() {

    }

    @Override
    protected void initEvent() {
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SettingActivity.class);
            }
        });
        rlSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(UserManagerActivity.this).create();
                View view = View.inflate(UserManagerActivity.this, R.layout.edit_dialog, null);
                final EditText etInput = (EditText) view.findViewById(R.id.etInput);
                final Button btSure = (Button) view.findViewById(R.id.btSure);
                final Button btCancel = (Button) view.findViewById(R.id.btCancel);
                alertDialog.setView(view);
                alertDialog.show();
                btSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etInput.getText())) {
                            Toast.makeText(UserManagerActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            user.setSignature(etInput.getText().toString());
                            try {
                                db.saveOrUpdate(user);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            tvSignature.setText(user.getSignature());
                            alertDialog.dismiss();
                        }
                    }
                });
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        rlNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(UserManagerActivity.this).create();
                View view = View.inflate(UserManagerActivity.this, R.layout.edit_dialog, null);
                final EditText etInput = (EditText) view.findViewById(R.id.etInput);
                final Button btSure = (Button) view.findViewById(R.id.btSure);
                final Button btCancel = (Button) view.findViewById(R.id.btCancel);
                alertDialog.setView(view);
                alertDialog.show();
                btSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(etInput.getText())) {
                            Toast.makeText(UserManagerActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if (etInput.getText().length() <= 30) {
                                user.setNickname(etInput.getText().toString());
                                try {
                                    db.saveOrUpdate(user);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                tvNickname.setText(user.getNickname());
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(UserManagerActivity.this, "昵称长度不可超过30个字符", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        rlChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(UserManagerActivity.this).create();
                View view = View.inflate(UserManagerActivity.this, R.layout.edit_change_dialog, null);
                final EditText etPassword = (EditText) view.findViewById(R.id.etPassword);
                final EditText etSure = (EditText) view.findViewById(R.id.etSure);
                final Button btSure = (Button) view.findViewById(R.id.btSure);
                final Button btCancel = (Button) view.findViewById(R.id.btCancel);
                alertDialog.setView(view);
                alertDialog.show();
                btSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(etPassword.getText())&!TextUtils.isEmpty(etSure.getText())){
                            if(etPassword.getText().length()>=6) {
                                if (etPassword.getText().toString().equals(etSure.getText().toString())) {
                                    user.setPassword(etPassword.getText().toString());
                                    try {
                                        db.saveOrUpdate(user);
                                    } catch (DbException e) {
                                        e.printStackTrace();
                                    }
                                    alertDialog.dismiss();
                                    Toast.makeText(UserManagerActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserManagerActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(UserManagerActivity.this, "密码长度不正确", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(UserManagerActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });
        btQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(LoginActivity.class);
                finishAll();
            }
        });
    }

    private void initInfo() {
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        try {
            user = db.findFirst(Selector.from(User.class).where("id", "=", String.valueOf(preferences.getInt("LoginId", -1))));
        } catch (DbException e) {
            e.printStackTrace();
        }
        tvPhone.setText(user.getPhone());

        if (user.getSignature() == null) {
            tvSignature.setText(R.string.signature_test);
        } else {
            tvSignature.setText(user.getSignature());
        }
        if (user.getNickname() == null) {
            tvNickname.setText(R.string.nick_test);
        } else {
            tvNickname.setText(user.getNickname());
        }
    }
}
