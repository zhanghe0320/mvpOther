package com.mvp.activity.statusbar;

import android.app.Activity;

/**
 *
 */
public class StatusationBarPresenter implements StatusBarContract.IStatusBarPresenter {
    private StatusBarContract.IStatusBarRelativeLayout mStatusBarRelativeLayout;

    public StatusationBarPresenter(StatusBarContract.IStatusBarRelativeLayout statusBarRelativeLayout) {
        super();
        this.mStatusBarRelativeLayout = statusBarRelativeLayout;
    }


    @Override
    public void statusLeft(Activity activity) {
        mStatusBarRelativeLayout.statusLeft(activity);
    }

    @Override
    public void statusCenter(Activity activity) {
        mStatusBarRelativeLayout.statusCenter(activity);
    }

    @Override
    public void statusRight(Activity activity) {
        mStatusBarRelativeLayout.statusRight(activity);
    }
}