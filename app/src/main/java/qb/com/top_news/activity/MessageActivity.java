package qb.com.top_news.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.fragment.ChatFragment;
import qb.com.top_news.fragment.ContactFragment;

public class MessageActivity extends BaseActivity {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;
    private TextView tvChat, tvContact;
    private LinearLayout linearLayout;//后期用来坐消息未读数量
    private int mScreen1_2;
    private ImageView ivLine, ivReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        initView();
        initData();
        setAdapter();
        initEvent();
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        tvChat = (TextView) findViewById(R.id.tvChat);
        tvContact = (TextView) findViewById(R.id.tvContact);
        ivLine = (ImageView) findViewById(R.id.ivLine);
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
        linearLayout = (LinearLayout) findViewById(R.id.layout_ll1);
        getWindowWidth();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivLine.getLayoutParams();
        layoutParams.width = mScreen1_2;
        ivLine.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        fragmentList = new ArrayList<>();
        ChatFragment chatFragment = new ChatFragment();
        ContactFragment contactFragment = new ContactFragment();
        fragmentList.add(chatFragment);
        fragmentList.add(contactFragment);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
    }

    @Override
    protected void setAdapter() {
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivLine.getLayoutParams();
                lp.leftMargin = (int) (position * mScreen1_2 + positionOffset * mScreen1_2);
                ivLine.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvChat.setTextColor(Color.parseColor("#3d85c6"));
                        tvContact.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        tvChat.setTextColor(Color.BLACK);
                        tvContact.setTextColor(Color.BLACK);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.class);
                finish();
            }
        });
        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                tvChat.setTextColor(Color.parseColor("#3d85c6"));
                tvContact.setTextColor(Color.BLACK);
            }
        });

        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
                tvChat.setTextColor(Color.BLACK);
                tvContact.setTextColor(Color.parseColor("#3d85c6"));
            }
        });
    }

    public void getWindowWidth() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        mScreen1_2 = displayMetrics.widthPixels / 2;
    }


}
