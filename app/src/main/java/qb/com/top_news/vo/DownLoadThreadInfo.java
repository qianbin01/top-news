package qb.com.top_news.vo;


public class DownLoadThreadInfo {
    private int id;//线程id
    private String url;//线程下载对应的Url
    private int start;//开始位置
    private int end;//结束位置
    private int progress;//下载进度

    public DownLoadThreadInfo() {
    }

    public DownLoadThreadInfo(int id, String url, int start, int end, int progress) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.progress = progress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "DownLoadThreadInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", progress=" + progress +
                '}';
    }
}
