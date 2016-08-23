package qb.com.top_news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import qb.com.top_news.R;
import qb.com.top_news.service.DownloadService;
import qb.com.top_news.utils.SpiderApp;
import qb.com.top_news.vo.AppDetail;


public class AppDetailActivity extends BaseActivity {
    private ImageView ivReturn, img, pic1, pic2, pic3, pic4, pic5, pic6;
    private TextView name, star, downCount, tips, author, uptime, version, language, desc;
    private Handler handler;
    private MyThread myThread;
    private AppDetail appDetail, mDetail;
    private Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        initView();
        initData();
        setAdapter();
        initEvent();


    }

    @Override
    protected void initView() {
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
        img = (ImageView) findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        star = (TextView) findViewById(R.id.star);
        downCount = (TextView) findViewById(R.id.downCount);
        tips = (TextView) findViewById(R.id.tips);
        download = (Button) findViewById(R.id.download);
        author = (TextView) findViewById(R.id.author);
        uptime = (TextView) findViewById(R.id.uptime);
        version = (TextView) findViewById(R.id.version);
        language = (TextView) findViewById(R.id.language);
        desc = (TextView) findViewById(R.id.desc);
        pic1 = (ImageView) findViewById(R.id.pic1);
        pic2 = (ImageView) findViewById(R.id.pic2);
        pic3 = (ImageView) findViewById(R.id.pic3);
        pic4 = (ImageView) findViewById(R.id.pic4);
        pic5 = (ImageView) findViewById(R.id.pic5);
        pic6 = (ImageView) findViewById(R.id.pic6);
    }

    @Override
    protected void initData() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mDetail = (AppDetail) msg.getData().getSerializable("app");
                downCount.setText(mDetail.getDownCount());
                author.setText(mDetail.getAuthor());
                uptime.setText(mDetail.getUpdateTime());
                version.setText(mDetail.getVersion());
                language.setText(mDetail.getLanguage());
                if (mDetail.getDesc() != "") {
                    desc.setText(mDetail.getDesc());
                }
                if (mDetail.getPicPath1() != null) {
                    Picasso.with(AppDetailActivity.this).load(mDetail.getPicPath1()).resize(300, 300).into(pic1);
                }
                if (mDetail.getPicPath2() != null) {
                    Picasso.with(AppDetailActivity.this).load(mDetail.getPicPath2()).resize(300, 300).into(pic2);
                }
                if (mDetail.getPicPath3() != null) {
                    Picasso.with(AppDetailActivity.this).load(mDetail.getPicPath3()).resize(300, 300).into(pic3);
                }
                if (mDetail.getPicPath4() != null) {
                    Picasso.with(AppDetailActivity.this).load(mDetail.getPicPath4()).resize(300, 300).into(pic4);
                }
                if (mDetail.getPicPath5() != null) {
                    Picasso.with(AppDetailActivity.this).load(mDetail.getPicPath5()).resize(300, 300).into(pic5);
                }
                if (mDetail.getPicPath6() != null) {
                    Picasso.with(AppDetailActivity.this).load(mDetail.getPicPath6()).resize(300, 300).into(pic6);
                }
            }
        };
        myThread = new MyThread(handler);
        myThread.start();
    }

    @Override
    protected void setAdapter() {
        name.setText(getIntent().getExtras().getString("name"));
        star.setText(getIntent().getExtras().getString("star"));
        tips.setText(getIntent().getExtras().getString("tips"));
        Picasso.with(this).load(getIntent().getExtras().getString("img")).into(img);
    }

    @Override
    protected void initEvent() {
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myThread.interrupt();
                myThread = null;
                openActivity(AppActivity.class);

                finish();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(AppDetailActivity.this, DownloadService.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", getIntent().getExtras().getString("down"));
                if (getIntent().getExtras().getString("down") != null) {
                    System.out.println("下载地址:" + getIntent().getExtras().getString("down"));
                    bundle.putString("filename", getIntent().getExtras().getString("name"));
                    openActivity(TestActivity.class, bundle);
                }
//                intent.putExtras(bundle);


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
            appDetail = SpiderApp.getDetail(getIntent().getExtras().getString("detail"));
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putSerializable("app", appDetail);
            message.setData(bundle);
            handler.sendMessage(message);

        }

    }
}


