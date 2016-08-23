package qb.com.top_news.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import qb.com.top_news.utils.DownLoadTask;
import qb.com.top_news.vo.FileInfo;


public class DownloadService extends Service {
    public static final String START_ACTION = "start_action";
    public static final String STOP_ACTION = "stop_action";
    public static final String UPDATE_ACTION = "update_action";
    public static final String FILE_INTENT = "file_intent";
    public static final int MESSAGE_INIT = 4;
    public static final String LOAD_PROGRESS = "load_progress";
    public static final String SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Downloads/";
    private FileInfo mFileInfo;
    private DownLoadTask loadTask;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (START_ACTION.equals(intent.getAction())) {
            mFileInfo = (FileInfo) intent.getSerializableExtra(FILE_INTENT);
            new LoadThread(mFileInfo).start();// 启动线程
        } else if (STOP_ACTION.equals(intent.getAction())) {
            mFileInfo = (FileInfo) intent.getSerializableExtra(FILE_INTENT);
            if (null != loadTask) {
                loadTask.isPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INIT:
                    FileInfo info = (FileInfo) msg.obj;
                    loadTask = new DownLoadTask(DownloadService.this, info);
                    try {
                        loadTask.getLoadThreadInfo();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    class LoadThread extends Thread {
        private FileInfo fileInfo;

        public LoadThread(FileInfo fileInfo) {
            this.fileInfo = fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            int length = -1;
            try {
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);// 设置超时
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() == 200) {//请求是否成功
                    length = conn.getContentLength();
                }
                if (length <= 0) {
                    return;
                }
                File fileDir = new File(SAVE_PATH);
                if (!fileDir.exists()) {//如果目录不存在则创建
                    fileDir.mkdir();
                }
                File file = new File(fileDir, fileInfo.getFilename());
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(length);
                fileInfo.setLength(length);
                mHandler.obtainMessage(MESSAGE_INIT, mFileInfo).sendToTarget();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.disconnect();
                    raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
