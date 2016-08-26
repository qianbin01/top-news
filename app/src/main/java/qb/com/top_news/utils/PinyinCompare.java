package qb.com.top_news.utils;

import java.util.Comparator;

import qb.com.top_news.vo.Contact;

/**
 * Created by qianbin on 16/8/26.
 */
public class PinyinCompare implements Comparator<Contact> {
    @Override
    public int compare(Contact o1, Contact o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序

        if (o2.getSortKey().equals("#")) {
            return -1;
        } else if (o1.getSortKey().equals("#")) {
            return 1;
        } else {
            return o1.getSortKey().compareTo(o2.getSortKey());
        }
    }
}
