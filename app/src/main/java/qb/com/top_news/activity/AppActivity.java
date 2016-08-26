package qb.com.top_news.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.adatper.MyAppAdapter;
import qb.com.top_news.utils.SpiderApp;
import qb.com.top_news.vo.AppInfo;

public class AppActivity extends BaseActivity {
    private ListView lvApp;
    private List<AppInfo> list;
    private MyAppAdapter mAdapter;
    private ImageView ivReturn;
    private EditText etSearch;
    private ImageView ivSearch;
    private Handler handler;
    private MyThread myThread;
    private String str = "abc";
    private LinearLayout app_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_layout);
        initView();
        initData();
        setAdapter();
        initEvent();
    }

    @Override
    protected void initView() {
        lvApp = (ListView) findViewById(R.id.lvApp);
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        app_layout = (LinearLayout) findViewById(R.id.app_layout);
    }

    @Override
    protected void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                list = (List<AppInfo>) msg.getData().getSerializable("list");
                if (list != null) {
                    mAdapter = new MyAppAdapter(AppActivity.this, list);
                    lvApp.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AppActivity.this, "对不起,没有搜索到相应的应用", Toast.LENGTH_SHORT).show();
                }

            }
        };
        myThread = new MyThread(handler);
        myThread.start();
    }

    @Override
    protected void setAdapter() {

    }

    @Override
    protected void initEvent() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = etSearch.getText().toString();
                myThread = null;
                myThread = new MyThread(handler);
                myThread.start();
            }
        });
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.class);
                finish();
            }
        });
        app_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidden(v);
            }
        });
        lvApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("detail", list.get(position).getDetail());
                bundle.putString("down", list.get(position).getDownPath());
                System.out.println(list.get(position).getDownPath());
                bundle.putString("name", list.get(position).getName());
                bundle.putString("img", list.get(position).getImgPath());
                bundle.putString("star", list.get(position).getStars());
                bundle.putString("tips", list.get(position).getTips());
                openActivity(AppDetailActivity.class, bundle);

            }
        });

    }

    class MyThread extends Thread {
        private Handler handler;

        public MyThread(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            if ("abc".equals(str)) {
            } else {
                List<AppInfo> threadList = SpiderApp.getSearch(str, "");
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) threadList);
                message.setData(bundle);
                handler.sendMessage(message);
            }

        }
    }


    //隐藏软键盘
    public void hidden(View v) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
