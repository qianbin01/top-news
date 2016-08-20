package qb.com.top_news.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import qb.com.top_news.vo.AppDetail;
import qb.com.top_news.vo.AppInfo;

public class SpiderApp {
    public static String look(String path) {
        String html = null;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                html = bos.toString("UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html;
    }


    public static List<AppInfo> getSearch(String searchWord, String page) {

        String url = "http://zhushou.360.cn/search/index/?kw=";
        List<AppInfo> infoList = new ArrayList<>();
        if (page == null || page == "") {
            url += searchWord;
        } else {
            url += searchWord + "&page=" + page;
        }
        String html = look(url);
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByClass("SeaCon");
        Element element = elements.get(0);
        String searchInfo = element.getElementsByClass("title_tr").get(0).getElementsByClass("red").get(0).text();
        Elements liElements = element.getElementsByTag("li");
        for (Element e : liElements) {
            AppInfo info = new AppInfo();
            info.setSearchNums(searchInfo);
            String appIcon = e.getElementsByTag("dt").get(0).getElementsByTag("a").get(0).getElementsByTag("img").get(0)
                    .attr("_src");
            info.setImgPath(appIcon);
            String appName = e.getElementsByTag("dd").get(0).getElementsByTag("a").get(0).text();
            info.setName(appName);
            String appDetail = e.getElementsByTag("dd").get(0).getElementsByTag("a").attr("href");
            info.setDetail("http://zhushou.360.cn/" + appDetail);
            String appTips = e.getElementsByTag("dd").get(0).getElementsByTag("p").get(0).text();
            info.setTips(appTips);
            String starsAndDownInfo = e.getElementsByClass("sdlft").get(0).text();
            info.setStars(starsAndDownInfo.substring(0, starsAndDownInfo.indexOf("分") + 1));
            info.setDownNums(starsAndDownInfo.substring(0, starsAndDownInfo.indexOf("分") + 2));
            String downPath = e.getElementsByClass("seaDown").get(0).getElementsByTag("a").attr("href");
            info.setDownPath(downPath);
            infoList.add(info);
        }
        return infoList;
    }

    public synchronized static AppDetail getDetail(String url) {
        AppDetail appDetail = new AppDetail();
        String html = look(url);
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByTag("body");
        Element element = elements.get(0);
        // app名字
        String name = element.getElementsByTag("h3").get(0).text();
        appDetail.setName(name);
        // app图标
        Elements elements2 = element.getElementsByClass("app-item");
        String imgPath = elements2.get(0).getElementsByTag("img").attr("src");
        appDetail.setImgPath(imgPath);

        Elements elements3 = elements.get(0).getElementsByTag("ul").get(0).getElementsByTag("li");
        String version = elements3.get(0).text();
        appDetail.setVersion(version);
        String size = elements3.get(1).text();
        appDetail.setSize(size);
        String language = elements3.get(2).text();
        appDetail.setLanguage(language);
        String time = elements3.get(3).text();
        appDetail.setUpdateTime(time);
        String author = elements3.get(4).text();
        appDetail.setAuthor(author);

        Element element2 = doc.getElementById("fullDesc");
        if (element2 != null) {
            appDetail.setDesc(element2.text());
        }
        Elements elements4 = elements2.get(0).getElementsByTag("p");
        appDetail.setDownCount(elements4.get(1).text());

        Elements elements5 = elements.get(0).getElementsByClass("swipe-wrap");
        Elements elements6 = elements5.get(0).getElementsByTag("img");
        if (elements6.size()>0) {
            appDetail.setPicPath1(elements6.get(0).attr("src"));
        }
        if (elements6.size()>1) {
            appDetail.setPicPath2(elements6.get(1).attr("src"));
        }
        if (elements6.size()>2) {
            appDetail.setPicPath3(elements6.get(2).attr("src"));
        }
        if (elements6.size()>3) {
            appDetail.setPicPath4(elements6.get(3).attr("src"));
        }
        if (elements6.size()>4) {
            appDetail.setPicPath5(elements6.get(4).attr("src"));

        }
        if (elements6.size()>5) {
            appDetail.setPicPath6(elements6.get(5).attr("src"));

        }
        return appDetail;
    }
}
