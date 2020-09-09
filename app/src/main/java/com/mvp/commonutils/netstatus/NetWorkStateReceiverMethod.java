package com.mvp.commonutils.netstatus;

import java.lang.reflect.Method;

/**
 * 保存接受状态变化的方法对象
 * AnnotationApplication
 */
public class NetWorkStateReceiverMethod {
    /**
     * 网络改变执行的方法
     */
    Method method;
    /**
     * 网络改变执行的方法所属的类
     */
    Object object;
    /**
     * 监听的网络改变类型
     */
    NetWorkState[] netWorkState = {NetWorkState.NET_NO_CONNECTION,
            NetWorkState.NET_TYPE_WIFI,
            NetWorkState.NET_TYPE_2G,
            NetWorkState.NET_TYPE_3G,
            NetWorkState.NET_TYPE_4G,
            NetWorkState.NET_TYPE_5G,
            NetWorkState.NET_TYPE_UNKNOWN};

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public NetWorkState[] getNetWorkState() {
        return netWorkState;
    }

    public void setNetWorkState(NetWorkState[] netWorkState) {
        this.netWorkState = netWorkState;
    }
}