package qb.com.top_news.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import qb.com.top_news.vo.News;
import qb.com.top_news.vo.User;

public class MyApplication extends Application {
    public static RequestQueue mQueue;
    public static DbUtils db;

    @Override
    public void onCreate() {
        super.onCreate();
        mQueue = Volley.newRequestQueue(getApplicationContext());
        db = DbUtils.create(this, "top_db");
        try {
            db.createTableIfNotExist(User.class);
            db.createTableIfNotExist(News.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static RequestQueue getHttpQueue() {
        return mQueue;
    }

    public static DbUtils getDb() {
        return db;
    }
}
