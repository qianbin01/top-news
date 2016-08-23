package qb.com.top_news.vo;


import java.io.Serializable;

public class FileInfo implements Serializable{

    private String url;
    private int id;
    private String filename;
    private int length;
    private int progress;//下载进度

    public FileInfo() {
    }

    public FileInfo(String url, int id, String filename, int length, int progress) {
        this.url = url;
        this.id = id;
        this.filename = filename;
        this.length = length;
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "url='" + url + '\'' +
                ", id=" + id +
                ", filename='" + filename + '\'' +
                ", length=" + length +
                ", progress=" + progress +
                '}';
    }

}
