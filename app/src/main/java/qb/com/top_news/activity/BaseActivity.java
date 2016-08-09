package qb.com.top_news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import qb.com.top_news.utils.ActivityController;

/**
 * Created by qianbin on 16/8/9.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    //初始化控件
    protected abstract void initView();

    //初始化数据
    protected abstract void initData();

    //设置适配器
    protected abstract void setAdapter();

    //设置监听事件
    protected abstract void initEvent();

    //通过类名启动Activity
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    //通过类名启动Activity并携带Bundle数据
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    //通过action启动Activity
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    //通过action启动Activity并携带Bundle数据
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    //结束所有Activity允许子类调用
    protected void finishAll() {
        ActivityController.finishAll();
    }

}
