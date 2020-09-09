package com.mvp.commonutils;

import com.mvp.base.MvpApplication;

/**
 * 文件操作类 操作xml配置文件信息
 *
 */
public class SystemSharePreferenceMgr extends BaseSharePreference{
    private final String SP_NAME = "system";
    public final static String system_password = "system_password";
    private static SystemSharePreferenceMgr sManager;

    public static SystemSharePreferenceMgr getInstance(){
        if(sManager == null){
            synchronized (SystemSharePreferenceMgr.class){
                if(sManager == null){
                    sManager = new SystemSharePreferenceMgr();
                }
            }
        }
        return sManager;
    }

    public SystemSharePreferenceMgr() {
        init(MvpApplication.getmContext(), SP_NAME);
    }

}
