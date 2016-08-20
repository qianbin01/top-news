package qb.com.top_news.vo;


import java.io.Serializable;

public class AppInfo implements Serializable {
    private String id;
    private String name;
    private String imgPath;
    private String downPath;
    private String tips;
    private String downNums;
    private String Stars;
    private String searchNums;
    private String detail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getDownPath() {
        return downPath;
    }

    public void setDownPath(String downPath) {
        this.downPath = downPath;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getDownNums() {
        return downNums;
    }

    public void setDownNums(String downNums) {
        this.downNums = downNums;
    }

    public String getStars() {
        return Stars;
    }

    public void setStars(String stars) {
        Stars = stars;
    }

    public String getSearchNums() {
        return searchNums;
    }

    public void setSearchNums(String searchNums) {
        this.searchNums = searchNums;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", downPath='" + downPath + '\'' +
                ", tips='" + tips + '\'' +
                ", downNums='" + downNums + '\'' +
                ", Stars='" + Stars + '\'' +
                ", searchNums='" + searchNums + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
