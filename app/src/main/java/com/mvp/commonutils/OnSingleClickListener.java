package com.mvp.commonutils;

import android.view.View;

/**
 * 防重复点击监听器
 */
public class OnSingleClickListener implements View.OnClickListener {

    public static final int CLICK_INTERVAL_TIME = 100; //点击时间间隔
    private static long sLastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currTime = System.currentTimeMillis();
        if (Math.abs(currTime - sLastClickTime) >= CLICK_INTERVAL_TIME) {
            sLastClickTime = currTime;
            onSingleClick(v);
        }
    }

    public void onSingleClick(View v) {

    }

    public static boolean isNotAllowClick() {
        long currTime = System.currentTimeMillis();
        if (Math.abs(currTime - sLastClickTime) >= CLICK_INTERVAL_TIME) {
            return false;
        }
        return true;
    }

}
