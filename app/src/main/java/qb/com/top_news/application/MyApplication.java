package qb.com.top_news.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import qb.com.top_news.vo.ChatDetail;
import qb.com.top_news.vo.SystemChatInfo;
import qb.com.top_news.vo.ChatList;
import qb.com.top_news.vo.DownLoadThreadInfo;
import qb.com.top_news.vo.News;
import qb.com.top_news.vo.User;

public class MyApplication extends Application {
    public static RequestQueue mQueue;
    public static DbUtils db;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("应用程序启动");
        mQueue = Volley.newRequestQueue(getApplicationContext());
        db = DbUtils.create(this, "top_db");
        try {
            db.createTableIfNotExist(User.class);
            db.createTableIfNotExist(News.class);
            db.createTableIfNotExist(DownLoadThreadInfo.class);
            db.createTableIfNotExist(SystemChatInfo.class);
            db.createTableIfNotExist(ChatList.class);
            db.createTableIfNotExist(ChatDetail.class);
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
