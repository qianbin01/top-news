package qb.com.top_news.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import qb.com.top_news.R;


public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private ImageView ivReturn;
    private Bundle mBundle;
    private ProgressDialog dialog;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        mWebView = (WebView) findViewById(R.id.mWebView);
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
    }

    @Override
    protected void initData() {
        dialog = new ProgressDialog(WebActivity.this);
        mBundle = getIntent().getExtras();
        flag = mBundle.getString("flag");
        String url = mBundle.getString("url");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置可读取JavaScript网页
        webSettings.setAllowFileAccess(true);//可读取文件
        webSettings.setBuiltInZoomControls(true);
        mWebView.loadUrl(url);//要跳转的网页地址
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    closeDialog();
                } else {
                    openDialog(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private void openDialog(int newProgress) {
        if (dialog != null) {
            dialog.setTitle("正在加载");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setProgress(newProgress);
            dialog.show();
        }
    }


    private void closeDialog() {
        if (dialog != null) {
            dialog.hide();
        }
        dialog = null;
    }


    @Override
    protected void setAdapter() {

    }

    @Override
    protected void initEvent() {
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBundle.putString("flag", flag);
                openActivity(MainActivity.class, mBundle);
            }
        });

    }

}
