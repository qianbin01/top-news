package qb.com.top_news.vo;

import java.io.Serializable;


public class AppDetail implements Serializable {
    private String id;
    private String name;
    private String imgPath;
    private String star;
    private String downCount;
    private String size;
    private String downPath;
    private String author;
    private String updateTime;
    private String version;
    private String desc;
    private String language;
    private String picPath1;
    private String picPath2;
    private String picPath3;
    private String picPath4;
    private String picPath5;
    private String picPath6;
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

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getDownCount() {
        return downCount;
    }

    public void setDownCount(String downCount) {
        this.downCount = downCount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDownPath() {
        return downPath;
    }

    public void setDownPath(String downPath) {
        this.downPath = downPath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPicPath1() {
        return picPath1;
    }

    public void setPicPath1(String picPath1) {
        this.picPath1 = picPath1;
    }

    public String getPicPath2() {
        return picPath2;
    }

    public void setPicPath2(String picPath2) {
        this.picPath2 = picPath2;
    }

    public String getPicPath3() {
        return picPath3;
    }

    public void setPicPath3(String picPath3) {
        this.picPath3 = picPath3;
    }

    public String getPicPath4() {
        return picPath4;
    }

    public void setPicPath4(String picPath4) {
        this.picPath4 = picPath4;
    }

    public String getPicPath5() {
        return picPath5;
    }

    public void setPicPath5(String picPath5) {
        this.picPath5 = picPath5;
    }

    public String getPicPath6() {
        return picPath6;
    }

    public void setPicPath6(String picPath6) {
        this.picPath6 = picPath6;
    }

    @Override
    public String toString() {
        return "AppDetail [id=" + id + ", name=" + name + ", imgPath=" + imgPath + ", star=" + star + ", downCount="
                + downCount + ", size=" + size + ", downPath=" + downPath + ", author=" + author + ", updateTime="
                + updateTime + ", version=" + version + ", desc=" + desc + ", language=" + language + ", picPath1="
                + picPath1 + ", picPath2=" + picPath2 + ", picPath3=" + picPath3 + ", picPath4=" + picPath4
                + ", picPath5=" + picPath5 + ", picPath6=" + picPath6 + "]";
    }
}
