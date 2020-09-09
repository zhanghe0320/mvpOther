package com.mvp.commonutils.netstatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解
 * 网络监听lib库
 */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target(ElementType.METHOD)//标记在方法上
public @interface NetWorkMonitor {
    //监听的网络状态变化 默认全部监听并提示
    NetWorkState[] monitorFilter() default {
            NetWorkState.NET_NO_CONNECTION,
            NetWorkState.NET_TYPE_WIFI,
            NetWorkState.NET_TYPE_2G,
            NetWorkState.NET_TYPE_3G,
            NetWorkState.NET_TYPE_4G,
            NetWorkState.NET_TYPE_5G,
            NetWorkState.NET_TYPE_UNKNOWN};
}
