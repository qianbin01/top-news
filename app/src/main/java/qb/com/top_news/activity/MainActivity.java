package qb.com.top_news.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.fragment.NewsFragment;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;
    private List<TextView> mTextViews;
    private TextView top, shehui, guonei, guoji, yule;
    private Bundle mBundle;
    private NewsFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setAdapter();
    }

    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        top = (TextView) findViewById(R.id.top);
        shehui = (TextView) findViewById(R.id.shehui);
        guonei = (TextView) findViewById(R.id.guonei);
        guoji = (TextView) findViewById(R.id.guoji);
        yule = (TextView) findViewById(R.id.yule);

    }

    protected void initData() {
        mTextViews = new ArrayList<TextView>();
        mFragments = new ArrayList<Fragment>();
        for (int i = 0; i < 5; i++) {
            mFragment = new NewsFragment();
            mBundle = new Bundle();
            mBundle.putInt("url", i);
            mFragment.setArguments(mBundle);
            mFragments.add(mFragment);
        }
        mTextViews.add(top);
        mTextViews.add(shehui);
        mTextViews.add(guonei);
        mTextViews.add(guoji);
        mTextViews.add(yule);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
            }
        };

    }

    protected void setAdapter() {
        mViewPager.setAdapter(mAdapter);
        switchItem();
    }

    protected void initEvent() {

    }

    public void initEvent(View v) {
        switch (v.getId()) {
            case R.id.top:
                mViewPager.setCurrentItem(0);
                resetColor();
                top.setTextColor(Color.RED);
                break;
            case R.id.shehui:
                mViewPager.setCurrentItem(1);
                resetColor();
                shehui.setTextColor(Color.RED);
                break;
            case R.id.guonei:
                mViewPager.setCurrentItem(2);
                resetColor();
                guonei.setTextColor(Color.RED);
                break;
            case R.id.guoji:
                mViewPager.setCurrentItem(3);
                resetColor();
                guoji.setTextColor(Color.RED);
                break;
            case R.id.yule:
                mViewPager.setCurrentItem(4);
                resetColor();
                yule.setTextColor(Color.RED);
                break;

        }
    }

    private void resetColor() {
        for (int i = 0; i < mTextViews.size(); i++) {
            mTextViews.get(i).setTextColor(Color.BLACK);
        }
    }

    private void switchItem() {
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().getString("flag") != null) {
                    switch (getIntent().getExtras().getString("flag")) {
                        case "top":
                            mViewPager.setCurrentItem(0);
                            top.setTextColor(Color.RED);
                            break;
                        case "shehui":
                            mViewPager.setCurrentItem(1);
                            shehui.setTextColor(Color.RED);
                            break;
                        case "guonei":
                            mViewPager.setCurrentItem(2);
                            guonei.setTextColor(Color.RED);
                            break;
                        case "guoji":
                            mViewPager.setCurrentItem(3);
                            guoji.setTextColor(Color.RED);
                            break;
                        case "yule":
                            mViewPager.setCurrentItem(4);
                            yule.setTextColor(Color.RED);
                            break;
                    }
                }
            }
        } else {
            resetColor();
            top.setTextColor(Color.RED);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
