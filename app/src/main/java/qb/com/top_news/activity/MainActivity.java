package qb.com.top_news.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.R;
import qb.com.top_news.adatper.MyListViewAdapter;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.fragment.NewsFragment;
import qb.com.top_news.utils.ImageUtils;
import qb.com.top_news.vo.User;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;
    private ImageView ivHead, ivRefresh, ivItem;
    private List<TextView> mTextViews;
    private SlidingMenu menu;
    private TextView top, shehui, guonei, guoji, yule;

    private NewsFragment mFragment;

    private String[] items = new String[]{"选择本地图片", "拍照"};
    private static final String SHAREDPREFERENCES_NAME = "Login_Status";
    private DbUtils db;
    private User user;

    //menu item
    private Button btLogOut;
    private TextView tvSearch, tvApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setSlidingMenu();
        initData();
        resetColor();
        setAdapter();
        initEvent();
    }


    protected void initView() {
        menu = new SlidingMenu(this);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        top = (TextView) findViewById(R.id.top);
        shehui = (TextView) findViewById(R.id.shehui);
        guonei = (TextView) findViewById(R.id.guonei);
        guoji = (TextView) findViewById(R.id.guoji);
        yule = (TextView) findViewById(R.id.yule);
        ivHead = (ImageView) findViewById(R.id.ivHead);
        ivItem = (ImageView) findViewById(R.id.ivItem);
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
    }

    private void setSlidingMenu() {
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.color.colorPrimaryDark);

        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.leftmenu);
        btLogOut = (Button) menu.findViewById(R.id.btlogOut);
        tvSearch = (TextView) menu.findViewById(R.id.tvSearch);
        tvApp = (TextView) menu.findViewById(R.id.tvApp);
    }

    protected void initData() {
        mTextViews = new ArrayList<>();
        mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mFragment = new NewsFragment();
            Bundle mBundle = new Bundle();
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
        db = MyApplication.getDb();
        try {
            SetHeadByDb();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    protected void setAdapter() {
        mViewPager.setAdapter(mAdapter);
        switchItem();
    }

    protected void initEvent() {
        mViewPager.addOnPageChangeListener(this);
        ivItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RotateAnimation animation = new RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(1000);
                ivRefresh.startAnimation(animation);
                mFragment = (NewsFragment) mFragments.get(mViewPager.getCurrentItem());
                mFragment.doVolley(mFragment.getUrl());
            }
        });
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setTitle("设置头像").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ImageUtils.chooseImageFromGallery(MainActivity.this);
                                break;
                            case 1:
                                ImageUtils.chooseImageFromCamera(MainActivity.this);
                                break;
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(LoginActivity.class);
                finish();
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SearchActivity.class);
            }
        });
        tvApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(AppActivity.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case ImageUtils.CODE_GALLERY_REQUEST:
                ImageUtils.cropRawPhoto(this, data.getData());
                break;

            case ImageUtils.CODE_CAMERA_REQUEST:
                if (ImageUtils.hasSDCard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            ImageUtils.IMAGE_FILE_NAME);
                    ImageUtils.cropRawPhoto(this, Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case ImageUtils.CODE_RESULT_REQUEST:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = bundle.getParcelable("data");
                        ivHead.setImageBitmap(photo);
                        user.setHeadPath(ImageUtils.Bitmap2String(photo));
                        try {
                            db.saveOrUpdate(user);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            } else {
                top.setTextColor(Color.RED);
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                resetColor();
                top.setTextColor(Color.RED);
                break;
            case 1:
                menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                resetColor();
                shehui.setTextColor(Color.RED);
                break;
            case 2:
                resetColor();
                menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                guonei.setTextColor(Color.RED);
                break;
            case 3:
                resetColor();
                menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                guoji.setTextColor(Color.RED);
                break;
            case 4:
                resetColor();
                menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                yule.setTextColor(Color.RED);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void SetHeadByDb() throws DbException {
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        user = db.findFirst(Selector.from(User.class).where("id", "=", String.valueOf(preferences.getInt("LoginId", -1))));
        if (user.getHeadPath() != null) {
            ivHead.setImageBitmap(ImageUtils.String2Bitmap(user.getHeadPath()));
        }
    }

}
