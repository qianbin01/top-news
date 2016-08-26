package qb.com.top_news.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.fragment.OpinionFragment;
import qb.com.top_news.fragment.QuestionFragment;
import qb.com.top_news.vo.SystemChatInfo;


public class FeedbackActivity extends BaseActivity {
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;
    private TextView tvOpinion, tvQuestion;
    private LinearLayout linearLayout;//后期用来坐消息未读数量
    private int mScreen1_2;
    private EditText etMessage;
    private ImageView ivLine, ivReturn, ivSend;
    private OpinionFragment opinionFragment;
    private QuestionFragment questionFragment;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        initView();
        initData();
        setAdapter();
        initEvent();
    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        etMessage = (EditText) findViewById(R.id.etMessage);
        tvOpinion = (TextView) findViewById(R.id.tvOpinion);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        ivLine = (ImageView) findViewById(R.id.ivLine);
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
        ivSend = (ImageView) findViewById(R.id.ivSend);
        linearLayout = (LinearLayout) findViewById(R.id.layout_ll1);
        getWindowWidth();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivLine.getLayoutParams();
        layoutParams.width = mScreen1_2;
        ivLine.setLayoutParams(layoutParams);
    }

    @Override
    protected void initData() {
        fragmentList = new ArrayList<>();
        opinionFragment = new OpinionFragment();
        questionFragment = new QuestionFragment();
        fragmentList.add(opinionFragment);
        fragmentList.add(questionFragment);
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
        intentFilter = new IntentFilter("send");
        registerReceiver(mReceiver, intentFilter);
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
                        tvOpinion.setTextColor(Color.parseColor("#3d85c6"));
                        tvQuestion.setTextColor(Color.BLACK);
                        break;
                    case 1:
                        tvOpinion.setTextColor(Color.BLACK);
                        tvQuestion.setTextColor(Color.parseColor("#3d85c6"));
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
        tvOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                tvOpinion.setTextColor(Color.parseColor("#3d85c6"));
                tvQuestion.setTextColor(Color.BLACK);
            }
        });
        tvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                tvOpinion.setTextColor(Color.BLACK);
                tvQuestion.setTextColor(Color.parseColor("#3d85c6"));
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().toString() != null & !etMessage.getText().toString().equals("")) {
                    SystemChatInfo systemChatInfo = new SystemChatInfo("", "", "", etMessage.getText().toString(), false);
                    try {
                        opinionFragment.addItem(systemChatInfo);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    etMessage.setText("");
                    Intent intent = new Intent("send");
                    sendBroadcast(intent);
                } else {
                    Toast.makeText(FeedbackActivity.this, "请输入你想反馈的问题", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getWindowWidth() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        mScreen1_2 = displayMetrics.widthPixels / 2;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SystemChatInfo systemChatInfo = new SystemChatInfo("", "1", "", "反馈问题成功，感谢你的支持。稍后会有专业解答", true);
            try {
                opinionFragment.addItem(systemChatInfo);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}