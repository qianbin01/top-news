package qb.com.top_news.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianbin on 16/8/9.
 */
public class ActivityController {
    private static List<Activity> activities = new ArrayList<Activity>();


    //加入activity
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    //移除某个activity
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //结束所有activity
    public static void finishAll() {
        //遍历activity
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }


}
