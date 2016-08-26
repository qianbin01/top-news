package qb.com.top_news.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.adatper.MyLikeAdapter;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.vo.News;


public class LikeActivity extends BaseActivity {
    private ListView lvLike;
    private MyLikeAdapter mAdapter;
    private ImageView ivReturn;
    private List<News> newsList;
    private DbUtils db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.like_layout);
        initView();
        initData();
        setAdapter();
        initEvent();
    }

    @Override
    protected void initView() {
        lvLike = (ListView) findViewById(R.id.lvLike);
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
    }

    @Override
    protected void initData() {
        db = MyApplication.getDb();
        try {
            newsList = db.findAll(Selector.from(News.class).where("like", "=", "1"));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setAdapter() {
        if (newsList != null) {
            mAdapter = new MyLikeAdapter(this, newsList);
            lvLike.setAdapter(mAdapter);
        }else{
        }
    }

    @Override
    protected void initEvent() {
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.class);
                
            }
        });
    }
}
