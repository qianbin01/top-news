package qb.com.top_news.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import qb.com.top_news.R;
import qb.com.top_news.service.DownloadService;
import qb.com.top_news.vo.FileInfo;


public class TestActivity extends Activity {
    private TextView tvFileName;
    private ProgressBar pbFile;
    private Button btnStart, btnStop;
    private FileInfo fileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        this.tvFileName = (TextView) findViewById(R.id.tvFileName);
        this.pbFile = (ProgressBar) findViewById(R.id.pbFile);
        this.btnStart = (Button) findViewById(R.id.btnStart);
        this.btnStop = (Button) findViewById(R.id.btnStop);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        System.out.println(url);
        String name = bundle.getString("filename");
        fileInfo = new FileInfo(url, 0, name + ".apk", 0, 0);
        tvFileName.setText(name + ".apk");
        pbFile.setMax(100);
    }

    private void initEvent() {
        this.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(TestActivity.this, DownloadService.class);
                startIntent.setAction(DownloadService.START_ACTION);
                startIntent.putExtra(DownloadService.FILE_INTENT, fileInfo);
                startService(startIntent);

            }
        });
        this.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(TestActivity.this, DownloadService.class);
                stopIntent.setAction(DownloadService.STOP_ACTION);
                stopIntent.putExtra(DownloadService.FILE_INTENT, fileInfo);
                startService(stopIntent);
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.UPDATE_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadService.UPDATE_ACTION)) {
                int nowProgress = intent.getIntExtra(DownloadService.LOAD_PROGRESS, 0);
                System.out.println(nowProgress);
                pbFile.setProgress(nowProgress);
                if (pbFile.getProgress() >= 100) {
                    Toast.makeText(context, "下载完成！", Toast.LENGTH_SHORT).show();
                }
            }


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        stopService(new Intent(TestActivity.this, DownloadService.class));
    }
}
