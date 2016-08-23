package qb.com.top_news.utils;

import android.content.Context;
import android.content.Intent;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import qb.com.top_news.application.MyApplication;
import qb.com.top_news.service.DownloadService;
import qb.com.top_news.vo.DownLoadThreadInfo;
import qb.com.top_news.vo.FileInfo;

public class DownLoadTask {
    private Context mContext;
    private FileInfo mFileInfo;
    private int progress = 0;
    public boolean isPause = false;//暂停标志
    private DbUtils db;

    public DownLoadTask(Context mContext, FileInfo mFileInfo) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        db = MyApplication.getDb();
    }

    public void getLoadThreadInfo() throws DbException {
        List<DownLoadThreadInfo> threadInfos = db.findAll(Selector.from(DownLoadThreadInfo.class).where("url", "=", mFileInfo.getUrl()));
        DownLoadThreadInfo downLoadThreadInfo = null;
        if (threadInfos.size() == 0) {
            downLoadThreadInfo = new DownLoadThreadInfo(0, mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);//线程信息表中没有则创建一个先的线程信息对象
        } else {
            downLoadThreadInfo = threadInfos.get(0);
        }
        new LoadTaskThread(downLoadThreadInfo).start();
    }

    class LoadTaskThread extends Thread {
        private DownLoadThreadInfo downLoadThreadInfo;

        public LoadTaskThread(DownLoadThreadInfo downLoadThreadInfo) {
            this.downLoadThreadInfo = downLoadThreadInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                if (isThreadExist(mFileInfo.getUrl(), mFileInfo.getId())) {
                } else {
                    db.save(downLoadThreadInfo);
                }
                URL url = new URL(downLoadThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");

                //设置线程下载位置
                int start = downLoadThreadInfo.getStart() + downLoadThreadInfo.getProgress();
                conn.setRequestProperty("Range", "bytes=" + start + "-" + downLoadThreadInfo.getEnd());

                //设置文件写入位置
                File file = new File(DownloadService.SAVE_PATH, mFileInfo.getFilename());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);//将开始移动到应该的位置
                Intent intent = new Intent(DownloadService.UPDATE_ACTION);
                progress += downLoadThreadInfo.getProgress();
                //开始继续下载

                if (conn.getResponseCode() == 206) {
                    //读
                    InputStream is = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buffer)) != -1) {
                        //写
                        raf.write(buffer, 0, len);
                        progress += len;
                        //广播通知界面
                        intent.putExtra(DownloadService.LOAD_PROGRESS, progress * 100 / mFileInfo.getLength());
                        mContext.sendBroadcast(intent);
                        if (isPause) {
                            System.out.println("暂停");
                            downLoadThreadInfo.setProgress(progress);
                            db.update(downLoadThreadInfo);
                            return;
                        }
                    }
                    db.delete(downLoadThreadInfo);
                }


            } catch (DbException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    raf.close();
                    conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isThreadExist(String url, int id) throws DbException {
        boolean isExist;
        List<DownLoadThreadInfo> threadInfos = db.findAll(Selector.from(DownLoadThreadInfo.class).where("url", "=", url).and("id", "=", id));
        if (threadInfos.size() != 0) {
            isExist = true;
        } else {
            isExist = false;
        }
        return isExist;
    }
}
