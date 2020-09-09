package com.mvp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.mvp.base.MvpApplication;
import com.mvp.commonutils.NetWorkUtils;

/**
 * 通过广播接收网络状态改变
 */
public class NetConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int connectionType = NetWorkUtils.getConnectionType(context);

            /**
             * 更改网络状态
             */
//            if (MvpApplication.getInstance() != null) {
//                MvpApplication.getInstance().notifyNetObserver(connectionType);
//            }
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
