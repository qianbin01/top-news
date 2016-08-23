package qb.com.top_news.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import qb.com.top_news.R;

public class AboutActivity extends BaseActivity {
    @ViewInject(R.id.ivReturn)
    private ImageView ivReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        ViewUtils.inject(this);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setAdapter() {

    }

    @Override
    protected void initEvent() {

    }

    @OnClick(R.id.ivReturn)
    public void OnClick(View v) {
        openActivity(SettingActivity.class);
        finish();
    }
}
