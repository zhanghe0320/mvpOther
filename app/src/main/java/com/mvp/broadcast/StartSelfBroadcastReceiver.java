package com.mvp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mvp.activity.WelcomeActivity;

/**
 * 自启动广播
 */
public class StartSelfBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";
    static final String action_boot0 = "android.media.AUDIO_BECOMING_NOISY";
    private static final String TAG = "BootBroadcastReceiver";
    private static boolean BOOT_COMPLETED = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals(action_boot) || intent.getAction().equals(action_boot0)/*&&!BOOT_COMPLETED*//*&&intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY")*/) {
            if (!BOOT_COMPLETED) {
                Log.e(TAG, "开机启动");
                //开机启动
                Intent mainIntent = new Intent(context, WelcomeActivity.class);
                //在BroadcastReceiver中显示Activity，必须要设置FLAG_ACTIVITY_NEW_TASK标志
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainIntent);
                BOOT_COMPLETED = true;
            }

        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
