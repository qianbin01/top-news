package qb.com.top_news.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import qb.com.top_news.R;


public class SplashActivity extends Activity {

    boolean isLogin = false;
    private static final int GO_LOGIN = 1000;
    private static final int GO_MAIN = 1001;
    private static final long DELAY_TIMES = 3000;
    private static final String SHAREDPREFERENCES_NAME = "Login_Status";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_LOGIN:
                    goLogin();
                    break;
                case GO_MAIN:
                    goMain();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        init();
    }

    private void init() {
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        isLogin = preferences.getBoolean("isLogin", false);
        if (isLogin) {
            mHandler.sendEmptyMessageDelayed(GO_MAIN, DELAY_TIMES);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, DELAY_TIMES);
        }
    }

    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private void goMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
}
