package qb.com.top_news.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.adatper.MyListViewAdapter;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.vo.News;


public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private EditText etSearch;
    private ImageView ivSearch, ivReturn;
    private ListView lvSearch;
    private List<News> newsList;
    private MyListViewAdapter mAdapter;
    private DbUtils db;
    private RelativeLayout top_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        lvSearch = (ListView) findViewById(R.id.lvSearch);
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
    }

    @Override
    protected void initData() {
        db = MyApplication.getDb();
        newsList = new ArrayList<>();
    }

    @Override
    protected void setAdapter() {

    }

    @Override
    protected void initEvent() {
        ivSearch.setOnClickListener(this);
        ivReturn.setOnClickListener(this);
        top_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearch:
                try {
                    System.out.println("hahah");
                    newsList = db.findAll(Selector.from(News.class).where("title", "like", "%" + etSearch.getText().toString() + "%")
                            .or("date", "like", "%" + etSearch.getText().toString() + "%").or("category", "like", "%" + etSearch.getText().toString() + "%")
                            .or("realtype", "like", "%" + etSearch.getText().toString() + "%").or("author_name", "like", "%" + etSearch.getText().toString() + "%"));
                    if (newsList.size() != 0) {
                        mAdapter = new MyListViewAdapter(SearchActivity.this, newsList);
                        lvSearch.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(SearchActivity.this, "搜索结束，并为您展现数据", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SearchActivity.this, "很遗憾，没有相关的数据", Toast.LENGTH_SHORT).show();
                    }
                    hidden(v);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ivReturn:
                openActivity(MainActivity.class);
                finish();
                break;
            case R.id.top_layout:
                hidden(v);
        }
    }

    public void hidden(View v) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
