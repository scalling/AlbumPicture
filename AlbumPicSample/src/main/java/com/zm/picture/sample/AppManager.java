package com.zm.picture.sample;

/**
 * Created by shake on 2017/5/25.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity管理类
 */
public class AppManager {
    Map<String, List<Activity>> activities = new HashMap<String, List<Activity>>();
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(String name, Activity a) {
        a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (!activities.containsKey(name)) {
            activities.put(name, new ArrayList<Activity>());
        }
        activities.get(name).add(a);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        String name = cls.getName();
        if (activities.containsKey(name)) {
            List<Activity> activityList = activities.get(name);
            if (activityList != null)
                for (Activity activity : activityList) {
                    if (activity != null && !activity.isFinishing()) {
                        activity.finish();
                    }
                    activities.remove(name);
                }
        }
    }

    public boolean getActivity(Class<?> cls) {
        String name = cls.getName();
        if (activities.containsKey(name)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Map.Entry<String, List<Activity>> entry : activities.entrySet()) {
            for (Activity activity : entry.getValue()) {
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        activities.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
