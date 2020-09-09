package com.mvp.oserver;

/**
 *网络状态观察者
 */
public interface NetConnectionSubject {
    /**
     * 注册观察者
     *
     * @param observer
     */
    public void addNetObserver(NetConnectionObserver observer);

    /**
     * 移除观察者
     *
     * @param observer
     */
    public void removeNetObserver(NetConnectionObserver observer);

    /**
     * 状态更新通知
     *
     * @param type
     */
    public void notifyNetObserver(int type);
}
