package qb.com.top_news.vo;

/**
 * Created by qianbin on 16/8/9.
 */
public class News {
    private String title;
    private String date;
    private String realtype;
    private String category;
    private String author_name;
    private String thumbnail_pic_s;
    private String thumbnail_pic_s03;
    private String url;

    public News() {
    }

    public News(String title, String date, String realtype, String category,String author_name, String thumbnail_pic_s, String thumbnail_pic_s03, String url) {
        this.title = title;
        this.date = date;
        this.realtype = realtype;
        this.category=category;
        this.author_name = author_name;
        this.thumbnail_pic_s = thumbnail_pic_s;
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRealtype() {
        return realtype;
    }

    public void setRealtype(String realtype) {
        this.realtype = realtype;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getThumbnail_pic_s03() {
        return thumbnail_pic_s03;
    }

    public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
