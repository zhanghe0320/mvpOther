package com.mvp.base;

import android.annotation.SuppressLint;
import android.app.Application;

import com.mvp.base.db.Singleton;

import java.lang.reflect.InvocationTargetException;

/**
 *和通过反射的方法获取application 对象
 * 当前进程
 */
public class ActivityApplication {

    public static ActivityApplication getInstance() {
        // 双重判空机制 保证线程安全
        return sInstance.getInstance();
    }

    private static Singleton<ActivityApplication> sInstance = new Singleton<ActivityApplication>() {
        @Override
        protected ActivityApplication newInstance() {
            return new ActivityApplication();
        }
    };

    static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }
}
