package com.mvp.activity.statusbar;

import android.app.Activity;

/**
 *
 */
public interface StatusBarContract {
    /**
     * view
     */
    interface IStatusBarRelativeLayout {
        void statusLeft(Activity activity);

        void statusCenter(Activity activity);

        void statusRight(Activity activity);
    }

    /**
     * 控制器
     */
    interface IStatusBarPresenter {

        void statusLeft(Activity activity);

        void statusCenter(Activity activity);

        void statusRight(Activity activity);
    }
}
