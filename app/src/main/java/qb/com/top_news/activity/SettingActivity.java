package qb.com.top_news.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.io.File;

import qb.com.top_news.R;
import qb.com.top_news.application.MyApplication;
import qb.com.top_news.utils.CacheManager;
import qb.com.top_news.utils.ImageUtils;
import qb.com.top_news.vo.User;


public class SettingActivity extends BaseActivity {

    private ImageView ivReturn, ivHead;
    private TextView tvCache;
    private RelativeLayout rlCache, rlDownload, rlUser, rlAbout;
    private String[] items = new String[]{"选择本地图片", "拍照"};
    private static final String SHAREDPREFERENCES_NAME = "Login_Status";
    private DbUtils db;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void initView() {
        ivReturn = (ImageView) findViewById(R.id.ivReturn);
        ivHead = (ImageView) findViewById(R.id.ivHead);
        tvCache = (TextView) findViewById(R.id.tvCache);
        rlCache = (RelativeLayout) findViewById(R.id.rlCache);
        rlDownload = (RelativeLayout) findViewById(R.id.rlDownload);
        rlUser = (RelativeLayout) findViewById(R.id.rlUser);
        rlAbout = (RelativeLayout) findViewById(R.id.rlAbout);
    }

    @Override
    protected void initData() {
        db = MyApplication.getDb();
        try {
            tvCache.setText(CacheManager.getTotalCacheSize(this));
            setHeadByDb();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setAdapter() {

    }

    @Override
    protected void initEvent() {
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingActivity.this).setTitle("设置头像").setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ImageUtils.chooseImageFromGallery(SettingActivity.this);
                                break;
                            case 1:
                                ImageUtils.chooseImageFromCamera(SettingActivity.this);
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
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MainActivity.class);
                finish();
            }
        });
        rlDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("下载管理测试");
            }
        });
        rlUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(UserManagerActivity.class);
            }
        });
        rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(AboutActivity.class);
            }
        });
        rlCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingActivity.this).setTitle("清理缓存").setMessage("确认清理吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CacheManager.clearAllCache(SettingActivity.this);
                        try {
                            tvCache.setText(CacheManager.getTotalCacheSize(SettingActivity.this));
                        } catch (Exception e) {
                            e.printStackTrace();
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
                        Intent intent = new Intent("change_head");
                        sendBroadcast(intent);
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

    private void setHeadByDb() throws DbException {
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        user = db.findFirst(Selector.from(User.class).where("id", "=", String.valueOf(preferences.getInt("LoginId", -1))));
        if (user.getHeadPath() != null) {
            ivHead.setImageBitmap(ImageUtils.String2Bitmap(user.getHeadPath()));
        }
    }
}
